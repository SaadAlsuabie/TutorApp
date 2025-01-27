package com.edu.tutorapp.ui.tutor.recordedmaterials;

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
import com.edu.tutorapp.databinding.FragmentTutorRecordedMaterialsBinding;

public class TutorRecordedMaterialsFragment extends Fragment {

    private TutorRecordedMaterialsViewModel mViewModel;
    private FragmentTutorRecordedMaterialsBinding binding;

    public static TutorRecordedMaterialsFragment newInstance() {
        return new TutorRecordedMaterialsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorRecordedMaterialsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/tutor/recorded_materials.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorRecordedMaterialsViewModel.class);
        // TODO: Use the ViewModel
    }

}