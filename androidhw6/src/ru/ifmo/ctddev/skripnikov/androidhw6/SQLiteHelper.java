package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "feed_reader.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CHANNELS = "channels";
    public static final String TABLE_ITEMS = "items";

    public static final String DROP_TABLE_CHANNELS = "drop table if exists " + TABLE_CHANNELS;
    public static final String DROP_TABLE_ITEMS = "drop table if exists " + TABLE_ITEMS;

    private static final String CREATE_CHANNELS_QUERY = "create table "
            + TABLE_CHANNELS + "("
            + ChannelCols.ID + " integer not null primary key autoincrement, "
            + ChannelCols.NAME + " text unique on conflict ignore, "
            + ChannelCols.LINK + " text, "
            + ChannelCols.ENCODING + " text, "
            + ChannelCols.TIME + " integer not null, "
            + ChannelCols.NUMBER_OF_NEW_ENTRYS + " integer not null)";

    private static final String CREATE_ITEMS_QUERY = "create table "
            + TABLE_ITEMS + "("
            + ItemsCols.ID + " integer not null primary key autoincrement, "
            + ItemsCols.NAME + " text unique on conflict ignore, "
            + ItemsCols.DESCRIPTION + " text, "
            + ItemsCols.LINK + " text, "
            + ItemsCols.CHANNEL_ID + " integer not null, "
            + ItemsCols.TIME + " integer not null, "
            + "foreign key (" + ItemsCols.CHANNEL_ID + ") references "
            + TABLE_CHANNELS + "(" + ChannelCols.ID + ") on delete cascade)";

    public static final class ChannelCols {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LINK = "link";
        public static final String ENCODING = "encoding";
        public static final String TIME = "time";
        public static final String NUMBER_OF_NEW_ENTRYS = "number";
    }

    public static final class ItemsCols {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";
        public static final String CHANNEL_ID = "channelId";
        public static final String TIME = "time";
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHANNELS_QUERY);
        db.execSQL(CREATE_ITEMS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_ITEMS);
        db.execSQL(DROP_TABLE_CHANNELS);
        onCreate(db);
    }
}