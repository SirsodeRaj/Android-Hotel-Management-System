package com.example.hotelmanagementsystem;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class SystemBarHelper {

    public static void setup(Activity activity) {

        Window window = activity.getWindow();

        // Allow the application to handle system bar insets
        WindowCompat.setDecorFitsSystemWindows(
                window,
                false
        );

        // Make status/navigation bars transparent
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        View content =
                activity.findViewById(
                        android.R.id.content
                );

        if (content == null) {
            return;
        }

        // Save original padding
        final int left =
                content.getPaddingLeft();

        final int top =
                content.getPaddingTop();

        final int right =
                content.getPaddingRight();

        final int bottom =
                content.getPaddingBottom();


        ViewCompat.setOnApplyWindowInsetsListener(
                content,
                (view, windowInsets) -> {

                    Insets insets =
                            windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    view.setPadding(
                            left + insets.left,
                            top + insets.top,
                            right + insets.right,
                            bottom + insets.bottom
                    );

                    return windowInsets;
                }
        );

        ViewCompat.requestApplyInsets(content);
    }
}