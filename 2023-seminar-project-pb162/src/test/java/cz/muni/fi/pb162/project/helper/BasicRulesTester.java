package cz.muni.fi.pb162.project.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.assertj.core.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Radek Oslejsek, Petr Adamek, Marek Sabo, Alzbeta Strompova
 */
public class BasicRulesTester {

    public static void attributesAmount(Class<?> clazz, int expected) {
        long notConstantAttributes = Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> !isConstant(x.getModifiers()))
                .count();
        Assertions.assertThat(notConstantAttributes)
                .as("Too many non-constant attributes: %s", notConstantAttributes)
                .isLessThanOrEqualTo(expected);
    }

    private static boolean isConstant(int mod) {
        return Modifier.isStatic(mod) && Modifier.isFinal(mod);
    }

    public static void methodsAmount(Class<?> clazz, int expected) {
        long nonPrivateMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(x -> !Modifier.isPrivate(x.getModifiers()))
                .count();
        Assertions.assertThat(nonPrivateMethods)
                .as("Too many non-private methods: %s.", nonPrivateMethods)
                .isLessThanOrEqualTo(expected);
    }

    public static void testInheritance(Class<?> superClass, Class<?> checkedClass) {
        assertTrue(superClass.isAssignableFrom(checkedClass));
    }

    public static void attributesFinal(Class<?> clazz, int number) {
        Field[] attributes = BasicRulesTester.getFields(clazz);
        var count = 0;
        for (Field field : attributes) {
            if (Modifier.isFinal(field.getModifiers())) {
                count++;
            }
        }
        Assertions.assertThat(count)
                .as("Too less non-final attributes: %s.", count)
                .isGreaterThanOrEqualTo(number);
    }

    private static Field[] getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .toArray(Field[]::new);
    }

    public static void testAbstractClass(Class<?> clazz) {
        assertTrue(Modifier.isAbstract(clazz.getModifiers()));
    }

}
