package cn.noload.nas.config.consumer;


import cn.noload.nas.config.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQTransactionConsumerConfiguration {

    private final ApplicationProperties applicationProperties;
    private final TransactionConsumerListener transactionConsumerListener;

    @Value("${spring.application.name}")
    private String applicationName;

    public RocketMQTransactionConsumerConfiguration(
        ApplicationProperties applicationProperties,
        TransactionConsumerListener transactionConsumerListener) {
        this.applicationProperties = applicationProperties;
        this.transactionConsumerListener = transactionConsumerListener;
    }

    @Bean
    @Qualifier("transactionConsumer")
    public DefaultMQPushConsumer transactionConsumer () throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(StringUtils.join(applicationProperties.getRocket().getHosts(), ";"));
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setConsumeThreadMin(4);
        consumer.setConsumeThreadMax(8);
        consumer.setConsumerGroup(applicationName);
        consumer.subscribe("transaction", applicationName);
        consumer.registerMessageListener(transactionConsumerListener);
        consumer.start();
        return consumer;
    }
}
