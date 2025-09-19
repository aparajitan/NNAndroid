package com.app_neighbrsnook.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app_neighbrsnook.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DocumentPagerFragment extends Fragment {

    private static final String ARG_URL = "fileUrl";
    private String fileUrl;
    private PDFView pdfView;
    private ImageView imageView;

    public static DocumentPagerFragment newInstance(String url) {
        DocumentPagerFragment fragment = new DocumentPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_document_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pdfView = view.findViewById(R.id.pdfView);
        imageView = view.findViewById(R.id.iv_image);

        if (getArguments() != null) {
            fileUrl = getArguments().getString(ARG_URL);

            if (fileUrl.endsWith(".pdf")) {
                imageView.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);
                new DownloadAndShowPDF(pdfView).execute(fileUrl);
            } else {
                pdfView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(requireContext()).load(fileUrl).into(imageView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (pdfView != null) pdfView.recycle();
        super.onDestroyView();
    }

    static class DownloadAndShowPDF extends AsyncTask<String, Void, File> {

        private final WeakReference<PDFView> pdfViewRef;

        public DownloadAndShowPDF(PDFView pdfView) {
            this.pdfViewRef = new WeakReference<>(pdfView);
        }

        @Override
        protected File doInBackground(String... strings) {
            String urlStr = strings[0];
            File pdfFile = null;

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream in = conn.getInputStream();
                File file = new File(pdfViewRef.get().getContext().getCacheDir(),
                        "doc_" + System.currentTimeMillis() + ".pdf");
                FileOutputStream out = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                out.close();
                in.close();
                pdfFile = file;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return pdfFile;
        }

        @Override
        protected void onPostExecute(File file) {
            PDFView pdfView = pdfViewRef.get();
            if (file != null && pdfView != null) {
                pdfView.fromFile(file)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .spacing(10)
                        .load();
            }
        }
    }
}
