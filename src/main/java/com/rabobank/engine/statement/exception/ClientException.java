/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.exception;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author KARTHIK
 */
public class ClientException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final Map<String, String> errorMap = new HashMap<>();
	private final String message;
	private final String code;

	static {
		// ID
		ClientException.errorMap.put("RABO1001", "Reference should not be empty.");
		ClientException.errorMap.put("RABO1002", "Reference should be numeric.");
		ClientException.errorMap.put("RABO1003", "Reference should be unique.");
		// Account Number
		ClientException.errorMap.put("RABO1004", "AccountNumber should not be empty.");
		ClientException.errorMap.put("RABO1005", "AccountNumber should be IBAN format.");
		// Description
		ClientException.errorMap.put("RABO1006", "Description should not be empty.");
		ClientException.errorMap.put("RABO1007", "Description characters should be lessthan 100.");
		// Start,End,Mutation
		ClientException.errorMap.put("RABO1008", "StartBalance should not be empty.");
		ClientException.errorMap.put("RABO1009", "Mutation should not be empty.");
		ClientException.errorMap.put("RABO1010", "EndBalance should not be empty.");
		ClientException.errorMap.put("RABO1011", "The upload file should be the type of CSV/XML.");
		ClientException.errorMap.put("RABO1013", "No records found to process");
	}

	public ClientException(String errorCode) {
		super(errorMap.get(errorCode));
		this.message = errorMap.get(errorCode);
		this.code = errorCode;
	}

	public ClientException(String message, String code) {
		super(code + message);
		this.message = message;
		this.code = code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return code + "<>" + message;
	}
}
