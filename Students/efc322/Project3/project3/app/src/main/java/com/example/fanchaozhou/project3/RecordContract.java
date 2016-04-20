package com.example.fanchaozhou.project3;

import android.provider.BaseColumns;

/**
 * Created by Fanchao Zhou on 4/5/2016.
 */
public class RecordContract {

    public static final String TYPE_TABLE_NAME = "type_table";  //Table Name for all the target classes
    public static final String RECORD_TABLE_NAME = "record_table";//Table Name for the samples

    public static final class TypeEntry implements BaseColumns{
        public static final String TYPE_NAME = "NAME";
    }

    public static final class RecordEntry implements BaseColumns{
        public static final String TYPE_ID = "CLASSID";
        public static final String TYPE_NAME = "CLASSNAME";
        public static final String THUMBNAIL_PHOTO = "THUMBNAILPHOTO";
        public static final String PHOTO_DIR = "PHOTODIR";
    }
}
