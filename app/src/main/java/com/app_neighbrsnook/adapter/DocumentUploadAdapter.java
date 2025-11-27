package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.app_neighbrsnook.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentUploadAdapter extends RecyclerView.Adapter<DocumentUploadAdapter.ViewHolder> {
    Dialog dialog;
    List<Uri> docList = new ArrayList<>();
    DocRequest docRequest;
    Context context;

    public DocumentUploadAdapter(Context context, ArrayList<Uri> docList, DocRequest imageRequest) {
        this.docList = docList;
        this.docRequest = imageRequest;
        this.context = context;
    }

    @NonNull
    @Override
    public DocumentUploadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_pdf_upload, parent, false);
        return new DocumentUploadAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentUploadAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Integer pageNumber = 0;
        holder.pdfView.fromUri(docList.get(position))
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)
                .load();

        holder.pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRequest.onDocClick(position);
            }
        });

        holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRequest.onDocRemove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        PDFView pdfView;
        CardView iv_remove_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfView = itemView.findViewById(R.id.pdfView);
            iv_remove_image = itemView.findViewById(R.id.cardView);

        }
    }

    public interface DocRequest {
        void onDocClick(int position);
        void onDocRemove(int position);
    }

}