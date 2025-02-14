package com.edu.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;
import okhttp3.*;


public class AuthActivity extends AppCompatActivity {
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authmain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myWebView = findViewById(R.id.authWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        WebAppInterface androidInterface = new WebAppInterface(this);
        myWebView.addJavascriptInterface(androidInterface, "AndroidInterface");

        myWebView.loadUrl("file:///android_asset/auth/login.html");

    }


    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            this.mContext = c;
        }

        /**
         * Verify user login credentials via the API using OkHttp.
         *
         * @param username The username entered by the user.
         * @param password The password entered by the user.
         * @return true if login is successful, false otherwise.
         */
        @JavascriptInterface
        public boolean verifyLogin(String username, String password) {
            OkHttpClient client = new OkHttpClient();

            // Create JSON payload
            JSONObject jsonPayload = new JSONObject();
            try {
                jsonPayload.put("username", username);
                jsonPayload.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            // Build the request
            RequestBody body = RequestBody.create(
                    jsonPayload.toString(),
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url("https://localhost/login/") // Replace with your API URL
                    .post(body)
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    ((AuthActivity) mContext).runOnUiThread(() -> {
                        myWebView.loadUrl("javascript:showError('Failed to connect to server')");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody != null) {
                                String responseData = responseBody.string();
                                JSONObject jsonResponse = new JSONObject(responseData);
                                String role = jsonResponse.getString("role"); // Assuming the API returns the user role

                                // Navigate to appropriate activity
                                ((AuthActivity) mContext).runOnUiThread(() -> {
                                    navigateToAppropriateActivity(role);
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((AuthActivity) mContext).runOnUiThread(() -> {
                                myWebView.loadUrl("javascript:showError('Invalid response from server')");
                            });
                        }
                    } else {
                        ((AuthActivity) mContext).runOnUiThread(() -> {
                            myWebView.loadUrl("javascript:showError('Invalid username or password')");
                        });
                    }
                }
            });

            return true; // Indicate that the verification process has started
        }

        /**
         * Register a new tutor.
         *
         * @return true if registration is successful, false otherwise.
         */
        @JavascriptInterface
        public boolean registerTutor() {
            return true; // Placeholder for registration logic
        }

        /**
         * Navigate to the main activity for students.
         */
        @JavascriptInterface
        public void navToMainActivity() {
            Intent mainActivity = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }

        /**
         * Navigate to the main activity for tutors.
         */
        @JavascriptInterface
        public void navToTutorMainActivity() {
            Intent tutorActivity = new Intent(AuthActivity.this, TutorMainActivity.class);
            startActivity(tutorActivity);
        }

        @JavascriptInterface
        public void navToMainActivity() {
            Intent mainActivity = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }
        @JavascriptInterface
        public void navToTutorMainActivity() {
            Intent tutorActivity = new Intent(AuthActivity.this, TutorMainActivity.class);
            startActivity(tutorActivity);
        }
        @JavascriptInterface
        public void nav(String page) {
            try{
                ((AuthActivity) mContext).runOnUiThread(() -> {
                    switch (page) {
                        case "login.html":
                            myWebView.loadUrl("file:///android_asset/auth/login.html");
                            break;

                        case "register.html":
                            myWebView.loadUrl("file:///android_asset/auth/register.html");
                            break;

                        case "forgot_password.html":
                            myWebView.loadUrl("file:///android_asset/auth/forgot_password.html");
                            break;

                        default:
                            myWebView.loadUrl("file:///android_asset/splash.html");
                    }
                });
            } catch(Exception e){

            }
        }

    }

    private void navigateToAppropriateActivity(String role) {
        if ("student".equals(role)) {
            Intent mainActivity = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(mainActivity);
        } else if ("tutor".equals(role)) {
            Intent tutorActivity = new Intent(AuthActivity.this, TutorMainActivity.class);
            startActivity(tutorActivity);
        } else {
            myWebView.loadUrl("javascript:showError('Unknown user role')");
        }
    }
}