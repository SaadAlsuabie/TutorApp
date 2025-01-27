package com.edu.tutorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {
    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myWebView = findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        WebAppInterface androidInterface = new WebAppInterface(this);
         myWebView.addJavascriptInterface(androidInterface, "AndroidInterface");

        myWebView.loadUrl("file:///android_asset/splash.html");
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public boolean registerTutor() {

            return true; // or false if invalid
        }

        @JavascriptInterface
        public void navToMainActivity() {
            Intent mainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(mainActivity);
        }
        @JavascriptInterface
        public void navToTutorMainActivity() {
            Intent tutorActivity = new Intent(SplashScreenActivity.this, TutorMainActivity.class);
            startActivity(tutorActivity);
        }
        @JavascriptInterface
        public void nav(String page) {
            try{
                ((SplashScreenActivity) mContext).runOnUiThread(() -> {
                    switch (page) {
                        case "login.html":
                            Intent loginIntent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                            startActivity(loginIntent);
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
}