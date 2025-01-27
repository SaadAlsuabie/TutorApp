package com.edu.tutorapp.ui.student.courses;

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
import com.edu.tutorapp.databinding.FragmentStudentDashboardBinding;
import com.edu.tutorapp.databinding.FragmentStudentRecordedLecturesBinding;

public class StudentRecordedLecturesFragment extends Fragment {

    private StudentRecordedLecturesViewModel mViewModel;
    private FragmentStudentRecordedLecturesBinding binding;

    public static StudentRecordedLecturesFragment newInstance() {
        return new StudentRecordedLecturesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentRecordedLecturesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/student/recorded_lectures.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentRecordedLecturesViewModel.class);
        // TODO: Use the ViewModel
    }

}