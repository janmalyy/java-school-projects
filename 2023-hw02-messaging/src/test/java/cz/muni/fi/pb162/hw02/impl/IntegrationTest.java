package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import cz.muni.fi.pb162.hw02.mesaging.client.Consumer;
import cz.muni.fi.pb162.hw02.mesaging.client.Producer;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class IntegrationTest extends TestBase {

    private Broker broker;
    private Producer producer;
    private Consumer consumer;

    @BeforeEach
    public void setup() {
        broker = Messaging.broker();
        producer = Messaging.producer(broker);
        consumer = Messaging.consumer(broker);

        // Populate broker with some data
        broker.push(List.of(
                msg(TOPIC_HOUSE, Map.of("name", "Tom")),
                msg(TOPIC_HOUSE, Map.of("name", "Jerry")),
                msg(TOPIC_HOUSE, Map.of("name", "Butch")),
                msg(TOPIC_GARDEN, Map.of("name", "Nibbles"))
        ));
    }

    @Test
    public void shouldListInitialTopics() {
        // then
        softly.assertThat(broker.listTopics())
                .describedAs("Topics from broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);
        softly.assertThat(consumer.listTopics())
                .describedAs("Topics from consumer")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);
        softly.assertThat(producer.listTopics())
                .describedAs("Topics from producer")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);
    }

    /**
     * At the start of this test the Broker contains the following messages
     *
     * House (3): Tom, Jerry, Butch
     * Garden (1): Nibbles
     *
     * The test does the following
     * 1) Read 3 messages from House and 3 messages from Garden
     *  - store consumer offsets (x)
     *  - Verify 4 messages were read (there are no overlaps, however only 1 message is in Garden)
     *  - Verify 4 unique message IDs were read
     *  - Verify messages with key "name" and values "Tom", "Jerry", "Butch" and "Nibbles"  were read
     */
    @Test
    public void shouldConsumeAllData() {
        // when
        var consumed = consumer.consume(3, TOPIC_HOUSE, TOPIC_GARDEN);
        var ids = consumed.stream().map(Message::id).collect(toSet());
        var topics = consumed.stream().map(Message::topics).flatMap(Set::stream).toList();
        var names = consumed.stream()
                .map(Message::data).map(d -> d.get("name").toString()).collect(toSet());
        // then
        softly.assertThat(consumed)
                .describedAs("Messages consumed from topics")
                .hasSize(4);
        softly.assertThat(ids)
                .describedAs("Unique IDs of consumed messages")
                .hasSameSizeAs(consumed);
        softly.assertThat(topics)
                .describedAs("Topics of consumed messages")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_HOUSE, TOPIC_HOUSE, TOPIC_GARDEN);
        softly.assertThat(names)
                .describedAs("Names extracted from consumed messages")
                .hasSameSizeAs(consumed)
                .containsExactlyInAnyOrder("Tom", "Jerry", "Butch", "Nibbles");
    }

    /**
     * At the start of this test the Broker contains the following messages
     *
     * House (3): Tom, Jerry, Butch
     * Garden (1): Nibbles
     *
     * The test does the following
     *
     * 1) Produce 3 messages. After which the Broker contains following messages
     *
     * House (5): Tom, Jerry, Butch, time, lunch
     * Garden (2): Nibbles, time
     * Patio (3) time, lunch, Toodles
     *
     * 2) Read 2 messages from House and 2 messages from Patio
     *  - store consumer offsets (x)
     *  - Verify 4 messages were read (there are no overlap between first 2 messages stored in House and in Patio)
     *  - Verify 4 unique message IDs were read
     *  - Verify consumer offsets contain entries for House and Patio
     *
     * 3) Read 2 messages from Garden
     *  - Verify 2 messages were read
     *  - Verify 2 unique message IDs were read
     *  - Verify consumer offsets contain entries for House, Patio and Garden
     *
     * 4) Read 2 messages from House and 2 messages from Patio
     *  - Verify 3 messages were read (there is a common message in next 2 messages stored in House and in Patio)
     *  - Verify 3 unique message IDs were read
     *  - Verify consumer offsets contain entries for House, Patio and Garden
     *
     *  5) Read 1 message from House
     *  - Verify 1 message was read
     *  - Verify consumer offsets contain entries for House, Patio and Garden
     *
     *  5) Restore consumer offsets (x)
     *   - Verify consumer offsets contain entries for House and Patio
     *
     *  6) Read 2 messages from Patio
     *  - Verify 1 message was read (there was only 1 remaining unread message in Patio)
     *  - Verify consumer offset contains entries for House and Patio
     */
    @Test
    public void shouldProduceAndConsumeAdditionalMessage() {
        // when
        var produced = producer.produce(List.of(
            msg(Set.of(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO), Map.of("time", 12)),
            msg(Set.of(TOPIC_HOUSE, TOPIC_PATIO), Map.of("lunch", true)),
            msg(Set.of(TOPIC_PATIO), Map.of("name", "Toodles"))
        ));
        // then
        softly.assertThat(broker.listTopics())
                .describedAs("Topics from broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO);
        softly.assertThat(consumer.listTopics())
                .describedAs("Topics from consumer")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO);
        softly.assertThat(producer.listTopics())
                .describedAs("Topics from producer")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO);
        softly.assertThat(produced)
                .describedAs("Messages returned by producer")
                .hasSize(3);
        softly.assertThat(produced.stream().map(Message::id).collect(toSet()))
                .describedAs("Unique IDs of produced messages")
                .hasSameSizeAs(produced);

        // when
        var fstConsumed = consumer.consume(2, TOPIC_HOUSE, TOPIC_PATIO);
        var offsets = Map.copyOf(consumer.getOffsets());
        // then
        softly.assertThat(offsets)
                .describedAs("Consumer offset after consuming messages from '%s' and '%s'", TOPIC_HOUSE, TOPIC_PATIO)
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO);
        softly.assertThat(fstConsumed)
                .describedAs("Messages consumed from '%s' and '%s'", TOPIC_HOUSE, TOPIC_PATIO)
                .hasSize(4);
        softly.assertThat(fstConsumed.stream().map(Message::id).collect(toSet()))
                .describedAs("IDs of messages consumed from '%s' and '%s'", TOPIC_HOUSE, TOPIC_PATIO)
                .hasSameSizeAs(fstConsumed);

        // when
        var sndConsumed = consumer.consume(2, TOPIC_GARDEN);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset after next consumption from '%s'", TOPIC_GARDEN)
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO, TOPIC_GARDEN);
        softly.assertThat(sndConsumed)
                .describedAs("Messages consumed from '%s'", TOPIC_GARDEN)
                .hasSize(2);
        softly.assertThat(sndConsumed.stream().map(Message::id).collect(toSet()))
                .describedAs("IDs of messages consumed from '%s'", TOPIC_GARDEN)
                .hasSameSizeAs(sndConsumed);

        // when
        var thrdConsumed = consumer.consume(2, TOPIC_HOUSE, TOPIC_PATIO);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset after next consumption from '%s' and '%s", TOPIC_HOUSE, TOPIC_PATIO)
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO, TOPIC_GARDEN);
        softly.assertThat(thrdConsumed)
                .describedAs("Messages consumed from '%s' and '%s'", TOPIC_HOUSE, TOPIC_PATIO)
                .hasSize(3);
        softly.assertThat(thrdConsumed.stream().map(Message::id).collect(toSet()))
                .describedAs("IDs of messages consumed from '%s' and %s", TOPIC_HOUSE, TOPIC_PATIO)
                .hasSameSizeAs(thrdConsumed);

        // when
        var frthConsumed = consumer.consume(1, TOPIC_HOUSE);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset after next consumption from '%s'", TOPIC_HOUSE)
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO, TOPIC_GARDEN);
        softly.assertThat(frthConsumed)
                .describedAs("Messages consumed from '%s'", TOPIC_HOUSE)
                .hasSize(1);

        // when
        consumer.setOffsets(offsets);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset after set'")
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO);

        // when
        var fthConsumed = consumer.consume(1, TOPIC_PATIO);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset after next consumption from '%s'", TOPIC_PATIO)
                .containsOnlyKeys(TOPIC_HOUSE, TOPIC_PATIO);
        softly.assertThat(fthConsumed)
                .describedAs("Messages consumed from '%s'", TOPIC_GARDEN)
                .hasSize(1);
    }


    private Condition<String> isOneOf(String... names) {
        return new Condition<>(s -> Arrays.asList(names).contains(s), "Has nam from '%s' topic", TOPIC_HOUSE);
    }

}
