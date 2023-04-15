package ch.uzh.ifi.hase.soprafs23.translator;
// Imports the Google Cloud Translation library.

import com.google.cloud.translate.v3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Translator {
    static String projectId = "sopra-fs23-group-25-server";
    static LocationName parent = LocationName.of(projectId, "global");

    private static TranslateTextResponse translateText(String sourceLanguage, String word, TranslationServiceClient client) throws IOException {

        // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
        TranslateTextRequest request = TranslateTextRequest.newBuilder().setParent(parent.toString()).setMimeType("text/plain").setSourceLanguageCode(sourceLanguage).setTargetLanguageCode("en").addContents(word).build();

        return client.translateText(request);
    }

    public String translateSingle(String word, String language) throws IOException {
        String translationText = "";
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. using it in a try statement should ensure automatic close call, need to see if this is a problem
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            TranslateTextResponse response = translateText(language, word, client);
            for (Translation translation : response.getTranslationsList()) {
                translationText = translation.getTranslatedText();
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return translationText;
    }

    public List<String> translateList(List<String> wordList, String language) throws IOException {

        List<String> translatedStrings = new ArrayList<String>();
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. using it in a try statement should ensure automatic close call, need to see if this is a problem
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            for (String word : wordList) {
                TranslateTextResponse response = translateText(language, word, client);
                for (Translation translation : response.getTranslationsList()) {
                    translatedStrings.add(translation.getTranslatedText());

                }
            }
        }
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return translatedStrings;
    }
}

