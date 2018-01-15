package com.example.dimazatolokin.filemanager.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileItem {

    private String name;
    private String path;
    private double size;
    private long created;
    boolean isDirectory;
    boolean empty;

    public FileItem(boolean empty) {
        this.empty = empty;
        name = "";
        path = "";
        size = 0;
        created = 0;
    }

    public FileItem(File file) {
        name = file.getName();
        path = file.getAbsolutePath();
        size = file.length();
        isDirectory = file.isDirectory();
        created = file.lastModified();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getCreatedInDate() {
        Date date = new Date(created);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:MM:SS");
        return dateFormat.format(date);
    }
}
