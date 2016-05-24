package com.nairbspace.octoandroid.data.net.stream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class CanvasCreator extends Canvas {

    private final Bitmap mBitmap;
    private final int mDisplayMode;
    private final boolean mShowFps;

    private long mStartTime;
    private int mFrameCounter;

    public CanvasCreator(Bitmap bitmap, DisplayMode displayMode, boolean showFps) {
        mBitmap = bitmap;
        mDisplayMode = displayMode.getValue();
        mShowFps = showFps; // TODO need to implement counter outside of this canvas
    }

    private void render() {
        mStartTime = System.currentTimeMillis();
        Rect destRect = createRectangle(mBitmap.getWidth(), mBitmap.getHeight());
        this.drawColor(Color.BLACK);

        if (mShowFps) {
            drawBitmapWithFps(destRect);
        } else {
            this.drawBitmap(mBitmap, null, destRect, null);
        }
    }

    private Rect createRectangle(int width, int height) {
        int displayWidth = mBitmap.getWidth();
        int displayHeight = mBitmap.getHeight();
        int tempx;
        int tempy;

        if (mDisplayMode == DisplayMode.BEST_FIT.getValue()) {
            float bmasp = (float) width / (float) height;
            width = displayWidth;
            height = (int) (displayWidth / bmasp);
            if (height > displayHeight) {
                height = displayHeight;
                width = (int) (displayHeight * bmasp);
            }
            tempx = (displayWidth / 2) - (width / 2);
            tempy = (displayHeight / 2) - (height / 2);
            return new Rect(tempx, tempy, width + tempx, height + tempy);
        } else if (mDisplayMode == DisplayMode.FULLSCREEN.getValue()) {
            return new Rect(0, 0, displayWidth, displayHeight);
        } else { // DisplayMode.SIZE_STANDARD.getValue()
            tempx = (displayWidth / 2) - (width / 2);
            tempy = (displayHeight / 2) - (height / 2);
            return new Rect(tempx, tempy, width + tempx, height + tempy);
        }
    }

    private void drawBitmapWithFps(Rect destRect) {
        int height;
        int width;
        int ovlPos = DisplayMode.STANDARD.getValue();

        height = ((ovlPos & 1) == 1) ? destRect.top
                : destRect.bottom
                - mBitmap.getHeight();
        width = ((ovlPos & 8) == 8) ? destRect.left
                : destRect.right
                - mBitmap.getWidth();

        mFrameCounter++;

        if ((System.currentTimeMillis() - mStartTime) >= 1000) {
            String fps = String.valueOf(mFrameCounter) + "fps";
            mFrameCounter = 0;
            mStartTime = System.currentTimeMillis();
            Bitmap bitmap = makeFpsOverlay(fps);
            this.drawBitmap(bitmap, width, height, null); // TODO how to combine with current bitmap?
        }
    }

    private Bitmap makeFpsOverlay(String fpsText) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(12);
        paint.setTypeface(Typeface.DEFAULT);


        Rect b = new Rect();
        paint.getTextBounds(fpsText, 0, fpsText.length(), b);
        int bwidth = b.width() + 2;
        int bheight = b.height() + 2;
        Bitmap bm = Bitmap.createBitmap(bwidth, bheight,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);

        int overlayBackgroundColor = Color.BLACK;
        paint.setColor(overlayBackgroundColor);
        c.drawRect(0, 0, bwidth, bheight, paint);

        int overlayTextColor = Color.WHITE;
        paint.setColor(overlayTextColor);
        c.drawText(fpsText, -b.left + 1,
                (bheight / 2) - ((paint.ascent() + paint.descent()) / 2) + 1, paint);
        return bm;
    }
}