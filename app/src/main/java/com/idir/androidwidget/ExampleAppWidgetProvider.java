package com.idir.androidwidget;

import static com.idir.androidwidget.ExampleAppWidgetConfig.KEY_BUTTON_TEXT;
import static com.idir.androidwidget.ExampleAppWidgetConfig.SHARED_PRES;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, 0);

            SharedPreferences preferences = context.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE);
            String buttonText = preferences.getString(KEY_BUTTON_TEXT + appWidgetId, "press me");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
            views.setCharSequence(R.id.example_widget_button, "setText", buttonText);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
