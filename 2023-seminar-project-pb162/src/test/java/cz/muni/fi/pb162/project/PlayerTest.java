package cz.muni.fi.pb162.project;

import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alzbeta Strompova
 */
class PlayerTest {

    @Test
    void attributesAndMethods() {
        BasicRulesTester.attributesAmount(Player.class, 2);
        BasicRulesTester.methodsAmount(Player.class, 5);
        BasicRulesTester.attributesFinal(Player.class, 2);
    }

    @Test
    void getters() {
        var player = new Player("Mat", Color.WHITE);
        assertEquals("Mat", player.name());
        assertEquals(Color.WHITE, player.color());
        var player2 = new Player("Pat", Color.BLACK);
        assertEquals("Pat", player2.name());
        assertEquals(Color.BLACK, player2.color());
    }

    @Test
    void testToString() {
        assertEquals("Mat-WHITE", new Player("Mat", Color.WHITE).toString());
        assertEquals("Pat-BLACK", new Player("Pat", Color.BLACK).toString());
    }
}
