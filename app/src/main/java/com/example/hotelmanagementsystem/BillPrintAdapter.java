package com.example.hotelmanagementsystem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.graphics.pdf.PdfDocument;
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;

public class BillPrintAdapter extends PrintDocumentAdapter {

    private final Context context;
    private final String billText;

    private PrintedPdfDocument pdfDocument;

    private int pageCount;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillPrintAdapter(
            Context context,
            String billText) {

        this.context = context;
        this.billText = billText;
    }


    // =====================================================
    // LAYOUT
    // =====================================================

    @Override
    public void onLayout(
            PrintAttributes oldAttributes,
            PrintAttributes newAttributes,
            CancellationSignal cancellationSignal,
            LayoutResultCallback callback,
            Bundle extras) {

        pdfDocument =
                new PrintedPdfDocument(
                        context,
                        newAttributes
                );


        if (cancellationSignal.isCanceled()) {

            callback.onLayoutCancelled();

            return;
        }


        // -------------------------------------------------
        // Calculate number of pages
        // -------------------------------------------------

        Paint paint =
                new Paint();

        paint.setTextSize(12);

        Paint.FontMetrics fontMetrics =
                paint.getFontMetrics();

        float lineHeight =
                fontMetrics.bottom -
                        fontMetrics.top;


        int pageHeight =
                pdfDocument.getPageContentRect().height();


        int linesPerPage =
                Math.max(
                        1,
                        (int)
                                (pageHeight /
                                        lineHeight)
                );


        String[] lines =
                billText.split(
                        "\n",
                        -1
                );


        pageCount =
                (int) Math.ceil(
                        (double) lines.length /
                                linesPerPage
                );


        if (pageCount < 1) {

            pageCount = 1;
        }


        // -------------------------------------------------
        // Print document information
        // -------------------------------------------------

        PrintDocumentInfo info =
                new PrintDocumentInfo.Builder(
                        "Hotel_Bill.pdf"
                )
                        .setContentType(
                                PrintDocumentInfo
                                        .CONTENT_TYPE_DOCUMENT
                        )
                        .setPageCount(
                                pageCount
                        )
                        .build();


        callback.onLayoutFinished(
                info,
                true
        );
    }


    // =====================================================
    // WRITE PDF
    // =====================================================

    @Override
    public void onWrite(
            PageRange[] pages,
            ParcelFileDescriptor destination,
            CancellationSignal cancellationSignal,
            WriteResultCallback callback) {

        if (pdfDocument == null) {

            callback.onWriteFailed(
                    "Print document is not ready."
            );

            return;
        }


        Paint paint =
                new Paint();

        paint.setColor(
                android.graphics.Color.BLACK
        );

        paint.setTextSize(12);

        paint.setTypeface(
                Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.NORMAL
                )
        );


        String[] lines =
                billText.split(
                        "\n",
                        -1
                );


        Paint.FontMetrics fontMetrics =
                paint.getFontMetrics();

        float lineHeight =
                fontMetrics.bottom -
                        fontMetrics.top;


        int pageHeight =
                pdfDocument.getPageContentRect().height();


        int linesPerPage =
                Math.max(
                        1,
                        (int)
                                (pageHeight /
                                        lineHeight)
                );


        try {

            for (
                    int pageNumber = 0;
                    pageNumber < pageCount;
                    pageNumber++
            ) {

                if (cancellationSignal.isCanceled()) {

                    callback.onWriteCancelled();

                    return;
                }


                // -------------------------------------------------
                // Start page
                // -------------------------------------------------

                PdfDocument.PageInfo pageInfo =
                        new PdfDocument.PageInfo.Builder(
                                pdfDocument.getPageWidth(),
                                pdfDocument.getPageHeight(),
                                pageNumber + 1
                        ).create();


                PdfDocument.Page page =
                        pdfDocument.startPage(pageInfo);


                Canvas canvas =
                        page.getCanvas();


                float x = 30;

                float y =
                        40 -
                                fontMetrics.top;


                int startLine =
                        pageNumber *
                                linesPerPage;


                int endLine =
                        Math.min(
                                startLine +
                                        linesPerPage,
                                lines.length
                        );


                // -------------------------------------------------
                // Draw lines
                // -------------------------------------------------

                for (
                        int i = startLine;
                        i < endLine;
                        i++
                ) {

                    canvas.drawText(
                            lines[i],
                            x,
                            y,
                            paint
                    );

                    y += lineHeight;
                }


                // -------------------------------------------------
                // Finish page
                // -------------------------------------------------

                pdfDocument.finishPage(page);
            }


            // -----------------------------------------------------
            // Write PDF to destination
            // -----------------------------------------------------

            FileOutputStream outputStream =
                    new FileOutputStream(
                            destination.getFileDescriptor()
                    );


            pdfDocument.writeTo(
                    outputStream
            );


            outputStream.flush();

            outputStream.close();


            destination.close();


            pdfDocument.close();

            pdfDocument = null;


            callback.onWriteFinished(
                    new PageRange[]{
                            PageRange.ALL_PAGES
                    }
            );

        } catch (Exception e) {

            try {

                destination.close();

            } catch (Exception ignored) {
            }


            if (pdfDocument != null) {

                pdfDocument.close();

                pdfDocument = null;
            }


            callback.onWriteFailed(
                    e.getMessage()
            );
        }
    }


    // =====================================================
    // PAGE CHECK
    // =====================================================

    @Override
    public void onFinish() {

        if (pdfDocument != null) {

            pdfDocument.close();

            pdfDocument = null;
        }

        super.onFinish();
    }
}