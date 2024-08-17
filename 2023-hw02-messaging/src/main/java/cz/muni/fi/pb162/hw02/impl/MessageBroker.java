package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.broker.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jan Maly
 */
public class MessageBroker implements Broker {
    private final HashMap<String, ArrayList<Message>> database = new HashMap<>();

    private static final AtomicLong ATOMIC_LONG_ID = new AtomicLong();

    /**
     * constructor
     */
    public MessageBroker() {
    }

    @Override
    public Collection<String> listTopics() {
        return database.keySet();
    }

    @Override
    public Collection<Message> push(Collection<Message> messages) {
        Collection<Message> remadeMessages = new ArrayList<>();
        for (Message message : messages) {
            Message messageWithId = new MessageImpl(ATOMIC_LONG_ID.incrementAndGet(), message.topics(), message.data());
            remadeMessages.add(messageWithId);
            for (String topic : message.topics()) {
                database.putIfAbsent(topic, new ArrayList<>());
            }
            for (String topic : database.keySet()) {
                if (message.topics().contains(topic)) {
                    database.get(topic).add(messageWithId);
                }
            }
        }
        return remadeMessages;
    }

    @Override
    public Collection<Message> poll(Map<String, Long> offsets, int num, Collection<String> topics) {
        HashSet<Message> messagesToBeReturned = new HashSet<>();
        for (String topic : topics) {
            if (database.get(topic) != null) {
                Long currentOffset = offsets.get(topic);
                if (currentOffset == null) {
                    currentOffset = 0L;
                }
                Long finalCurrentOffset = currentOffset;
                // where magic happens
                // chooses only num messages with higher id than the CurrentOffset is
                List<Message> wantedMessages = database.get(topic).stream()
                        .filter(m -> m.id() > finalCurrentOffset)
                        .sorted(Comparator.comparingLong(Message::id))
                        .limit(num)
                        .toList();
                messagesToBeReturned.addAll(wantedMessages);
            } else {
                break;
            }
        }
        return messagesToBeReturned;
    }
}