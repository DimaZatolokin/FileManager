package com.example.dimazatolokin.filemanager.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dimazatolokin.filemanager.R;
import com.example.dimazatolokin.filemanager.core.listeners.OnFileClickListener;
import com.example.dimazatolokin.filemanager.model.FileItem;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.VH> {

    private Context context;
    private List<FileItem> items = new ArrayList<>();
    private OnFileClickListener onFileClickListener;

    public FileListAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<FileItem> items) {
        this.items = new ArrayList<>();
        this.items.add(new FileItem(true));
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnFileClickListener(OnFileClickListener onFileClickListener) {
        this.onFileClickListener = onFileClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.file_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.applyData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView dir;
        private TextView created;
        private View layoutFileInfo;
        private TextView txtGoUp;
        private View layoutFullFile;

        public VH(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_file_name);
            dir = itemView.findViewById(R.id.txt_file_is_dir);
            created = itemView.findViewById(R.id.txt_created);
            layoutFileInfo = itemView.findViewById(R.id.layout_file_info);
            txtGoUp = itemView.findViewById(R.id.txt_go_up);
            layoutFullFile = itemView.findViewById(R.id.layout_full_file);
        }

        public void applyData(final FileItem fileItem) {
            if (fileItem.isEmpty()) {
                txtGoUp.setVisibility(View.VISIBLE);
                layoutFullFile.setVisibility(View.GONE);
            } else {
                name.setText(fileItem.getName());
                dir.setText(fileItem.isDirectory() ? "DIR" : "File");
                created.setText(fileItem.getCreatedInDate());
                txtGoUp.setVisibility(View.GONE);
                layoutFullFile.setVisibility(View.VISIBLE);
            }
            layoutFileInfo.setOnTouchListener(new View.OnTouchListener() {
                long startTime = 0;
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        onFileClickListener.onLongPress(fileItem, itemView.getY());
                    }
                };

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            layoutFileInfo.setBackgroundColor(context.getResources()
                                    .getColor(android.R.color.holo_blue_light));
                            if (fileItem.isEmpty()) {
                                onFileClickListener.onItemClick(fileItem);
                            } else {
                                startTime = System.currentTimeMillis();
                                handler.postDelayed(runnable, 600);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            layoutFileInfo.setBackgroundColor(context.getResources()
                                    .getColor(android.R.color.transparent));
                            if (fileItem.isEmpty()) {
                                break;
                            }
                            handler.removeCallbacks(runnable);
                            long totalTime = System.currentTimeMillis() - startTime;
                            if (totalTime > 70 && totalTime < 600) {
                                onFileClickListener.onItemClick(fileItem);
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }
}
