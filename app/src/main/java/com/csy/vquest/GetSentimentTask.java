package com.csy.vquest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;

/**
 * Created by Chirag on 02-12-2017.
 */

public class GetSentimentTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {

        try {
            GoogleCredential credential = GoogleCredential.getApplicationDefault();
            LanguageServiceClient language = LanguageServiceClient.create();

            // The text to analyze
            String text = strings[0];
            Document doc = Document.newBuilder()
                    .setContent(text).setType(Document.Type.PLAIN_TEXT).build();

            // Detects the sentiment of the text
            Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

            Log.d("Score : ", String.valueOf(sentiment.getScore()));
            Log.d("Magnitude : ", String.valueOf(sentiment.getMagnitude()));

            return null;

//            System.out.printf("Text: %s%n", text);
//            System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

//    @Override
//    protected void onPostExecute(String s) {
//        Log.d("Sentiment analysis : ", s);
//    }
}
