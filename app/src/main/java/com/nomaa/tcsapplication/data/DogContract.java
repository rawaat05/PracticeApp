package com.nomaa.tcsapplication.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DogContract {

    public static final String CONTENT_AUTHORITY = "com.nomaa.tcsapplication";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String path_dog = "dog";

    private DogContract(){};

    public static final class DogEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path_dog).build();

        public static final String TABLE_NAME = "dog";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_IMAGE_PATH = "image";

        public static final String _ID = "id";

    }
}