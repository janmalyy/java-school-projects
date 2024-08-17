package cz.muni.fi.pb162.hw02.mesaging.broker;

import java.util.Collection;
import java.util.Map;

/**
 * This class represents a messaging broker. A database of sorts used to store and deliver messages.
 * The broker stores messages in topics (retrievable from message object via {@link Message#topics()})
 *
 * In real world, this is the part of the system which would be running as a server.
 * Clients would then connect to the broker over the network.
 * For our purpose the broker will be implemented as part of the same codebase.
 */
public interface Broker {

    /**
     * Lists topics currently registered by the broker.
     * A registered topic is any topic which was previously used to store messages
     *
     * @return registered topics
     */
    Collection<String> listTopics();


    /**
     * Stores messages to their destinations (as defined by {@link Message#topics()}
     *
     *  The storage procedure for each message goes as follows:
     *  1) The message is assigned a unique identifier
     *  2) List of destination topics is extracted from the message
     *  3) Message is stored in each topic
     * <br>
     * Note: How exactly are the messages stored internally is up to you.
     * Note: Don't assume anything besides the fact that messages are instances of {@link Message}
     *
     * @param messages messages to store
     * @return Collection of message objects with populated identifiers
     */
    Collection<Message> push(Collection<Message> messages);

    /**
     * Polls requested number of unread messages from this broker's topics.
     * <br>
     *
     * Message is considered read if its {@code id} is lower or equal to the last read id for a particular topic
     * in offset definition. If no such entry exists,then all messages from that topic are considered unread.
     * <br>
     *
     * The offset definition is a map where
     *      - the key is a topic name
     *      - the value is an id of the last message read in that topic
     * <br>
     *
     * If a message is stored in multiple topics then it is not duplicated in the returned collection.
     *
     * @param offsets offset definition
     * @param num maximal number of messages to fetch from each topic
     * @param topics list of topics
     * @return collection of unique messages
     */
    Collection<Message> poll(Map<String, Long> offsets, int num, Collection<String> topics);
}
