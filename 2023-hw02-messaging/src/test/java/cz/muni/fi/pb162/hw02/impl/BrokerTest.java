package cz.muni.fi.pb162.hw02.impl;

import cz.muni.fi.pb162.hw02.mesaging.broker.Broker;
import cz.muni.fi.pb162.hw02.mesaging.broker.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class BrokerTest extends TestBase {
    private Broker broker;

    @BeforeEach
    public void setup() {
        broker = Messaging.broker();
    }

    @Test
    public void shouldBeEmpty() {
        assertThat(broker.listTopics()).isEmpty();
    }

    @Test
    public void shouldReturnMessageWhenStoring() {
        // given
        Map<String, Object> data = Map.of("name", "Tom", "age", 3);
        // when
        var batch = push(msg(Set.of(TOPIC_HOUSE, TOPIC_GARDEN), data));
        var tom = filterByName(batch, "Tom");
        // then
        assertThat(tom).isNotNull();
        softly.assertThat(tom.id())
                .describedAs("Id of stored message")
                .isNotNull();
        softly.assertThat(tom.topics())
                .describedAs("Topics of stored message")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);
        softly.assertThat(tom.data())
                .describedAs("Tata of stored message")
                .containsAllEntriesOf(data);

        // given
        Map<String, Object> data2 = Map.of("name", "Jerry", "age", 2);
        // when
        batch = push(msg(Set.of(TOPIC_HOUSE), data2), msg(Set.of(TOPIC_HOUSE), data2));
        // then
        softly.assertThat(batch)
                .describedAs("Returned messages with same topics and data stored by broker")
                .hasSize(2);
        softly.assertThat(batch.stream().map(Message::id).collect(toSet()))
                .describedAs("IDs of messages with same topics and data stored by broker")
                .hasSize(2);
        softly.assertThat(batch.stream().map(Message::data).toList())
                .describedAs("Data of each message")
                .allMatch(data2::equals);
        softly.assertThat(batch.stream().map(Message::topics).toList())
                .describedAs("Data of each message")
                .allSatisfy(topics -> assertThat(topics).containsExactly(TOPIC_HOUSE));
    }

    @Test
    public void shouldStoreMessagesInSingleTopic() {
        // when
        var polled = broker.poll(Map.of(), 1, List.of(TOPIC_HOUSE));
        var topics = broker.listTopics();
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from an empty broker")
                .isEmpty();
        softly.assertThat(topics)
                .describedAs("Topics listed from an empty broker")
                .isEmpty();

        // when
        var batch = push(msg(TOPIC_HOUSE, Map.of("name", "Tom")));
        var tom = filterByName(batch, "Tom");
        polled = broker.poll(Map.of(), 1, List.of(TOPIC_HOUSE));
        topics = broker.listTopics();
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from '%s' after pushing Tom", TOPIC_HOUSE)
                .containsExactlyInAnyOrder(tom);
        softly.assertThat(topics)
                .describedAs("Topics returned by the broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE);


        // when
        batch = push(msg(TOPIC_HOUSE, Map.of("name", "Jerry")));
        polled = broker.poll(Map.of(), 1, List.of(TOPIC_HOUSE));
        var jerry = filterByName(batch, "Jerry");
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from '%s' after pushing Jerry", TOPIC_HOUSE)
                .containsExactlyInAnyOrder(tom);

        // when
        polled = broker.poll(Map.of(), 2, List.of());
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from no topics")
                .isEmpty();

        // when
        polled = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE));
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from topic '%s'", TOPIC_HOUSE)
                .containsExactlyInAnyOrder(tom, jerry);

        // when
        polled = broker.poll(Map.of(TOPIC_HOUSE, tom.id()), 1, List.of(TOPIC_HOUSE));
        // then
        softly.assertThat(polled)
                .describedAs("Message polled from topic with offset")
                .containsExactlyInAnyOrder(jerry);

        // when
        polled = broker.poll(Map.of(TOPIC_HOUSE, jerry.id()), 1, List.of(TOPIC_HOUSE));
        // then
        softly.assertThat(polled)
                .describedAs("Message polled from topic with maximal offset.")
                .isEmpty();

        // when
        polled = broker.poll(Map.of(TOPIC_HOUSE, jerry.id()), 2, List.of(TOPIC_HOUSE));
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from topic with maximal offset.")
                .isEmpty();
    }

    @Test
    public void shouldStoreMessagesInMultipleTopics() {
        // when
        var polled = broker.poll(Map.of(), 1, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        var topics = broker.listTopics();
        // then
        softly.assertThat(polled).isEmpty();
        softly.assertThat(topics).isEmpty();

        // when
        polled = broker.poll(Map.of(), 1, List.of(TOPIC_GARDEN));
        topics = broker.listTopics();
        // then
        softly.assertThat(polled).isEmpty();
        softly.assertThat(topics).isEmpty();

        // when
        var batch = push(
                msg(TOPIC_HOUSE, Map.of("name", "Tom")),
                msg(TOPIC_GARDEN, Map.of("name", "Jerry"))
        );
        polled = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        topics = broker.listTopics();
        var tom = filterByName(batch, "Tom");
        var jerry = filterByName(batch, "Jerry");
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(tom, jerry);
        softly.assertThat(topics).containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);

        // when
        polled = broker.poll(Map.of(), 1, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        topics = broker.listTopics();
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(tom, jerry);
        softly.assertThat(topics).containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);

        // when
        polled = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE));
        topics = broker.listTopics();
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(tom);
        softly.assertThat(topics).containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);


        // when
        polled = broker.poll(Map.of(), 2, List.of(TOPIC_GARDEN));
        topics = broker.listTopics();
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(jerry);
        softly.assertThat(topics).containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);

        // when
        batch = push(msg(TOPIC_GARDEN, Map.of("name", "Nibbles")));
        polled = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        var nibbles = filterByName(batch, "Nibbles");
        // then
        softly.assertThat(batch).hasSize(1);
        softly.assertThat(polled).containsExactlyInAnyOrder(tom, jerry, nibbles);


        // when
        polled = broker.poll(Map.of(TOPIC_GARDEN, jerry.id()), 1, List.of(TOPIC_GARDEN));
        // then
        softly.assertThat(nibbles.data()).containsEntry("name", "Nibbles");
        softly.assertThat(polled).containsExactlyInAnyOrder(nibbles);

        // when
        polled = broker.poll(Map.of(TOPIC_GARDEN, jerry.id()), 2, List.of(TOPIC_GARDEN));
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(nibbles);

        // when
        polled = broker.poll(Map.of(TOPIC_GARDEN, nibbles.id()), 2, List.of(TOPIC_GARDEN));
        // then
        softly.assertThat(polled).isEmpty();

        // when
        polled = broker.poll(Map.of(TOPIC_GARDEN, jerry.id(), TOPIC_HOUSE, tom.id()), 2, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        // then
        softly.assertThat(polled).containsExactlyInAnyOrder(nibbles);
    }

    @Test
    public void shouldStoreMessagesWithMultipleTopics() {
        // when
        var batch = push(
                msg(Set.of(TOPIC_HOUSE, TOPIC_GARDEN), Map.of("name", "Tom")),
                msg(TOPIC_GARDEN, Map.of("name", "Jerry"))
        );
        var polled = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE, TOPIC_GARDEN));
        var tom = filterByName(batch, "Tom");
        var jerry = filterByName(batch, "Jerry");
        // then
        softly.assertThat(polled)
                .describedAs("Pushed messages")
                .hasSize(2)
                .containsExactlyInAnyOrder(tom, jerry);
        softly.assertThat(broker.listTopics())
                .describedAs("Topics listed by broker")
                .containsExactlyInAnyOrder(TOPIC_HOUSE, TOPIC_GARDEN);


        // when
        batch = broker.poll(Map.of(), 2, List.of(TOPIC_HOUSE));
        var tomFromHouse = filterByName(batch, "Tom");
        batch = broker.poll(Map.of(), 2, List.of(TOPIC_GARDEN));
        var tomFromGarden = filterByName(batch, "Tom");
        // then
        softly.assertThat(tomFromHouse.id()).isEqualTo(tomFromGarden.id());
        softly.assertThat(tomFromHouse.topics()).isEqualTo(tomFromGarden.topics());
        softly.assertThat(tomFromHouse.data()).isEqualTo(tomFromGarden.data());
    }

    @Test
    public void shouldCorrectlyPollMessagesWithOverlappingTopics() {
        // when
        push(
                msg(Set.of(TOPIC_HOUSE, TOPIC_GARDEN, TOPIC_PATIO), Map.of()),
                msg(Set.of(TOPIC_GARDEN, TOPIC_PATIO), Map.of()),
                msg(Set.of(TOPIC_HOUSE, TOPIC_PATIO, "elsewhere"), Map.of())
        );
        var polled = broker.poll(Map.of(),2, Set.of(TOPIC_HOUSE, TOPIC_PATIO));
        // then
        softly.assertThat(polled)
                .describedAs("Messages polled from broker")
                .hasSize(3);
    }

    private Collection<Message> push(Message... messages) {
        return broker.push(Arrays.asList(messages));
    }

}
