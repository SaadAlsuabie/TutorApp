package com.edu.tutorapp.ui.tutor.requests;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.edu.tutorapp.R;
import com.edu.tutorapp.databinding.FragmentTutorDashboardBinding;
import com.edu.tutorapp.databinding.FragmentTutorRequestsBinding;
import com.edu.tutorapp.ui.tutor.dashboard.TutorDashboardFragment;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TutorRequestsFragment extends Fragment {

    private TutorRequestsViewModel mViewModel;
    private FragmentTutorRequestsBinding binding;
    private WebView webview;
    private API api;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public static TutorRequestsFragment newInstance() {
        return new TutorRequestsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorRequestsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/tutor/requests.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorRequestsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface{
        @JavascriptInterface
        public void fetchPendingRequests(){
            getPendingRequests();
        }

        @JavascriptInterface
        public void acceptRequest(String requestID){
            sendAcceptRequest(requestID);
        }
        @JavascriptInterface
        public void declineRequest(String requestID){
            sendDeclineRequest(requestID);
        }
    }

    public void sendDeclineRequest(String requestID) {
        String url = "/accept-decline-session/"+requestID+"/?action=decline";
        try {
            JSONObject payload = new JSONObject();
            payload.put("decline_reason", "Unknown");
            api.post(url, payload.toString(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred while sending the request', 'danger', 4000)", null);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("fetchPendingRequests()", null);
                    });
                }
            });
        } catch (JSONException e) {
            requireActivity().runOnUiThread(() ->{
                webview.evaluateJavascript("hideLoadingSpinner()", null);
                webview.evaluateJavascript("showToast('An error occurred while sending the request', 'danger', 4000)", null);
            });
        }

    }
    public  void sendAcceptRequest(String requestID){
        String url = "/accept-decline-session/"+requestID+"/?action=accept";
        JSONObject jsonBody = new JSONObject();
        api.post(url, jsonBody.toString(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->{
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('An error occurred while sending the request', 'danger', 4000)", null);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                requireActivity().runOnUiThread(() ->{
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("fetchPendingRequests()", null);
                });
            }
        });
    }
    public void getPendingRequests(){
        api.get("/request-session-listings/?query=pending", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->{
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('Getting sessions failed', 'danger', 4000)", null);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject jsonData = API.parseResponse(response);
                    String data;
                    if (jsonData != null) {
                        data = jsonData.toString();
                        requireActivity().runOnUiThread(() ->{
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("displayRequests("+data+", 'pending', 'tutor')", null);
                        });
                    } else {
//                        data = null;
                        requireActivity().runOnUiThread(() ->{
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('An error occurred.', 'danger', 4000)", null);
                        });
                    }


                } else {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred.', 'danger', 4000)", null);
                    });
                }

            }
        });
    }

}