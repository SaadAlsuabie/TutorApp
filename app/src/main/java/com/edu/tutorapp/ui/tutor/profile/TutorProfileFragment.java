package com.edu.tutorapp.ui.tutor.profile;

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
import com.edu.tutorapp.databinding.FragmentTutorProfileBinding;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TutorProfileFragment extends Fragment {

    private TutorProfileViewModel mViewModel;
    private FragmentTutorProfileBinding binding;
    private WebView webview;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private JSONObject profile;

    public static TutorProfileFragment newInstance() {
        return new TutorProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/tutor/profile.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        try {
            profile = sharedPreferencesUtils.getProfile();
        } catch (JSONException e) {
            profile = null;
        }


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface{
        @JavascriptInterface
        public void fetchProfile(){
            if (!profile.equals(null)){
                requireActivity().runOnUiThread(() ->{
//                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("updateProfile("+profile.toString()+")", null);
                });
            } else {
                requireActivity().runOnUiThread(() ->{
//                    webview.evaluateJavascript("hideLoadingSpinner()", null);
                    webview.evaluateJavascript("showToast('An error occurred', 'danger', 4000)", null);
                });
            }
        }
    }

}