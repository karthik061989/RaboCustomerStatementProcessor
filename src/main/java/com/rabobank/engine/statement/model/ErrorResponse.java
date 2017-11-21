package com.rabobank.engine.statement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse {

	@XmlAttribute(name = "statusCode", required = true)
	private String statusCode;
	@XmlElement(name = "errorCode", required = true)
	private String errorCode;
	@XmlElement(name = "errorMessage", required = true)
	private String errorMessage;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
