package com.example.translator

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var translator: Translator
    private lateinit var sourceLanguageSpinner: Spinner
    private lateinit var targetLanguageSpinner: Spinner
    private lateinit var translateBtn: Button
    private lateinit var inputEditText: EditText
    private lateinit var outputTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEditText = findViewById(R.id.editInText1)
        translateBtn = findViewById(R.id.translateBtn)
        outputTextView = findViewById(R.id.translatedText)

        // Initialize the Spinners
        sourceLanguageSpinner = findViewById(R.id.sourceLanguageSpinner)
        targetLanguageSpinner = findViewById(R.id.targetLanguageSpinner)

        // Language options
        val languages = arrayOf("English", "Hindi", "Spanish", "French", "German")

        // Set up the spinners with the language options
        val adapter = ArrayAdapter(this, R.layout.spinner_item, languages)
        adapter.setDropDownViewResource(R.layout.spinner_item)

        // Apply the adapter to the spinners
        sourceLanguageSpinner.adapter = adapter
        targetLanguageSpinner.adapter = adapter
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        sourceLanguageSpinner.adapter = adapter
//        targetLanguageSpinner.adapter = adapter

        // Set default languages
        sourceLanguageSpinner.setSelection(languages.indexOf("English"))
        targetLanguageSpinner.setSelection(languages.indexOf("Hindi"))

        translateBtn.setOnClickListener {
            val inputText = inputEditText.text.toString()

            // Get selected languages
            val sourceLanguage = getLanguageCode(sourceLanguageSpinner.selectedItem.toString())
            val targetLanguage = getLanguageCode(targetLanguageSpinner.selectedItem.toString())

            translateText(inputText, sourceLanguage, targetLanguage)
        }
    }

    private fun getLanguageCode(language: String): String {
        return when (language) {
            "English" -> TranslateLanguage.ENGLISH
            "Hindi" -> TranslateLanguage.HINDI
            "Spanish" -> TranslateLanguage.SPANISH
            "French" -> TranslateLanguage.FRENCH
            "German" -> TranslateLanguage.GERMAN
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun translateText(inputText: String, sourceLang: String, targetLang: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()

        translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model successfully downloaded, proceed with translation
                translator.translate(inputText)
                    .addOnSuccessListener { translatedText ->
                        outputTextView.text = translatedText
                    }
                    .addOnFailureListener { exception ->
                        outputTextView.text = "Translation failed!"
                        exception.printStackTrace() // Log the exception
                    }
            }
            .addOnFailureListener { exception ->
                // Model download failed, handle it
                outputTextView.text = "Model Download Failed!"
                exception.printStackTrace() // Log the exception
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        translator.close()
    }
}

//Normal

//class MainActivity : AppCompatActivity() {
//    private lateinit var translator: Translator
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val inputEditText = findViewById<EditText>(R.id.editInText1)
//        val translateBtn = findViewById<Button>(R.id.translateBtn)
//        val output = findViewById<TextView>(R.id.translatedText)
//
//        // Create an English-Hindi translator
//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(TranslateLanguage.ENGLISH)
//            .setTargetLanguage(TranslateLanguage.HINDI)
//            .build()
//
//        translator = Translation.getClient(options)
//
//        val conditions = DownloadConditions.Builder()
//            .requireWifi()
//            .build()
//
//        translator.downloadModelIfNeeded(conditions)
//            .addOnSuccessListener {
//                translateBtn.setOnClickListener {
//                    val inputText = inputEditText.text.toString()
//                    translateText(inputText, output)
//                }
//            }
//            .addOnFailureListener { exception ->
//                output.text = "Model Download Failed!"
//            }
//    }
//
//    private fun translateText(inputText: String, outTextView: TextView) {
//        translator.translate(inputText)
//            .addOnSuccessListener { translatedText ->
//                outTextView.text = translatedText
//            }
//            .addOnFailureListener { exception ->
//                outTextView.text = "Translation failed!"
//            }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        translator.close()
//    }
//}


