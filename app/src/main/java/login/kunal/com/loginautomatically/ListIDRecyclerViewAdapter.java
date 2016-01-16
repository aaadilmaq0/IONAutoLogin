package login.kunal.com.loginautomatically;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import login.kunal.com.loginautomatically.Functions.SQLiteDatabaseAdapter;
import login.kunal.com.loginautomatically.Functions.SharedPreferencesClass;
import login.kunal.com.loginautomatically.Functions.User;

import java.util.List;

public class ListIDRecyclerViewAdapter extends RecyclerView.Adapter<ListIDRecyclerViewAdapter.ViewHolder> {

    private final List<User> Users;
    Context context;
    SharedPreferencesClass spc;
    String defaultID = "";
    int green;

    public ListIDRecyclerViewAdapter(List<User> items, Context context) {
        Users = items;
        this.context = context;
        databaseAdapter = SQLiteDatabaseAdapter.getInstance(context);
        spc = SharedPreferencesClass.getInstance(context);
        green = context.getResources().getColor(R.color.connected_green);
        defaultID = spc.getDefaultID();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listid_row, parent, false);
        return new ViewHolder(view);
    }

    SQLiteDatabaseAdapter databaseAdapter;


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.id_txt.setText(Users.get(position).getUsername());
        holder.id_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spc.saveString(SharedPreferencesClass.SP_DEFAULT, Users.get(position).getUsername());
                Toast.makeText(context, Users.get(position).getUsername() + " is now default!", Toast.LENGTH_LONG).show();

                Intent returnIntent = new Intent();
                Activity act = ((Activity) context);

                act.setResult(Activity.RESULT_OK, returnIntent);
                act.finish();
            }
        });
        holder.id_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseAdapter.removeUser(Users.get(position).getUsername())) {
                    if (Users.get(position).getUsername() == spc.getDefaultID()) {
                        spc.saveDefaultID("");
                    }
                    Users.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, Users.size());
                    notifyDataSetChanged();

                } else {
                    Toast.makeText(context, "Cannot Remove User!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout id_container;
        public TextView id_txt;
        public ImageButton id_remove;

        public ViewHolder(View view) {
            super(view);
            id_txt = (TextView) view.findViewById(R.id.id_txt);
            id_remove = (ImageButton) view.findViewById(R.id.remove_id);
            id_container = (RelativeLayout) view.findViewById(R.id.id_container);
        }
    }
}
