package com.blablabla.passwordswithcer;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int VERY_EASY_DIFFICULT = 0;
    private static final int EASY_DIFFICULT = 1;
    private static final int MEDIUM_DIFFICULT = 2;
    private static final int STRONG_DIFFICULT = 3;
    private static final int VERY_STRONG_DIFFICULT = 4;

    private EditText sourceEditText;
    private TextView resultTextView;
    private PasswordsConverter passwordsConverter;
    private PasswordGenerator passwordGenerator;
    private View resultCopyButton;
    private CompoundButton capsCheckBox;
    private CompoundButton digitCheckBox;
    private CompoundButton symbolCheckBox;
    private View generateButton;
    private TextView passwordDifficult;
    private View passwordDifficultIndicator;
    private SeekBar passwordLengthSeekBar;
    private TextView passwordLengthText;
    private View generatedPasswordCopyButton;
    private TextView generatedPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        String[] russians = getResources().getStringArray(R.array.russians);
        String[] latin = getResources().getStringArray(R.array.latin);

        passwordsConverter = new PasswordsConverter(russians, latin);
        passwordGenerator = new PasswordGenerator();

        sourceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String password = passwordsConverter.convertRussianToLatin(charSequence);
                int difficult = PasswordGenerator.getPasswordDifficult(charSequence.toString());
                setDifficultIndicator(difficult);
                resultTextView.setText(password);
                resultCopyButton.setEnabled(charSequence.length() > 0);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        resultCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyResultToClipboard(resultTextView);
            }
        });

        generatedPasswordCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyResultToClipboard(generatedPasswordText);
            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
                generatedPasswordCopyButton.setEnabled(true);

                getSymbolCountText(generatedPasswordText.getText().toString());

                passwordLengthText.setText(getSymbolCountText(generatedPasswordText.getText().toString()));
            }
        });
    }

    private void initView() {
        sourceEditText = findViewById(R.id.source_field);
        resultTextView = findViewById(R.id.result_text_view);
        resultCopyButton = findViewById(R.id.result_copy_button);
        resultCopyButton.setEnabled(false);

        capsCheckBox = findViewById(R.id.caps_check_box);
        digitCheckBox = findViewById(R.id.digits_check_box);
        symbolCheckBox = findViewById(R.id.symbol_check_box);

        generateButton = findViewById(R.id.generate_button);
        passwordDifficult = findViewById(R.id.password_difficult_text_view);
        passwordDifficultIndicator = findViewById(R.id.password_difficult_indicator);
        passwordLengthSeekBar = findViewById(R.id.password_length_seek_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            passwordLengthSeekBar.setMin(1);
        }
        passwordLengthText = findViewById(R.id.password_length_text_view);
        generatedPasswordCopyButton = findViewById(R.id.generated_password_copy_button);
        generatedPasswordCopyButton.setEnabled(false);
        generatedPasswordText = findViewById(R.id.generated_password_text_view);
    }

    private void setDifficultIndicator(int difficult) {
        switch (difficult) {
            case VERY_EASY_DIFFICULT:
                passwordDifficult.setText(getString(R.string.difficult_very_easy));
                passwordDifficultIndicator.getBackground().setLevel(2000);
                passwordDifficultIndicator.getBackground().setColorFilter(
                        getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                break;
            case EASY_DIFFICULT:
                passwordDifficult.setText(getString(R.string.difficult_easy));
                passwordDifficultIndicator.getBackground().setLevel(4000);
                passwordDifficultIndicator.getBackground().setColorFilter(
                        getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
                break;
            case MEDIUM_DIFFICULT:
                passwordDifficult.setText(getString(R.string.difficult_medium));
                passwordDifficultIndicator.getBackground().setLevel(6000);
                passwordDifficultIndicator.getBackground().setColorFilter(
                        getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                break;
            case STRONG_DIFFICULT:
                passwordDifficult.setText(getString(R.string.difficult_strong));
                passwordDifficultIndicator.getBackground().setLevel(8000);
                passwordDifficultIndicator.getBackground().setColorFilter(
                        getResources().getColor(R.color.salad), PorterDuff.Mode.SRC_ATOP);
                break;
            case VERY_STRONG_DIFFICULT:
                passwordDifficult.setText(getString(R.string.difficult_very_strong));
                passwordDifficultIndicator.getBackground().setLevel(10000);
                passwordDifficultIndicator.getBackground().setColorFilter(
                        getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

    private String getSymbolCountText(String generatedPassword) {
        int specials = 0;
        int digits = 0;
        for (int i = 0; i < generatedPassword.length(); i++) {
            char currentChar = generatedPassword.charAt(i);
            if (Character.isDigit(currentChar)) {
                digits++;
            } else if (!Character.isLetter(currentChar)) {
                specials++;
            }
        }
        int letters = generatedPassword.length() - specials - digits;

        return buildSymbolCountText(generatedPassword, specials, digits, letters);
    }

    private String buildSymbolCountText(String generatedPassword, int specials, int digits, int letters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.length));
        stringBuilder.append(getResources().getQuantityString(R.plurals.letters, letters, letters));
        if (digits > 0) {
            stringBuilder.append(getResources().getQuantityString(R.plurals.digits, digits, digits));
        }
        if (specials > 0) {
            stringBuilder.append(getResources().getQuantityString(R.plurals.specials, specials, specials));
        }
        stringBuilder.append(getResources().getQuantityString(
                R.plurals.symbols, generatedPassword.length(), generatedPassword.length()));
        return stringBuilder.toString();
    }

    private void copyResultToClipboard(TextView textView) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                            getString(R.string.clipboard_item), textView.getText()));

            Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void generatePassword() {
        List<Integer> checkedTypes = new ArrayList<>();
        checkedTypes.add(PasswordGenerator.LOWER_CASE);
        if (capsCheckBox.isChecked()) checkedTypes.add(PasswordGenerator.UPPER_CASE);
        if (digitCheckBox.isChecked()) checkedTypes.add(PasswordGenerator.DIGITS);
        if (symbolCheckBox.isChecked()) checkedTypes.add(PasswordGenerator.SPECIAL_SYMBOL);
        String password = passwordGenerator.generatePassword(passwordLengthSeekBar.getProgress(), checkedTypes);
        generatedPasswordText.setText(password);
    }
}
