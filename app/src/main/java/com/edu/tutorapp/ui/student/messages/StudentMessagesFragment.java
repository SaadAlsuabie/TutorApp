package com.edu.tutorapp.ui.student.messages;

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
import com.edu.tutorapp.databinding.FragmentStudentMessagesBinding;

public class StudentMessagesFragment extends Fragment {

    private StudentMessagesViewModel mViewModel;
    private FragmentStudentMessagesBinding binding;

    public static StudentMessagesFragment newInstance() {
        return new StudentMessagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentMessagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/student/messages.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentMessagesViewModel.class);
        // TODO: Use the ViewModel
    }

}