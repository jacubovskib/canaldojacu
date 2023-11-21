package com.github;

import com.github.connectors.Kafka;
import com.github.process.SimpleProcessing;
import com.github.record.Transaction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.function.Consumer;

public class Main {
  public static void main(String[] args) throws Exception {
    ParameterTool params = ParameterTool.fromArgs(args);
    runJub(params);
  }

  private static void runJub(ParameterTool params) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    String bootstrapServer = params.getRequired("bootstrap-server");
    String sourceTopic = params.getRequired("source-topic");
    String sinkTopic = params.getRequired("sink-topic");

    KafkaSource<Transaction> source = Kafka.source(bootstrapServer, sourceTopic);
    KafkaSink<Transaction> sink = Kafka.sink(bootstrapServer, sinkTopic);

    defineWorkflow(env, source, wokrflow -> wokrflow.sinkTo(sink));

    env.execute("Simple Processing JOb");
  }

  public static  void defineWorkflow(
      StreamExecutionEnvironment env,
      Source<Transaction, ?, ?> source,
      Consumer<DataStream<Transaction>> sinkApplier
  ) {
    DataStream<Transaction> origin =
        env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Transactions");

    DataStream<Transaction> dsFinal = origin.process(new SimpleProcessing());

    sinkApplier.accept(dsFinal);
  }

}