package com.example.semester4uts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> arrayList;

    public MainAdapter(Activity activity, ArrayList<String> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_main, parent, false);
        return  new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                switch (position){
                    case 0:
                        //Redirect to home
                        activity.startActivity(new Intent(activity, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case 1:
                        //Redirect to recom
                        activity.startActivity(new Intent(activity, Recomendation.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case 2:
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setTitle("Logout");
                        builder.setMessage(R.string.message);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finishAffinity();
                                System.exit(0);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
        }
    }

}
