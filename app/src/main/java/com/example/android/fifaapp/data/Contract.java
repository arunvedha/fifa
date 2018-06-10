package com.example.android.fifaapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
        private Contract(){}
        public static final String CONTENT_AUTHORITY ="com.example.android.fifaapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_FIFA = "fifa";
        public static final class MatchEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FIFA);
        public final static String TABLE_NAME = "fifa";
        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TEAM__A_NAME ="team_A";
        public final static String COLUMN_TEAM__B_NAME ="team_B";
        public final static String COLUMN_MATCH_DATE ="date";
        public final static String COLUMN_MATCH_TIME ="time";
        public final static String COLUMN_MATCH_VENUE ="venue";

                public final static String COLUMN_TEAMA_ICON = "teamAicon";

                public final static String COLUMN_TEAMB_ICON = "teamBicon";




                public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIFA;
         public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FIFA;



        }
}
