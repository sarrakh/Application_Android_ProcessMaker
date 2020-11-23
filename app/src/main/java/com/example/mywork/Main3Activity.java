package com.example.mywork;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main3Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String MY_PREFS_NAME = "MyPreferences";
    private static final int READ_REQUEST_CODE = 42;
    Retrofit.Builder builder;
    Retrofit retrofit;
    private ProgressDialog progressDialog;
    String token ;
    String proc ;
    String task ;
    String step_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        progressDialog = new ProgressDialog(Main3Activity.this);
        progressDialog.setTitle("Veuillez patienter");
        progressDialog.setMessage("Chargement ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        token = preferences.getString("PrefToken","");
        proc = getIntent().getStringExtra("proc");
        task = getIntent().getStringExtra("task");
        Submit();
    }

    private void Submit() {
        progressDialog.show();
        builder = new Retrofit.Builder()
                .baseUrl("http://process.isiforge.tn/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        ApiServices apiService = retrofit.create(ApiServices.class);
        Call<List<RetroStep>> call = apiService.getStep("Bearer "+ token,proc,task);

        call.enqueue(new Callback<List<RetroStep>>() {
            @Override
            public void onResponse(Call<List<RetroStep>> call, Response<List<RetroStep>> response) {
                if(response.isSuccessful()){
                    List<RetroStep> listSteps = response.body();
                    for(RetroStep step:listSteps){
                        if(step.getStep_type_obj().contains("DYNAFORM")){
                            step_id = step.getStep_uid_obj();
                            getDataForm();
                        }
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Aucun formulaire disponible",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RetroStep>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"error 1 "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDataForm(){
        builder = new Retrofit.Builder()
                .baseUrl("http://process.isiforge.tn/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        ApiServices apiService = retrofit.create(ApiServices.class);
        Call<RetroForm> call = apiService.getForm("Bearer "+ token,proc,step_id);

        call.enqueue(new Callback<RetroForm>() {
            @Override
            public void onResponse(Call<RetroForm> call, Response<RetroForm> response) {
                if(response.isSuccessful()) {
                    loadForm(response.body());
                }
            }

            @Override
            public void onFailure(Call<RetroForm> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Erreur !! "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadForm(RetroForm form) {
        RetroForm formulaire = form;
        String dyn_content = formulaire.getDyn_content();
        JSONObject object = null;
        final LinearLayout FormLayout = findViewById(R.id.layout_form);
        FormLayout.setPadding(48, 32, 48, 0);

        try {
            //Returns the next value from the input
            object = (JSONObject) new JSONTokener(dyn_content).nextValue();
            //extracting data array from json string
            JSONArray jsonArray1 = object.getJSONArray("items");
            final JSONObject objectVars = new JSONObject();
            final JSONArray arrayVars = new JSONArray();
            int indice = 1;
            //loop to get all json objects from data json array
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject contentItems1 = jsonArray1.getJSONObject(i);
                // getting inner array Ingredients
                JSONArray jsonArray2 = contentItems1.getJSONArray("items");

                final JSONArray jsonArrayDeb = jsonArray2.getJSONArray(0);
                TextView textViewTitre = findViewById(R.id.titreForm);
                String titre_form;
                for (int x = 0; x < jsonArrayDeb.length(); x++) {
                    JSONObject objectDebut = jsonArrayDeb.getJSONObject(x);
                    if(objectDebut.has("type")){
                        String test_type = objectDebut.getString("type");
                        if(test_type.equals("title")) {
                            titre_form = objectDebut.getString("label");
                            textViewTitre.setText(titre_form);
                            break;
                        }

                    }
                }

                for (int j = 1; j < jsonArray2.length(); j++) {
                    final JSONArray jsonArray3 = jsonArray2.getJSONArray(j);
                    for (int k = 0; k < jsonArray3.length(); k++) {
                        JSONObject contentTabItems2 = jsonArray3.getJSONObject(k);
                        TextView labels = new TextView(getApplicationContext());
                        final EditText inputs = new EditText(getApplicationContext());
                        String required = " *";
                        if(contentTabItems2.has("type")){

                            String type = contentTabItems2.getString("type");



                            if(type.contains("title")){
                                String title_bloc = contentTabItems2.getString("label");
                                labels.setText(title_bloc);
                                if(type.equals("title")){
                                    labels.setTextSize(24);
                                    labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorBlack));

                                }
                                else{
                                    labels.setTextSize(17);
                                    labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorRed));
                                }
                                FormLayout.addView(labels);
                            }



                            if(type.contains("text")){

                                String text_label = contentTabItems2.getString("label");
                                String text_placeholder = contentTabItems2.getString("placeholder");
                                boolean text_required = contentTabItems2.getBoolean("required");
                                String text_variable = contentTabItems2.getString("variable");

                                if(text_required==true){
                                    labels.setText(text_label+required);
                                }
                                else{
                                    labels.setText(text_label);
                                }
                                labels.setTextSize(16);
                                labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorBlack));

                                inputs.setHint(text_placeholder);
                                inputs.setTextSize(14);
                                inputs.setId(indice);
                                objectVars.put(text_variable,indice);
                                indice++;
                                if (type.equals("textarea")){
                                    inputs.setScroller(new Scroller(getApplicationContext()));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                    inputs.setLayoutParams(params);
                                    inputs.setSingleLine(false);
                                    inputs.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                    inputs.setVerticalScrollBarEnabled(true);
                                    inputs.setMinLines(2);
                                }
                                FormLayout.addView(labels);
                                FormLayout.addView(inputs);
                            }

                            if(type.contains("radio")){
                                String group_label = contentTabItems2.getString("label");
                                boolean radio_required = contentTabItems2.getBoolean("required");

                                if(radio_required == true){
                                    labels.setText(group_label+required);
                                }
                                else{
                                    labels.setText(group_label);
                                }
                                labels.setTextSize(20);
                                labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

                                JSONArray options = contentTabItems2.getJSONArray("options");
                                RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                                for(int l=0;l<options.length();l++){
                                    JSONObject option = options.getJSONObject(l);
                                    RadioButton op = new RadioButton(getApplicationContext());
                                    String label_radio = option.getString("label");
                                    op.setText(label_radio);
                                    radioGroup.addView(op);
                                }
                                FormLayout.addView(labels);
                                FormLayout.addView(radioGroup);
                            }

                            if(type.contains("dropdown")){
                                String dropdown_label = contentTabItems2.getString("label");
                                String dropdown_placeholder = contentTabItems2.getString("placeholder");
                                boolean dropdown_required = contentTabItems2.getBoolean("required");

                                if(dropdown_required == true){
                                    labels.setText(dropdown_label+required);
                                }
                                else{
                                    labels.setText(dropdown_label);
                                }
                                labels.setTextSize(20);
                                labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorBlack));

                                JSONArray options = contentTabItems2.getJSONArray("options");
                                Spinner spinner = new Spinner(getApplicationContext());
                                String[] list_options = new String[options.length()+1];
                                list_options[0] = dropdown_placeholder;
                                for(int l=0;l<options.length();l++){
                                    JSONObject option = options.getJSONObject(l);
                                    String label_optionDrop = option.getString("label");
                                    list_options[l+1] = label_optionDrop;
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_options) {
                                    @Override
                                    public boolean isEnabled(int position) {
                                        if (position == 0) { return false; }
                                        else { return true; }
                                    }

                                    @Override
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getDropDownView(position, convertView, parent);
                                        TextView tv = (TextView) view;
                                        if(position == 0){ tv.setTextColor(Color.GRAY); }
                                        else { tv.setTextColor(Color.BLACK); }
                                        return view;
                                    }
                                };
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);

                                //String select = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                                //Toast.makeText(getApplicationContext(),select,Toast.LENGTH_LONG).show();

                                FormLayout.addView(labels);
                                FormLayout.addView(spinner);
                            }

                            if(type.contains("datetime")){
                                String date_label = contentTabItems2.getString("label");
                                String date_placeholder = contentTabItems2.getString("placeholder");
                                boolean date_required = contentTabItems2.getBoolean("required");
                                String date_variable = contentTabItems2.getString("variable");

                                if(date_required == true){
                                    labels.setText(date_label+required);
                                }
                                else{
                                    labels.setText(date_label);
                                }
                                labels.setTextSize(20);
                                labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

                                inputs.setHint(date_placeholder);
                                final Calendar myCalendar = Calendar.getInstance();
                                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        myCalendar.set(Calendar.YEAR, year);
                                        myCalendar.set(Calendar.MONTH, monthOfYear);
                                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        String myFormat = "dd/MM/yy";
                                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                                        inputs.setText(sdf.format(myCalendar.getTime()));
                                    }
                                };

                                inputs.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(Main3Activity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                    }
                                });

                                inputs.setFocusable(false);
                                inputs.setTextSize(18);
                                inputs.setId(indice);
                                objectVars.put(date_variable,indice);
                                indice++;
                                FormLayout.addView(labels);
                                FormLayout.addView(inputs);
                            }

                            if(type.contains("file")){
                                String file_label = contentTabItems2.getString("label");
                                boolean file_required = contentTabItems2.getBoolean("required");
                                if(file_required == true){
                                    labels.setText(file_label+required);
                                }
                                else{
                                    labels.setText(file_label);
                                }
                                labels.setTextSize(16);
                                labels.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTurquoise));

                                final Button upload = new Button(getApplicationContext());
                                upload.setText("Selectionner un fichier");
                                upload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_upload_black_24dp, 0, 0, 0);
                                upload.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite2));
                                upload.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorTurquoise));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(70, 20, 70, 20);
                                upload.setPadding(20,10,10,10);
                                upload.setGravity(Gravity.CENTER);
                                upload.setLayoutParams(params);

                                upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("*/*");
                                        startActivityForResult(intent, READ_REQUEST_CODE);
                                    }
                                });
                                FormLayout.addView(labels);
                                FormLayout.addView(upload);
                            }

                            if(type.contains("submit")){
                                String btn_label = contentTabItems2.getString("label");
                                final String btn_name = contentTabItems2.getString("name");
                                final Button btn = new Button(getApplicationContext());
                                btn.setText(btn_label);
                                btn.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite2));
                                btn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(40, 20, 40, 20);
                                btn.setLayoutParams(params);
                                btn.setGravity(Gravity.CENTER);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                            progressDialog.show();
                                            try {
                                                JSONObject objectSubmit = new JSONObject(objectVars.toString());

                                                for (Iterator iterator = objectVars.keys(); iterator.hasNext();) {
                                                    //It returns the next element in the collection until the hasNext()method return true.
                                                    String cle = iterator.next().toString();
                                                    int valeur = objectVars.getInt(cle);
                                                    TextView text_input = findViewById(valeur);
                                                    String contenu_input = text_input.getText().toString();
                                                    objectSubmit.put(cle,contenu_input);
                                                }
                                                arrayVars.put(objectSubmit);
                                                JSONObject jsonObjectForSub = new JSONObject();
                                                jsonObjectForSub.put("pro_uid", proc);
                                                jsonObjectForSub.put("tas_uid", task);
                                                jsonObjectForSub.put("variables", arrayVars);

                                                String jsonString = jsonObjectForSub.toString();
                                                JsonObject jsonObjectFinal = (new JsonParser()).parse(jsonString).getAsJsonObject();
                                                SubmitFormCase(jsonObjectFinal);
                                            }
                                            catch (JSONException e) { e.printStackTrace(); }
                                        }

                                });
                                FormLayout.addView(btn);
                            }
                        }
                    }
                }
            }
            progressDialog.dismiss();
        }
        catch (JSONException e) { e.printStackTrace();}
    }

    private void SubmitFormCase(JsonObject object) {
        builder = new Retrofit.Builder()
                .baseUrl("http://process.isiforge.tn/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        ApiServices apiService = retrofit.create(ApiServices.class);
        Call<JsonObject> call = apiService.submitCase("Bearer "+token,object);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Demande envoyée avec succée", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Erreur !!  "+t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }




    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }



}

