package com.icodeshivam.statistics.service;

import com.icodeshivam.statistics.model.Statistics;
import com.icodeshivam.statistics.model.StatsInternal;
import com.icodeshivam.statistics.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final int SUMMARY_WINDOW = 60000;
    private Map<Integer, StatsInternal> statsInWindow = new ConcurrentHashMap<>();

    public void addTransaction(Transaction transaction) {
        if(!validateTransaction(transaction))
            return;

        int second = LocalDateTime.ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneId.systemDefault()).getSecond();
        statsInWindow.compute(second, (secondKey, stats)-> {

            if(null == stats || System.currentTimeMillis() - stats.getTimestamp() >= SUMMARY_WINDOW) {
                // discard old statsInWindow
                stats = new StatsInternal();
                stats.setTimestamp(transaction.getTimestamp());
                stats.setSum(transaction.getAmount());
                stats.setMin(transaction.getAmount());
                stats.setMax(transaction.getAmount());
                stats.setCount(1L);
                return stats;
            }

            stats.setSum(stats.getSum() + transaction.getAmount());
            if(transaction.getAmount() > stats.getMax())
                stats.setMax(transaction.getAmount());
            if(transaction.getAmount() < stats.getMin())
                stats.setMin(transaction.getAmount());
            stats.setCount(stats.getCount() + 1);
            return stats;
        });
    }

    private boolean validateTransaction(Transaction transaction) {
        if(transaction.getTimestamp() < System.currentTimeMillis() && System.currentTimeMillis() - transaction.getTimestamp() < SUMMARY_WINDOW) {
            return true;
        }
        return false;
    }

    public Statistics getStatistics() {
        Optional<Statistics> statistics = statsInWindow.values().stream()
                .filter(stats -> {
                    return System.currentTimeMillis() - stats.getTimestamp() < SUMMARY_WINDOW;
                })
                .map(Statistics::create)
                .reduce((statistics1, statistics2) -> {
                    statistics1.setSum(statistics1.getSum() + statistics2.getSum());
                    if(statistics2.getMax() > statistics1.getMax())
                        statistics1.setMax(statistics2.getMax());
                    if(statistics2.getMin() < statistics1.getMin())
                        statistics1.setMin(statistics2.getMin());
                    statistics1.setCount(statistics1.getCount() + statistics2.getCount());
                    return statistics1;
                });

        if(statistics.isPresent()) {
            if(statistics.get().getCount() > 0L) {
                statistics.get().setAvg(statistics.get().getSum() / statistics.get().getCount());
            }
        }
        return statistics.orElse(new Statistics());
    }

}
