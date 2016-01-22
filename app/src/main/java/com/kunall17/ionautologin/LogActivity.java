package com.kunall17.ionautologin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunall17.ionautologin.Functions.Logger;

import com.kunall17.ionautologin.R;

public class LogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Logger log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        log = Logger.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.log_recyclerView);

    }


    public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private LayoutInflater inflater;

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.log_row, parent, false);
            LogViewHolder holder = new LogViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
//TODO logs
        }

        @Override
        public int getItemCount() {
            return log.getSize();
        }


    }

    public class LogViewHolder extends RecyclerView.ViewHolder {

        TextView time_tv;
        TextView log_tv;

        public LogViewHolder(final View itemView) {
            super(itemView);

            time_tv = (TextView) itemView.findViewById(R.id.log_txt_time);
            log_tv = (TextView) itemView.findViewById(R.id.log_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }
}


