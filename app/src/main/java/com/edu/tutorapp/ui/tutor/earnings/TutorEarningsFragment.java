package com.edu.tutorapp.ui.tutor.earnings;

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
import com.edu.tutorapp.databinding.FragmentTutorEarningsBinding;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TutorEarningsFragment extends Fragment {

    private TutorEarningsViewModel mViewModel;
    private FragmentTutorEarningsBinding binding;
    private WebView webview;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private API api;

    public static TutorEarningsFragment newInstance() {
        return new TutorEarningsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorEarningsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/tutor/earnings_and_transactions.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorEarningsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface{
        @JavascriptInterface
        public void fetchTransactions(){
            getTransactions();
        }
    }

    public void getTransactions(){
        String url = "/get-transactions/";

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
                    JSONObject transactions = API.parseResponse(response);

                    requireActivity().runOnUiThread(() ->{
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("displayTransactions("+transactions.toString()+")", null);
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