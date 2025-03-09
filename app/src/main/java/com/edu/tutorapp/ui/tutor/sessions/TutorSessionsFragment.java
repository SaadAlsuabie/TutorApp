package com.edu.tutorapp.ui.tutor.sessions;

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
import com.edu.tutorapp.databinding.FragmentTutorSessionsBinding;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TutorSessionsFragment extends Fragment {

    private TutorSessionsViewModel mViewModel;
    private FragmentTutorSessionsBinding binding;
    private WebView webview;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private API api;

    public static TutorSessionsFragment newInstance() {
        return new TutorSessionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorSessionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/tutor/my_classes.html");
        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorSessionsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface{
        @JavascriptInterface
        public void fetchPendingRequests(){
            getAcceptedRequests();
        }
        @JavascriptInterface
        public void sendMessage(String requestId, String message){
            postMessage(requestId, message);
        }

        @JavascriptInterface
        public void fetchMessagesFromServer(String requestId, String studentId){
            getChatFromServer(requestId, studentId);
        }
    }

    public void getAcceptedRequests(){
        String url = "/request-session-listings/?query=accepted";
        api.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->{
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject acceptedRequests = API.parseResponse(response);

                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("displayRequests("+acceptedRequests.toString()+", 'accepted', 'tutor')", null);
                    });

                } else {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                    });
                }

            }
        });
    }

    public void postMessage(String requestID, String message){

        String url = "/send-message/";
        try {
            JSONObject payload = new JSONObject();
            payload.put("session_request_id", requestID);
            payload.put("content", message);

            api.post(url, payload.toString(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        JSONObject res = API.parseResponse(response);
                        requireActivity().runOnUiThread(() ->{
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('Message sent', 'success', 4000)", null);
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->{
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                        });
                    }
                }
            });
        } catch (JSONException e) {
            requireActivity().runOnUiThread(() ->{
                webview.evaluateJavascript("hideLoadingSpinner()", null);
                webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
            });
        }

    }

    public void getChatFromServer(String requestId, String studentId){
        String url = "/get-messages/?query="+requestId;
        Log.e("MS", url);
        api.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->{
                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject messages = API.parseResponse(response);

                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("displayMessagesFromServer("+messages.toString()+", "+requestId+")", null);
                    });

                } else {
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                    });
                }
            }
        });
    }

}