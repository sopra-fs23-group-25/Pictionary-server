package ch.uzh.ifi.hase.soprafs23.translator;
// Imports the Google Cloud Translation library.

import com.google.cloud.translate.v3.*;

import java.io.IOException;
import java.util.*;

public class Translator {
    private static final String SYSTEM_LANGUAGE = "en";
    static String projectId = "sopra-fs23-group-25-server";
    static LocationName parent = LocationName.of(projectId, "global");
    private static Translator instance = null;
    private static TranslationServiceClient client = null;
    private static Queue<TranslationRequest> requestQueue = new LinkedList<>();
    private Thread translationThread;

    private Translator() throws IOException {
        String projectId = "sopra-fs23-group-25-server";
        LocationName parent = LocationName.of(projectId, "global");

        // Initialize the TranslationServiceClient once when the Translator object is created
        try {
            client = TranslationServiceClient.create();
            translationThread = new Thread(new TranslationThread());
            translationThread.start();

        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static synchronized Translator getInstance() throws IOException {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }


    public synchronized String getSingleTranslation(String word, String language, boolean playerToSystem) throws InterruptedException {
        if(Objects.equals(language, "en")){
            return word;
        }
        TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
        addSingleRequest(currentRequest);
        while (currentRequest.translatedWord == null) {
            this.wait();
        }
        return currentRequest.translatedWord;
    }


    public synchronized List<String> getListTranslation(List<String> wordList, String language, boolean playerToSystem) throws InterruptedException {
        List<String> translatedWordList = new ArrayList<>();
        for (String word:wordList){
            TranslationRequest currentRequest = new TranslationRequest(word, language, playerToSystem);
            addSingleRequest(currentRequest);
            while (currentRequest.translatedWord == null) {
                this.wait();
            }
            translatedWordList.add(currentRequest.translatedWord);

        }
        return translatedWordList;
    }



/////// Helper Classes and functions
    private synchronized void addSingleRequest(TranslationRequest newRequest) {
        requestQueue.add(newRequest);
        this.notifyAll();
    }

    private synchronized void addMultipleRequest(LinkedList<TranslationRequest> newRequestList) {
        requestQueue.addAll(newRequestList);
    }
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

    private class TranslationThread implements Runnable {

        private TranslationRequest currentRequest;
        private TranslateTextResponse response;
        private volatile boolean running = true;
        public void stop() {
            running = false;
        }
        @Override
        public void run() {
            while (running) {
                synchronized (Translator.this) {
                    while (requestQueue.isEmpty()) {
                        try {
                            Translator.this.wait();
                        }
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    while (!requestQueue.isEmpty()) {
                        currentRequest = requestQueue.poll();
                        try {
                            String currentRequestLanguage = currentRequest.getLanguage();
                            if(currentRequest.playerToSystem){
                                response = translateTextToServerLanguage(currentRequestLanguage, currentRequest.getWord(), client);
                            }else{
                                response = translateTextToUserLanguage(currentRequestLanguage, currentRequest.getWord(), client);
                            }
                            setTranslationText(currentRequest);
                            Translator.this.notifyAll();
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        private static TranslateTextResponse translateTextToServerLanguage(String sourceLanguage, String word, TranslationServiceClient client) throws IOException {

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(sourceLanguage).setTargetLanguageCode(SYSTEM_LANGUAGE).addContents(word).build();

            return client.translateText(request);
        }

        private static TranslateTextResponse translateTextToUserLanguage(String targetLanguage, String word, TranslationServiceClient client) throws IOException {

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(SYSTEM_LANGUAGE).setTargetLanguageCode(targetLanguage).addContents(word).build();

            return client.translateText(request);
        }

        private void setTranslationText(TranslationRequest request) {
            for (Translation translation : response.getTranslationsList()) {
                 request.translatedWord = translation.getTranslatedText();
            }
        }
    }
}


