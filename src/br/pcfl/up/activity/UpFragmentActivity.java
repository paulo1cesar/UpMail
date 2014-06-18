package br.pcfl.up.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import br.pcfl.up.activity.UpActivityCommon.K9ActivityMagic;
import br.pcfl.up.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public class UpFragmentActivity extends SherlockFragmentActivity implements K9ActivityMagic {

    private UpActivityCommon mBase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mBase = UpActivityCommon.newInstance(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mBase.preDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setupGestureDetector(OnSwipeGestureListener listener) {
        mBase.setupGestureDetector(listener);
    }
}
