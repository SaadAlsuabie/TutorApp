package com.edu.tutorapp.ui.student.findtutor;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.edu.tutorapp.AuthActivity;
import com.edu.tutorapp.MainActivity;
import com.edu.tutorapp.R;
import com.edu.tutorapp.TutorMainActivity;
import com.edu.tutorapp.databinding.FragmentStudentFindTutorBinding;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StudentFindTutorFragment extends Fragment {

    private StudentFindTutorViewModel mViewModel;
    private FragmentStudentFindTutorBinding binding;
    private API api;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private WebView webview;

    public static StudentFindTutorFragment newInstance() {
        return new StudentFindTutorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentFindTutorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);

        webview.getSettings().setJavaScriptEnabled(true);

        WebAppInterface webAppInterface = new WebAppInterface(requireContext());
        webview.addJavascriptInterface(webAppInterface, "AndroidInterface");

        webview.loadUrl("file:///android_asset/student/find_a_tutor.html");



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentFindTutorViewModel.class);
        // TODO: Use the ViewModel
    }



    // JavaScript Interface class
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void getTutors(String tutorname, String faculty, String major, String course, String when){
            String url;
            if ("onload".equals(when)){
                url = "/search-tutors/?all=true";
            } else {
                url = "/search-tutors/";
                if (!"".equals(tutorname)){
                    url += "?tutorname="+tutorname;
                } else {
                    url += "?tutorname=";
                }
                if (!"".equals(faculty)){
                    url += "&faculty="+faculty;
                } else {
                    url += "&faculty=";
                }
                if (!"".equals(major)){
                    url += "&major="+major;
                } else {
                    url += "&major=";
                }
                if (!"".equals(course)){
                    url += "&course="+course;
                } else {
                    url += "&course=";
                }
                Log.e("name", tutorname);
            }


            api.get(url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    requireActivity().runOnUiThread(() -> {
                        if (webview != null) {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('No tutors found.', 'danger', 4000)", null);
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonResponse = API.parseResponse(response);

                            JSONArray tutorList = jsonResponse.getJSONArray("data");
                            JSONArray sessionsList = jsonResponse.getJSONArray("sessions");


                            String tutorListString = tutorList.toString();
                            String sessionsJson = sessionsList.toString();


                            String script = "displayTutors(" + tutorListString + ", " + tutorListString + ")";

                            requireActivity().runOnUiThread(() -> {
                                webview.evaluateJavascript(script, null);
                            });
                            requireActivity().runOnUiThread(() -> {
                                webview.evaluateJavascript("hideLoadingSpinner()", null);
                                webview.evaluateJavascript("showToast('Tutors successfully fetched.', 'success', 4000)", null);
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            requireActivity().runOnUiThread(() -> {
                                webview.evaluateJavascript("hideLoadingSpinner()", null);
                                webview.evaluateJavascript("showToast('Invalid response from server.', 'danger', 4000)", null);
                            });
                        }
                    } else {
                        requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('No tutors found.', 'danger', 4000)", null);
                        });
                    }
                }
            });
        }

        @JavascriptInterface
        public void requestSession(String userId, String sessionType, String requestTime, String message){
            String url = "/request-session/";

            try {
                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("tutor", userId);
                jsonPayload.put("session_type", sessionType);
                jsonPayload.put("requested_time", requestTime);
                jsonPayload.put("message", message);

                api.post(url, jsonPayload.toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('Failed to connect to server during request. Please try again', 'danger', 4000)", null);
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonResponse = API.parseResponse(response);
//                                String message = jsonResponse.optString("message", "request successful");
                                requireActivity().runOnUiThread(() -> {
                                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                                    webview.evaluateJavascript("showToast(\"Session request sent\", 'success', 4000)", null);
                                });
                            } catch (Exception e) {
                                requireActivity().runOnUiThread(() -> {
                                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                                    webview.evaluateJavascript("showToast('Invalid response from server during request.', 'danger', 4000)", null);
                                });
                            }
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                webview.evaluateJavascript("hideLoadingSpinner()", null);
                                webview.evaluateJavascript("showToast('An error occurred.', 'danger', 4000)", null);
                            });
                        }
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('An error occurred. Try again.', 'danger', 4000)", null);
                });
            }
        }
        @JavascriptInterface
        public void navigateToActivity(String activityName) {
            Intent intent = null;
            switch (activityName) {
                case "MainActivity":
                    intent = new Intent(mContext, MainActivity.class);
                    break;
                case "TutorMainActivity":
                    intent = new Intent(mContext, TutorMainActivity.class);
                    break;
                // Add more cases as needed
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
    }

}