package com.app_neighbrsnook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

public class EmojiUtil {

    public static Bitmap getEmojiBitmap(Context context, String emoji, int textSizeDp) {

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, textSizeDp, context.getResources().getDisplayMetrics()));

        float textWidth = paint.measureText(emoji);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;

        if (textWidth <= 0 || textHeight <= 0) {
            throw new IllegalArgumentException("Invalid dimensions for emoji bitmap");
        }

        Bitmap bitmap = Bitmap.createBitmap((int) textWidth, (int) textHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(emoji, 0, -fontMetrics.top, paint);

        return bitmap;
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}