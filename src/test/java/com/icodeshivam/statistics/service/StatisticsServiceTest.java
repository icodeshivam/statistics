package com.icodeshivam.statistics.service;

import com.icodeshivam.statistics.model.Statistics;
import com.icodeshivam.statistics.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testSummary() {

        statisticsService.addTransaction(new Transaction(10.0, System.currentTimeMillis() - 4000L));
        statisticsService.addTransaction(new Transaction(0.0, System.currentTimeMillis() - 12000L));
        statisticsService.addTransaction(new Transaction(5.0, System.currentTimeMillis() - 6000L));
        statisticsService.addTransaction(new Transaction(15.0, System.currentTimeMillis() - 7000L));
        statisticsService.addTransaction(new Transaction(10.0, System.currentTimeMillis() - 2000L));

        Statistics statistics = statisticsService.getStatistics();
        assertTrue(statistics.getCount().equals(5L));
        assertTrue(statistics.getSum().equals(40.0));
        assertTrue(statistics.getMax().equals(15.0));
        assertTrue(statistics.getMin().equals(0.0));
        assertTrue(statistics.getAvg().equals(8.0));

    }
}
