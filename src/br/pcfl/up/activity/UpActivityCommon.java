package br.pcfl.up.activity;

import java.util.Locale;

import br.pcfl.up.Up;
import br.pcfl.up.activity.misc.SwipeGestureDetector;
import br.pcfl.up.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;
import br.pcfl.up.helper.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * This class implements functionality common to most activities used in K-9 Mail.
 *
 * @see UpActivity
 * @see UpListActivity
 * @see UpFragmentActivity
 */
public class UpActivityCommon {
    /**
     * Creates a new instance of {@link UpActivityCommon} bound to the specified activity.
     *
     * @param activity
     *         The {@link Activity} the returned {@code K9ActivityCommon} instance will be bound to.
     *
     * @return The {@link UpActivityCommon} instance that will provide the base functionality of the
     *         "K9" activities.
     */
    public static UpActivityCommon newInstance(Activity activity) {
        return new UpActivityCommon(activity);
    }

    public static void setLanguage(Context context, String language) {
        Locale locale;
        if (StringUtils.isNullOrEmpty(language)) {
            locale = Locale.getDefault();
        } else if (language.length() == 5 && language.charAt(2) == '_') {
            // language is in the form: en_US
            locale = new Locale(language.substring(0, 2), language.substring(3));
        } else {
            locale = new Locale(language);
        }

        Configuration config = new Configuration();
        config.locale = locale;
        Resources resources = context.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    /**
     * Base activities need to implement this interface.
     *
     * <p>The implementing class simply has to call through to the implementation of these methods
     * in {@link UpActivityCommon}.</p>
     */
    public interface K9ActivityMagic {
        void setupGestureDetector(OnSwipeGestureListener listener);
    }


    private Activity mActivity;
    private GestureDetector mGestureDetector;


    private UpActivityCommon(Activity activity) {
        mActivity = activity;
        setLanguage(mActivity, Up.getUpLanguage());
        mActivity.setTheme(Up.getUpThemeResourceId());
    }

    /**
     * Call this before calling {@code super.dispatchTouchEvent(MotionEvent)}.
     */
    public void preDispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
    }

    /**
     * Get the background color of the theme used for this activity.
     *
     * @return The background color of the current theme.
     */
    public int getThemeBackgroundColor() {
        TypedArray array = mActivity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.colorBackground });

        int backgroundColor = array.getColor(0, 0xFF00FF);

        array.recycle();

        return backgroundColor;
    }

    /**
     * Call this if you wish to use the swipe gesture detector.
     *
     * @param listener
     *         A listener that will be notified if a left to right or right to left swipe has been
     *         detected.
     */
    public void setupGestureDetector(OnSwipeGestureListener listener) {
        mGestureDetector = new GestureDetector(mActivity,
                new SwipeGestureDetector(mActivity, listener));
    }
}
