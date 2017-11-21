/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabobank.engine.statement.config.Utility;
import com.rabobank.engine.statement.exception.ClientException;
import com.rabobank.engine.statement.model.Record;
import com.rabobank.engine.statement.model.Records;

/**
 * This manager class is used to validate records and check end balance.
 *
 * @author KARTHIK
 */
@Service("raboCustomStatementManaerImpl")
public class RaboCustomerStatementManagerImpl implements RaboCustomerStatementManager {

	private static final Logger logger = LoggerFactory.getLogger(RaboCustomerStatementManagerImpl.class);

	@Autowired
	private Utility utility;

	/**
	 *
	 * This method used to validate records and check end balance.
	 *
	 * @param records
	 * @throws ClientException
	 */
	public Map<String, String> processStatement(Records records) throws ClientException {
		logger.info("Manager call start");
		Map<String, String> errorMap = new HashMap<>();
		if (null == records || null == records.getRecordList() || records.getRecordList().isEmpty()) {
			throw new ClientException("RABO1013");
		}
		List<Record> recordList = records.getRecordList();
		for (Record record : recordList) {
			validateId(record, recordList);
			validateAccountNumber(record);
			validateDescription(record);
			validateStartEndAndMutationValue(record);
			formatStartEndMutationValue(record);
			checkEndBalance(errorMap, record);
		}

		logger.info("Manager call end");
		return errorMap;
	}

	private void checkEndBalance(Map<String, String> errorMap, Record record) {
		logger.info("Check end balance");
		Double caluculatedEndValue = utility.formatDouble(record.getStartBalance() + record.getMutation());
		if (caluculatedEndValue.compareTo(record.getEndBalance()) != 0) {
			errorMap.put(record.getId(),
					"The end balance " + record.getEndBalance() + " is wrong. The correct value is " + caluculatedEndValue);
		}
	}

	private void formatStartEndMutationValue(Record record) {
		logger.info("Format start/end/mutation value");
		record.setStartBalance(utility.formatDouble(record.getStartBalance()));
		record.setMutation(utility.formatDouble(record.getMutation()));
		record.setEndBalance(utility.formatDouble(record.getEndBalance()));
	}

	private void validateStartEndAndMutationValue(Record record) throws ClientException {
		logger.info("Validate start/end/mutation");
		if (null == record.getStartBalance()) {
			throw new ClientException("RABO1008");
		} else if (null == record.getMutation()) {
			throw new ClientException("RABO1009");
		} else if (null == record.getEndBalance()) {
			throw new ClientException("RABO1010");
		}
	}

	private void validateDescription(Record record) throws ClientException {
		logger.info("Validate description");
		if (utility.isNullOrEmpty(record.getDescription())) {
			throw new ClientException("RABO1006");
		} else if (record.getDescription().length() > 100) {
			throw new ClientException("RABO1007");
		}
	}

	private void validateAccountNumber(Record record) throws ClientException {
		logger.info("Validate account number");
		if (utility.isNullOrEmpty(record.getAccountNumber())) {
			throw new ClientException("RABO1004");
		} else if (!utility.isIBAN(record.getAccountNumber())) {
			throw new ClientException("RABO1005");
		}
	}

	private void validateId(Record record, List<Record> recordList) throws ClientException {
		logger.info("Validate reference");
		if (utility.isNullOrEmpty(record.getId())) {
			throw new ClientException("RABO1001");
		} else if (!utility.isNumeric(record.getId())) {
			throw new ClientException("RABO1002");
		} else if (Collections.frequency(recordList, record) > 1) {
			throw new ClientException("RABO1003");
		}
	}
}
