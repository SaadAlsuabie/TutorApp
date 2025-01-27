package com.edu.tutorapp.ui.tutor.sessions;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.edu.tutorapp.R;
import com.edu.tutorapp.databinding.FragmentTutorDashboardBinding;
import com.edu.tutorapp.databinding.FragmentTutorSessionsBinding;

public class TutorSessionsFragment extends Fragment {

    private TutorSessionsViewModel mViewModel;
    private FragmentTutorSessionsBinding binding;

    public static TutorSessionsFragment newInstance() {
        return new TutorSessionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorSessionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/tutor/my_classes.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorSessionsViewModel.class);
        // TODO: Use the ViewModel
    }

}