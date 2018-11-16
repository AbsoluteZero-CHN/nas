package cn.noload.nas.web.rest;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.stream.IntStream;

public class RocketMQTest {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("niwei_producer_group");
        producer.setNamesrvAddr("10.0.0.202:9876");
        producer.start();

        IntStream.range(0, 1000).forEach(i -> {
            Message message = null;
            try {
                message = new Message(
                    "topic_example_java",   // 消息主题
                    "TagA", // 消息标签
                    ("Hello Java demo RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)  // 消息内容
                );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            SendResult sendResult = null;
            try {
                sendResult = producer.send(message);
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s%n", sendResult);
        });
    }
}
