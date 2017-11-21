/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rabobank.engine.statement.exception.ServerException;
import com.rabobank.engine.statement.model.ErrorResponse;
import com.rabobank.engine.statement.model.Record;
import com.rabobank.engine.statement.model.Records;

/**
 *
 * @author KARTHIK
 */
@Service("utility")
public class Utility {
	
	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public Double formatDouble(Double value) {
		logger.info("Format double value");
		return Double.parseDouble(new DecimalFormat("#0.00").format(value));
	}

	public boolean isNullOrEmpty(String value) {
		logger.info("Check null or empty");
		boolean flag = false;
		if (null == value || value.isEmpty()) {
			flag = true;
		}
		return flag;
	}

	public boolean isNumeric(String value) {
		logger.info("Check value is numeric");
		boolean flag = true;
		try {
			Integer.parseInt(value);			
		} catch (NumberFormatException ex) {
			logger.error(ex.getMessage(), ex);
			flag = false;
		}
		return flag;
	}

	public Records getXMLRecords(InputStream inputStream) throws ServerException {
		logger.info("Get xml records from inputStream");
		try {
			JAXBContext jc = JAXBContext.newInstance(Records.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			return (Records) unmarshaller.unmarshal(inputStream);
		} catch (JAXBException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServerException(Constants.RABO1012);
		}
	}

	public void getErrorResponse(String statusCode, String errorCode, String errorMessage, PrintWriter printWriter) {
		logger.info("Get error response");
		try {
			JAXBContext contextObj = JAXBContext.newInstance(ErrorResponse.class);
			Marshaller marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setStatusCode(statusCode);
			errorResponse.setErrorCode(errorCode);
			errorResponse.setErrorMessage(errorMessage);
			marshallerObj.marshal(errorResponse, printWriter);
		} catch (JAXBException ex) {
			logger.error(ex.getMessage(), ex);
			ServerException serverException = new ServerException(Constants.RABO1012);
			getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), serverException.getCode(),
					serverException.getMessage(), printWriter);
		}
	}

	public Records getCSVRecords(InputStream inputStream) throws ServerException {
		logger.info("Get CSV records");
		Records records = new Records();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			List<Record> recordList = new ArrayList<>();
			String line = "";
			boolean flag = true;
			while ((line = br.readLine()) != null) {
				if (flag) {
					flag = false;
					continue;
				}
				// use comma as separator
				String[] recordData = line.split(Constants.COMMA);
				recordList.add(setRecordData(recordData));
			}
			records.setRecordList(recordList);
		} catch (FileNotFoundException e) {
			throw new ServerException(Constants.RABO1012);
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServerException(Constants.RABO1012);
		}
		return records;
	}

	public Record setRecordData(String[] recordData) {
		logger.info("Set record data");
		Record record = new Record();
		record.setId(recordData[0]);
		record.setAccountNumber(recordData[1]);
		record.setDescription(recordData[2]);
		record.setStartBalance(Double.parseDouble(recordData[3]));
		record.setMutation(Double.parseDouble(recordData[4]));
		record.setEndBalance(Double.parseDouble(recordData[5]));
		return record;
	}

	public boolean isIBAN(String accountNumber) {
		logger.info("Check IBAN number");
		String newAccountNumber = accountNumber.trim();

		// Check that the total IBAN length is correct as per the country. If
		// not, the
		// IBAN is invalid. We could also check
		// for specific length according to country, but for now we won't
		if (newAccountNumber.length() < Constants.IBANNUMBER_MIN_SIZE
				|| newAccountNumber.length() > Constants.IBANNUMBER_MAX_SIZE) {
			return false;
		}

		// Move the four initial characters to the end of the string.
		newAccountNumber = newAccountNumber.substring(4) + newAccountNumber.substring(0, 4);

		// Replace each letter in the string with two digits, thereby expanding
		// the
		// string, where A = 10, B = 11, ..., Z = 35.
		StringBuilder numericAccountNumber = new StringBuilder();
		for (int i = 0; i < newAccountNumber.length(); i++) {
			numericAccountNumber.append(Character.getNumericValue(newAccountNumber.charAt(i)));
		}

		// Interpret the string as a decimal integer and compute the remainder
		// of that
		// number on division by 97.
		BigInteger ibanNumber = new BigInteger(numericAccountNumber.toString());		
		return ibanNumber.mod(Constants.IBANNUMBER_MAGIC_NUMBER).intValue() == 1;
	}

	public void setInternalServerError(HttpServletResponse response) {
		logger.info("Set response status 500");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public void setUnprocessableEntity(HttpServletResponse response) {
		logger.info("Set response status 422");
		response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
	}
}
