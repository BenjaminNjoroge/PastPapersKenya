package com.pastpaperskenya.app.presentation.main.downloads.viewpdf;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.pastpaperskenya.app.R;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;


public class ViewPfdActivity extends AppCompatActivity
        implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {

    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfName;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView=findViewById(R.id.pdfview);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("filepath"))
                pdfName = bundle.getString("filepath");
            Log.e("pdfName====",pdfName);
            //new DownloadFilesTask().execute(pdfName, String.valueOf(flag));
            pdfView.setVisibility(View.VISIBLE);
            File file = new File(pdfName);

            /*File file = new File(getCacheDir(), pdfName);
            if (!file.exists()) {
                InputStream asset = null;
                try {
                    asset = getAssets().open(pdfName);
                    FileOutputStream output = null;
                    output = new FileOutputStream(file);
                    final byte[] buffer = new byte[1024];
                    int size;

                    while ((size = asset.read(buffer)) != -1) {
                        output.write(buffer, 0, size);
                    }
                    asset.close();
                    output.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .defaultPage(pageNumber)
                    .onPageChange(ViewPfdActivity.this)
                    .enableAnnotationRendering(true)
                    .onLoad(ViewPfdActivity.this)
                    .scrollHandle(new DefaultScrollHandle(ViewPfdActivity.this))
                    .spacing(10) // in dp
                    .onPageError(ViewPfdActivity.this)
                    .onLoad(ViewPfdActivity.this)
                    .load();
        }else {
            pdfView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        //progressDialog.dismiss();
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("DocumentDetails", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }



    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e("DocumentDetails", "Cannot load page " + page);
    }

}