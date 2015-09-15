package com.github.florent37.glidepalette.sample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText editText;
    private ProgressBar progressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edit_text);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        button = (Button) findViewById(R.id.button);

        final TextView textVibrant = (TextView) findViewById(R.id.textVibrant);
        final TextView textVibrantLight = (TextView) findViewById(R.id.textVibrantLight);
        final TextView textVibrantDark = (TextView) findViewById(R.id.textVibrantDark);
        final TextView textMuted = (TextView) findViewById(R.id.textMuted);
        final TextView textMutedLight = (TextView) findViewById(R.id.textMutedLight);
        final TextView textMutedDark = (TextView) findViewById(R.id.textMutedDark);

        View.OnClickListener colorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable background = (ColorDrawable) v.getBackground();
                if (null == background) {
                    // The Palette was unable to get a color for us.
                    Snackbar.make(button, "Sorry, the Palette was unable to put a color here.", Snackbar
                            .LENGTH_SHORT).show();
                } else {
                    int backgroundColor = background.getColor();
                    int textColor = ((TextView) v).getCurrentTextColor();
                    final String hexColor = String.format("#%06X", (0xFFFFFF & backgroundColor));
                    Snackbar snack = Snackbar.make(button, "Color is " + hexColor, Snackbar.LENGTH_LONG);
                    View snackbarView = snack.getView();
                    snackbarView.setBackgroundColor(backgroundColor);
                    snack.setActionTextColor(textColor);
                    snack.setAction("Copy hex code", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Gets a handle to the clipboard service.
                            ClipboardManager clipboard = (ClipboardManager)
                                    getSystemService(Context.CLIPBOARD_SERVICE);
                            // Creates a new text clip to put on the clipboard
                            ClipData clip = ClipData.newPlainText("simple text", hexColor);
                            // Set the clipboard's primary clip.
                            clipboard.setPrimaryClip(clip);
                        }
                    });
                    snack.show();
                }
            }
        };

        textVibrant.setOnClickListener(colorListener);
        textVibrantLight.setOnClickListener(colorListener);
        textVibrantDark.setOnClickListener(colorListener);
        textMuted.setOnClickListener(colorListener);
        textMutedLight.setOnClickListener(colorListener);
        textMutedDark.setOnClickListener(colorListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String url = editText.getText().toString().trim();
                if (url.isEmpty()) {
                    Snackbar.make(button, "Please enter a URL in the box!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load(url)
                        .fitCenter()
                        .listener(GlidePalette.with(url)
                                .use(GlidePalette.Profile.VIBRANT)
                                .intoBackground(textVibrant, GlidePalette.Swatch.RGB)
                                .intoTextColor(textVibrant, GlidePalette.Swatch.BODY_TEXT_COLOR)
                                .use(GlidePalette.Profile.VIBRANT_DARK)
                                .intoBackground(textVibrantDark, GlidePalette.Swatch.RGB)
                                .intoTextColor(textVibrantDark, GlidePalette.Swatch.BODY_TEXT_COLOR)
                                .use(GlidePalette.Profile.VIBRANT_LIGHT)
                                .intoBackground(textVibrantLight, GlidePalette.Swatch.RGB)
                                .intoTextColor(textVibrantLight, GlidePalette.Swatch.BODY_TEXT_COLOR)

                                .use(GlidePalette.Profile.MUTED)
                                .intoBackground(textMuted, GlidePalette.Swatch.RGB)
                                .intoTextColor(textMuted, GlidePalette.Swatch.BODY_TEXT_COLOR)
                                .use(GlidePalette.Profile.MUTED_DARK)
                                .intoBackground(textMutedDark, GlidePalette.Swatch.RGB)
                                .intoTextColor(textMutedDark, GlidePalette.Swatch.BODY_TEXT_COLOR)
                                .use(GlidePalette.Profile.MUTED_LIGHT)
                                .intoBackground(textMutedLight, GlidePalette.Swatch.RGB)
                                .intoTextColor(textMutedLight, GlidePalette.Swatch.BODY_TEXT_COLOR)

                                .intoCallBack(new GlidePalette.CallBack() {

                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        //specific task
                                        progressBar.setVisibility(View.GONE);
                                        editText.setVisibility(View.GONE);
                                        button.setVisibility(View.GONE);
                                        findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
                                    }
                                }))
                        .into(imageView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (editText.getVisibility() == View.VISIBLE) {
            // Exit the app, the user has pressed Back twice
            finish();
        } else {
            // Reset the app for another image into the palette
            editText.setText("");
            editText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(null);
            findViewById(R.id.bottom_layout).setVisibility(View.GONE);
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}