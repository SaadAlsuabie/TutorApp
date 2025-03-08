package com.edu.tutorapp.ui.tutor.recordedmaterials;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.edu.tutorapp.MainActivity;
import com.edu.tutorapp.R;
import com.edu.tutorapp.databinding.FragmentStudentRecordedLecturesBinding;
import com.edu.tutorapp.databinding.FragmentTutorDashboardBinding;
import com.edu.tutorapp.databinding.FragmentTutorRecordedMaterialsBinding;
import com.edu.tutorapp.ui.student.courses.StudentRecordedLecturesViewModel;
import com.edu.tutorapp.utils.API;
import com.edu.tutorapp.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Okio;

public class TutorRecordedMaterialsFragment extends Fragment {

    private TutorRecordedMaterialsViewModel mViewModel;
    private FragmentTutorRecordedMaterialsBinding binding;
    private ActivityResultLauncher<Intent> mGetContent;
    private ValueCallback<Uri[]> mFilePathCallback;
    private WebView webview;
    private API api;
    private SharedPreferencesUtils sharedPreferencesUtils;

    public static TutorRecordedMaterialsFragment newInstance() {
        return new TutorRecordedMaterialsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorRecordedMaterialsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webview = binding.UIwebView;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(), "Android");
        webview.loadUrl("file:///android_asset/tutor/recorded_materials.html");

        sharedPreferencesUtils = new SharedPreferencesUtils(requireContext());
        String access_token = sharedPreferencesUtils.getAccessToken();
        api = new API();
        api.setAccessToken(access_token);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                mGetContent.launch(Intent.createChooser(intent, "Select Video"));
                return true;
            }
        });

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the ActivityResultLauncher
        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri uri = result.getData().getData();
                            if (mFilePathCallback != null) {
                                mFilePathCallback.onReceiveValue(new Uri[]{uri});
                                mFilePathCallback = null;
                            }
                        }
                    } else {
                        if (mFilePathCallback != null) {
                            mFilePathCallback.onReceiveValue(null);
                            mFilePathCallback = null;
                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TutorRecordedMaterialsViewModel.class);
        // TODO: Use the ViewModel
    }

    public class WebAppInterface {

        @JavascriptInterface
        public void uploadFile(String fileData, String fileName, String title, String course, String price, String description) {
            sendFileToServer(fileData, fileName, title, course, price, description);
        }

        @JavascriptInterface
        public void fetchUploads() {
            fetchUploadsFromServer();
        }

        @JavascriptInterface
        public void fetchVideo(int materialId) {
            fetchVideoFromServer(materialId);
        }
    }

    private void fetchUploadsFromServer(){
        api.get("/recording/", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    if (webview != null) {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('No recordings found.', 'danger', 4000)", null);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = API.parseResponse(response);

                        JSONObject data = jsonResponse.getJSONObject("data");
                        JSONArray uploads = data.getJSONArray("uploads");

                        String uploadsString = uploads.toString();

                        String script = "displayUploads(" + uploadsString + ")";

                        requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript(script, null);
                        });
                        requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('recordings successfully fetched.', 'success', 4000)", null);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                            webview.evaluateJavascript("showToast('Invalid response from server.', 'danger', 4000)", null);
                        });
                    }
                } else {
                    requireActivity().runOnUiThread(() -> {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("showToast('No recordings found.', 'danger', 4000)", null);
                    });
                }
            }
        });
    }
    private void sendFileToServer(String fileData, String fileName, String title, String course, String price, String description) {

        byte[] fileBytes = android.util.Base64.decode(fileData, android.util.Base64.DEFAULT);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(fileBytes, MediaType.parse("application/octet-stream")))
                .addFormDataPart("title", title)
                .addFormDataPart("course", course)
                .addFormDataPart("price", price)
                .addFormDataPart("description", description)
                .build();

        api.post("/recording/?query=upload", requestBody, true, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    requireActivity().runOnUiThread(() -> {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                            Toast.makeText(requireActivity(), "Upload successful!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                            Toast.makeText(requireActivity(), "Upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void fetchVideoFromServer(int materialId) {

        String url = "/video/"+materialId+"/";
        api.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                            webview.evaluateJavascript("hideLoadingSpinner()", null);
                    Toast.makeText(requireActivity(), "Failed to fetch video: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Save the video file locally
                    File videoFile = new File(requireActivity().getCacheDir(), "video.mp4");
                    try (var sink = Okio.buffer(Okio.sink(videoFile))) {
                        sink.writeAll(response.body().source());
                    }

                    // Send the video file path to the HTML template
                    // requireActivity().runOnUiThread(() -> webview.loadUrl("javascript:playVideo('" + videoFile.getAbsolutePath() + "')"));
                    requireActivity().runOnUiThread(() -> {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        webview.evaluateJavascript("playVideo('"+ videoFile.getAbsolutePath() +"')", null);
                    });

                } else {
                    requireActivity().runOnUiThread(() -> {
                        webview.evaluateJavascript("hideLoadingSpinner()", null);
                        Toast.makeText(requireActivity(), "Failed to fetch video: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
//                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), "Failed to fetch video: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}