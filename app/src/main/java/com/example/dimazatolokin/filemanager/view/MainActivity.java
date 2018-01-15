package com.example.dimazatolokin.filemanager.view;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dimazatolokin.filemanager.FileManagerApp;
import com.example.dimazatolokin.filemanager.R;
import com.example.dimazatolokin.filemanager.core.CoreService;
import com.example.dimazatolokin.filemanager.core.callback.OnActionSelectedCallback;
import com.example.dimazatolokin.filemanager.model.FileItem;
import com.example.dimazatolokin.filemanager.view.adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewManager {

    private static final int DIALOG_HORIZONTAL_POSITION = 300;

    private ViewPager viewPager;
    private MainPagerAdapter mainPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private FileListFragment fragmentLeft;
    private FileListFragment fragmentRight;
    private CoreService coreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coreService = ((FileManagerApp) getApplication()).getCoreService();

        fragmentLeft = coreService.getFragmentLeft();

        fragmentRight = coreService.getFragmentRight();

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        coreService.bindView(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        fragmentLeft.setClickListener(coreService.getClickListenerLeft());
        fragmentRight.setClickListener(coreService.getClickListenerRight());
        fragments.add(fragmentLeft);
        fragments.add(fragmentRight);
        mainPagerAdapter = new MainPagerAdapter(getApplicationContext(), getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mainPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        coreService.unbindView();
    }

    @Override
    public void setFileListLeft(List<FileItem> fileItems) {
        fragmentLeft.setItems(fileItems);
    }

    @Override
    public void setFileListRight(List<FileItem> fileItems) {
        fragmentRight.setItems(fileItems);
    }

    @Override
    public void showDialog(float posY, final OnActionSelectedCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.action_dialog, null);

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.y = (int) posY;
        layoutParams.x = DIALOG_HORIZONTAL_POSITION;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

        TextView txtCopy = view.findViewById(R.id.txt_dialog_copy);
        TextView txtMove = view.findViewById(R.id.txt_dialog_move);
        TextView txtDelete = view.findViewById(R.id.txt_dialog_delete);

        txtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActionSelected(CoreService.ACTION_COPY);
                dialog.dismiss();
            }
        });
        txtMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActionSelected(CoreService.ACTION_MOVE);
                dialog.dismiss();
            }
        });
        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onActionSelected(CoreService.ACTION_DELETE);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
