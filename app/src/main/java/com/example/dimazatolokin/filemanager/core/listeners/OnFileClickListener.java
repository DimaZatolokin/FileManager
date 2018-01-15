package com.example.dimazatolokin.filemanager.core.listeners;

import com.example.dimazatolokin.filemanager.model.FileItem;

public interface OnFileClickListener {

    void onItemClick(FileItem fileItem);

    void onLongPress(FileItem fileItem, float posY);
}