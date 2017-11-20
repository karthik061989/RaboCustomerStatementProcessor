package com.rabobank.engine.statement.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.rabobank.engine.statement.config.Utility;
import com.rabobank.engine.statement.exception.ClientException;
import com.rabobank.engine.statement.model.Record;
import com.rabobank.engine.statement.model.Records;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
public class RaboCustomerStatementManagerMockitoTest {
	
	@InjectMocks
	private RaboCustomerStatementManager raboCustomerStatementManager=new RaboCustomerStatementManagerImpl();
	
	@Mock
	private Utility utility;

	Records records;

	@Before
	public void setUp() throws Exception {
		records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription(Mockito.anyString());
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		record.setEndBalance(new Double("100"));
	}

	@Test
	public void testProcessStatement() {
		try {
			Map<String, String> map = raboCustomerStatementManager.processStatement(records);
			Assert.assertEquals(true, map.size() > 0);
			Assert.assertTrue(null != map.get("12345"));
		} catch (ClientException e) {
		}
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_RecordNull() throws ClientException {
		raboCustomerStatementManager.processStatement(null);
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_RecordEmpty() throws ClientException {
		raboCustomerStatementManager.processStatement(new Records());
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_RecordEmptyList() throws ClientException {
		Records records = new Records();
		records.setRecordList(new ArrayList<>());
		raboCustomerStatementManager.processStatement(new Records());
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_IdNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId(null);
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("Test descrption.");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_IdEmpty() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("Test descrption.");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_AccountNumberNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber(null);
		record.setDescription("Test descrption.");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}

	@Test(expected = ClientException.class)
	public void testProcessStatement_AccountNumberEmpty() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("");
		record.setDescription("Test descrption.");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_AccountNumberIBAN() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("54321");
		record.setDescription("Test descrption.");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_DescNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription(null);
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_DescEmpty() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_DescLength() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_StartNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("test desc");
		record.setStartBalance(null);
		record.setMutation(new Double("100"));
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_MutationNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("test desc");
		record.setStartBalance(new Double("100"));
		record.setMutation(null);
		record.setEndBalance(new Double("100"));
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
	
	@Test(expected = ClientException.class)
	public void testProcessStatement_EndNull() throws ClientException {
		Records records = new Records();
		List<Record> recordList = new ArrayList<>();
		Record record = new Record();
		record.setId("12345");
		record.setAccountNumber("NL93ABNA0585619023");
		record.setDescription("test desc");
		record.setStartBalance(new Double("100"));
		record.setMutation(new Double("100"));
		record.setEndBalance(null);
		recordList.add(record);
		records.setRecordList(recordList);
		raboCustomerStatementManager.processStatement(records);
	}
}
