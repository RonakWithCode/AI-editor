package com.pushstartuphub.smartwrite.view;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class GradientTextView extends AppCompatTextView {

    private int startColor = 0xFFFF0000; // Default Red
    private int endColor = 0xFF00FF00; // Default Green

    public GradientTextView(Context context) {
        super(context);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // You can obtain colors from attributes if needed
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        // Create a LinearGradient
        LinearGradient shader = new LinearGradient(
                0, 0, getWidth(), getTextSize(),
                startColor, endColor,
                Shader.TileMode.CLAMP
        );
        getPaint().setShader(shader);
        super.onDraw(canvas);
    }

    public void setGradientColors(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        invalidate(); // Refresh the view
    }
}
