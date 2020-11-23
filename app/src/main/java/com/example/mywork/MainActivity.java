package com.example.mywork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static Retrofit.Builder builder;
    public static Retrofit retrofit;
    private ProgressDialog progressDialog;
    private String MY_PREFS_NAME = "MyPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mEmailView = (EditText) findViewById(R.id.username);
        final EditText mPasswordView = (EditText) findViewById(R.id.password);
        Button mBtnView = (Button) findViewById(R.id.btn_login);

        mBtnView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String mEmail = mEmailView.getText().toString();
        String mPassword = mPasswordView.getText().toString();

        mEmailView.setError(null);
        mPasswordView.setError(null);

        View focusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(mEmail)){
            mEmailView.setError("Email requis");
            focusView = mEmailView;
            cancel = true;
        }

        else if(!isEmailValid(mEmail)){
            mEmailView.setError("Email incorrect");
            focusView = mEmailView;
            cancel = true;
        }

        if(TextUtils.isEmpty(mPassword)){
            mPasswordView.setError("Mot de passe requis");
            focusView = mPasswordView;
            cancel = true;
        }

        else if(!isPasswordValid(mPassword)){
            mPasswordView.setError("Mot de passe très court");
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) { focusView.requestFocus(); }
        else {
            User data = new User("password", "*","WMZNSSETCJDPTZSVETRNOPGYFKMAKHHQ","5813427175e8e5d18452a90035077331", mEmail, mPassword);
            Login(data);
        }
    }
});
    }
    private void Login(User data) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        builder = new Retrofit.Builder()
                .baseUrl("http://process.isiforge.tn/")
                //convert json data to object
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        ApiServices apiService = retrofit.create(ApiServices.class);
        //execute network connection
        Call<Reponse> call = apiService.connexion(data);
        call.enqueue(new Callback<Reponse>() {
            @Override
            public void onResponse(Call<Reponse> call, Response<Reponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    //utilisateur authentifier
                    String key = response.body().getAccess_token();
                    Toast.makeText(getApplicationContext(), "Welcome ", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra("token", key);

                    // sauvegarde access_token  avec un Mode de créaction du SharedPreferences privé
                    SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    //ajouter des valeurs
                    editor.putString("PrefToken", key);
                    //Valider les nouvelles valeurs
                    editor.commit();

                    startActivity(i);
                }
                    else if (response.code() == 400 && response.raw().networkResponse() != null) {
                        JSONObject jsonObject = null;
                    try {
                        progressDialog.dismiss();
                        String errorResponse = response.errorBody().string();
                        jsonObject = new JSONObject(errorResponse);
                        String errorMessage = jsonObject.getString("error_description");
                        Context context = getApplicationContext();
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.LoginContainer),errorMessage,Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(context.getResources().getColor(R.color.colorWhite2));
                        snackbar.show();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<Reponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!" + t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
    private boolean isEmailValid(String emailUser) {

        return emailUser.contains("");
    }

    private boolean isPasswordValid(String passwordUser) {

        return passwordUser.length() > 5;
    }

}
