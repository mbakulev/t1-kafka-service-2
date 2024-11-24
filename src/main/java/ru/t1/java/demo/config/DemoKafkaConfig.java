package ru.t1.java.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.java.demo.dto.*;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.MessageDeserializer;
import ru.t1.java.demo.model.DataSourceErrorLog;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DemoKafkaConfig<T> {

    @Value("${t1.kafka.consumer.group-id}")
    private String groupId;
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;
    @Value("${t1.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.max.poll.records:1}")
    private String maxPollRecords;
    @Value("${t1.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalsMs;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;

    private Map<String, Object> getDefaultProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        return props;
    }

//    @Bean
//    public ConsumerFactory<String, ClientDto> consumerListenerFactory() {
//        Map<String, Object> props = getDefaultProps();
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.dto.ClientDto");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
//        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);
//
//        DefaultKafkaConsumerFactory<String, ClientDto> factory = new DefaultKafkaConsumerFactory<>(props);
//        factory.setKeyDeserializer(new StringDeserializer());
//
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, DataSourceErrorLog> dataSourceErrorLogConsumerFactory() {
//        Map<String, Object> props = getDefaultProps();
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, DataSourceErrorLog.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
//        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConsumerFactory<String, AccountDto> accountConsumerListenerFactory() {
//        Map<String, Object> props = getDefaultProps();
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.dto.AccountDto");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConsumerFactory<String, TransactionDto> transactionConsumerListenerFactory() {
//        Map<String, Object> props = getDefaultProps();
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.dto.TransactionDto");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }

    @Bean
    public ConsumerFactory<String, TransactionAccept> transactionAcceptConsumerListenerFactory() {
        Map<String, Object> props = getDefaultProps();
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.dto.TransactionAccept");

        return new DefaultKafkaConsumerFactory<>(props);
    }

//    @Bean
//    public ConsumerFactory<String, TransactionAcceptResult> transactionAcceptResultConsumerListenerFactory() {
//        Map<String, Object> props = getDefaultProps();
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.dto.TransactionAcceptResult");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }

//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, ClientDto> kafkaListenerContainerFactory(@Qualifier("consumerListenerFactory") ConsumerFactory<String, ClientDto> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, ClientDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryBuilder(consumerFactory, factory);
//        return factory;
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, DataSourceErrorLog> dataSourceErrorLogKafkaListenerContainerFactory(@Qualifier("dataSourceErrorLogConsumerFactory") ConsumerFactory<String, DataSourceErrorLog> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, DataSourceErrorLog> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryBuilder(consumerFactory, factory);
//        return factory;
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, AccountDto> accountDtoKafkaListenerContainerFactory(@Qualifier("accountConsumerListenerFactory") ConsumerFactory<String, AccountDto> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryBuilder(consumerFactory, factory);
//        return factory;
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, TransactionDto> transactionKafkaListenerContainerFactory(@Qualifier("transactionConsumerListenerFactory") ConsumerFactory<String, TransactionDto> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, TransactionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryBuilder(consumerFactory, factory);
//        return factory;
//    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionAccept> transactionAcceptKafkaListenerContainerFactory(@Qualifier("transactionAcceptConsumerListenerFactory") ConsumerFactory<String, TransactionAccept> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TransactionAccept> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, TransactionAcceptResult> transactionAcceptResultKafkaListenerContainerFactory(@Qualifier("transactionAcceptResultConsumerListenerFactory") ConsumerFactory<String, TransactionAcceptResult> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, TransactionAcceptResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factoryBuilder(consumerFactory, factory);
//        return factory;
//    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory, ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.error(" RetryListeners message = {}, offset = {} deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt);
        });
        return handler;
    }

//    @Bean("client")
//    @Primary
//    public KafkaTemplate<String, T> kafkaClientTemplate(@Qualifier("producerClientFactory") ProducerFactory<String, T> producerPatFactory) {
//        return new KafkaTemplate<>(producerPatFactory);
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
//            havingValue = "true",
//            matchIfMissing = true)
//    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate<String, ClientDto> template) {
//        template.setDefaultTopic(clientTopic);
//        return new KafkaClientProducer(template);
//    }
//
//    @Bean("producerClientFactory")
//    public ProducerFactory<String, T> producerClientFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
//        return new DefaultKafkaProducerFactory<>(props);
//    }

}