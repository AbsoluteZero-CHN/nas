package cn.noload.nas.config.consumer;

import cn.noload.nas.domain.TransactionConsumerCheck;
import cn.noload.nas.repository.TransactionConsumerCheckRepository;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Component
public class TransactionConsumerListener implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumerListener.class);

    @Value("${server.port}")
    private Integer port;

    private final TransactionConsumerCheckRepository transactionConsumerCheckRepository;
    private final RestTemplate restTemplate;

    public TransactionConsumerListener(
        TransactionConsumerCheckRepository transactionConsumerCheckRepository,
        @Qualifier("vanillaRestTemplate") RestTemplate restTemplate) {
        this.transactionConsumerCheckRepository = transactionConsumerCheckRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        // RocketMQ 默认发送单条消息
        MessageExt message = list.get(0);
        Optional<TransactionConsumerCheck> transactionConsumerCheck = transactionConsumerCheckRepository.findByTransactionId(message.getTransactionId());
        if(transactionConsumerCheck.isPresent()) {
            // 如果存在, 表示当前消息已经被消费过, 当前消息数据重复消息
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } else {
            MessageBody messageBody = JSON.parseObject(message.getBody(), MessageBody.class);
            // 执行本地回环地址调用策略
            return innerCall(messageBody, message.getTransactionId()) ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS : ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    private boolean innerCall(MessageBody messageBody, String transactionId) {
        // 使用本地回环地址调用, 可以保证不会出现网络丢包现象
        String url = "http://127.0.0.1:" + port + messageBody.getUrl();
        HttpMethod httpMethod = null;
        switch (messageBody.getHttpMethod()) {
            case POST: httpMethod = HttpMethod.POST; break;
            case PUT: httpMethod = HttpMethod.PUT; break;
            case DELETE: httpMethod = HttpMethod.DELETE; break;
        }
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        messageBody.getHeaders().forEach((key, value) -> headers.add(key, value));
        HttpEntity<Object> httpEntity = new HttpEntity<>(messageBody.getBody(), headers);
        try {
            ResponseEntity<Object> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, Object.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                logger.error("执行分布式事务本地回环调用失败: {}, 响应: {}", messageBody, responseEntity);
            }
            TransactionConsumerCheck transactionConsumerCheck = new TransactionConsumerCheck();
            transactionConsumerCheck.setTransactionId(transactionId);
            transactionConsumerCheckRepository.save(transactionConsumerCheck);
        } catch (Exception e) {
            // 记录日志
            logger.error("执行分布式事务本地回环调用失败: {}, 错误信息: {}", messageBody, e);
            return false;
        }
        return false;
    }
}
