/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rabobank.engine.statement.manager;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.rabobank.engine.statement.exception.ClientException;
import com.rabobank.engine.statement.model.Records;

/**
 *
 * @author KARTHIK
 */
@Service("raboCustomStatementManager")
public interface RaboCustomerStatementManager {
    
	Map<String, String> processStatement(Records records) throws ClientException;
    
}
