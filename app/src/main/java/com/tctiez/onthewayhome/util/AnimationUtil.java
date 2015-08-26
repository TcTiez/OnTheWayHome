package com.tctiez.onthewayhome.util;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class AnimationUtil {
    public static ObjectAnimator getTranslationX(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "translationX", values);
            ret.setInterpolator(new DecelerateInterpolator());
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getTranslationY(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "translationY", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getRotation(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "rotation", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getRotationX(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "rotationX", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getRotationY(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "rotationY", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getScaleX(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "scaleX", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getScaleY(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "scaleY", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getPivotX(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "pivotX", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getPivotY(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "pivotY", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getAlpha(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "alpha", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getMoveX(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "x", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static ObjectAnimator getMoveY(View v, long duration, float... values) {
        ObjectAnimator ret = null;
        try {
            ret = ObjectAnimator.ofFloat(v, "y", values);
            ret.setDuration(duration);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }
}
