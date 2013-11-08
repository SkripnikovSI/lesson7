package ru.ifmo.ctddev.skripnikov.androidhw6;


import java.io.Serializable;

public class Channel implements Serializable {
    public long id;
    public String name;
    public String link;
    public String encoding;
    public long time;
    public int numberOfNewEntrys;

    Channel(long id,
            String name,
            String link,
            String encoding,
            long time,
            int numberOfNewEntrys) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.encoding = encoding;
        this.time = time;
        this.numberOfNewEntrys = numberOfNewEntrys;
    }
}
