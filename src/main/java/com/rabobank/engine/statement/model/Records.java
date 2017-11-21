/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author KARTHIK
 */
@XmlRootElement(name = "records")
@XmlAccessorType(XmlAccessType.FIELD)
public class Records {

	@XmlElement(name = "record", required = true)
	private List<Record> recordList;

	public List<Record> getRecordList() {
		return recordList;
	}
	
	public void setRecordList(List<Record> recordList) {
		this.recordList = recordList;
	}
}
