package br.com.saga.saga.saga.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DirectMessageListenerContainerBuilder {

    public static DirectMessageListenerContainer build(ConnectionFactory connectionFactory, String exchangeName, CompletableFuture<Map<String, Object>> executionResponseFuture) {
        ObjectMapper objectMapper = new ObjectMapper();
        DirectMessageListenerContainer container = new DirectMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(exchangeName);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            try {
                byte[] body = message.getBody();
                Map<String, Object> responseMap = objectMapper.readValue(body, new TypeReference<>() {});

                executionResponseFuture.complete(responseMap);
            } catch (Exception e) {
                executionResponseFuture.completeExceptionally(e);
            }
        });

        return container;
    }
}
