package cz.muni.fi.pb162.hw02.mesaging.client;

import cz.muni.fi.pb162.hw02.mesaging.broker.Message;

import java.util.Collection;

/**
 * Message producer allows sending messages to the Broker.
 *
 * Note: consumer also needs to internally manage topic offsets.
 */
public interface Producer extends Client {

    /**
     * Send a message to the broker
     *
     * @param message message to send
     * @return Equivalent message object as returned by broker
     */
    Message produce(Message message);

    /**
     * Send messages to the broker
     *
     * @param messages messages to send
     * @return Equivalent message objects as returned by broker
     */
    Collection<Message> produce(Collection<Message> messages);

}
