package com.pushstartuphub.smartwrite.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar; // Import ProgressBar for loading indication
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.pushstartuphub.smartwrite.R;
import com.pushstartuphub.smartwrite.databinding.FragmentTextGenerateBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TextGenerateFragment extends Fragment {
    FragmentTextGenerateBinding binding;
    private ProgressBar loadingIndicator; // Progress bar for loading indication

    private String[] feelings = {"Happy", "Sad", "Excited", "Angry", "Neutral"};
    private String[] textTypes = {"Letter", "Application", "Email", "Report"};
    String[] languages = {"English","Hindi" , "Hienglish" ,"Spanish", "French", "German"};

    public TextGenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTextGenerateBinding.inflate(inflater, container, false);
        loadingIndicator = binding.loadingIndicator; // Assuming you have a ProgressBar in your layout

        setupSpinners();

        binding.fab.setOnClickListener(view -> {
            validateFields();
        });

        return binding.getRoot();
    }

    private void setupSpinners() {
        ArrayAdapter<String> feelingsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, feelings);
        feelingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.feelingsSpinner.setAdapter(feelingsAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, textTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typeSpinner.setAdapter(typeAdapter);


        ArrayAdapter<String> LanguageAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, languages);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.LanguageSpinner.setAdapter(LanguageAdapter);


    }

    private void validateFields() {
        String prompt = binding.promptInput.getText().toString().trim();
        String selectedFeeling = binding.feelingsSpinner.getSelectedItem() != null ? binding.feelingsSpinner.getSelectedItem().toString() : null;
        String selectedType = binding.typeSpinner.getSelectedItem() != null ? binding.typeSpinner.getSelectedItem().toString() : null;
        String selectedLanguage = binding.LanguageSpinner.getSelectedItem() != null ? binding.typeSpinner.getSelectedItem().toString() : null;

        if (prompt.isEmpty()) {
            showSnackbar("Please enter a prompt.");
            return;
        }
        if (selectedFeeling == null) {
            showSnackbar("Please select a feeling.");
            return;
        }
        if (selectedType == null) {
            showSnackbar("Please select a text type.");
            return;
        }
        if (selectedLanguage == null) {
            showSnackbar("Please select a Language.");
            return;
        }

        GenerateText(prompt, selectedFeeling, selectedType,selectedLanguage);
    }

//    private void GenerateText(String originalText, String selectedFeeling, String selectedType) {
//        String mainPrompt = "Please generate a " + selectedType + " in HTML format that expresses a " + selectedFeeling +
//                " based on the following text: " + originalText +
//                ". Highlight the important parts and provide only the generated text, without any additional explanation.";
//
//        loadingIndicator.setVisibility(View.VISIBLE); // Show loading indicator
//
//        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", "YOUR_API_KEY");
//        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
//
//        Content content = new Content.Builder().addText(mainPrompt).build();
//        Executor executor = Executors.newSingleThreadExecutor();
//
//        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
//        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
//            @Override
//            public void onSuccess(GenerateContentResponse result) {
//                String resultText = result.getText();
//                requireActivity().runOnUiThread(() -> {
//                    loadingIndicator.setVisibility(View.GONE); // Hide loading indicator
//                    navigateToNextFragment(originalText, resultText);
//                });
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                loadingIndicator.setVisibility(View.GONE); // Hide loading indicator
//                showSnackbar("Error generating text. Please try again.");
//                t.printStackTrace();
//            }
//        }, executor);
//    }



    private void GenerateText(String originalText, String selectedFeeling, String selectedType,String selectedLanguage) {
//        String mainPrompt = "Please generate a " + selectedType + " in HTML format that expresses a " + selectedFeeling +
//                " based on the following text: " + originalText +
//                ". and Highlight the important parts and provide only the generated text In a proper document format, without any additional explanation.";

        // Create the main prompt for Gemini
        String mainPrompt = String.format("Generate a properly formatted HTML document as a %s in %s that expresses a %s tone based on the following prompt: %s. Include headings, paragraphs, and any other necessary HTML elements, and provide only the generated HTML without additional explanations.",
                selectedType, selectedLanguage, selectedFeeling, originalText);

//        String mainPrompt = String.format("Generate a %s in %s that expresses a %s tone based on the following prompt: %s",
//                selectedFormat, selectedLanguage, selectedTone, userPrompt);



        // Show loading indicator on the main thread
        requireActivity().runOnUiThread(() -> loadingIndicator.setVisibility(View.VISIBLE));

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
                    loadingIndicator.setVisibility(View.GONE); // Hide loading indicator
//                    String cleanText = cleanAndFormatText(resultText);

                    navigateToNextFragment(originalText, resultText,selectedFeeling,selectedType,selectedLanguage);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    loadingIndicator.setVisibility(View.GONE); // Hide loading indicator
                    showSnackbar("Error generating text. Please try again.");
                });
                t.printStackTrace();
            }
        }, executor);
    }


    private String cleanAndFormatText(String rawText) {
        // Remove HTML tags and trim the text
        String cleanedText = rawText.replaceAll("<[^>]*>", "").trim();

        // Handle special characters and any additional formatting you need
        cleanedText = cleanedText.replaceAll("[^\\p{L}\\p{N}\\s.,;:!?\"'()\\[\\]]", ""); // Retain only letters, numbers, and common punctuation
        cleanedText = cleanedText.replaceAll("\\s+", " "); // Replace multiple spaces with a single space

        // Add additional formatting based on the selected type, if necessary
        // For example, if the format is a letter, you might want to add a header, etc.

        return cleanedText;
    }




    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void navigateToNextFragment(String prompt, String resultText ,String selectedFeeling ,String selectedType,String languages) {
        Bundle bundle = new Bundle();
        bundle.putString("prompt", prompt);
        bundle.putString("result", resultText);
        bundle.putString("selectedFeeling", selectedFeeling);
        bundle.putString("selectedType", selectedType);
        bundle.putString("languages", languages);
        String cleanText = resultText.replaceAll("<[^>]*>", ""); // Remove HTML tags
        int wordCount = cleanText.trim().isEmpty() ? 0 : cleanText.trim().split("\\s+").length;
        bundle.putInt("Length", wordCount);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_text_generate_Fragment_to_resultTextGenerationFragment, bundle);
    }

}
