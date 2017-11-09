package kr.co.toywings.floatingbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

public class V_CheckImageView extends ImageView implements Checkable
    {
    private boolean mChecked;
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public V_CheckImageView(Context context)
        {
        super(context);

        init();
        }

    public V_CheckImageView(Context context, AttributeSet attrs)
        {
        super(context, attrs);

        init();
        }

    private void init()
        {

        }

    @Override
    public int[] onCreateDrawableState(final int extraSpace)
        {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
        }

    @Override
    public void setChecked(boolean checked)
        {
        if (mChecked != checked)
            {
            mChecked = checked;
            refreshDrawableState();

            if (mOnCheckedChangeListener != null)
                {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
                }
            }
        }

    @Override
    public boolean isChecked()
        {
        return mChecked;
        }

    @Override
    public void toggle()
        {
        setChecked(!mChecked);
        }


    public interface OnCheckedChangeListener
        {
        void onCheckedChanged(View checkableView, boolean isChecked);
        }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
        {
        mOnCheckedChangeListener = listener;
        }
    }