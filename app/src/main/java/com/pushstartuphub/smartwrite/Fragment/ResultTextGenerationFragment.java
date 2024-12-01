package com.pushstartuphub.smartwrite.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.pushstartuphub.smartwrite.R;
import com.pushstartuphub.smartwrite.databinding.FragmentResultTextGenerationBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ResultTextGenerationFragment extends Fragment {
    private FragmentResultTextGenerationBinding binding;
    private AlertDialog progressDialog;

    // Define arrays for feelings and formats
    private String[] feelings = {"Happy", "Sad", "Excited", "Angry", "Neutral"};
    private String[] formats = {"Letter", "Application", "Email", "Report"};
    String[] languagesList = {"English","Hindi" , "Hienglish" ,"Spanish", "French", "German"};

    String prompt, result, selectedFeeling, selectedType,languages;
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

        // Enable scrolling
        binding.richTextEditor.setVerticalScrollBarEnabled(true);
        binding.richTextEditor.setHorizontalScrollBarEnabled(true);
        if (getArguments() != null) {
            result = getArguments().getString("result");
            prompt = getArguments().getString("prompt");
            selectedFeeling = getArguments().getString("selectedFeeling");
            selectedType = getArguments().getString("selectedType");
            languages = getArguments().getString("languages");
            length = getArguments().getInt("Length");

            displayFormattedResult(result, selectedFeeling, selectedType,languages ,length);
        }

        setupAutoCompleteTextViews();

        binding.change.setOnClickListener(v -> {
            String newPrompt = binding.userInput.getText().toString().trim();
            String newFeeling = binding.feelingsInput.getText().toString().trim();
            String newType = binding.formatInput.getText().toString().trim();
            String newLength = binding.WordsCount.getText().toString().trim();
            String selectedLanguage = binding.languagesInput.getText().toString().trim();

//            // Create the main prompt for Gemini
//            String mainPrompt = "Based on the previous results:\n" + result + "\n\n" +
//                    "Please generate a " + newType + " in " + selectedLanguage +
//                    " that expresses a " + newFeeling + " based on the following new prompt: " +
//                    newPrompt + ". The output should be formatted according to the specified length: " + newLength +
//                    ". Provide only the generated text in proper document format, without any additional explanation.";

            String mainPrompt = String.format("Based on the previous results:\n%s\n\n" +
                            "Please generate a properly formatted HTML page that expresses a %s " +
                            "based on the following new prompt: %s. " +
                            "The output should be in %s and formatted according to the specified length of %s words, " +
                            "including headings, paragraphs, lists, and any other appropriate formatting. " +
                            "Provide only the generated HTML without any additional explanation.",
                    result, newFeeling, newPrompt, selectedLanguage, newLength);

            // Call the GenerateText method
            GenerateText(mainPrompt, newFeeling, newType, selectedLanguage);
        });


        return view;
    }

    private void GenerateText(String mainPrompt, String selectedFeeling, String selectedType,String languages) {
        // Show progress dialog
        showProgressDialog();

        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", "AIzaSyBhn6ci7VGIn5K9z0F3WWNX_7ElabKbNfM");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder().addText(mainPrompt).build();
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                requireActivity().runOnUiThread(() -> {
                    dismissProgressDialog(); // Dismiss progress dialog
//                    String cleanText = resultText.replaceAll("<[^>]*>", ""); // Remove HTML tags

                    int wordCount = resultText.trim().isEmpty() ? 0 : resultText.trim().split("\\s+").length;
                    displayFormattedResult(resultText, selectedFeeling, selectedType,languages ,wordCount); // Display the new result in the WebView
                });
            }

            @Override
            public void onFailure(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    dismissProgressDialog(); // Dismiss progress dialog
                    // Optionally show a Snackbar or Toast here to inform the user of the error
                });
                t.printStackTrace();
            }
        }, executor);
    }
//
//    private String cleanAndFormatText(String rawText) {
//        // Remove HTML tags and trim the text
//        String cleanedText = rawText.replaceAll("<[^>]*>", "").trim();
//
//        // Handle special characters and any additional formatting you need
//        cleanedText = cleanedText.replaceAll("[^\\p{L}\\p{N}\\s.,;:!?\"'()\\[\\]]", ""); // Retain only letters, numbers, and common punctuation
//        cleanedText = cleanedText.replaceAll("\\s+", " "); // Replace multiple spaces with a single space
//
//        // Add additional formatting based on the selected type, if necessary
//        // For example, if the format is a letter, you might want to add a header, etc.
//
//        return cleanedText;
//    }

    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(R.layout.progress_dialog); // Create a layout for your dialog
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setupAutoCompleteTextViews() {
        // Feelings Adapter
        ArrayAdapter<String> feelingsAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, feelings);
        binding.feelingsInput.setAdapter(feelingsAdapter);
        binding.feelingsInput.setThreshold(1); // Show suggestions after 1 character

        // Format Adapter
        ArrayAdapter<String> formatsAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, formats);
        binding.formatInput.setAdapter(formatsAdapter);
        binding.formatInput.setThreshold(1); // Show suggestions after 1 character

        // Language Adapter
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, languagesList);
        binding.languagesInput.setAdapter(languageAdapter);
        binding.languagesInput.setThreshold(1); // Show suggestions after 1 character

        // Show dropdown on focus
        binding.languagesInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.languagesInput.showDropDown(); // Show suggestions on focus
            }
        });
    }


    private void displayFormattedResult(String result, String selectedFeeling, String selectedType,String languages ,int length) {
        Log.i("ResultTextGenerationFragment", "displayFormattedResult: " + result);

        // Load the HTML content
        String mimeType = "text/html";
        String encoding = "utf-8";
        String baseUrl = "file:///android_asset/";

        this.result = result;

        binding.richTextEditor.loadDataWithBaseURL(baseUrl, result, mimeType, encoding, null);

        binding.feelingsInput.setText(selectedFeeling);
        binding.formatInput.setText(selectedType);
        binding.WordsCount.setText(String.valueOf(length));
        binding.radioWords.setChecked(true); // Select the 'Words' option
    }
}
