package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.ifmo.ctddev.skripnikov.androidhw6.SQLiteHelper.*;

import java.util.ArrayList;

public class DBStorage {
    private SQLiteDatabase database;

    public DBStorage(Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void destroy() {
        database.close();
    }

    public Channel[] getChannels() {
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_CHANNELS,
                    new String[]{ChannelCols.ID, ChannelCols.NAME, ChannelCols.LINK, ChannelCols.ENCODING,
                            ChannelCols.TIME, ChannelCols.NUMBER_OF_NEW_ENTRYS},
                    null, null, null, null, null);
            ArrayList<Channel> answer = new ArrayList<Channel>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                answer.add(new Channel(cursor.getLong(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getLong(4), cursor.getInt(5)));
                cursor.moveToNext();
            }
            return answer.toArray(new Channel[answer.size()]);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<FeedItem> getItemsByChannelId(Long channelId) {
        String where = ItemsCols.CHANNEL_ID + "=" + channelId;
        Cursor cursor = null;
        try {
            cursor = database.query(SQLiteHelper.TABLE_ITEMS,
                    new String[]{ItemsCols.LINK, ItemsCols.NAME, ItemsCols.DESCRIPTION},
                    where, null, null, null, ItemsCols.TIME + " desc, " + ItemsCols.ID+ " asc");
            ArrayList<FeedItem> answer = new ArrayList<FeedItem>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                answer.add(new FeedItem(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                cursor.moveToNext();
            }

            return answer;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public long addChannel(String name, String link, String encoding) {
        name = name.trim();
        ContentValues values = new ContentValues();
        values.put(ChannelCols.NAME, name);
        values.put(ChannelCols.LINK, link);
        values.put(ChannelCols.ENCODING, encoding);
        Long time = System.currentTimeMillis();
        values.put(ChannelCols.TIME, time);
        values.put(ChannelCols.NUMBER_OF_NEW_ENTRYS, 0);
        return database.insert(SQLiteHelper.TABLE_CHANNELS, null, values);
    }

    public boolean changeChannel(Channel channel) {
        String where = ChannelCols.ID + " = " + channel.id;
        ContentValues values = new ContentValues();
        values.put(ChannelCols.NAME, channel.name);
        values.put(ChannelCols.LINK, channel.link);
        values.put(ChannelCols.ENCODING, channel.encoding);
        values.put(ChannelCols.TIME, channel.time);
        values.put(ChannelCols.NUMBER_OF_NEW_ENTRYS, channel.numberOfNewEntrys);
        return database.update(SQLiteHelper.TABLE_CHANNELS, values, where, null) > 0;
    }

    public boolean deleteChannel(long id) {
        String where = ChannelCols.ID + " = " + id;
        return database.delete(SQLiteHelper.TABLE_CHANNELS, where, null) > 0;
    }

    public int addItems(long channelId, ArrayList<FeedItem> list) {
        Long time = System.currentTimeMillis();
        int numberOfNewEntrys = 0;
        for (FeedItem aList : list) {
            ContentValues values = new ContentValues();
            values.put(ItemsCols.NAME, aList.title);
            values.put(ItemsCols.LINK, aList.link);
            values.put(ItemsCols.DESCRIPTION, aList.description);
            values.put(ItemsCols.CHANNEL_ID, channelId);
            values.put(ChannelCols.TIME, time);
            if(database.insert(SQLiteHelper.TABLE_ITEMS, null, values) != -1)
                numberOfNewEntrys++;
        }
        return numberOfNewEntrys;
    }
}
