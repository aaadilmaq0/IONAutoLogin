package com.kunall17.ionautologin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.kunall17.ionautologin.Functions.Encryption;
import com.kunall17.ionautologin.Functions.Logger;
import com.kunall17.ionautologin.Functions.SQLiteDatabaseAdapter;
import com.kunall17.ionautologin.Functions.SharedPreferencesClass;
import com.kunall17.ionautologin.Functions.User;

import com.kunall17.ionautologin.R;

public class ID_list extends AppCompatActivity {
    List<User> users;
    RecyclerView recyclerView;
    ListIDRecyclerViewAdapter adapter;
    SQLiteDatabaseAdapter databaseAdapter;
    Encryption enc;
    Logger log;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("zxc");

        System.out.println(item.getItemId());
        if (item.getItemId() == R.id.action_idList_add) {


            final Dialog dialog = new Dialog(ID_list.this);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("ION ID");
            final EditText username_et = (EditText) dialog.findViewById(R.id.userName_ET);
            final EditText pass_et = (EditText) dialog.findViewById(R.id.pass_ET);
            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
            Button save_btn = (Button) dialog.findViewById(R.id.save_btn);
            final TextInputLayout user_til = (TextInputLayout) dialog.findViewById(R.id.name_txt_layout);
            final TextInputLayout pass_til = (TextInputLayout) dialog.findViewById(R.id.pass_txt_layout);
            // if button is clicked, close the custom dialog


            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username_et.getText().toString() == "") {
                        user_til.setError("Enter UserName");
                        return;
                    }
                    if (pass_et.getText().toString() == "") {
                        pass_til.setError("Enter Password");
                        return;
                    }
                    try {
                        int a = databaseAdapter.insertData(username_et.getText().toString(), enc.encrypt(pass_et.getText().toString()));
                        System.out.println("long-" + a);
                        if (a != -1) {
                            users.add(new User(username_et.getText().toString(), enc.encrypt(pass_et.getText().toString())));
                            adapter.notifyItemInserted(a);
                            adapter.notifyDataSetChanged();

                            log.addToLog("userAdded-" + username_et.getText().toString());
                        }

                    } catch (SQLIntegrityConstraintViolationException e) {
                        Toast.makeText(ID_list.this, "This ID already Found!", Toast.LENGTH_SHORT).show();
                        username_et.setText("");
                        pass_et.setText("");
                    }
                    Toast.makeText(ID_list.this, "Saved!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (item.getItemId() == android.R.id.home) {
            SharedPreferencesClass spc = SharedPreferencesClass.getInstance(ID_list.this);
            if (!spc.ifExists(SharedPreferencesClass.SP_DEFAULT)) {
                Toast.makeText(ID_list.this, "Make some ID your default by clicking on the ID", Toast.LENGTH_LONG).show();
                return true;
            }
            this.finish();
            return true;
        }


        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("ID List");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_id);
        enc = new Encryption();
        databaseAdapter = SQLiteDatabaseAdapter.getInstance(ID_list.this);
        users = databaseAdapter.getAllData();
        adapter = new ListIDRecyclerViewAdapter(users, ID_list.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ID_list.this));
        log = Logger.getInstance();
    }

}