package com.edu.tutorapp.ui.student.dashboard;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.edu.tutorapp.R;
import com.edu.tutorapp.databinding.FragmentStudentDashboardBinding;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StudentDashboardFragment extends Fragment {

    private StudentDashboardViewModel mViewModel;
    private FragmentStudentDashboardBinding binding;
    private static final String SHARED_PREF_NAME = "MyAppPreferences"; // Name of the preference file
    private static final String KEY_USERNAME = "username"; // Key for storing username
    private static final String KEY_ROLE = "role"; // Key for storing role
    private static final String KEY_ACCESS_TOKEN = "access_token"; // Key for storing access token
    private static final String KEY_REFRESH_TOKEN = "refresh_token"; // Key for storing refresh token
    private static final String KEY_TIMEOUT = "timeout"; // Key for storing timeout

    private SharedPreferencesUtils sharedPreferencesUtils;
    private API api;
    private WebView webview;

    public static StudentDashboardFragment newInstance() {
        return new StudentDashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
//        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/student/dashboard.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String accessToken = sharedPreferencesUtils.getAccessToken();

        api = new API();
        api.setAccessToken(accessToken);

//        webview.evaluateJavascript("showToast(\"Login successful!\", \"success\", 4000)", null);
        // Set WebViewClient to handle page load events
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//                // Call the JavaScript function after the page has finished loading
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    // For API 19+ (KitKat and above)
//                    view.evaluateJavascript("showToast(\"Login successful!\", \"success\", 4000)", null);
//                } else {
//                    // For older versions (API < 19)
//                    view.loadUrl("javascript:showToast(\"Login successful!\", \"success\", 4000)");
//                }
//            }
//        });
//        String username = sharedPreferences.getString(KEY_USERNAME, "DefaultUser");
//        Log.e("USERNAME-DASHBOARD", username);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentDashboardViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface{

        @JavascriptInterface
        public void fetchDashboard(){
            getDashboard();
        }
    }

    public void getDashboard(){
        api.get("/dashboard/", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    if (webview != null){
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred','danger',4000)", null);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                JSONObject jsonData = API.parseResponse(response);
                String bookings, requests, earnings, sessions;

                if (jsonData != null) {
                    //
                    requireActivity().runOnUiThread(() -> {
                        if (webview != null){
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("displayDetails("+jsonData.toString()+", 'tutor')", null);
                        }
                    });


                } else {
//                    dashboardDetails = null;
                    requireActivity().runOnUiThread(() -> {
                        if (webview != null){
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('Data null','danger',4000)", null);
                        }
                    });
                }

            }
        });
    }

}