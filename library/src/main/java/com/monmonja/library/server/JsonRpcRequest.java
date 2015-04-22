/*
 * Copyright 2014 Monmonja. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monmonja.library.server;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by almondjoseph on 29/1/15.
 */
public class JsonRpcRequest<T> {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String API_URL = "";
    private final JSONObject jsonRequest;
    private final Class<T> clazz;
    private Request request;
    private final String methodName;
    private Gson gson;


    public JsonRpcRequest (String method, Class<T> clazz, JSONObject params) {
        this.methodName = method;
        this.jsonRequest = params;
        this.clazz = clazz;

        try {
            RequestBody body = RequestBody.create(JSON, new JSONObject() {
                {

                    putOpt("id", UUID.randomUUID().toString());
                    putOpt("method", methodName);
                    putOpt("params", jsonRequest == null ? "" : jsonRequest);
                    putOpt("jsonrpc", "2.0");
                }
            }.toString());

            this.request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        makeGsonBuilder();
    }

    private void makeGsonBuilder () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(JsonResponse.class, new JSonResponseDeserializer());
        this.gson = gsonBuilder.create();
    }

    public void run (OkHttpClient client, final Success success, final Failure failure) {
        client.newCall(this.request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                failure.failure(e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseData = response.body().string();
                JsonResponse jsonResponse = gson.fromJson(responseData, JsonResponse.class);
                if (jsonResponse.result != null) {
                    success.success(gson.fromJson(jsonResponse.result, clazz));
                } else {
                    Log.d("JsonRpc", responseData);
                    failure.failure("JSONPRC2 error");
//                    successHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    public static interface Success<T> {
        public void success(T clazz);
    }


    public static interface Failure<T> {
        public void failure(String message);
    }


    /** required for gson */
    public static class JSonResponseDeserializer implements JsonDeserializer<JsonResponse> {
        public JsonResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            JsonResponse response = new JsonResponse();
            response.jsonrpc = object.get("jsonrpc").getAsString();
            response.id = object.get("id").getAsString();
            if (object.get("error") == null) {
                response.result = object.get("result").toString();
            } else {
                response.error = object.get("error").toString();
            }

            return response;
        }
    }

    public static class JsonResponse {
        public String jsonrpc = "2.0";
        public String result = null;
        public String error = null;
        public String id;

        public JsonResponse(){}
    }
}
