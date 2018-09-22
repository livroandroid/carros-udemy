package br.com.livroandroid.carros.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.coordinatorlayout.widget.CoordinatorLayout

// https://stackoverflow.com/questions/41153619/floating-action-button-not-visible-on-scrolling-after-updating-google-support/41386278#41386278
class ScrollingFABBehavior(context: Context, attrs: AttributeSet)
    : FloatingActionButton.Behavior() {

    companion object {
        private const val TAG = "carros"
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return true
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

//        Log.e(TAG, "onNestedScroll called $dyConsumed")
        if (dyConsumed > 0) {
//            Log.d(TAG, "child.hide()")
            //child.hide()

            child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                /**
                 * Called when a FloatingActionButton has been hidden
                 *
                 * @param fab the FloatingActionButton that was hidden.
                 */
                @SuppressLint("RestrictedApi")
                override fun onHidden(fab: FloatingActionButton?) {
                    super.onHidden(fab)
                    fab?.visibility = View.INVISIBLE
                }
            })
        } else {
//            Log.d(TAG, "child.show()")
            child.show()
        }
    }
}