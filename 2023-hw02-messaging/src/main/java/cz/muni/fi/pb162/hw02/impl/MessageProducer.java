package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import cz.muni.fi.pb162.hw02.mesaging.client.Producer;

import java.util.Collection;
import java.util.Set;

/**
 * @param broker associated with this producer
 * @author Jan Maly
 */
public record MessageProducer(Broker broker) implements Producer {

    @Override
    public Collection<String> listTopics() {
        return broker.listTopics();
    }

    @Override
    public Message produce(Message message) {
        //this whole code is only about converting set into message and otherwise
        Set<Message> messageInSet = Set.of(message);
        Collection<Message> collection = broker.push(messageInSet);
        for (Message messageNotInCollection : collection) {
            return messageNotInCollection;
        }
        return null;
    }

    @Override
    public Collection<Message> produce(Collection<Message> messages) {
        return broker.push(messages);
    }
}
