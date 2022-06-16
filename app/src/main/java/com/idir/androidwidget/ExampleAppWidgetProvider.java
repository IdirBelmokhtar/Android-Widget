package com.idir.androidwidget;

import static com.idir.androidwidget.ExampleAppWidgetConfig.KEY_BUTTON_TEXT;
import static com.idir.androidwidget.ExampleAppWidgetConfig.SHARED_PRES;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_TOAST = "actionToast";
    public static final String EXTRA_ITEM_POSITION = "extraItemPosition";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds){
            Toast.makeText(context, "onUpdate", Toast.LENGTH_SHORT).show();

            Intent buttonIntent = new Intent(context, MainActivity.class);
            PendingIntent buttonPendingIntent = PendingIntent.getActivity(context,0, buttonIntent, 0);

            SharedPreferences preferences = context.getSharedPreferences(SHARED_PRES, Context.MODE_PRIVATE);
            String buttonText = preferences.getString(KEY_BUTTON_TEXT + appWidgetId, "thank you");

            Intent serviceIntent = new Intent(context, ExampleWidgetServices.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent clickIntent = new Intent(context, ExampleAppWidgetProvider.class);
            clickIntent.setAction(ACTION_TOAST);
            PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            views.setOnClickPendingIntent(R.id.example_widget_button, buttonPendingIntent);
            views.setCharSequence(R.id.example_widget_button, "setText", buttonText);
            views.setRemoteAdapter(R.id.example_widget_stack_view, serviceIntent);
            views.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view);
            views.setPendingIntentTemplate(R.id.example_widget_stack_view, clickPendingIntent);

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

        if (maxHeight > 250 /*your maxHeight phone widget*/){
            views.setViewVisibility(R.id.example_widget_text, View.VISIBLE);
            views.setViewVisibility(R.id.example_widget_button, View.VISIBLE);
        }else {
            views.setViewVisibility(R.id.example_widget_text, View.GONE);
            views.setViewVisibility(R.id.example_widget_button, View.GONE);
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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_TOAST.equals(intent.getAction())){
            int clickPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);
            Toast.makeText(context, "Clicked position: " + clickPosition, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
}
