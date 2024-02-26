package com.samschabel.pw.api;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.util.StringUtils;

public class TestNameUtils extends DisplayNameGenerator.Standard {

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return this.replaceCamelCaseAndSnakeCase(testMethod.getName());
    }

    String replaceCamelCaseAndSnakeCase(String text) {
        StringBuilder builder = new StringBuilder();
        for(char character : text.toCharArray()) {
            if (Character.isUpperCase(character) || character == '_') {
                insertSpace(builder, character);
            } else {
                builder.append(character);
            }
        }
        return StringUtils.capitalize(builder.toString());
    }

    private void insertSpace(StringBuilder result, char character) {
        result.append(' ');
        result.append(Character.toLowerCase(character));
    }

}
