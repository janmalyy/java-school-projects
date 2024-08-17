package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.client.Consumer;
import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.client.Producer;

public final class Messaging {

    private Messaging() {
        // intentionally made private
    }

    /**
     * Creates new message broker capable of storing messages
     *
     * @return broker instance
     */
    public static Broker broker() {
        return new MessageBroker();
    }

    /**
     * Creates messaging client which will produce messages for given broker
     *
     * @param broker broker used to produce messages for
     * @return client instance
     */
    public static Producer producer(Broker broker) {
            return new MessageProducer(broker);
    }

    /**
     * Creates messaging client which will consume messages from given broker
     *
     * @param broker broker to consume messages from
     * @return client instance
     */
    public static Consumer consumer(Broker broker) {
            return new MessageConsumer(broker);
    }
}
