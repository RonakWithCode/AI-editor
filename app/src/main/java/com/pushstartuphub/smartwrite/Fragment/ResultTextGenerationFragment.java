package com.pushstartuphub.smartwrite.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import com.pushstartuphub.smartwrite.databinding.FragmentResultTextGenerationBinding;

public class ResultTextGenerationFragment extends Fragment {
    private FragmentResultTextGenerationBinding binding;

    // Define arrays for feelings and formats
    private String[] feelings = {"Happy", "Sad", "Excited", "Angry", "Neutral"};
    private String[] formats = {"Letter", "Application", "Email", "Report"};

    String prompt, result, selectedFeeling, selectedType;
    int length;

    public ResultTextGenerationFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultTextGenerationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Enable JavaScript in the WebView
        binding.richTextEditor.getSettings().setJavaScriptEnabled(true);
        binding.richTextEditor.setWebViewClient(new WebViewClient());

        if (getArguments() != null) {
            result = getArguments().getString("result");
            prompt = getArguments().getString("prompt");
            selectedFeeling = getArguments().getString("selectedFeeling");
            selectedType = getArguments().getString("selectedType");
            length = getArguments().getInt("Length");
            binding.feelingsInput.setText(selectedFeeling);
            binding.formatInput.setText(selectedType);
            binding.WordsCount.setText(String.valueOf(length));
            binding.radioWords.setChecked(true); // Select the 'Words' option

            displayFormattedResult(result);
        }

        setupAutoCompleteTextViews();
        setupButtons();

        return view;
    }

    private void setupAutoCompleteTextViews() {
        ArrayAdapter<String> feelingsAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, feelings);
        binding.feelingsInput.setAdapter(feelingsAdapter);
        binding.feelingsInput.setThreshold(1); // Show suggestions after 1 character

        binding.feelingsInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.feelingsInput.showDropDown(); // Show suggestions on focus
            }
        });

        ArrayAdapter<String> formatsAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, formats);
        binding.formatInput.setAdapter(formatsAdapter);
        binding.formatInput.setThreshold(1); // Show suggestions after 1 character

        binding.formatInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.formatInput.showDropDown(); // Show suggestions on focus
            }
        });
    }

    private void setupButtons() {
        // Uncomment and implement button logic if needed
        // binding.btnShort.setOnClickListener(v -> {
        //     Log.d("User Input", "Short: " + binding.userInput.getText().toString().trim());
        // });

        // binding.btnLong.setOnClickListener(v -> {
        //     Log.d("User Input", "Long: " + binding.userInput.getText().toString().trim());
        // });
    }

    private void displayFormattedResult(String result) {
        Log.i("ResultTextGenerationFragment", "displayFormattedResult: " + result);

        // Load the HTML content
        String mimeType = "text/html";
        String encoding = "utf-8";
        String baseUrl = "file:///android_asset/";

        binding.richTextEditor.loadDataWithBaseURL(baseUrl, result, mimeType, encoding, null);
    }
}
