package com.kunall17.ionautologin;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kunall17.ionautologin.Functions.LoginThread;

/**
 * Implementation of App Widget functionality.
 */
public class LoginWidget extends AppWidgetProvider {

    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Toast.makeText(context, "Widget Loaded", Toast.LENGTH_SHORT).show();
        views = new RemoteViews(context.getPackageName(), R.layout.login_widget);
        Intent newintent = new Intent(context, MainActivity.class);
        newintent.putExtra("startFromWidget", true);
        newintent.putExtra("startFromWidget1", "zxc");
        newintent.addCategory(Intent.CATEGORY_HOME);
        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pd = PendingIntent.getActivity(context, 0, newintent, 0);
        views.setOnClickPendingIntent(R.id.widgetLayout, pd);
        // Construct the RemoteViews object
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

