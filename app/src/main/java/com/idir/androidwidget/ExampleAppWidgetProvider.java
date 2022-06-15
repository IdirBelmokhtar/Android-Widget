package com.idir.androidwidget;

import static com.idir.androidwidget.ExampleAppWidgetConfig.KEY_BUTTON_TEXT;
import static com.idir.androidwidget.ExampleAppWidgetConfig.SHARED_PRES;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, 0);

            SharedPreferences preferences = context.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE);
            String buttonText = preferences.getString(KEY_BUTTON_TEXT + appWidgetId, "thank you");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
            views.setCharSequence(R.id.example_widget_button, "setText", buttonText);

            Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
            resizeWidget(appWidgetOptions, views);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.example_widget);

        resizeWidget(newOptions,views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views){
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        /*String dimension = "minWidth: " + minWidth + "\nmaxWidth: " + maxWidth
                + "\nminHeight: " + minHeight + "\nmaxHeight: " + maxHeight;
        Toast.makeText(context, dimension, Toast.LENGTH_SHORT).show();*/

        if (maxHeight > 102 /*your maxHeight phone widget*/){
            views.setViewVisibility(R.id.example_widget_text, View.VISIBLE);
        }else {
            views.setViewVisibility(R.id.example_widget_text, View.GONE);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Toast.makeText(context, "onDeleted", Toast.LENGTH_SHORT).show();
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
        super.onDisabled(context);
    }
}
