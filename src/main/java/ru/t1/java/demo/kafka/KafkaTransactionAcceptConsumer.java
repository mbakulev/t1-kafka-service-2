package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionAccept;
import ru.t1.java.demo.dto.TransactionAcceptResult;
import ru.t1.java.demo.service.TransactionService;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.System.out;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAcceptConsumer {
    private final KafkaTemplate<String, TransactionAcceptResult> kafkaTemplate;
    @Value("${t1.maxTransactionsPerInterval}")
    private Long maxTransactionsPerInterval;
    @Value("${t1.transactionsInterval}")
    private Long transactionInterval;
    private final TransactionService transactionService;
    @KafkaListener(id = "${t1.kafka.consumer.t1_demo_transaction_accept}",
            topics = "${t1.kafka.topic.t1_demo_transaction_accept}",
            containerFactory = "transactionAcceptKafkaListenerContainerFactory")
    public void listener(@Payload TransactionAccept transactionAccept,
                         Acknowledgment ack) {
        log.debug("KafkaTransactionAcceptConsumer: Обработка новых сообщений");

        try {
            out.println("TransactionAccept: " + transactionAccept);
            TransactionAcceptResult transactionAcceptResult = new TransactionAcceptResult();
            transactionAcceptResult.setTransactionId(transactionAccept.getTransactionId());
            transactionAcceptResult.setTransactionAmount(transactionAccept.getTransactionAmount());
            transactionAcceptResult.setDateTime(transactionAccept.getDateTime());
            transactionAcceptResult.setAccountId(transactionAccept.getAccountId());
            transactionAcceptResult.setUserId(transactionAccept.getUserId());
            transactionAcceptResult.setAccountBalance(transactionAccept.getAccountBalance());

            LocalDateTime nowMinusInterval = LocalDateTime.now().minusSeconds(transactionInterval);

            Long userTransactionsCount = transactionService.userTransactionsCountInInterval(transactionAccept.getUserId(), nowMinusInterval);
            if (userTransactionsCount > maxTransactionsPerInterval) {
                transactionAcceptResult.setStatus("BLOCKED");
            } else {
                out.println();
                if (transactionAccept.getAccountBalance().compareTo(transactionAccept.getTransactionAmount()) < 0) {
                    transactionAcceptResult.setStatus("REJECTED");
                } else {
                    transactionAcceptResult.setStatus("ACCEPTED");
                }
            }
            kafkaTemplate.send("t1_demo_transaction_result", UUID.randomUUID().toString(), transactionAcceptResult);
            kafkaTemplate.flush();
        } finally {
            ack.acknowledge();
        }

        log.debug("KafkaTransactionAcceptConsumer: записи обработаны");
    }
}
