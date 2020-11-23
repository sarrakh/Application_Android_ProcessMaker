package com.example.mywork;


import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {

    @POST("isi/oauth2/token")
    Call<Reponse> connexion(@Body User u);

    @GET("api/1.0/isi/case/start-cases")
    Call<List<RetroCase>> getCases(@Header("Authorization") String key);

    @GET("api/1.0/isi/cases/participated")
    Call<List<RetroParticipated>> getParticipatedCases(@Header("Authorization") String key);


    @GET("api/1.0/isi/project/{pro_uid}/activity/{tas_uid}/steps")
    Call<List<RetroStep>> getStep(@Header("Authorization") String key,@Path ("pro_uid")String proID,@Path("tas_uid")String tasID);


    @GET("api/1.0/isi/project/{pro_uid}/dynaform/{step_uid_obj}")
    Call<RetroForm> getForm(@Header("Authorization") String key,@Path ("pro_uid")String proID,@Path("step_uid_obj")String stepID);


    @POST("api/1.0/isi/cases")
    Call<JsonObject> submitCase(@Header("Authorization") String key, @Body JsonObject object);

}
