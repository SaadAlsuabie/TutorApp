package com.edu.tutorapp.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    private static final String TAG = "API";
    private final String BASEURL = "https://accentor490.pythonanywhere.com";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    private String accessToken; // Store the access token for authorization

    public API() {
        this.client = new OkHttpClient();
    }

    /**
     * Set the access token for authorization.
     *
     * @param token The Bearer token to be included in the Authorization header.
     */
    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    /**
     * Perform a GET request to the specified URL.
     *
     * @param url      The API endpoint URL.
     * @param callback The callback to handle the response.
     */
    public void get(String url, Callback callback) {
        Request request = buildRequest(BASEURL+url, "GET", null);
        client.newCall(request).enqueue(callback);
    }

    /**
     * Perform a POST request to the specified URL with a JSON payload.
     *
     * @param url      The API endpoint URL.
     * @param json     The JSON payload as a string.
     * @param callback The callback to handle the response.
     */
    public void post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = buildRequest(BASEURL+url, "POST", body);
        client.newCall(request).enqueue(callback);
    }

    /**
     * Perform a PUT request to the specified URL with a JSON payload.
     *
     * @param url      The API endpoint URL.
     * @param json     The JSON payload as a string.
     * @param callback The callback to handle the response.
     */
    public void put(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = buildRequest(BASEURL+url, "PUT", body);
        client.newCall(request).enqueue(callback);
    }

    /**
     * Perform a DELETE request to the specified URL.
     *
     * @param url      The API endpoint URL.
     * @param callback The callback to handle the response.
     */
    public void delete(String url, Callback callback) {
        Request request = buildRequest(BASEURL+url, "DELETE", null);
        client.newCall(request).enqueue(callback);
    }

    /**
     * Build a request with the appropriate method, body, and headers.
     *
     * @param url    The API endpoint URL.
     * @param method The HTTP method (e.g., GET, POST, PUT, DELETE).
     * @param body   The request body (can be null for GET/DELETE).
     * @return A fully constructed Request object.
     */
    private Request buildRequest(String url, String method, RequestBody body) {
        Headers.Builder headersBuilder = new Headers.Builder()
                .add("Content-Type", "application/json");

        // Add the Authorization header if an access token is set
        if (accessToken != null && !accessToken.isEmpty()) {
            headersBuilder.add("Authorization", "Bearer " + accessToken);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .headers(headersBuilder.build());

        // Set the appropriate HTTP method and body
        switch (method) {
            case "GET":
                return requestBuilder.get().build();
            case "POST":
                return requestBuilder.post(body).build();
            case "PUT":
                return requestBuilder.put(body).build();
            case "DELETE":
                return requestBuilder.delete(body).build();
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    /**
     * A helper method to parse the response body as a JSON object.
     *
     * @param response The HTTP response.
     * @return A JSONObject representation of the response body.
     * @throws IOException If there's an issue reading the response body.
     */
    public static JSONObject parseResponse(Response response) throws IOException {
        if (response.body() != null) {
            String responseBody = response.body().string();
            try {
                return new JSONObject(responseBody);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
            }
        }
        return null;
    }
}