package com.icodeshivam.statistics.service;

import com.icodeshivam.statistics.model.Statistics;
import com.icodeshivam.statistics.model.Transaction;

public interface StatisticsService {

    void addTransaction(Transaction transaction);

    Statistics getStatistics();

}
