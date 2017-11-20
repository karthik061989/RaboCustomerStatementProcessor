/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.config;

import java.math.BigInteger;

/**
 *
 * @author KARTHIK
 */
public class Constants {

	private Constants() {
	}

	public static final String CSV_FILETYPE = ".csv";
	public static final String XML_FILETYPE = ".xml";
	public static final String COMMA = ",";
	public static final String UNDER_SCORE = "_";
	public static final int IBANNUMBER_MIN_SIZE = 15;
	public static final int IBANNUMBER_MAX_SIZE = 34;
	public static final BigInteger IBANNUMBER_MAGIC_NUMBER = new BigInteger("97");
	public static final String RABO1012="RABO1012";

}
