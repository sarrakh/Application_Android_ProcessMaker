package com.example.mywork.ui.demande;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mywork.Main3Activity;
import com.example.mywork.R;
import com.example.mywork.RetroCase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    private List<RetroCase> dataList;
    private final ActivitiesFragment.OnListFragmentInteractionListener mListener;

    Context context;

    public MyAdapter(List<RetroCase> dataList, ActivitiesFragment.OnListFragmentInteractionListener mListener, Context context) {
        this.dataList = dataList;
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the Layout with the UI that we have created for the RecyclerView
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
     return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        // set the data in items of the RecyclerView
        String title = dataList.get(position).getPro_title();
        int pos1 = title.indexOf('(');
        int pos2 = title.indexOf(')');
        String titre_demande = title.substring(0,pos1);
        String desc = title.substring(pos1+1,pos2);
        holder.textView.setText(titre_demande);
        holder.descrip.setText(desc);
        final String tache = dataList.get(position).getTas_uid();
        final String processus = dataList.get(position).getPro_uid();
        holder.task.setText(tache);
        holder.proc.setText(processus);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Main3Activity.class);
                intent.putExtra("task",holder.task.getText().toString());
                intent.putExtra("proc",holder.proc.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        //return the item count
        return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        //connecting od with the text views
        public final View view;
        TextView textView;
        TextView descrip;
        TextView task;
        TextView proc;
        LinearLayout parentLayout;

        CustomViewHolder(View itemView){
            super(itemView);
            view = itemView;
            textView = view.findViewById(R.id.demande);
            descrip = view.findViewById(R.id.description);
            task = view.findViewById(R.id.tache);
            proc = view.findViewById(R.id.processus);
            parentLayout = view.findViewById(R.id.parent_layout);
        }
        @Override
        public String toString() {
            return super.toString() ;
        }

    }

}

