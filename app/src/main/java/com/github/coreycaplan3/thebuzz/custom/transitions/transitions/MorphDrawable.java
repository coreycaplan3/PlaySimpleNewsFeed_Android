package com.github.coreycaplan3.thebuzz.custom.transitions.transitions;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Property;

import com.github.coreycaplan3.thebuzz.utilities.visual.AnimationUtility;

/**
 * Created by Corey on 6/4/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: A drawable that can morph size, shape (via it's corner radius) and color.
 * Specifically this is useful for animating between a {@link FloatingActionButton} and a dialog
 * activity.
 */
class MorphDrawable extends Drawable {

    static final Property<MorphDrawable, Float> CORNER_RADIUS =
            new AnimationUtility.FloatProperty<MorphDrawable>("mCornerRadius") {

                @Override
                public void setValue(MorphDrawable morphDrawable, float value) {
                    morphDrawable.setCornerRadius(value);
                }

                @Override
                public Float get(MorphDrawable morphDrawable) {
                    return morphDrawable.getCornerRadius();
                }

            };

    static final Property<MorphDrawable, Integer> COLOR =
            new AnimationUtility.IntProperty<MorphDrawable>("color") {

                @Override
                public void setValue(MorphDrawable morphDrawable, int value) {
                    morphDrawable.setColor(value);
                }

                @Override
                public Integer get(MorphDrawable morphDrawable) {
                    return morphDrawable.getColor();
                }

            };

    private Paint paint;
    private float mCornerRadius;

    MorphDrawable(@ColorInt int color, float cornerRadius) {
        mCornerRadius = cornerRadius;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    private float getCornerRadius() {
        return mCornerRadius;
    }

    private void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
        invalidateSelf();
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(@ColorInt int color) {
        paint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(
                getBounds().left,
                getBounds().top,
                getBounds().right,
                getBounds().bottom,
                mCornerRadius,
                mCornerRadius,
                paint);
    }

    @Override
    public void getOutline(@NonNull Outline outline) {
        outline.setRoundRect(getBounds(), mCornerRadius);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }

}
