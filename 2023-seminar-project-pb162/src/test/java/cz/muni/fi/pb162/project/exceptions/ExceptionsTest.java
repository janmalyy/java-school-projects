package cz.muni.fi.pb162.project.exceptions;

import cz.muni.fi.pb162.project.exceptions.EmptySquareException;
import cz.muni.fi.pb162.project.exceptions.InvalidFormatOfInputException;
import cz.muni.fi.pb162.project.exceptions.NotAllowedMoveException;
import cz.muni.fi.pb162.project.helper.BasicRulesTester;
import org.junit.jupiter.api.Test;

/**
 * @author Alzbeta Strompova
 */
class ExceptionsTest {

    @Test
    void attributesAndMethodsAmount() {
        BasicRulesTester.attributesAmount(EmptySquareException.class, 0);
        BasicRulesTester.methodsAmount(EmptySquareException.class, 0);
        BasicRulesTester.attributesAmount(InvalidFormatOfInputException.class, 0);
        BasicRulesTester.methodsAmount(InvalidFormatOfInputException.class, 0);
        BasicRulesTester.attributesAmount(NotAllowedMoveException.class, 0);
        BasicRulesTester.methodsAmount(NotAllowedMoveException.class, 0);
    }

    @Test
    void inheritance() {
        BasicRulesTester.testInheritance(Exception.class, EmptySquareException.class);
        BasicRulesTester.testInheritance(Exception.class, NotAllowedMoveException.class);
    }

}
