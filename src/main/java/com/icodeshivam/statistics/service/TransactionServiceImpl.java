package com.icodeshivam.statistics.service;

import com.icodeshivam.statistics.exception.ValidationException;
import com.icodeshivam.statistics.model.ErrorCodes;
import com.icodeshivam.statistics.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private StatisticsService statisticsService;

    @Override
    public void addTransaction(Transaction transaction) {

        validateTransaction(transaction);

        statisticsService.addTransaction(transaction);
    }

    private void validateTransaction(Transaction transaction) {
        if(null == transaction || null == transaction.getAmount()) {
            throw new ValidationException(ErrorCodes.MISSING_PARAMETER_AMOUNT);
        } if(null == transaction.getTimestamp()) {
            throw new ValidationException(ErrorCodes.MISSING_PARAMETER_TIMESTAMP);
        } else if(0 > transaction.getAmount()){
            throw new ValidationException(ErrorCodes.INVALID_PARAMETER_AMOUNT);
        } if(0 == transaction.getTimestamp()) {
            throw new ValidationException(ErrorCodes.INVALID_PARAMETER_TIMESTAMP);
        } if(System.currentTimeMillis() - transaction.getTimestamp() < 0) {
            throw new ValidationException(ErrorCodes.CLIENT_SERVER_OUT_OF_SYNC);
        } if(System.currentTimeMillis() - transaction.getTimestamp() > 60000L) {
            throw new ValidationException(ErrorCodes.STALE_TIMESTAMP);
        }
    }
}
