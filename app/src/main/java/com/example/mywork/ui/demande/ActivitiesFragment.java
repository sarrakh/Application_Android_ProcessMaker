package com.example.mywork.ui.demande;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.mywork.ApiServices;
import com.example.mywork.R;
import com.example.mywork.RetroCase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitiesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private static final String MY_PREFS_NAME = "MyPreferences";
    private String token;
    Retrofit.Builder builder;
    Retrofit retrofit;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    List<RetroCase> casesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public ActivitiesFragment() { }

    public static ActivitiesFragment newInstance(){
        ActivitiesFragment fragment= new ActivitiesFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        token = preferences.getString("PrefToken","");
        Submit();
        return  inflater.inflate(R.layout.fragment_activities, container, false);



    }
    private void Submit(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        builder = new Retrofit.Builder()
                .baseUrl("http://process.isiforge.tn/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        ApiServices apiService = retrofit.create(ApiServices.class);
        Call<List<RetroCase>> call = apiService.getCases("Bearer "+token);
        call.enqueue(new Callback<List<RetroCase>>() {
            @Override
            public void onResponse(Call<List<RetroCase>> call, Response<List<RetroCase>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    casesList = response.body();

                    recyclerView = getView().findViewById(R.id.myRecycleView);
                    adapter = new MyAdapter(casesList, mListener, getContext());

                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<RetroCase>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Something went wrong...Please try later!"+t.getMessage(),Toast.LENGTH_LONG).show();

                }
        });


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(RetroCase retroCase);
    }
}
