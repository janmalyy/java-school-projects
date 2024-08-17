package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.client.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsumerTest extends TestBase {

    protected TestBroker broker;
    private Consumer consumer;


    @BeforeEach
    public void setup() {
        broker = new TestBroker();
        consumer = Messaging.consumer(broker);
    }

    @Test
    public void shouldReturnTopics() {
        // then
        softly.assertThat(consumer.listTopics()).isEmpty();

        // when
        broker.setTopics("house", "garden");
        // then
        softly.assertThat(consumer.listTopics())
                .describedAs("Topics returned by producer")
                .containsExactlyInAnyOrder("house", "garden");

        // when
        broker.setTopics("pool");
        // then
        softly.assertThat(consumer.listTopics())
                .describedAs("Topics returned by producer")
                .containsExactlyInAnyOrder("pool");
    }


    @Test
    public void shouldHaveSecureOffsets() {
        // given
        var offsets = consumer.getOffsets();
        // when
        try {
            offsets.put("cheeseCount", Long.MAX_VALUE);
        } catch (Exception e) {
            // ignore
        }
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets")
                .isEmpty();
    }

    @Test
    public void shouldConsumeNothingFromEmptyBroker() {
        // when
        var consumed = consumer.consume(3, "house");
        // then
        softly.assertThat(consumed)
                .describedAs("Messages from empty broker")
                .isEmpty();
    }


    @Test
    public void shouldHaveEmptyOffsetForEmptyBroker(){
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offset before consuming any messages")
                .isEmpty();
    }

    @Test
    public void shouldNotUpdateOffset() {
        // when
        broker.setBatch(
                msg(1L, TOPIC_HOUSE, Map.of())
        );
        var consumed = consumer.consume(Map.of(TOPIC_HOUSE, 1L), 1, TOPIC_HOUSE);
        // then
        softly.assertThat(consumed)
                .describedAs("Messages consumed with explicit offset")
                .hasSize(1)
                .allSatisfy(msg -> assertThat(msg.id()).isEqualTo(1L));
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets")
                .isEmpty();
    }

    @Test
    public void shouldHaveCorrectOffsetForEachTopicAfterMultipleConsumptions() {
        // when
        broker.setBatch(
                msg(1L, TOPIC_HOUSE, Map.of()),
                msg(10L, TOPIC_GARDEN, Map.of())
        );
        consumer.consume(1, TOPIC_HOUSE, TOPIC_GARDEN);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after consuming single message from each topic")
                .containsExactlyInAnyOrderEntriesOf(Map.of(TOPIC_HOUSE, 1L, TOPIC_GARDEN, 10L));

        // when
        broker.setBatch(
                msg(2L, TOPIC_HOUSE, Map.of())
        );
        consumer.consume(1, TOPIC_HOUSE);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after consuming another message from each topic")
                .containsExactlyInAnyOrderEntriesOf(Map.of(TOPIC_HOUSE, 2L, TOPIC_GARDEN, 10L));

        // when
        consumer.updateOffsets(Map.of(TOPIC_HOUSE, 1L));
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after updating offset for topic '%s'", TOPIC_HOUSE)
                .containsExactlyInAnyOrderEntriesOf(Map.of(TOPIC_HOUSE, 1L, TOPIC_GARDEN, 10L));

        // when
        consumer.setOffsets(Map.of(TOPIC_HOUSE, 2L));
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after setting custom offset")
                .containsExactlyInAnyOrderEntriesOf(Map.of(TOPIC_HOUSE, 2L));

        // when
        consumer.clearOffsets();
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after clear")
                .isEmpty();

        // when
        broker.setBatch(
                msg(1L, TOPIC_HOUSE, Map.of()),
                msg(2L, TOPIC_HOUSE, Map.of()),
                msg(10L, TOPIC_GARDEN, Map.of())
        );
        consumer.consume(2, TOPIC_HOUSE, TOPIC_GARDEN);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets after consuming two messages for each topic")
                .containsExactlyInAnyOrderEntriesOf(Map.of(TOPIC_HOUSE, 2L, TOPIC_GARDEN, 10L));
    }

    @Test
    public void shouldConsumeNoMessagesFromEmptyBroker() {
        // when
        broker.setBatch();
        var offsets = Map.copyOf(consumer.getOffsets());
        var consumed = consumer.consume(2, TOPIC_HOUSE);
        // then
        softly.assertThat(consumed)
                .describedAs("Consuming from empty broker")
                .isEmpty();
        softly.assertThat(broker.lastPollOffsets())
                .describedAs("Last offsets used to query broker")
                .containsExactlyInAnyOrderEntriesOf(offsets);
        softly.assertThat(broker.lastPollNum())
                .describedAs("Last message number used to query broker")
                .isEqualTo(2);
        softly.assertThat(broker.lastPollTopics())
                .describedAs("Last topics used to query broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE);
    }

    @Test
    public void shouldConsumeNoMessagesFromEmptyTopic() {
        // when
        broker.setBatch();
        var offsets = Map.copyOf(consumer.getOffsets());
        var consumed = consumer.consume(2, TOPIC_HOUSE);
        // then
        softly.assertThat(consumed)
                .describedAs("Consuming from empty topic")
                .isEmpty();
        softly.assertThat(broker.lastPollOffsets())
                .describedAs("Last offsets used to query broker")
                .containsExactlyInAnyOrderEntriesOf(offsets);
        softly.assertThat(broker.lastPollNum())
                .describedAs("Last message number used to query broker")
                .isEqualTo(2);
        softly.assertThat(broker.lastPollTopics())
                .describedAs("Last topics used to query broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE);
    }


    @Test
    public void shouldConsumeSingleMessageFromTopic() {
        // when
        broker.setBatch(msg(1L, TOPIC_GARDEN, Map.of()));
        var offsets = Map.copyOf(consumer.getOffsets());
        var consumed = consumer.consume(1, TOPIC_GARDEN);
        // then
        softly.assertThat(consumed)
                .describedAs("Consuming single message from a single topic with multiple messages")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(msg(1L, TOPIC_GARDEN, Map.of()));
        softly.assertThat(broker.lastPollOffsets())
                .describedAs("Last offsets used to query broker")
                .containsExactlyInAnyOrderEntriesOf(offsets);
        softly.assertThat(broker.lastPollNum())
                .describedAs("Last message number used to query broker")
                .isEqualTo(1);
        softly.assertThat(broker.lastPollTopics())
                .describedAs("Last topics used to query broker")
                .containsExactlyInAnyOrder(TOPIC_GARDEN);

    }

    @Test
    public void shouldConsumeMultipleMessages() {
        // when
        broker.setBatch(
                msg(1L, TOPIC_GARDEN, Map.of()),
                msg(2L, TOPIC_GARDEN, Map.of()),
                msg(10L, TOPIC_HOUSE, Map.of()),
                msg(11L, TOPIC_HOUSE, Map.of())

        );
        var offsets = Map.copyOf(consumer.getOffsets());
        var consumed = consumer.consume(2, TOPIC_HOUSE, TOPIC_GARDEN);
        // then
        softly.assertThat(consumed)
                .describedAs("Consuming multiple messages from a multiple topics")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        msg(1L, TOPIC_GARDEN, Map.of()),
                        msg(2L, TOPIC_GARDEN, Map.of()),
                        msg(10L, TOPIC_HOUSE, Map.of()),
                        msg(11L, TOPIC_HOUSE, Map.of())
                );
        softly.assertThat(broker.lastPollOffsets())
                .describedAs("Last offsets used to query broker")
                .containsExactlyInAnyOrderEntriesOf(offsets);
        softly.assertThat(broker.lastPollNum())
                .describedAs("Last message number used to query broker")
                .isEqualTo(2);
        softly.assertThat(broker.lastPollTopics())
                .describedAs("Last topics used to query broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);

        // when
        broker.setBatch(
                msg(3L, TOPIC_GARDEN, Map.of())
        );
        offsets = Map.copyOf(consumer.getOffsets());
        consumed = consumer.consume(1, TOPIC_GARDEN, TOPIC_HOUSE);
        // then
        softly.assertThat(consumed)
                .describedAs("Consuming single message from a multiple topics when one is exhausted")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        msg(3L, TOPIC_GARDEN, Map.of())
                );
        softly.assertThat(broker.lastPollOffsets())
                .describedAs("Last offsets used to query broker")
                .containsExactlyInAnyOrderEntriesOf(offsets);
        softly.assertThat(broker.lastPollNum())
                .describedAs("Last message number used to query broker")
                .isEqualTo(1);
        softly.assertThat(broker.lastPollTopics())
                .describedAs("Last topics used to query broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);
    }

    /**
     * This tests describes scenario from {@link Consumer#consume(int, String...)}
     */
    @Test
    public void shouldHaveCorrectOffsetsAsDocumented() {
        // when
        broker.setBatch(
                msg(1L, Set.of(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO), Map.of()),
                msg(2L, Set.of(TOPIC_GARDEN, TOPIC_PATIO), Map.of()),
                msg(5L, Set.of(TOPIC_HOUSE, TOPIC_PATIO, "elsewhere"), Map.of())
        );
        consumer.consume(2, TOPIC_HOUSE, TOPIC_PATIO);
        // then
        softly.assertThat(consumer.getOffsets())
                .describedAs("Consumer offsets")
                .containsExactlyInAnyOrderEntriesOf(Map.of(
                        TOPIC_HOUSE, 5L, TOPIC_PATIO, 2L));
    }
}
