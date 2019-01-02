package com.nomaa.tcsapplication.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private static final String DOGS_URL = "https://dog.ceo/api/breeds/list/all";

    private static final String BASE_IMAGE_PATH = "https://dog.ceo/api/breed/";
    private static final String END_IMAGE_PATH = "/images/random";


    public static URL getDogUrl() {

        Uri dogUri = Uri.parse(DOGS_URL).buildUpon().build();

        try {
            URL dogURL = new URL(dogUri.toString());
            return dogURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getDogImageUrl(String name) {

        Uri dogImageUri = Uri.parse(BASE_IMAGE_PATH + name + END_IMAGE_PATH).buildUpon().build();

        try {
            URL dogImageURL = new URL(dogImageUri.toString());
            return dogImageURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getResponseFromHttpURL(URL url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();

            return response;

        } finally {
            httpURLConnection.disconnect();
        }
    }
}