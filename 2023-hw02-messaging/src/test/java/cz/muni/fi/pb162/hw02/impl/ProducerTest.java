package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import cz.muni.fi.pb162.hw02.mesaging.client.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ProducerTest extends TestBase {

    private TestBroker broker;
    private Producer producer;

    @BeforeEach
    public void setup() {
        broker = new TestBroker();
        producer = Messaging.producer(broker);
    }

    @Test
    public void shouldReturnTopics() {
        // then
        softly.assertThat(producer.listTopics()).isEmpty();


        // when
        broker.setTopics("house", "garden");
        // then
        softly.assertThat(producer.listTopics())
                .describedAs("Topics returned by producer")
                .containsExactlyInAnyOrder("house", "garden");

        // when
        broker.setTopics("pool");
        // then
        softly.assertThat(producer.listTopics())
                .describedAs("Topics returned by producer")
                .containsExactlyInAnyOrder("pool");
    }

    @Test
    public void shouldStoreMessages() {
        broker.setIDs(1);
        var batch = producer.produce(List.of(
                msg(TOPIC_HOUSE, Map.of("name", "Tom"))
        ));
        var tom = filterByName(batch, "Tom");
        //then
        softly.assertThat(batch.stream().map(Message::id))
                .describedAs("Populated IDs")
                .containsExactlyInAnyOrder(1L);
        softly.assertThat(batch).hasSize(1);
        softly.assertThat(last(broker.messages(), TOPIC_HOUSE))
                .describedAs("Last message in topic '%s'", TOPIC_HOUSE)
                .usingRecursiveComparison()
                .isEqualTo(tom);
        softly.assertThat(broker.messages())
                .describedAs("Messages in broker")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(tom);

        // when
        broker.setIDs(10, 11);
        batch = producer.produce(List.of(
                msg(TOPIC_HOUSE, Map.of("name", "Jerry")),
                msg(TOPIC_GARDEN, Map.of("name", "Nibbles"))
        ));
        var jerry = filterByName(batch, "Jerry");
        var nibbles = filterByName(batch, "Nibbles");// then
        softly.assertThat(batch.stream().map(Message::id))
                .describedAs("Populated IDs")
                .containsExactlyInAnyOrder(10L, 11L);
        softly.assertThat(batch).hasSize(2);
        softly.assertThat(last(broker.messages(), TOPIC_HOUSE))
                .describedAs("Last message in topic '%s'", TOPIC_HOUSE)
                .usingRecursiveComparison()
                .isEqualTo(jerry);
        softly.assertThat(last(broker.messages(), TOPIC_GARDEN))
                .describedAs("Last message in topic '%s'", TOPIC_GARDEN)
                .usingRecursiveComparison()
                .isEqualTo(nibbles);
        softly.assertThat(broker.messages())
                .describedAs("Messages in broker")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(tom, jerry, nibbles);

        // when
        broker.setIDs(42);
        var butch = producer.produce(msg(TOPIC_HOUSE, Map.of("name", "Butch")));
        // then
        softly.assertThat(butch.id())
                .describedAs("Populated ID")
                .isEqualTo(42);
        softly.assertThat(last(broker.messages(), TOPIC_HOUSE))
                .describedAs("Last message in topic '%s'", TOPIC_HOUSE)
                .usingRecursiveComparison()
                .isEqualTo(butch);
        softly.assertThat(broker.messages())
                .describedAs("Messages in broker")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(tom, jerry, nibbles, butch);
    }
}
