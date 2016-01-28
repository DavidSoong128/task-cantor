package com.horizon.cantor.transfer.mq;

import com.horizon.cantor.transfer.handler.TransferMsgHandler;
import com.horizon.mqclient.api.Message;
import com.horizon.mqclient.core.consumer.KafkaHighConsumer;
import com.horizon.mqclient.core.producer.KafkaClientProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 14:18
 * @see
 * @since : 1.0.0
 */
public class TransferClientManager implements ClientManager{

    private Logger logger = LoggerFactory.getLogger(TransferClientManager.class);

    private KafkaClientProducer producer;
    private KafkaHighConsumer   consumer;

    private TransferClientManager(){
        producer = KafkaClientProducer.kafkaClientProducer();
        consumer = KafkaHighConsumer.kafkaHighConsumer();
    }

    private static class ClientManager{
        private static TransferClientManager clientManager = new TransferClientManager();
    }

    public static TransferClientManager clientManagerHolder(){
        return ClientManager.clientManager;
    }

    @Override
    public void start(String topic) {
        consumer.subscribe(topic,new TransferMsgHandler());
        logger.info("subscribe topic :{}",topic);
    }

    @Override
    public void callback(String topic,byte[] taskMsg) {
        Message message = new Message(topic,taskMsg);
        producer.send(message);
    }
    @Override
    public void shutdown(){
        consumer.close();
        producer.close();
        logger.info("transfer client stop!");
    }
}
