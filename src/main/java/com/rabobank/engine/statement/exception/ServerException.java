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
public class ServerException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final Map<String, String> errorMap = new HashMap<>();
	private final String message;
	private final String code;

	static {
		ServerException.errorMap.put("RABO1012", "Internal server error.");
	}

	public ServerException(String errorCode) {
		super(errorMap.get(errorCode));
		this.message = errorMap.get(errorCode);
		this.code = errorCode;
	}

	public ServerException(String message, String code) {
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
