package com.example.dimazatolokin.filemanager.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.dimazatolokin.filemanager.R;
import com.example.dimazatolokin.filemanager.Utils;
import com.example.dimazatolokin.filemanager.core.callback.OnActionSelectedCallback;
import com.example.dimazatolokin.filemanager.core.listeners.OnFileClickListener;
import com.example.dimazatolokin.filemanager.model.FileItem;
import com.example.dimazatolokin.filemanager.view.FileListFragment;
import com.example.dimazatolokin.filemanager.view.ViewManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoreService {

    public static final int ACTION_COPY = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_DELETE = 3;

    private Context context;
    private File currentDirLeft;
    private File currentDirRight;
    private OnFileClickListener clickListenerLeft;
    private OnFileClickListener clickListenerRight;
    private ViewManager viewManager;
    private FileListFragment fragmentLeft;
    private FileListFragment fragmentRight;

    public CoreService(final Context context) {
        this.context = context;

        currentDirLeft = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        currentDirRight = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        clickListenerLeft = new OnFileClickListener() {
            @Override
            public void onItemClick(FileItem fileItem) {
                if (fileItem.isEmpty()) {
                    if (currentDirLeft.getParentFile() != null) {
                        clickListenerLeft.onItemClick(new FileItem(currentDirLeft.getParentFile()));
                        if (currentDirLeft.getParentFile() != null) {
                            currentDirLeft = currentDirLeft.getParentFile();
                        }
                    } else {
                        if (viewManager != null) {
                            viewManager.showMessage(context.getString(R.string.no_parent));
                        }
                    }
                } else {
                    File file = new File(fileItem.getPath());
                    if (file.isDirectory()) {
                        currentDirLeft = file;
                        List<FileItem> fileItems = fileItemsFromDir(file);
                        if (viewManager != null) {
                            viewManager.setFileListLeft(fileItems);
                        }
                    } else {
                        openFile(file);
                    }
                }
            }

            @Override
            public void onLongPress(final FileItem fileItem, float posY) {
                viewManager.showDialog(posY, new OnActionSelectedCallbackImpl(fileItem) {
                    @Override
                    public void copyFile() {
                        if (currentDirRight == null || !currentDirRight.isDirectory()) {
                            return;
                        }
                        Utils.copyFileOrDir(fileItem.getPath(), currentDirRight.getAbsolutePath());
                        List<FileItem> fileItems = fileItemsFromDir(currentDirRight);
                        if (viewManager != null) {
                            viewManager.setFileListRight(fileItems);
                        }
                    }

                    @Override
                    public void moveFile() {
                        Utils.moveFileOrDir(fileItem.getPath(), currentDirRight.getAbsolutePath());
                        refreshScreens();
                    }
                });
            }
        };
        clickListenerRight = new OnFileClickListener() {
            @Override
            public void onItemClick(FileItem fileItem) {
                if (fileItem.isEmpty()) {
                    if (currentDirRight.getParentFile() != null) {
                        clickListenerRight.onItemClick(new FileItem(currentDirRight.getParentFile()));
                        if (currentDirRight.getParentFile() != null) {
                            currentDirRight = currentDirRight.getParentFile();
                        }
                    } else {
                        if (viewManager != null) {
                            viewManager.showMessage(context.getString(R.string.no_parent));
                        }
                    }
                } else {
                    File file = new File(fileItem.getPath());
                    if (file.isDirectory()) {
                        currentDirRight = file;
                        List<FileItem> fileItems = fileItemsFromDir(file);
                        if (viewManager != null) {
                            viewManager.setFileListRight(fileItems);
                        }
                    } else {
                        openFile(file);
                    }
                }
            }

            @Override
            public void onLongPress(final FileItem fileItem, float posY) {
                viewManager.showDialog(posY, new OnActionSelectedCallbackImpl(fileItem) {
                    @Override
                    public void copyFile() {
                        if (currentDirLeft == null || !currentDirLeft.isDirectory()) {
                            return;
                        }
                        Utils.copyFileOrDir(fileItem.getPath(), currentDirLeft.getAbsolutePath());
                        List<FileItem> fileItems = fileItemsFromDir(currentDirLeft);
                        if (viewManager != null) {
                            viewManager.setFileListLeft(fileItems);
                        }
                    }

                    @Override
                    public void moveFile() {
                        Utils.moveFileOrDir(fileItem.getPath(), currentDirLeft.getAbsolutePath());
                        refreshScreens();
                    }
                });
            }
        };

    }

    private void refreshScreens() {
        List<FileItem> fileItems = fileItemsFromDir(currentDirRight);
        List<FileItem> fileItems2 = fileItemsFromDir(currentDirLeft);
        if (viewManager != null) {
            viewManager.setFileListRight(fileItems);
            viewManager.setFileListLeft(fileItems2);
        }
    }

    private void openFile(File file) {
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        Uri uri = Uri.fromFile(file);
        viewIntent.setDataAndType(uri, "*/*");
        editIntent.setDataAndType(uri, "*/*");
        Intent chooserIntent = Intent.createChooser(editIntent, "Open in...");
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{viewIntent});
        context.startActivity(chooserIntent);
    }

    @NonNull
    private List<FileItem> fileItemsFromDir(File file) {
        File[] files = file.listFiles();
        List<FileItem> fileItems = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                fileItems.add(new FileItem(f));
            }
        }
        return fileItems;
    }

    public FileListFragment getFragmentLeft() {
        if (fragmentLeft == null) {
            fragmentLeft = FileListFragment.getInstance(1);
        }
        return fragmentLeft;
    }

    public FileListFragment getFragmentRight() {
        if (fragmentRight == null) {
            fragmentRight = FileListFragment.getInstance(2);
        }
        return fragmentRight;
    }

    public void bindView(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.viewManager.setFileListLeft(fileItemsFromDir(currentDirLeft));
        clickListenerRight.onItemClick(new FileItem(currentDirRight));
    }

    public void unbindView() {
        viewManager = null;
    }

    public OnFileClickListener getClickListenerLeft() {
        return clickListenerLeft;
    }

    public OnFileClickListener getClickListenerRight() {
        return clickListenerRight;
    }


    private abstract class OnActionSelectedCallbackImpl implements OnActionSelectedCallback {

        private FileItem fileItem;

        OnActionSelectedCallbackImpl(FileItem fileItem) {
            this.fileItem = fileItem;
        }

        @Override
        public void onActionSelected(int action) {
            switch (action) {
                case ACTION_COPY:
                    copyFile();
                    break;
                case ACTION_MOVE:
                    moveFile();
                    break;
                case ACTION_DELETE:
                    deleteFile();
                    break;
            }
        }

        public abstract void copyFile();

        public abstract void moveFile();

        void deleteFile() {
            File file = new File(fileItem.getPath());
            Utils.deleteRecursive(file);
            refreshScreens();
        }
    }
}
