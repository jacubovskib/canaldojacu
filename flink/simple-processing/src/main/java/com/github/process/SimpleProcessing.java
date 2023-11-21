package com.github.process;

import com.github.record.Transaction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class SimpleProcessing extends ProcessFunction<Transaction, Transaction> {
  @Override
  public void processElement(
      Transaction transaction,
      ProcessFunction<Transaction, Transaction>.Context context,
      Collector<Transaction> collector) throws Exception {
    double amount = transaction.getAmount();
    transaction.setSuspicious(amount > 5000.00);
    collector.collect(transaction);
  }
}
