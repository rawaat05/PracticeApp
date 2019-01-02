package com.nomaa.tcsapplication.Utilities;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import com.nomaa.tcsapplication.data.DogContract;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DogJsonUtilsTest {

    private String jsonResponseString = "{\"status\":\"success\",\"message\":{\"affenpinscher\":[],\"african\":[]}}";

    ContentValues[] expectedDogValues = new ContentValues[2];

    @Before
    public void setUp() throws Exception {

        for (int i=0; i<expectedDogValues.length; i++) {
            if(i == 0) {
                ContentValues dogValue = new ContentValues(2);
                dogValue.put("name", "affenpinscher");
                dogValue.put("image", "null");
                dogValue.put(DogContract.DogEntry._ID, 1000 + i);

                expectedDogValues[i] = dogValue;
            } else {
                ContentValues dogValue = new ContentValues(2);
                dogValue.put("name", "african");
                dogValue.put("image", "null");
                dogValue.put(DogContract.DogEntry._ID, 1000 + i);

                expectedDogValues[i] = dogValue;
            }
        }
    }

    @Test
    public void getDogContentValuesFromJson() {
        try {
            ContentValues[] returnedValues =
                    DogJsonUtils.getDogContentValuesFromJson(new MockContext(), jsonResponseString);

            assertEquals("affenpinscher", returnedValues[0].getAsString("name"));
            assertEquals("african", returnedValues[1].getAsString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}