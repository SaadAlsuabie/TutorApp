package com.edu.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.*;

public class AuthActivity extends AppCompatActivity {
    private WebView myWebView;
    private final String BASEURL = "https://accentor490.pythonanywhere.com";
    private static final String SHARED_PREF_NAME = "MyAppPreferences"; // Name of the preference file
    private static final String KEY_USERNAME = "username"; // Key for storing username
    private static final String KEY_ROLE = "role"; // Key for storing role
    private static final String KEY_ACCESS_TOKEN = "access_token"; // Key for storing access token
    private static final String KEY_REFRESH_TOKEN = "refresh_token"; // Key for storing refresh token
    private static final String KEY_TIMEOUT = "timeout"; // Key for storing timeout

    private SharedPreferences sharedPreferences;
    private SharedPreferencesUtils sharedPreferencesUtils;

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

//         sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        String access_token = sharedPreferences.getString(KEY_ACCESS_TOKEN, "logged_out");
//        String role = sharedPreferences.getString(KEY_ROLE, "unknown role");

        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        String access_token = sharedPreferencesUtils.getAccessToken();
        String role = sharedPreferencesUtils.getRole();

        myWebView = findViewById(R.id.authWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        WebAppInterface androidInterface = new WebAppInterface(this);
        myWebView.addJavascriptInterface(androidInterface, "AndroidInterface");

        if ("none".equals(access_token) || "none".equals(role)){
            myWebView.loadUrl("file:///android_asset/auth/login.html");
        } else {
            navigateToAppropriateActivity(role);
        }
    }


    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            this.mContext = c;
        }

        /**
         * Register a new user via the API using OkHttp.
         *
         * @param username The username entered by the user.
         * @param email    The email entered by the user.
         * @param password The password entered by the user.
         * @param role     The user role (e.g., "student", "tutor").
         * @return true if registration is successful, false otherwise.
         */
        @JavascriptInterface
        public boolean registerUser(String fullname, String username, String email, String password, String role, String faculty, String major, String year, String yearLevelstudent) {
            OkHttpClient client = new OkHttpClient();

            // Create JSON payload
            JSONObject jsonPayload = new JSONObject();
            try {
                jsonPayload.put("username", username);
                jsonPayload.put("fullname", fullname);
                jsonPayload.put("email", email);
                jsonPayload.put("password", password);
                jsonPayload.put("role", role); // Role can be "student" or "tutor"
                jsonPayload.put("faculty", faculty);
                jsonPayload.put("major", major);
                jsonPayload.put("yearleveltutor", year);
                jsonPayload.put("yearlevelstudent", yearLevelstudent);
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
                    .url(BASEURL+"/register/") // Replace with your API URL
                    .post(body)
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody != null) {
                                String responseData = responseBody.string();

                                JSONObject jsonResponse = new JSONObject(responseData);
                                String message = jsonResponse.optString("message", "Registration successful");

                                // Show success message
                                ((AuthActivity) mContext).runOnUiThread(() -> {
                                    myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            // Handle the result returned from JavaScript (if any)
                                            Log.d("WebView", "Result from JS: " + value);
                                        }
                                    });
                                    myWebView.evaluateJavascript("showToast(\"Registration Successfull!.\", \"success\", 4000)", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            // Handle the result returned from JavaScript (if any)
                                            Log.d("WebView", "Result from JS: " + value);
                                        }
                                    });
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        myWebView.loadUrl("file:///android_asset/auth/login.html");
                                    }, 5000);
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((AuthActivity) mContext).runOnUiThread(() -> {
                                myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // Handle the result returned from JavaScript (if any)
                                        Log.d("WebView", "Result from JS: " + value);
                                    }
                                });
                                myWebView.evaluateJavascript("showToast(\"Invalid response from server during registration.\", \"danger\", 4000)", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // Handle the result returned from JavaScript (if any)
                                        Log.d("WebView", "Result from JS: " + value);
                                    }
                                });
                            });
                        }
                    } else {
                            String errorRes = "null";
                            ResponseBody responseBody = response.body();
                            if (responseBody != null) {
                                String responseData = responseBody.string();
                                JSONObject jsonResponse = null;
                                try {
                                    jsonResponse = new JSONObject(responseData);
                                    errorRes = jsonResponse.getString("error");

                                } catch (JSONException e) {
    //                                throw new RuntimeException(e);
                                }

                            }

                        String finalErrorRes = errorRes;
                        ((AuthActivity) mContext).runOnUiThread(() -> {
                            myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    // Handle the result returned from JavaScript (if any)
                                    Log.d("WebView", "Result from JS: " + value);
                                }
                            });
                            String res = "Registration failed." + finalErrorRes;
                            myWebView.evaluateJavascript("showToast(\"Registration failed. "+finalErrorRes+"\", \"danger\", 4000)", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    // Handle the result returned from JavaScript (if any)
                                    Log.d("WebView", "Result from JS: " + value);
                                }
                            });
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    ((AuthActivity) mContext).runOnUiThread(() -> {
                        myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // Handle the result returned from JavaScript (if any)
                                Log.d("WebView", "Result from JS: " + value);
                            }
                        });
                        myWebView.evaluateJavascript("showToast(\"Failed to connect to server during registration.\", \"danger\", 4000)", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // Handle the result returned from JavaScript (if any)
                                Log.d("WebView", "Result from JS: " + value);
                            }
                        });
                    });
                }

            });

            return true; // Indicate that the registration process has started
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
                jsonPayload.put("username_or_email", username);
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
                    .url(BASEURL+"/login/") // Replace with your API URL
                    .post(body)
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try (ResponseBody responseBody = response.body()) {
                            if (responseBody != null) {
                                String responseData = responseBody.string();
                                JSONObject jsonResponse = new JSONObject(responseData);
                                String role = jsonResponse.getString("role"); // Assuming the API returns the user role
                                String access_token = jsonResponse.getString("access");
                                String refresh_token = jsonResponse.getString("refresh");
                                String username = jsonResponse.getString("username");

                                sharedPreferencesUtils.saveRole(role);
                                sharedPreferencesUtils.saveUsername(username);
                                sharedPreferencesUtils.saveAccessToken(access_token);
                                sharedPreferencesUtils.saveRefreshToken(refresh_token);

                                ((AuthActivity) mContext).runOnUiThread(() -> {
                                    myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            // Handle the result returned from JavaScript (if any)
                                            Log.d("WebView", "Result from JS: " + value);
                                        }
                                    });
                                    navigateToAppropriateActivity(role);
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((AuthActivity) mContext).runOnUiThread(() -> {
                                myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // Handle the result returned from JavaScript (if any)
                                        Log.d("WebView", "Result from JS: " + value);
                                    }
                                });
                                myWebView.evaluateJavascript("showToast(\"An error occurred in the application\", \"danger\", 4000)", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // Handle the result returned from JavaScript (if any)
                                        Log.d("WebView", "Result from JS: " + value);
                                    }
                                });
                            });
                        }
                    } else {
                        ((AuthActivity) mContext).runOnUiThread(() -> {
                            myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    // Handle the result returned from JavaScript (if any)
                                    Log.d("WebView", "Result from JS: " + value);
                                }
                            });
                            myWebView.evaluateJavascript("showToast(\"Invalid credentials!!!\", \"danger\", 4000)", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    // Handle the result returned from JavaScript (if any)
                                    Log.d("WebView", "Result from JS: " + value);
                                }
                            });
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    ((AuthActivity) mContext).runOnUiThread(() -> {
                        myWebView.evaluateJavascript("hideLoadingSpinner()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // Handle the result returned from JavaScript (if any)
                                Log.d("WebView", "Result from JS: " + value);
                            }
                        });
                        myWebView.evaluateJavascript("showToast(\"Failed to connect to server!\", \"danger\", 4000)", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                // Handle the result returned from JavaScript (if any)
                                Log.d("WebView", "Result from JS: " + value);
                            }
                        });
                    });
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
        public void navToTutorTutorActivity() {
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