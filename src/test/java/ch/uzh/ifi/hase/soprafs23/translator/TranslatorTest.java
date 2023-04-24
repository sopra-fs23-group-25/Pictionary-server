
package ch.uzh.ifi.hase.soprafs23.translator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    Translator translator = Translator.getInstance();


    public TranslatorTest() throws IOException {
    }

    @Test
    public void testTranslator_singleWordInput_UserToSystem() {
        String testString = "Er";
        String testLanguage = "de";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, true);
            assertEquals( testResult, "He");
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void testTranslator_multipleWords_UserToSystem() {
        List<String> testList = new ArrayList<>(Arrays.asList("Sie", "du", "er"));
        List<String> translatedTestList = new ArrayList<>( Arrays.asList("She", "you", "he"));
        String testLanguage = "de";
        try {
            assertEquals(translator.getListTranslation(testList, testLanguage, true), translatedTestList);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testTranslator_singleWord_SystemToUser(){
        String testString = "He";
        String testLanguage = "de";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, false);
            assertEquals( testResult, "Er");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void testTranslator_multipleWords_SystemToUser() {
        List<String> testList = new ArrayList<>(Arrays.asList("She", "you", "he"));
        List<String> translatedTestList = new ArrayList<>( Arrays.asList("Sie", "Du", "Er"));
        String testLanguage = "de";
        try {
            assertEquals(translatedTestList,translator.getListTranslation(testList, testLanguage, false));
        }
        catch (Exception e) {
        }
    }
}
