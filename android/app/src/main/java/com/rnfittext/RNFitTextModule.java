package com.rnfittext;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.views.text.ReactFontManager;

public class RNFitTextModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String TAG = RNFitTextModule.class.getSimpleName();
    private static final String ERROR_INVALID_TEXT = "ERR_INVALID_TEXT";
    private static final String ERROR_INVALID_FONT_SIZE = "ERR_INVALID_FONT_SIZE";
    private static final String ERROR_INVALID_FONT_FAMILY = "ERR_INVALID_FONT_FAMILY";
    private static final String ERROR_INVALID_MAX_WIDTH = "ERR_INVALID_MAX_WIDTH";
    private static final String ERROR_UNKNOWN_ERROR = "ERR_UNKNOWN_ERROR";
    private float spacingMult = 1.0f;
    private float spacingAdd = 0.0f;
    private TextPaint textPaint;
    private Layout layout;

    public RNFitTextModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNFitText";
    }

    private TextPaint createTextPaint(float fontSize, String fontFamily) {
        TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(fontSize * this.reactContext.getResources().getConfiguration().fontScale);
        AssetManager assetManager = getReactApplicationContext().getAssets();
        Typeface typeface = ReactFontManager.getInstance().getTypeface(fontFamily, Typeface.NORMAL, assetManager);
        textPaint.setTypeface(typeface);
        return textPaint;
    }

    @ReactMethod
    public void fitText(ReadableMap options, Promise promise) {
        if (!options.hasKey("text")) {
            promise.reject(ERROR_INVALID_TEXT, "missing required 'text' property");
            return;
        }
        if (!options.hasKey("minFontSize")) {
            promise.reject(ERROR_INVALID_FONT_SIZE, "missing required 'minFontSize' property");
            return;
        }
        if (!options.hasKey("fontFamily")) {
            promise.reject(ERROR_INVALID_FONT_FAMILY, "missing required 'fontFamily' property");
            return;
        }
        if (!options.hasKey("maxWidth")) {
            promise.reject(ERROR_INVALID_MAX_WIDTH, "missing required 'maxWidth' property");
            return;
        }
        String text = options.getString("text");
        float minFontSize = (float)options.getDouble("minFontSize");
        String fontFamily = options.getString("fontFamily");
        int maxWidth = Math.round((float)options.getDouble("maxWidth"));

        textPaint = createTextPaint(minFontSize, fontFamily);
        WritableArray results = Arguments.createArray();
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                layout = new StaticLayout(
                        text,
                        textPaint,
                        maxWidth,
                        Layout.Alignment.ALIGN_CENTER,
                        spacingMult,
                        spacingAdd,
                        true);
            } else {
                layout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, maxWidth)
                        .setAlignment(Layout.Alignment.ALIGN_CENTER)
                        .setLineSpacing(spacingAdd, spacingMult)
                        .setIncludePad(true)
                        .build();
            }
            int lineCount = layout.getLineCount();
            for (int lineNumber = 0; lineNumber < lineCount; lineNumber++) {
                Log.e(TAG, lineNumber + " " + layout.getLineStart(lineNumber) + " " + layout.getLineEnd(lineNumber));
                results.pushInt(layout.getLineStart(lineNumber));
            }
            promise.resolve(results);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            promise.reject(ERROR_UNKNOWN_ERROR, "Unknown Error");
        }


    }
}