
package ch.uzh.ifi.hase.soprafs23.translator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    Translator translator = Translator.getInstance();

    public TranslatorTest() throws IOException {
    }

    @Test
    public void testTranslator_singleWordInput() {
        String testString = "Er";
        String testLanguage = "de";
        try {
            assertEquals(translator.getSingleTranslation(testString, testLanguage), "He");
        }
        catch (Exception e) {
        }

    }

    @Test
    public void testTranslator_multipleWords() {
        LinkedList<String> testList = (LinkedList<String>) Arrays.asList("Sie", "du", "er");
        LinkedList<String> translatedTestList = (LinkedList<String>) Arrays.asList("She", "you", "he");
        String testLanguage = "de";
        try {
            assertEquals(translator.getListTranslation(testList, testLanguage), translatedTestList);
        }
        catch (Exception e) {
        }
    }
}
