package com.edu.tutorapp.ui.student.sessions;

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
import com.edu.tutorapp.databinding.FragmentStudentSessionsBinding;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StudentSessionsFragment extends Fragment {

    private StudentSessionsViewModel mViewModel;
    private FragmentStudentSessionsBinding binding;
    private API api;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private WebView webview;

    public static StudentSessionsFragment newInstance() {
        return new StudentSessionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentSessionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/student/my_sessions.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String accessToken = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(accessToken);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentSessionsViewModel.class);
        // TODO: Use the ViewModel
    }
    private class WebAppInterface{
        @JavascriptInterface
        public void fetchConversations(){
            getConversations();
        }
        @JavascriptInterface
        public void fetchSessions(){
            getSessions();
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

   public void getSessions(){
        String url = "/request-session-listings/?query=all";
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
                    JSONObject sessions = API.parseResponse(response);
                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("displayRequests("+sessions.toString()+", 'accepted', 'student')", null);
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
    public void getConversations(){
        String url = "/get-messages/?query=all";

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
                    JSONObject conversations = API.parseResponse(response);

                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("displayConversations("+conversations+")", null);
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