package cz.muni.fi.pb162.hw02.mesaging.client;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;

import java.util.Collection;

/**
 * Messaging client
 */
public interface Client  {

    /**
     * @return broker object associated with this client
     */
    Broker broker();

    /**
     * Lists topics currently registered by the broker.
     * A registered topic is any topic which was previously used to store data
     *
     * @return topics registered by the broker
     */
    Collection<String> listTopics();

}
