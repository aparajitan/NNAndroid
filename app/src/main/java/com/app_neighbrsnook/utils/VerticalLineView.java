package com.app_neighbrsnook.utils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VerticalLineView extends View {

    private Paint paint;
    private int maxValue = 100;   // Maximum value (100%)
    private int currentValue = 0; // The dynamic value

    public VerticalLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize the Paint object
        paint = new Paint();
        paint.setColor(0xFF000000); // Default black color
        paint.setStrokeWidth(25f);   // Line width
    }

    // Set the current value dynamically
    public void setCurrentValue(int value) {
        this.currentValue = Math.min(value, maxValue); // Ensure value doesn't exceed maxValue
        invalidate(); // Redraw the view when the value changes
    }

    // Set the color dynamically
    public void setLineColor(int color) {
        paint.setColor(color);
        invalidate(); // Redraw the view when the color changes
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get the view height
        int height = getHeight();
        int width = getWidth();

        // Calculate the length of the line based on the currentValue (percentage of maxValue)
        float lineHeight = (currentValue / (float) maxValue) * height;

        // Draw a vertical line from top to a position proportional to the current value
        canvas.drawLine(width / 2, height - lineHeight, width / 2, height, paint);
    }
}
