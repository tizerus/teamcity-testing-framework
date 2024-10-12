package com.example.teamcity.api.rest.generators;

import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.RandomStringGenerator;

public class DataGenerator {

    public static final char[][] LATIN = new char[][] {{'a', 'z'}, {'A', 'Z'}};
    public static final char[][] CYRILLIC = new char[][] {{'а', 'я'}, {'А', 'Я'}};
    public static final char[][] DIGITS = new char[][] {{'0', '9'}};
    public static final char[][] SPECIAL_SYMBOLS = new char[][] {{' ', '/'}, {':', '@'}, {'[', '`'}, {'{', '~'}};

    public static final CharacterPredicate WITH_SPECIAL_SYMBOLS = codePoint ->
            (codePoint >= '!' && codePoint <= '/')
                    || (codePoint >= ':' && codePoint <= '@')
                    || (codePoint >= '[' && codePoint <= '`')
                    || (codePoint >= '{' && codePoint <= '~');

    public static RandomStringGenerator getRandomAsciiChars(final CharacterPredicate... predicates) {
        return RandomStringGenerator
                .builder()
                .withinRange(0, 127)
                .filteredBy(predicates)
                .get();
    }

    public static RandomStringGenerator getRandomCyrillicChars(final CharacterPredicate... predicates) {
        return RandomStringGenerator
                .builder()
                .withinRange(CYRILLIC)
                .filteredBy(predicates)
                .get();
    }

}
