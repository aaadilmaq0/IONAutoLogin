package com.kunall17.ionautologin;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunall17.ionautologin.Functions.CheckInternet;
import com.kunall17.ionautologin.Functions.Logger;
import com.kunall17.ionautologin.Functions.LoginConstants;
import com.kunall17.ionautologin.Functions.LoginThread;
import com.kunall17.ionautologin.Functions.SQLiteDatabaseAdapter;
import com.kunall17.ionautologin.Functions.SharedPreferencesClass;
import com.kunall17.ionautologin.Functions.differentFunctions;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    int minutes = 1;
    CountDownTimer cdt;
    Logger log;
    CheckInternet checkInternet;
    SharedPreferencesClass spc;
    private SQLiteDatabaseAdapter database;
    private String defaultUsername;
    private String defaultPassword;
    LoginThread loginThread;
    private SharedPreferencesClass preferencesClass;
    SQLiteDatabaseAdapter databaseAdapter;
    TextView internet_txt;
    FloatingActionButton fab;
    private LinearLayout status_layout;
    private int currentapiVersion;
    private TextView status_txt;

    final int CONNECTED = 1;
    final int NOT_CONNECTED = 2;
    int color_red;
    int color_green;

    differentFunctions listener;
    private boolean startFromWidget;

    public void LoginAutomatically() {
        loginThread.attemptToLogin();
    }

    public void saveCredentials() {
        defaultUsername = preferencesClass.getDefaultID();

        if (defaultUsername != "" && databaseAdapter.ifUserNameExists(defaultUsername)) {
            log.addToLog("Id Found");
            databaseAdapter.getAllData();
            defaultPassword = databaseAdapter.getPassword(defaultUsername);
        } else {
            defaultUsername = "";
            log.addToLog("ID NOT FOUND");
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        startFromWidget = getIntent().getBooleanExtra("startFromWidget", false);
        if (startFromWidget) {
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log = Logger.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
//        internet_txt = (TextView) findViewById(R.id.internet_txt);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        status_layout = (LinearLayout) findViewById(R.id.status_layout);

//        id_spn = (Spinner) findViewById(R.id.spinner_id);
        status_txt = (TextView) findViewById(R.id.status_txt);

        setupListener();

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Home");
        this.setTitle("Home");

        spc = SharedPreferencesClass.getInstance(MainActivity.this);
        preferencesClass = SharedPreferencesClass.getInstance(this);
        databaseAdapter = SQLiteDatabaseAdapter.getInstance(this);
        saveCredentials();
        checkInternet = new CheckInternet(listener, MainActivity.this);
        loginThread = new LoginThread(MainActivity.this, defaultUsername, defaultPassword, listener);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        saveCredentials();
        color_red = getResources().getColor(R.color.not_connected_red);
        color_green = getResources().getColor(R.color.connected_green);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new preferenceFragment()).commit();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInternetNow();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(color_red));


        System.out.println("Something here-" + startFromWidget);

        if (startFromWidget) {
            startedFromWidget();

        }
    }

    public void startedFromWidget() {
        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        System.out.println("Something here-" + wifiManager.getConnectionInfo().getSSID());

        if (wifiManager.getConnectionInfo().getSSID().contains("ION")) {
            System.out.println("Something here-" + wifiManager.getConnectionInfo().getSSID());
            LoginAutomatically();
        } else {
            System.out.println("Something here-disabled!");
            wifiManager.setWifiEnabled(true);
            this.registerReceiver(mWifiStateChangedReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startFromWidget = getIntent().getBooleanExtra("startFromWidget", false);
        System.out.println("SomethingnewIntenthere-" + startFromWidget);
        if (startFromWidget) startedFromWidget();

    }

    private BroadcastReceiver mWifiStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info1 = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                boolean connected = info1.isConnected();
                if (connected) {
                    LoginAutomatically();
                    Log.d("somethingmore-", "attempted login!");
                }
            }

        }
    };

    private void setupListener() {
        listener = new differentFunctions() {
            @Override
            public void LoginDone(int LOGIN_STATE) {
                log.addToLog(LOGIN_STATE + "");

                switch (LOGIN_STATE) {
                    case LoginConstants.LOGIN_NET_WORKING:
                        textUpdate(CONNECTED);
                        Toast.makeText(MainActivity.this, "Logged In!", Toast.LENGTH_SHORT).show();


                        break;
                    case LoginConstants.LOGIN_NET_NOTWORKING:
                        textUpdate(NOT_CONNECTED);
                        Toast.makeText(MainActivity.this, "Cannot Log In!", Toast.LENGTH_SHORT).show();
                        if (startFromWidget) { //minimize the app
                            Intent main = new Intent(Intent.ACTION_MAIN);
                            main.addCategory(Intent.CATEGORY_HOME);
                            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                        }
                        try {
                            if (mWifiStateChangedReceiver != null)
                                mWifiStateChangedReceiver.abortBroadcast();

                        } catch (IllegalStateException e) {
                            System.out.println(e.toString());
                        }
                        LoginAutomatically();
                        break;
                    case LoginConstants.LOGIN_DEACTIVATED:

                        break;
                    case LoginConstants.LOGIN_DONE:
//                        CheckInternetNow();
                        textUpdate(CONNECTED);
                        break;
                    case LoginConstants.LOGIN_NOTLOGGEDIN:
                        LoginAutomatically();
                        break;
                    case LoginConstants.LOGIN_SAMEUSER:

                        break;
                    case LoginConstants.LOGIN_UNSUCCESSFULL:

                        break;
                    case LoginConstants.LOGIN_WRONGUSERNAME:
                        break;
                }
            }

            @Override
            public void updateStatus(String text) {
                status_txt.setText(text);
            }
        };
    }

    private void CheckInternetNow() {
        checkInternet = new CheckInternet(listener, MainActivity.this);
        checkInternet.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to th
        // e action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_log) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.log_dialog);
            dialog.setTitle("LOG");

            Button ok_btn = (Button) dialog.findViewById(R.id.close_log_btn);
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            EditText et = (EditText) dialog.findViewById(R.id.log_et);
            et.setText(log.getText());
            dialog.show();
        } else if (id == R.id.action_contact) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "kunall.gupta17@gmail.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } else if (id == R.id.action_github) {
            String url = "https://github.com/kunall17/IONAutoLogin";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.action_login) {
            loginThread.attemptToLogin();
        }
        return super.onOptionsItemSelected(item);
    }


    public void textUpdate(int which) {
        switch (which) {
            case CONNECTED:
                if (((ColorDrawable) status_layout.getBackground()).getColor() == color_red) {
                    animateColors(color_red, color_green);
                }
                break;
            case NOT_CONNECTED:
                if (((ColorDrawable) status_layout.getBackground()).getColor() == color_green) {
                    animateColors(color_green, color_red);
                }
                break;
        }
    }


    public void animateColors(Integer colorFrom, Integer colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
                status_layout.setBackgroundColor((Integer) animator.getAnimatedValue());
                fab.setBackgroundTintList(ColorStateList.valueOf((Integer) animator.getAnimatedValue()));
//                        setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{(Integer) animator.getAnimatedValue()}));
            }
        });

        colorAnimation.setDuration(3000);
        colorAnimation.setStartDelay(0);
        colorAnimation.start();
    }

    public void startTimer() {
        Log.d("d", "timer started");
        cdt = new CountDownTimer(Integer.MAX_VALUE, minutes * 60000) { //60000
            @Override
            public void onTick(long millisUntilFinished) {
                CheckInternetNow();
            }

            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class preferenceFragment extends PreferenceFragment {
        Preference IDPref;
        SwitchPreference sp_autocheck;
        ListPreference lp;
        SwitchPreference check_wifi;
        private ListPreference check_list;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_settings);
            setHasOptionsMenu(true);

            IDPref = findPreference("studentID");

            check_list = (ListPreference) findPreference("check_list");
            sp_autocheck = (SwitchPreference) findPreference("check_enabled");


            IDPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), ID_list.class);
                    startActivityForResult(intent, 2);
                    return true;
                }
            });
            sp_autocheck.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (sp_autocheck.isChecked()) {
                        check_list.setEnabled(true);
                        startTimer();
                    } else {
                        if (cdt != null) cdt.cancel();
                        check_list.setEnabled(false);
                    }
                    return true;
                }
            });

            check_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    minutes = Integer.parseInt(lp.getValue());
                    spc.saveIntegerToSharedPreferences(SharedPreferencesClass.SP_MINUTES, minutes);
                    return true;
                }
            });

            getAndSetDefaults();

//            ListPreference lp = (ListPreference) findPreference("list_default_id");
//            String[] array = {"1", "2", "3"};
//            CharSequence[] entries = array;
//            CharSequence[] entryValues = array;
//            lp.setEntries(entries);
//            lp.setDefaultValue("1");
//            lp.setEntryValues(entryValues);


        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            saveCredentials();
            loginThread.changeCred(defaultUsername, defaultPassword);

        }

        @Override
        public void onResume() {
            super.onResume();
            saveCredentials();
            loginThread.changeCred(defaultUsername, defaultPassword);
            IDPref.setSummary("Default- " + spc.getDefaultID());

        }

        private void getAndSetDefaults() {
            if (spc.getDefaultID() != null && databaseAdapter.ifUserNameExists(spc.getDefaultID())) {
                IDPref.setSummary("Default- " + spc.getDefaultID());
            }
            sp_autocheck.setChecked(spc.getAutoCheckBoolean());
            check_list.setValue("" + spc.getDefaultMinutes());
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
