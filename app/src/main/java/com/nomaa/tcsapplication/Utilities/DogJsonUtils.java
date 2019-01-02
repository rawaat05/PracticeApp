package com.nomaa.tcsapplication.Utilities;


import android.content.ContentValues;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.nomaa.tcsapplication.data.DogContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class DogJsonUtils {

    public static ContentValues[] getDogContentValuesFromJson
            (Context context, String jsonDogResponse) throws JSONException {

        JSONObject base = new JSONObject(jsonDogResponse);

        JSONObject dogs = base.getJSONObject("message");

        Iterator<String> dogNamesIterator = dogs.keys();

        ArrayList<String> dogNames = new ArrayList<>();
        ArrayList<String> dogImages = new ArrayList<>();

        while(dogNamesIterator.hasNext()) {
            dogNames.add(dogNamesIterator.next());
            dogImages.add(fetchImage(dogNames.get(dogNames.size()-1)));
        }

        ContentValues[] dogValues = new ContentValues[dogNames.size()];

        for (int i=0; i<dogNames.size(); i++) {

            ContentValues dogValue = new ContentValues(2);
            dogValue.put(DogContract.DogEntry.COLUMN_NAME, dogNames.get(i));
            dogValue.put(DogContract.DogEntry.COLUMN_IMAGE_PATH, dogImages.get(i));
            dogValue.put(DogContract.DogEntry._ID, 1000 + i);

            dogValues[i] = dogValue;
        }

        return dogValues;
    }

    private static String fetchImage(String name) {

        URL url = NetworkUtils.getDogImageUrl(name);

        if (url == null || "".equals(url.toString())) {
            return null;//if url is null, return
        }

        String operationResultString = "";

        try {
            operationResultString = NetworkUtils.getResponseFromHttpURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject base = new JSONObject(operationResultString);

            return base.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}