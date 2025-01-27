package com.edu.tutorapp.ui.student.paymentshistory;

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
import com.edu.tutorapp.databinding.FragmentStudentPaymentsHistoryBinding;

public class StudentPaymentsHistoryFragment extends Fragment {

    private StudentPaymentsHistoryViewModel mViewModel;
    private FragmentStudentPaymentsHistoryBinding binding;

    public static StudentPaymentsHistoryFragment newInstance() {
        return new StudentPaymentsHistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentPaymentsHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        WebView webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/student/payments_and_history.html");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentPaymentsHistoryViewModel.class);
        // TODO: Use the ViewModel
    }

}