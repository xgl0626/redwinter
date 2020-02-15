package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.myapplication.R;

public class CircleImageView extends AppCompatImageView {
    private Paint mPaint = new Paint();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        float scale = (float) getWidth() / bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap rawBitmap = ((BitmapDrawable) drawable).getBitmap();

            Bitmap newBitmap = scaleBitmap(rawBitmap);
            Bitmap circleBitmap = getCircleBitmap(newBitmap);

            mPaint.reset();
            canvas.drawBitmap(circleBitmap, 0, 0, mPaint);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(circleBitmap);

        mPaint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int radius = bitmap.getWidth() / 2;
        canvas.drawCircle(radius, radius, radius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
        return circleBitmap;
    }

    public void setImageResource(Bitmap bitmap) {
    }
}
