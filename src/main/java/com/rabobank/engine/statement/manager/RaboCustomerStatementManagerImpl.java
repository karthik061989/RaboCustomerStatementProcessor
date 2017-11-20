/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabobank.engine.statement.config.Constants;
import com.rabobank.engine.statement.config.Utility;
import com.rabobank.engine.statement.exception.ClientException;
import com.rabobank.engine.statement.model.Record;
import com.rabobank.engine.statement.model.Records;

/**
 * This manager class is to perform validate records, unique reference and check end balance.
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
	 * This method is to validate records and check end balance.
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
		Set<String> reference=new HashSet<>();
		for (Record record : recordList) {
			validateId(record);
			validateAccountNumber(record);
			validateDescription(record);
			validateStartEndAndMutationValue(record);
			formatStartEndMutationValue(record);
			getErrorReports(errorMap, recordList, reference, record);
		}

		logger.info("Manager call end");
		return errorMap;
	}

	/**
	 * This method is to perform check reference and end balance.  
	 * 
	 * @param errorMap
	 * @param recordList
	 * @param reference
	 * @param record
	 */
	private void getErrorReports(Map<String, String> errorMap, List<Record> recordList, Set<String> reference,
			Record record) {
		StringBuilder builder=new StringBuilder();			
		int count=Collections.frequency(recordList, record);			
		if( count> 1 && !reference.contains(record.getId())){
			reference.add(record.getId());
			builder.append("The reference should be unique.It occours ").append(count).append(" times in the uploaded file.\n");
		}
		checkEndBalance(record,builder);
		if(builder.length()>0){
			errorMap.put(record.getId().concat(Constants.UNDER_SCORE).concat(record.getAccountNumber()), builder.toString());
		}
	}

	/**
	 * This method is to perform check end balance  
	 * 
	 * @param record
	 * @param builder
	 */
	private void checkEndBalance(Record record,StringBuilder builder) {
		logger.info("Check end balance");
		Double caluculatedEndValue = utility.formatDouble(record.getStartBalance() + record.getMutation());
		if (caluculatedEndValue.compareTo(record.getEndBalance()) != 0) {
			builder.append("The end balance ").append(record.getEndBalance()).append(" is wrong for the accountNumber ").append(record.getAccountNumber()).append(".");
			builder.append(" The correct value is (").append(record.getStartBalance()).append(Constants.COMMA).append(record.getMutation()).append(")").append("=").append(caluculatedEndValue);			
		}
	}

	/**
	 * This method is to perform format start/end/mutation. 
	 * 
	 * @param record
	 */
	private void formatStartEndMutationValue(Record record) {
		logger.info("Format start/end/mutation value");
		record.setStartBalance(utility.formatDouble(record.getStartBalance()));
		record.setMutation(utility.formatDouble(record.getMutation()));
		record.setEndBalance(utility.formatDouble(record.getEndBalance()));
	}

	/**
	 * This method is to perform validate start/end/mutation is null.  
	 * 
	 * @param record
	 * @throws ClientException
	 */
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

	/**
	 * This method is to perform validate description is null or empty and size. 
	 * 
	 * @param record
	 * @throws ClientException
	 */
	private void validateDescription(Record record) throws ClientException {
		logger.info("Validate description");
		if (utility.isNullOrEmpty(record.getDescription())) {
			throw new ClientException("RABO1006");
		} else if (record.getDescription().length() > 100) {
			throw new ClientException("RABO1007");
		}
	}

	/**
	 * This method is to perform validate account number is null or empty and IBAN. 
	 * 
	 * @param record
	 * @throws ClientException
	 */
	private void validateAccountNumber(Record record) throws ClientException {
		logger.info("Validate account number");
		if (utility.isNullOrEmpty(record.getAccountNumber())) {
			throw new ClientException("RABO1004");
		} else if (!utility.isIBAN(record.getAccountNumber())) {
			throw new ClientException("RABO1005");
		}
	}

	/**
	 * This method is to perform validate reference number.
	 *  
	 * @param record
	 * @throws ClientException
	 */
	private void validateId(Record record) throws ClientException {
		logger.info("Validate reference");
		if (utility.isNullOrEmpty(record.getId())) {
			throw new ClientException("RABO1001");
		} else if (!utility.isNumeric(record.getId())) {
			throw new ClientException("RABO1002");
		} 
	}
}
