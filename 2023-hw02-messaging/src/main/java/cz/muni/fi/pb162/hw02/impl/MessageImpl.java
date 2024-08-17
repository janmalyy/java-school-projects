package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Message;

import java.util.Map;
import java.util.Set;

/**
 * just a message unit with all information about it
 *
 * @param id id of the message
 * @param topics topics with witch the message is associated
 * @param data in the message
 */
public record MessageImpl(Long id, Set<String> topics, Map<String, Object> data) implements Message {
}
