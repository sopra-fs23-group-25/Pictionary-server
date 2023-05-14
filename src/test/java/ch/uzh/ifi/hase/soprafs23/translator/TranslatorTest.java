
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
        String testString = "er";
        String testLanguage = "de";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, true);
            assertEquals("He", testResult);
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void testTranslator_singleWordInput_UserToSystem_UserHasSystemLanguage() {
        String testString = "tree";
        String testLanguage = "en";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, true);
            assertEquals( "Tree",testResult );
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void testTranslator_singleWordInput_UserToSystemAndBAck() {
        String testString = "baum";
        String testLanguage = "de";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, true);
            testResult = translator.getSingleTranslation(testString, testLanguage, false);
            assertEquals("Baum", testResult);
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void testTranslator_singleWordInput_UserToSystem_backToUserInDifferentLanguage() {
        String testString = "tür";
        String testLanguage = "de";
        String secondLanguage = "en";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, true);
            testResult = translator.getSingleTranslation(testResult, secondLanguage, false);
            assertEquals( "Door", testResult);
        }
        catch (Exception e) {
            System.err.println(e);
        }

    }

    @Test
    public void testTranslator_multipleWords_UserToSystem() {
        List<String> testList = new ArrayList<>(Arrays.asList("Sie", "du", "er"));
        List<String> translatedTestList = new ArrayList<>( Arrays.asList("She", "You", "He"));
        String testLanguage = "de";
        try {
            assertEquals(translator.getListTranslation(testList, testLanguage, true), translatedTestList);
        }
        catch (Exception e) {
        }
    }

    @Test
    public void testTranslator_singleWord_SystemToUser(){
        String testString = "he";
        String testLanguage = "de";
        try {
            String testResult = translator.getSingleTranslation(testString, testLanguage, false);
            assertEquals("Er", testResult);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    @Test
    public void testTranslator_multipleWords_SystemToUser() {
        List<String> testList = new ArrayList<>(Arrays.asList("she", "you", "he"));
        List<String> translatedTestList = new ArrayList<>( Arrays.asList("Sie", "Du", "Er"));
        String testLanguage = "de";
        try {
            assertEquals(translatedTestList,translator.getListTranslation(testList, testLanguage, false));
        }
        catch (Exception e) {
        }
    }
}
