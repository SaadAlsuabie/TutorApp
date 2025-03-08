package com.edu.tutorapp.ui.tutor.dashboard;

import androidx.lifecycle.ViewModelProvider;

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

import com.edu.tutorapp.R;
import com.edu.tutorapp.databinding.FragmentTutorDashboardBinding;
import com.edu.tutorapp.ui.tutor.recordedmaterials.TutorRecordedMaterialsFragment;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TutorDashboardFragment extends Fragment {

    private TutorDashboardViewModel mViewModel;
    private FragmentTutorDashboardBinding binding;

    private WebView webview;
    private API api;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public static TutorDashboardFragment newInstance() {
        return new TutorDashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/tutor/dashboard.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorDashboardViewModel.class);
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
                            webview.evaluateJavascript("displayDashboard("+jsonData.toString()+", 'tutor')", null);
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