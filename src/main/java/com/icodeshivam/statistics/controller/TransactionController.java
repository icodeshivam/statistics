package com.icodeshivam.statistics.controller;

import com.icodeshivam.statistics.exception.ValidationException;
import com.icodeshivam.statistics.model.Transaction;
import com.icodeshivam.statistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> addTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.addTransaction(transaction);
        } catch (ValidationException ex) {
            if(1006 == ex.getErrorCode()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            throw ex;
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}
