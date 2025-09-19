package com.app_neighbrsnook.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.app_neighbrsnook.R;
import java.util.ArrayList;
import java.util.List;

public class DocumentUploadAdapter1 extends RecyclerView.Adapter<DocumentUploadAdapter1.ViewHolder> {
    Dialog dialog;
    List<Uri> docList = new ArrayList<>();
    DocumentUploadAdapter1.DocRequest docRequest;
    Context context;
    public DocumentUploadAdapter1(Context context, ArrayList<Uri> docList, DocumentUploadAdapter1.DocRequest imageRequest) {
        this.docList = docList;
        this.docRequest= imageRequest;
        this.context = context;
    }
    @NonNull
    @Override
    public DocumentUploadAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_doc_upload, parent, false);
        return new DocumentUploadAdapter1.ViewHolder(view); }
    @Override
    public void onBindViewHolder(@NonNull DocumentUploadAdapter1.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Integer pageNumber = 0;
        holder.pdfView.fromAsset(docList.get(position).getPath())
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)

                .load();
        holder.pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRequest.onDocClick(docList.get(position));
            }
        });

        holder.iv_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docList.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        PDFView pdfView;
        ImageView iv_remove_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfView= itemView.findViewById(R.id.pdfView);
            iv_remove_image = itemView.findViewById(R.id.iv_remove_image);

        }
    }

    public interface DocRequest {
        void onDocClick(Uri pdf);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj,
                null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}