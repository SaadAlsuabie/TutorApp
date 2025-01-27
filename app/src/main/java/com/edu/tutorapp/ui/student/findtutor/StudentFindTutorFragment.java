package com.edu.tutorapp.ui.student.findtutor;

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
import com.edu.tutorapp.databinding.FragmentStudentFindTutorBinding;

public class StudentFindTutorFragment extends Fragment {

    private StudentFindTutorViewModel mViewModel;
    private FragmentStudentFindTutorBinding binding;

    public static StudentFindTutorFragment newInstance() {
        return new StudentFindTutorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentFindTutorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/student/find_a_tutor.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentFindTutorViewModel.class);
        // TODO: Use the ViewModel
    }

}