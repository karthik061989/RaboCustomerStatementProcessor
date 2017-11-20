/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.engine.statement.config.Constants;
import com.rabobank.engine.statement.config.Utility;
import com.rabobank.engine.statement.exception.ClientException;
import com.rabobank.engine.statement.exception.ServerException;
import com.rabobank.engine.statement.manager.RaboCustomerStatementManager;

/**
 * This controller class is process the customer statements and provide
 * the error report.
 *
 * @author KARTHIK
 */
@Controller
@RequestMapping("/rabo/statement")
public class RaboCustomerStatementController {

	private static final Logger logger = LoggerFactory.getLogger(RaboCustomerStatementController.class);

	@Autowired
	private RaboCustomerStatementManager raboCustomStatementManager;

	@Autowired
	private Utility utility;

	/**
	 *
	 * This controller method is to perform process the customer statements and
	 * provide the error report.
	 *
	 * @param response
	 * @param request
	 * @param file
	 * @throws IOException
	 */
	@RequestMapping(value = "/report", method = RequestMethod.POST, consumes = "multipart/form-data")
	public @ResponseBody void processStatement(HttpServletResponse response, HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws IOException {
		logger.info("Start the statement process");
		String fileType = getFileType(file);
		Map<String, String> map = null;
		PrintWriter printWriter = response.getWriter();
		try {
			if (Constants.XML_FILETYPE.equalsIgnoreCase(fileType)) {
				map = raboCustomStatementManager.processStatement(utility.getXMLRecords(file.getInputStream()));
			} else if (Constants.CSV_FILETYPE.equalsIgnoreCase(fileType)) {
				map = raboCustomStatementManager.processStatement(utility.getCSVRecords(file.getInputStream()));
			} else {
				throw new ClientException("RABO1011");
			}
			writeMessage(map, response);
		} catch (ClientException ex) {
			logger.error(ex.getMessage(), ex);
			setClientException(response, printWriter, ex);
		} catch (ServerException ex) {
			logger.error(ex.getMessage(), ex);
			setServerException(response, printWriter, ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			setServerException(response, printWriter);
		}
		logger.info("End of the statement process");
	}

	/**
	 *
	 * This method is to perform set server exception
	 *
	 * @param response
	 * @param printWriter
	 * @param ex
	 */
	private void setServerException(HttpServletResponse response, PrintWriter printWriter, ServerException ex) {
		logger.info("Set server exception");
		if (utility.isNullOrEmpty(ex.getCode())) {
			setServerException(response, printWriter);
		} else {
			utility.setUnprocessableEntity(response);
			utility.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getCode(), ex.getMessage(),
					printWriter);
		}
	}

	/**
	 *
	 * This method is to perform set client exception
	 *
	 * @param response
	 * @param printWriter
	 * @param ex
	 */
	private void setClientException(HttpServletResponse response, PrintWriter printWriter, ClientException ex) {
		logger.info("Set client exception");
		if (utility.isNullOrEmpty(ex.getCode())) {
			setServerException(response, printWriter);
		} else {
			utility.setUnprocessableEntity(response);
			utility.getErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(), ex.getCode(), ex.getMessage(),
					printWriter);
		}
	}

	/**
	 *
	 * This method is to perform set server exception
	 *
	 * @param response
	 * @param printWriter
	 */
	private void setServerException(HttpServletResponse response, PrintWriter printWriter) {
		logger.info("Set server exception");
		utility.setInternalServerError(response);
		ServerException serverException = new ServerException("RABO1012");
		utility.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), serverException.getCode(),
				serverException.getMessage(), printWriter);
	}

	/**
	 *
	 * This method is to perform get file type
	 *
	 * @param file
	 */
	private String getFileType(MultipartFile file) {
		logger.info("Get file type");
		String fileName = file.getOriginalFilename();
		return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}

	/**
	 *
	 * This method is to perform write success message on the response.
	 *
	 * @param map
	 * @param response
	 */
	public void writeMessage(Map<String, String> map, HttpServletResponse response) throws IOException {
		logger.info("Write message");
		response.setStatus(HttpStatus.OK.value());
		PrintWriter printWriter = response.getWriter();
		StringBuilder builder = new StringBuilder();
		builder.append("<HTML><HEAD><TITLE>RaboBank Customer Statement Processor</TITLE></HEAD><BODY>");
		builder.append("<DIV align=\"center\">");
		builder.append("<TABLE border=\"1\" summary=\"Report\" frame=\"box\">");
		builder.append("<CAPTION><EM>Report</EM></CAPTION>");
		builder.append("<TR><TH>Reference</TH><TH>Description</TH></TR>");
		for (String key : map.keySet()) {
			builder.append("<TR><TD>").append(key.substring(0, key.indexOf(Constants.UNDER_SCORE))).append("</TD>").append("<TD>").append(map.get(key))
					.append("</TD></TR>");
		}
		builder.append("</TABLE></DIV>");
		builder.append("</BODY></HTML>");
		String message = builder.toString();
		logger.info(message);
		printWriter.write(message);
	}
}
