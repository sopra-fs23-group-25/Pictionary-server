package ch.uzh.ifi.hase.soprafs23.translator;
// Imports the Google Cloud Translation library.

import com.google.cloud.translate.v3.*;
import net.bytebuddy.implementation.bytecode.Throw;

import java.io.IOException;
import java.util.*;

public class Translator {
    private static final String SYSTEM_LANGUAGE = "en";
    static String projectId = "sopra-fs23-group-25-server";
    static LocationName parent = LocationName.of(projectId, "global");
    private static Translator instance = null;
    private static TranslationServiceClient client = null;
    private static Queue<TranslationRequest> requestQueue = new LinkedList<>();
    private TranslationRequest currentRequest;
    private TranslateTextResponse response;
    private Thread translationThread;


    // Constructor setting up Connection to Google Cloud Translate
    private Translator() throws IOException {
        String projectId = "sopra-fs23-group-25-server";
        LocationName parent = LocationName.of(projectId, "global");

        // Initialize the TranslationServiceClient once when the Translator object is created
        try {
            client = TranslationServiceClient.create();
        }
        catch (Exception e) {
            System.err.println(e);
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
    public synchronized String getSingleTranslation(String word, String language, boolean playerToSystem) throws InterruptedException {
        if (Objects.equals(language, "en")) {
            return word;
        }
        if (Objects.equals(word, null)){
            return "guess not submiited";
        }
        TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
        solveSingleRequest(currentRequest);

        return currentRequest.translatedWord;
    }

    // Entry Point To List Translation
    // Adds a List of words to the Translation Queue one by one
    // waits till its solved, then returns them as a List
    public synchronized List<String> getListTranslation(List<String> wordList, String language, boolean playerToSystem) throws InterruptedException {
        List<String> translatedWordList = new ArrayList<>();
        for (String word : wordList) {
            if (word != null){
                TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
                solveSingleRequest(currentRequest);
                translatedWordList.add(currentRequest.translatedWord);
            }
            else{
                throw new RuntimeException();
            }

        }
        return translatedWordList;
    }


    // Helper Classes and functions


    private void solveSingleRequest(TranslationRequest currentRequest){
        try {
            String currentRequestLanguage = currentRequest.getLanguage();
            if (currentRequest.playerToSystem) {
                response = translateTextToServerLanguage(currentRequestLanguage, currentRequest.getWord(), client);
            }
            else {
                response = translateTextToUserLanguage(currentRequestLanguage, currentRequest.getWord(), client);
            }
            setTranslationText(currentRequest);
            Translator.this.notifyAll();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TranslateTextResponse translateTextToUserLanguage(String targetLanguage, String word, TranslationServiceClient client) throws IOException {

        // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
        TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(SYSTEM_LANGUAGE).setTargetLanguageCode(targetLanguage).addContents(word).build();

        return client.translateText(request);
    }

    private static TranslateTextResponse translateTextToServerLanguage(String sourceLanguage, String word, TranslationServiceClient client) throws IOException {

        // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
        TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(sourceLanguage).setTargetLanguageCode(SYSTEM_LANGUAGE).addContents(word).build();

        return client.translateText(request);
    }


    private void setTranslationText(TranslationRequest request) {
        for (Translation translation : response.getTranslationsList()) {
            request.translatedWord = translation.getTranslatedText();
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

        public void setWord(String word) {
            this.word = word;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

}


