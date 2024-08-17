package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import cz.muni.fi.pb162.hw02.mesaging.client.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jan Maly
 */
public class MessageConsumer implements Consumer {
    private final Broker broker;
    private Map<String, Long> offsets = new HashMap<>();

    /**
     * constructor
     * @param broker broker associated with this consumer
     */
    public MessageConsumer(Broker broker) {
        this.broker = broker;
    }

    @Override
    public Broker broker() {
        return broker;
    }

    @Override
    public Collection<String> listTopics() {
        return broker.listTopics();
    }

    @Override
    public Map<String, Long> getOffsets() {
        //must be new HashMap<> in order to remain immutable, same in the setter
        // could be better to use unmodifiableCollection or copyOf
        return new HashMap<>(offsets);
    }
    @Override
    public void setOffsets(Map<String, Long> offsets) {
        this.offsets = new HashMap<>(offsets);
    }
    @Override
    public void clearOffsets() {
        offsets.clear();
    }

    @Override
    public void updateOffsets(Map<String, Long> offsets) {
        for (String topic : offsets.keySet()) {
            this.offsets.put(topic, offsets.get(topic));
        }
    }

    @Override
    public Collection<Message> consume(int num, String... topics) {
        Collection<Message> consumedMessages = broker.poll(offsets, num, new ArrayList<>(Arrays.asList(topics)));
        //the rest is only about the offsets update
        Map<String, Long> offsetsToBeUpdated = new HashMap<>();
        for (String topic: topics) {
            boolean topicIsPresentInMessages = false;
            for (Message message : consumedMessages) {
                if (message.topics().contains(topic)) {
                    topicIsPresentInMessages = true;
                    break;
                }
            }
            if (!topicIsPresentInMessages) {
                break;
            }
            //choose only messages which contain the current topic
            List<Message> topicMessages = consumedMessages.stream()
                    .filter(message -> message.topics().contains(topic))
                    .sorted(Comparator.comparingLong(Message::id))
                    .toList();
            //and depending on num choose the right offset
            if (num > topicMessages.size()) {
                offsetsToBeUpdated.put(topic, topicMessages.get(topicMessages.size()-1).id());
            } else {
                offsetsToBeUpdated.put(topic, topicMessages.get(num-1).id());
            }
        }
        updateOffsets(offsetsToBeUpdated);
        return consumedMessages;
    }

    @Override
    public Collection<Message> consume(Map<String, Long> offsets, int num, String... topics) {
        return broker.poll(offsets, num, new ArrayList<>(Arrays.asList(topics)));
    }
}
