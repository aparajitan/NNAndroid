package com.app_neighbrsnook.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.app_neighbrsnook.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;

public class UriPagerFragment extends Fragment {

    private static final String ARG_URI = "uri";
    private static final String ARG_TYPE = "type";

    public static UriPagerFragment newInstance(Uri uri, String type) {
        UriPagerFragment fragment = new UriPagerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    private PDFView pdfView;
    private ImageView imageView;
    private Uri fileUri;
    private String fileType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_document_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        pdfView = view.findViewById(R.id.pdfView);
        imageView = view.findViewById(R.id.iv_image);

        if (getArguments() != null) {
            fileUri = getArguments().getParcelable(ARG_URI);
            fileType = getArguments().getString(ARG_TYPE);

            if ("pdf".equals(fileType)) {
                pdfView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                pdfView.fromUri(fileUri)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .spacing(10)
                        .load();
            } else {
                pdfView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(requireContext()).load(fileUri).into(imageView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pdfView != null) pdfView.recycle();
    }
}

