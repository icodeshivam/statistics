package com.icodeshivam.statistics.service;

import com.icodeshivam.statistics.exception.ValidationException;
import com.icodeshivam.statistics.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Mock
    private StatisticsService statisticsServiceMock;

    @InjectMocks
    private TransactionServiceImpl transactionServiceMock;

    @Test(expected = ValidationException.class)
    public void whenStaleTimestamp_exceptionThrown() {
        transactionService.addTransaction(new Transaction(5.0, System.currentTimeMillis() - 120000L));
    }

    @Test(expected = ValidationException.class)
    public void whenClientOutOfSync_exceptionThrown() {
        transactionService.addTransaction(new Transaction(5.0, System.currentTimeMillis() + 60000L));
    }

    @Test(expected = ValidationException.class)
    public void whenInvalidAmount_exceptionThrown() {
        transactionService.addTransaction(new Transaction(null, System.currentTimeMillis() + 60000L));
    }

    @Test(expected = ValidationException.class)
    public void whenInvalidTimestamp_exceptionThrown() {
        transactionService.addTransaction(new Transaction(5.0, null));
    }

    @Test
    public void whenValidTransaction_addsTransaction() {
        doNothing().when(statisticsServiceMock).addTransaction(any(Transaction.class));
        transactionServiceMock.addTransaction(new Transaction(5.0, System.currentTimeMillis()));
        verify(statisticsServiceMock, times(1)).addTransaction(any(Transaction.class));

    }
}
