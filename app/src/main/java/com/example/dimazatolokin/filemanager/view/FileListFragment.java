package com.example.dimazatolokin.filemanager.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dimazatolokin.filemanager.R;
import com.example.dimazatolokin.filemanager.core.listeners.OnFileClickListener;
import com.example.dimazatolokin.filemanager.model.FileItem;
import com.example.dimazatolokin.filemanager.view.adapter.FileListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FileListFragment extends Fragment {

    private TextView txtNumber;
    private RecyclerView recyclerView;
    private FileListAdapter adapter;
    private List<FileItem> items = new ArrayList<>();
    private OnFileClickListener clickListener;


    public static FileListFragment getInstance(int number) {
        FileListFragment fragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("n", number);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);
        txtNumber = view.findViewById(R.id.txtNumber);
        int n = getArguments().getInt("n");
        txtNumber.setText(String.valueOf(n));

        recyclerView = view.findViewById(R.id.recyclerFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileListAdapter(getContext());
        adapter.setOnFileClickListener(clickListener);
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    public void setClickListener(OnFileClickListener clickListener) {
        this.clickListener = clickListener;
        if (adapter != null) {
            adapter.setOnFileClickListener(clickListener);
        }
    }

    public void setItems(List<FileItem> items) {
        this.items = items;
        if (adapter != null) {
            adapter.setItems(items);
        }
    }

}
