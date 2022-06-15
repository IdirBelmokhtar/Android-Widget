package com.idir.androidwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class ExampleAppWidgetConfig extends AppCompatActivity {

    public static final String SHARED_PRES = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_app_widget_config);

        Intent configIntent = getIntent();
        Bundle extra = configIntent.getExtras();
        if (extra != null) {
            appWidgetId = extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        editTextButton = findViewById(R.id.edit_text_button);
    }

    public void confirmConfiguration(View view) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String buttonText = editTextButton.getText().toString();

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.example_widget);
        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
        views.setCharSequence(R.id.example_widget_button, "setText", buttonText);
        //views.setInt(R.id.example_widget_button, "setBackgroundColor", Color.RED);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        SharedPreferences preferences = getSharedPreferences(SHARED_PRES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}