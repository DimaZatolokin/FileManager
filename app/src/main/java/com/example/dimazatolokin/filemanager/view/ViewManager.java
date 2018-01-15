package com.example.dimazatolokin.filemanager.view;

import com.example.dimazatolokin.filemanager.core.callback.OnActionSelectedCallback;
import com.example.dimazatolokin.filemanager.model.FileItem;

import java.util.List;

public interface ViewManager {

    void setFileListLeft(List<FileItem> fileItems);

    void setFileListRight(List<FileItem> fileItems);

    void showDialog(float posY, OnActionSelectedCallback callback);

    void showMessage(String message);

    void refreshFragments();
}
