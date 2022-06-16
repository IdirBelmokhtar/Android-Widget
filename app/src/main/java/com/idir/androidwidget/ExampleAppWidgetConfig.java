package com.idir.androidwidget;

import static com.idir.androidwidget.ExampleAppWidgetProvider.ACTION_TOAST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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

        Intent buttonIntent = new Intent(this, MainActivity.class);
        PendingIntent buttonPendingIntent = PendingIntent.getActivity(this, 0, buttonIntent, 0);

        String buttonText = editTextButton.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleWidgetServices.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent clickIntent = new Intent(this, ExampleAppWidgetProvider.class);
        clickIntent.setAction(ACTION_TOAST);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this, 0, clickIntent, 0);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.example_widget);
        views.setOnClickPendingIntent(R.id.example_widget_button, buttonPendingIntent);
        views.setCharSequence(R.id.example_widget_button, "setText", buttonText);
        //views.setInt(R.id.example_widget_button, "setBackgroundColor", Color.RED);
        views.setRemoteAdapter(R.id.example_widget_stack_view, serviceIntent);
        views.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view);
        views.setPendingIntentTemplate(R.id.example_widget_stack_view, clickPendingIntent);

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