package ch.uzh.ifi.hase.soprafs23.translator;
// Imports the Google Cloud Translation library.

import com.google.cloud.translate.v3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Translator {
    private static final String SYSTEM_LANGUAGE = "en";
    static String projectId = "sopra-fs23-group-25-server";
    static LocationName parent = LocationName.of(projectId, "global");
    private static Translator instance = null;
    private TranslationServiceClient client = null;
    private TranslateTextResponse response;
    private final Logger log = LoggerFactory.getLogger(Translator.class);

    // Constructor setting up Connection to Google Cloud Translate
    private Translator() throws IOException {
        // Initialize the TranslationServiceClient once when the Translator object is created
        try {
            client = TranslationServiceClient.create();
        }
        catch (Exception e) {
            log.error(e.toString());
        }
    }

    // Ensure only one Instance of Translator can exist at any Time
    public static synchronized Translator getInstance() throws IOException {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    // Entry Point To Single Word Translation
    // Adds one word to the Translation Queue,
    // waits till its solved then returns it as a String
    public synchronized String getSingleTranslation(String word, String language, boolean playerToSystem) {
        if (Objects.equals(language, "en")) {
            word =word.toLowerCase();
            word= word.substring(0, 1).toUpperCase() + word.substring(1);
            return word;
        }
        if (Objects.equals(word, null)) {
            return "guess not submitted";
        }
        TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
        solveSingleRequest(currentRequest);

        return currentRequest.translatedWord;
    }

    // Entry Point To List Translation
    // Adds a List of words to the Translation Queue one by one
    // waits till its solved, then returns them as a List
    public synchronized List<String> getListTranslation(List<String> wordList, String language, boolean playerToSystem) {
        if (Objects.equals(language, "en")) {
            List<String> newWordList = new ArrayList<>();
            for(String word:wordList){
                word = word.toLowerCase();
                word= word.substring(0, 1).toUpperCase() + word.substring(1);
                newWordList.add(word);
            }
            return newWordList;
        }
        List<String> translatedWordList = new ArrayList<>();
        for (String word : wordList) {
            if (Objects.equals(word, " ")) {
                translatedWordList.add(word);
            }
            else if (word != null) {
                TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
                solveSingleRequest(currentRequest);
                translatedWordList.add(currentRequest.translatedWord);
            }
            else {
                throw new RuntimeException("Word was null!");
            }

        }
        return translatedWordList;
    }


    // Helper Classes and functions


    private void solveSingleRequest(TranslationRequest currentRequest) {

        String currentRequestLanguage = currentRequest.getLanguage();
        if (currentRequest.playerToSystem) {
            response = translateTextToServerLanguage(currentRequestLanguage, currentRequest.getWord(), client);
        }
        else {
            response = translateTextToUserLanguage(currentRequestLanguage, currentRequest.getWord(), client);
        }

        setTranslationText(currentRequest);

    }

    private static TranslateTextResponse translateTextToUserLanguage(String targetLanguage, String word, TranslationServiceClient client) {

        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(parent.toString())
                .setMimeType("text/plain")
                .setSourceLanguageCode(SYSTEM_LANGUAGE)
                .setTargetLanguageCode(targetLanguage)
                .addContents(word).build();

        return client.translateText(request);
    }

    private static TranslateTextResponse translateTextToServerLanguage(String sourceLanguage, String word, TranslationServiceClient client) {

        TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(sourceLanguage).setTargetLanguageCode(SYSTEM_LANGUAGE).addContents(word).build();

        return client.translateText(request);
    }


    public void setTranslationText(TranslationRequest request) {
        for (Translation translation : response.getTranslationsList()) {
            request.translatedWord = translation.getTranslatedText();
            request.translatedWord = request.translatedWord.substring(0, 1).toUpperCase() + request.translatedWord.substring(1);
        }
    }

    // Class Entity used in Queue
    private static class TranslationRequest {

        public String translatedWord;
        private String word;
        private String language;
        private boolean playerToSystem;

        public TranslationRequest(String word, String language, boolean playerToSystem) {
            this.word = word;
            this.language = language;
            this.playerToSystem = playerToSystem;
            this.translatedWord = null;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {this.word = word;}

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

}


