package ru.ifmo.ctddev.skripnikov.androidhw6;

public class FeedItem {
    public final String link;
    public final String title;
    public final String description;

    FeedItem(String link,
             String title,
             String description) {
        this.link = link;
        this.title = title;
        this.description = description;
    }
}
