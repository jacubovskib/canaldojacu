package com.github.connectors;

import com.github.record.Transaction;
import com.github.record.TransactionDeserializer;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

public class Kafka {

  public static KafkaSource<Transaction> source(String bootstrapServer, String sourceTopic) {
    return KafkaSource.<Transaction>builder()
        .setBootstrapServers(bootstrapServer)
        .setTopics(sourceTopic)
        .setStartingOffsets(OffsetsInitializer.latest())
        .setValueOnlyDeserializer(new TransactionDeserializer())
        .build();
  }

  public static KafkaSink<Transaction> sink(String bootstrapServer, String sinkTopic) {
    return KafkaSink.<Transaction>builder()
        .setBootstrapServers(bootstrapServer)
        .setRecordSerializer(
            KafkaRecordSerializationSchema.builder()
                .setTopic(sinkTopic)
                .setValueSerializationSchema(
                    (SerializationSchema<Transaction>) el -> el.toString().getBytes()
                ).build()
        )
        .build();
  }
}
