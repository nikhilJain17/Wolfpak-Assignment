package com.hello.learning.wolfpak_camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by test on 7/5/15.
 */
public class DrawingOnCanvas extends View {

    public DrawingOnCanvas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw a rectangle for now
        Rect rect = new Rect();
        rect.set(0,0,100,100);

        Paint blue = new Paint();
        blue.setColor(Color.BLUE);

        canvas.drawRect(rect, blue);

    }


}
