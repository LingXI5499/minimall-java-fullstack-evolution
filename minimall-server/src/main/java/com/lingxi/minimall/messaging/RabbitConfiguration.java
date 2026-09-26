package com.lingxi.minimall.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 明确展示 Exchange、Queue、Binding 和死信队列之间的关系。 */
@Configuration
public class RabbitConfiguration {
    public static final String EXCHANGE = "minimall.orders";
    public static final String ROUTING_KEY = "order.created";
    public static final String QUEUE = "minimall.order.created";
    public static final String DEAD_EXCHANGE = "minimall.orders.dead";
    public static final String DEAD_QUEUE = "minimall.order.created.dead";

    @Bean DirectExchange orderExchange() { return new DirectExchange(EXCHANGE, true, false); }
    @Bean DirectExchange deadExchange() { return new DirectExchange(DEAD_EXCHANGE, true, false); }
    @Bean Queue orderQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY).build();
    }
    @Bean Queue deadQueue() { return QueueBuilder.durable(DEAD_QUEUE).build(); }
    @Bean Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ROUTING_KEY);
    }
    @Bean Binding deadBinding(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(ROUTING_KEY);
    }
}
