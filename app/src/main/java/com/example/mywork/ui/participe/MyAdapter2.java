package com.example.mywork.ui.participe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mywork.R;
import com.example.mywork.RetroParticipated;

import java.util.List;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {

    private List<RetroParticipated> suiviList;
    private  final ParticipedFragment.OnListFragmentInteractionListener mListener;
    Context context;

    public MyAdapter2(List<RetroParticipated> suiviList, ParticipedFragment.OnListFragmentInteractionListener mListener, Context context) {
        this.suiviList = suiviList;
        this.mListener = mListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //For getting a view for listview
     View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.row_participated, parent, false);
     return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String title = suiviList.get(position).getApp_title();
        String titleProc = suiviList.get(position).getApp_pro_title();
        String titleTask = suiviList.get(position).getApp_tas_title();
        String CreateDate = suiviList.get(position).getApp_create_date();
        String Status=suiviList.get(position).getApp_status();
        String current_user=suiviList.get(position).getApp_current_user();
        holder.titre.setText(title);
        holder.titreProcessus.setText(titleProc + " - " + titleTask);
        holder.user_name.setText("User Name : "+current_user);
        holder.DateCreation.setText("Envoyée le : " + CreateDate);
        holder.Statut.setText("Status :"+ Status);
    }

    @Override
    public int getItemCount() {
        return suiviList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        TextView titre;
        TextView titreProcessus;
        TextView DateCreation;
        TextView user_name;
        TextView Statut;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titre = view.findViewById(R.id.Title);
            titreProcessus = view.findViewById(R.id.ProTitle);
            DateCreation = view.findViewById(R.id.CreateDate);
            user_name=view.findViewById(R.id.username);
            Statut=view.findViewById(R.id.statut);

        }
        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
