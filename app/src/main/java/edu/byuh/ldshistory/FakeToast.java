package edu.byuh.ldshistory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by draperg on 8/10/16.
 */
public class FakeToast {

    private String text;
    private RectF bounds;
    private RectF closeButton;
    private Paint paint;
    private Paint textColor;
    //private Paint textShadowColor;
    private float textHeight;
    private String lines[];
    private PointF linePos[];

    public FakeToast(View parent, String t) {
        text = t;
        final float X_MARGIN = 0.01f;
        final float Y_MARGIN = 0.7f;
        final float leftMargin = parent.getWidth() * X_MARGIN;
        final float rightMargin = parent.getWidth() - leftMargin;
        float topMargin = leftMargin;
        float textSize;
        closeButton = new RectF(rightMargin-leftMargin*4, topMargin+leftMargin, rightMargin-leftMargin, topMargin+leftMargin*4);
        final float width = closeButton.left-leftMargin;
        textSize = MainActivity.findThePerfectFontSize(Math.max(parent.getHeight(), parent.getWidth()) * 0.025f);

        Typeface font = Typeface.SANS_SERIF;
        paint = new Paint();
        textColor = new Paint();
        //textShadowColor = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setAlpha(155);
        paint.setStyle(Paint.Style.FILL);
        //textColor.setColor(Color.argb(255, 230, 230, 230));
        textColor.setColor(Color.WHITE);
        textColor.setShadowLayer(5,5,5,Color.BLACK);
        textColor.setStyle(Paint.Style.FILL);
        //textShadowColor.setColor(Color.BLACK);
        textColor.setTextSize(textSize);
        textColor.setTypeface(font);
        //textColor.setStyle(Paint.Style.STROKE);
        //textShadowColor.setTextSize(textSize);
        //textShadowColor.setTypeface(font);
        textHeight = -textColor.ascent() + textColor.descent() + textColor.getFontMetrics().leading;
        //divide the text into lines...
        Log.d("CS203", "textHeight=" + textHeight);
        float totalLength = textColor.measureText(text);
        float maxLengthPerLine = width - 2*textHeight;
        int numLines = (int)Math.ceil(totalLength / maxLengthPerLine);
        float avgLengthPerLine = totalLength / numLines;
        lines = new String[numLines];
        Arrays.fill(lines, "");
        linePos = new PointF[numLines];
        String words[] = text.split(" ");
        float currentLength = 0;
        int index = 0;
        for (String word : words) {
            float wordLength = textColor.measureText(word + " ");
            lines[index] += (word + " ");
            currentLength += wordLength;
            if (currentLength > avgLengthPerLine && index < numLines-1) {
                index++;
                currentLength = 0;
            }
        }
        float allTextHeight = textHeight * numLines;
        final float toastHeight = Math.max(closeButton.height()*3, allTextHeight);
        final float bottomMargin = toastHeight + 2*topMargin;
        bounds = new RectF(leftMargin, topMargin, rightMargin, bottomMargin);
        PointF center = new PointF(leftMargin + width/2, bottomMargin - bounds.height()/2);
        float topRow = center.y - allTextHeight/2f + textHeight*0.75f;
        for (int i=0; i<numLines; ++i) {
            linePos[i] = new PointF(center.x - textColor.measureText(lines[i])/2f, topRow + i*textHeight);
        }
        //for debug
		for (String ln : lines) {
			Log.d("LINES*****", ln);
		}
        //closeButtonHotSpot = new RectF(bounds.centerX(), bounds.top-bounds.height()/2f, parent.getWidth(), bounds.bottom);
    }

    public void draw(Canvas c) {

        c.drawRoundRect(bounds, 10, 10, paint);
//        for (int i=0; i<lines.length; ++i) {
//            c.drawText(lines[i], linePos[i].x+1, linePos[i].y+1, textShadowColor);
//        }
        for (int i=0; i<lines.length; ++i) {
            c.drawText(lines[i], linePos[i].x, linePos[i].y, textColor);
        }
        //c.drawText("X", closeButton.left+closeButton.width()/2, closeButton.top+closeButton.height()/2, textColor);
        c.drawLine(closeButton.left, closeButton.top, closeButton.right, closeButton.bottom, textColor);
        c.drawLine(closeButton.left, closeButton.bottom, closeButton.right, closeButton.top, textColor);
        //c.drawRect(closeButton, textColor);
        c.drawLine(closeButton.left, closeButton.top, closeButton.right, closeButton.top, textColor);
        c.drawLine(closeButton.right, closeButton.top, closeButton.right, closeButton.bottom, textColor);
        c.drawLine(closeButton.right, closeButton.bottom, closeButton.left, closeButton.bottom, textColor);
        c.drawLine(closeButton.left, closeButton.bottom, closeButton.left, closeButton.top, textColor);
        Log.d("DRAW_FAKETOAST","DRAWING!");
    }

//    public boolean contains(float x, float y) {
//        return (closeButtonHotSpot.contains(x, y));
//    }

}
