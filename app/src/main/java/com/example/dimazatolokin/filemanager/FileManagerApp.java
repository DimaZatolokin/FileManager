package com.example.dimazatolokin.filemanager;

import android.app.Application;

import com.example.dimazatolokin.filemanager.core.CoreService;

public class FileManagerApp extends Application{

    private CoreService coreService;

    @Override
    public void onCreate() {
        super.onCreate();

        coreService = new CoreService(this);
    }

    public CoreService getCoreService() {
        return coreService;
    }
}
