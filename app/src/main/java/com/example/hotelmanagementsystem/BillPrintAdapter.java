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
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;

public class BillPrintAdapter extends PrintDocumentAdapter {

    // Android context
    private final Context context;

    // Complete bill text
    private final String billText;

    // PDF document
    private PrintedPdfDocument pdfDocument;

    // Number of pages
    private int pageCount = 1;

    // Number of lines that fit on one page
    private int linesPerPage = 40;

    // Text drawing settings
    private final Paint paint =
            new Paint(Paint.ANTI_ALIAS_FLAG);


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillPrintAdapter(
            Context context,
            String billText) {

        this.context = context;
        this.billText = billText;

        // Black text
        paint.setColor(
                android.graphics.Color.BLACK
        );

        // Text size
        paint.setTextSize(11f);

        // Monospace font makes receipt columns
        // easier to read
        paint.setTypeface(
                Typeface.create(
                        Typeface.MONOSPACE,
                        Typeface.NORMAL
                )
        );
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

        // User cancelled printing
        if (cancellationSignal.isCanceled()) {

            callback.onLayoutCancelled();

            return;
        }


        // Create PDF using printer settings
        pdfDocument =
                new PrintedPdfDocument(
                        context,
                        newAttributes
                );


        // Split bill into individual lines
        String[] lines =
                billText.split("\n", -1);


        // Space between lines
        float lineHeight = 18f;


        // Printable height
        int pageHeight =
                pdfDocument
                        .getPageContentRect()
                        .height();


        // Calculate how many lines fit
        linesPerPage =
                (int) (
                        pageHeight /
                                lineHeight
                );


        // Safety check
        if (linesPerPage <= 0) {

            linesPerPage = 1;
        }


        // Calculate total pages
        pageCount =
                (int) Math.ceil(
                        (double) lines.length /
                                linesPerPage
                );


        // At least one page
        if (pageCount <= 0) {

            pageCount = 1;
        }


        // Information shown to Android
        PrintDocumentInfo info =
                new PrintDocumentInfo.Builder(
                        "Hotel_Bill_" +
                                System.currentTimeMillis() +
                                ".pdf"
                )
                        .setContentType(
                                PrintDocumentInfo
                                        .CONTENT_TYPE_DOCUMENT
                        )
                        .setPageCount(pageCount)
                        .build();


        // Tell Android layout is ready
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

        try {

            // Safety check
            if (pdfDocument == null) {

                callback.onWriteFailed(
                        "PDF document is not ready."
                );

                return;
            }


            // Split bill into lines
            String[] lines =
                    billText.split("\n", -1);


            // -------------------------------------------------
            // Generate requested pages
            // -------------------------------------------------

            for (
                    int pageNumber = 0;
                    pageNumber < pageCount;
                    pageNumber++
            ) {

                // Check cancellation
                if (cancellationSignal.isCanceled()) {

                    callback.onWriteCancelled();

                    return;
                }


                // Print only requested pages
                if (!containsPage(
                        pages,
                        pageNumber
                )) {

                    continue;
                }


                // Create PDF page
                PrintedPdfDocument.Page page =
                        pdfDocument.startPage(
                                pageNumber
                        );


                Canvas canvas =
                        page.getCanvas();


                // -------------------------------------------------
                // Position
                // -------------------------------------------------

                float x = 40f;

                float y = 50f;


                // Line spacing
                float lineHeight = 18f;


                // First line for this page
                int startIndex =
                        pageNumber *
                                linesPerPage;


                // Last line for this page
                int endIndex =
                        Math.min(
                                startIndex +
                                        linesPerPage,
                                lines.length
                        );


                // -------------------------------------------------
                // Draw bill text
                // -------------------------------------------------

                for (
                        int i = startIndex;
                        i < endIndex;
                        i++
                ) {

                    // Check cancellation
                    if (cancellationSignal.isCanceled()) {

                        // Finish the current page before cancelling
                        pdfDocument.finishPage(page);

                        callback.onWriteCancelled();

                        return;
                    }


                    String line =
                            lines[i];


                    // Draw line
                    canvas.drawText(
                            line,
                            x,
                            y,
                            paint
                    );


                    // Move down
                    y += lineHeight;
                }


                // Finish page
                pdfDocument.finishPage(
                        page
                );
            }


            // -------------------------------------------------
            // Write PDF
            // -------------------------------------------------

            FileOutputStream outputStream =
                    new FileOutputStream(
                            destination.getFileDescriptor()
                    );


            pdfDocument.writeTo(
                    outputStream
            );


            outputStream.flush();


            // Tell Android printing is complete
            callback.onWriteFinished(
                    new PageRange[]{
                            PageRange.ALL_PAGES
                    }
            );


        } catch (IOException e) {

            callback.onWriteFailed(
                    e.getMessage()
            );

        } finally {

            // Close destination
            try {

                destination.close();

            } catch (IOException ignored) {
            }


            // Close PDF
            if (pdfDocument != null) {

                pdfDocument.close();

                pdfDocument = null;
            }
        }
    }


    // =====================================================
    // CHECK WHETHER PAGE WAS REQUESTED
    // =====================================================

    private boolean containsPage(
            PageRange[] pages,
            int pageNumber) {

        // If Android requests all pages
        if (pages == null ||
                pages.length == 0) {

            return true;
        }


        // Check each requested range
        for (PageRange range : pages) {

            if (range.getStart() <= pageNumber &&
                    range.getEnd() >= pageNumber) {

                return true;
            }
        }


        return false;
    }


    // =====================================================
    // FINISH PRINTING
    // =====================================================

    @Override
    public void onFinish() {

        super.onFinish();


        // Close PDF if still open
        if (pdfDocument != null) {

            pdfDocument.close();

            pdfDocument = null;
        }
    }
}