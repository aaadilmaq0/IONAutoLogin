package login.kunal.com.loginautomatically;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import login.kunal.com.loginautomatically.Functions.LoginConstants;
import login.kunal.com.loginautomatically.Functions.LoginThread;
import login.kunal.com.loginautomatically.Functions.differentFunctions;

/**
 * Implementation of App Widget functionality.
 */
public class LoginWidget extends AppWidgetProvider {
    LoginThread loginThread;


    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        differentFunctions listener = new differentFunctions() {
            @Override
            public void LoginDone(int LOGIN_STATE) {
                Log.d("gotLOGINSTATE", LOGIN_STATE + "");
                switch (LOGIN_STATE) {

                    case LoginConstants.LOGIN_NET_WORKING:
                        Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                        break;
                    case LoginConstants.LOGIN_NET_NOTWORKING:
                        Toast.makeText(context, "Cannot connect!", Toast.LENGTH_SHORT).show();
                        break;
                    case LoginConstants.LOGIN_DEACTIVATED:

                        break;
                    case LoginConstants.LOGIN_DONE:
//                        CheckInternetNow();
                        Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                        break;
                    case LoginConstants.LOGIN_NOTLOGGEDIN:
                        break;
                }
            }

            @Override
            public void updateStatus(String text) {

            }
        };

        for (int appWidgetId : appWidgetIds) {
            loginThread = new LoginThread(context, listener);
            loginThread.attemptToLogin();
        }
    }
}

