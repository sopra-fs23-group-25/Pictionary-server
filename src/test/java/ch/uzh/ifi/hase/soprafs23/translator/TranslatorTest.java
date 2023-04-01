package ch.uzh.ifi.hase.soprafs23.translator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    Translator translator = new Translator();

    @Test
    public void testTranslator_singleWordInput() {
        String testString = "Er";
        String testLanguage = "de";
        try {
            //System.out.printf("\n\n\n\n\n\nTranslated text: %s\n\n\n\n\n\n", translator.translateSingle(testString, testLanguage));
            assertEquals(translator.translateSingle(testString, testLanguage), "He");
        }
        catch (Exception e) {
        }

    }

    @Test
    public void testTranslator_multipleWords(){
        List<String> testList = Arrays.asList("Sie", "du", "er");
        List<String> translatedTestList = Arrays.asList("She", "you", "he");
        String testLanguage = "de";
        try {
            //System.out.printf("\n\n\n\n\n\nTranslated text: %s\n\n\n\n\n\n", translator.translateSingle(testString, testLanguage));
            assertEquals(translator.translateList(testList, testLanguage), translatedTestList);
        }
        catch (Exception e) {
        }
    }
}
