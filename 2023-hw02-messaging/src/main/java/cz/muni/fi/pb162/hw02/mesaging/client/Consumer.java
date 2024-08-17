package cz.muni.fi.pb162.hw02.mesaging.client;

import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;

import java.util.Collection;
import java.util.Map;

/**
 * Message consumer allows consuming messages stored by Broker.
 *
 * Note: consumer also needs to internally manage topic offsets.
 */
public interface Consumer extends Client {

    /**
     * Consumes next batch of unread messages from broker.
     * <br>
     *
     * The method internally also updates topic offsets so that only unread messages
     * are requested on consecutive calls of this method.
     * <br>
     *
     * Only offsets for requested topics are updated even when some message also belongs
     * to different topics.
     * <br>
     *
     * Note: The offset update is more complex than it looks. Make sure not to lose any message on
     * consecutive calls.
     *
     * An example (starting with empty offsets)
     *
     * <code>
     *     // Topic     :   Messages
     *     // A         :   #1, #5
     *     // B         :   #1, #2, #3
     *     // C         :   #1, #2, #5
     *     // D         :   #3, #4
     *
     *     // consume 2 messages from topic "A" and topic "C"
     *     consume(2, "A", "C"); // returns {#1, #2, #5}
     *
     *     assertThat(getOffsets()).containsExactlyEntriesOf(Map.of(
     *         "A", 5
     *         "C", 2
     *     )); //  Notice that the last read message from topic "C" is #2!!!
     * </code>
     *
     * @param num maximum number of messages to consume per topic
     * @param topics topics from which to consume messages
     * @return collection of consumed messages
     */
    Collection<Message> consume(int num, String... topics);

    /**
     * Same as {@link #consume(int, String...)} except message offsets are provided explicitly.
     * This method ignores internally stored offsets and requests messages from the broker which
     * are unread according to the provided offsets map.
     *
     * This method does not update internal offsets
     *
     * @param offsets offset map as described in {@link #getOffsets()}
     * @param num maximum number of messages to consume per topic
     * @param topics topics from which to consume messages
     * @return collection of consumed messages
     */
    Collection<Message> consume(Map<String, Long> offsets, int num, String... topics);

    /**
     * Returns offset for each topic as a map.
     * Key in this map is a topic name and value is the id of last message read from that topic
     * <br>
     *
     * See {@link Broker#poll(Map, int, Collection)}
     *
     * @return offset map (topic name : last read message id)
     */
    Map<String, Long> getOffsets();

    /**
     * Set stored offset to given positions
     *
     * @param offsets new offsets (only keys in this map will be kept)
     */
    void setOffsets(Map<String, Long> offsets);

    /**
     * Clear any stored offset
     */
    void clearOffsets();

    /**
     * Update stored offset to given positions
     *
     * @param offsets new offsets (only keys stored in this map will be updated)
     */
    void updateOffsets(Map<String, Long> offsets);
}
