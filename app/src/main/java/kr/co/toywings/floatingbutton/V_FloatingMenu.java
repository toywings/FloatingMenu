package kr.co.toywings.floatingbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by schneider on 2017. 11. 6..
 */

public class V_FloatingMenu extends RelativeLayout implements View.OnTouchListener
    {
    private Context CONTEXT;
    private ImageView DIM;
    private V_CheckImageView START_BUTTON;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private final int MARGIN = 50;      // 버튼 바깥여백
    private final int BOUNDARY = 5;     // 터치,클릭 구분 범위
    private final float ANGLE = 135f;   // 버튼회전각도
    private final int DURATION = 500;   // 버튼회전시간
    private float dX, dY;
    private float rawX, rawY;
    private int gapX, gapY;
    private int gap;
    private int START_BUTTON_WIDTH;
    private int START_BUTTON_HEIGHT;
    private ArrayList<Data> buttons;
    private ImageView TOP_ICON;
    private int TOP_ICON_WIDTH;
    private int TOP_ICON_HEIGHT;
    private long TIME;

    public V_FloatingMenu(Context context)
        {
        super(context);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu(Context context, @Nullable AttributeSet attrs)
        {
        super(context, attrs);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
        {
        super(context, attrs, defStyleAttr);
        CONTEXT = context;
        init();
        }

    public void init()
        {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels - getStatusBarHeight();
        gap = (int)getResources().getDimension(R.dimen.dp10);

        initViews();
        setListeners();
        addView(DIM);
        addView(START_BUTTON);
        addView(TOP_ICON);
        invalidate();
        }

    private void initViews()
        {
        RelativeLayout.LayoutParams params_start = getLayoutParamRightBottom();
        params_start.rightMargin = MARGIN;
        params_start.bottomMargin = MARGIN;

        RelativeLayout.LayoutParams params_dim = getLayoutParamRightBottom();
        params_dim.width = 100;
        params_dim.height = 100;

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(Color.parseColor("#b3000000"));
        DIM = new ImageView(CONTEXT);
        DIM.setImageDrawable(gd);
        DIM.setLayoutParams(params_dim);
        DIM.setAlpha(0f);

        START_BUTTON = new V_CheckImageView(CONTEXT);
        START_BUTTON.setImageResource(R.drawable.floating_home);
        START_BUTTON.setLayoutParams(params_start);

        TOP_ICON = new ImageView(CONTEXT);
        TOP_ICON.setImageResource(R.drawable.ic_floating_home);

        START_BUTTON.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        DIM.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        TOP_ICON.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        START_BUTTON_WIDTH = START_BUTTON.getMeasuredWidth();
        START_BUTTON_HEIGHT = START_BUTTON.getMeasuredHeight();
        TOP_ICON_WIDTH = TOP_ICON.getMeasuredWidth();
        TOP_ICON_HEIGHT = TOP_ICON.getMeasuredHeight();

        gapX = (START_BUTTON_WIDTH-params_dim.width)/2;
        gapY = (START_BUTTON_HEIGHT-params_dim.height)/2;
        params_dim.rightMargin = MARGIN + gapX;
        params_dim.bottomMargin = MARGIN + gapY;


        RelativeLayout.LayoutParams param_icon = getLayoutParamRightBottom();
        param_icon.rightMargin = MARGIN + ((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2);
        param_icon.bottomMargin = MARGIN + START_BUTTON_HEIGHT + gap;
        TOP_ICON.setLayoutParams(param_icon);
        }

    public void setButtons(ArrayList<Data> list)
        {
        buttons = list;
        }

    private void setListeners()
        {
        setOnTouchListener(this);

        START_BUTTON.setOnClickListener(new OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                START_BUTTON.setEnabled(false);
                new Handler().postDelayed(new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        START_BUTTON.setEnabled(true);
                        }
                    }, 1000);
                START_BUTTON.toggle();
                ViewPropertyAnimator vpa = view.animate();
                if(START_BUTTON.isChecked())
                    {
                    vpa.rotation(ANGLE); // 한번돌아간 상태면 안돌림 By는 현재상태에서 돌림
                    vpa.setInterpolator(new AnticipateInterpolator());
                    removeView(TOP_ICON);
                    START_BUTTON.setAlpha(0f);
                    START_BUTTON.animate().x(SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN).y(SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN-determineTitleBarHeight()).setDuration(0).start();
                    START_BUTTON.animate().alpha(1f).setDuration(300).start();
                    }
                else{
                    vpa.rotationBy(-ANGLE);
                    vpa.setInterpolator(new OvershootInterpolator());
                    show_top_button.sendEmptyMessageDelayed(0, 500);
                    }

                showButtons(START_BUTTON.isChecked());
                vpa.setDuration(DURATION);
                vpa.start();
                }
            });

        // 스타트버튼 터치이동
        START_BUTTON.setOnTouchListener(new OnTouchListener()
            {
            @Override
            public boolean onTouch(View view, MotionEvent event)
                {
                switch (event.getAction())
                    {
                    case MotionEvent.ACTION_DOWN:
                        rawX = event.getRawX();
                        rawY = event.getRawY();
                        dX = view.getX() - rawX;
                        dY = view.getY() - rawY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(START_BUTTON.isChecked()) return true; // 열린상태에서 이동안되게
                        float x = event.getRawX() + dX;
                        float y = event.getRawY() + dY;
                        int right = SCREEN_WIDTH - view.getWidth() - MARGIN;
                        int bottom = SCREEN_HEIGHT - view.getHeight() - MARGIN;
                        if(x<MARGIN) x=MARGIN;  // Left     Limit
                        int top = MARGIN + TOP_ICON_HEIGHT + gap;
                        if(y<top) y=top;  // Top      Limit
                        if(x>right) x=right;    // Right    Limit
                        if(y>bottom) y=bottom;  // Bottom   Limit
                        TOP_ICON.animate().x(x+((START_BUTTON_WIDTH - TOP_ICON_WIDTH)/2)).y(y-gap-TOP_ICON_HEIGHT).setDuration(0).start();
                        view.animate().x(x).y(y).setDuration(0).start();
                        DIM.animate().x(x+gapX).y(y+gapY).setDuration(0).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Math.abs(rawX-event.getRawX())<BOUNDARY&&Math.abs(rawY-event.getRawY())<BOUNDARY)
                            {
                            return false;
                            }
                        else{
                            return true;
                            }
                    }
                return false;
                }
            });

//        START_BUTTON.setOnCheckedChangeListener(new V_CheckImageView.OnCheckedChangeListener()
//            {
//            @Override
//            public void onCheckedChanged(View checkableView, boolean isChecked)
//                {
//
//                }
//            });
        }

    Handler show_top_button = new Handler()
        {
        @Override
        public void handleMessage(Message msg)
            {
            super.handleMessage(msg);

            if(START_BUTTON.isChecked())
                {
                Log.i("####", "탑버튼 없애기");
                removeView(TOP_ICON);
//                removeView(topl);
                }
            else{
                Log.i("####", "탑버튼 나오기");
                int[] location = new int[2];
                START_BUTTON.getLocationOnScreen(location);
                int start_button_margin_x = SCREEN_WIDTH - (location[0] + START_BUTTON_WIDTH);
                int start_button_margin_y = SCREEN_HEIGHT + getStatusBarHeight() - location[1];
                TOP_ICON = new ImageView(CONTEXT);
                TOP_ICON.setImageResource(R.drawable.ic_floating_home);

                RelativeLayout.LayoutParams param_icon = getLayoutParamRightBottom();
                param_icon.rightMargin = start_button_margin_x + ((START_BUTTON_WIDTH - TOP_ICON_WIDTH)/2);
                param_icon.bottomMargin = gap + start_button_margin_y;
                TOP_ICON.setLayoutParams(param_icon);
                addView(TOP_ICON);
                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                TOP_ICON.startAnimation(anim_show);

//                topl = new TextView(CONTEXT);
//                topl.setText("Top");
//                topl.setTextColor(Color.WHITE);
//                topl.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                RelativeLayout.LayoutParams param_label = getLayoutParamRightBottom();
//                param_label.rightMargin = param_icon.rightMargin + TOP_ICON.getMeasuredWidth() + gap;
//                param_label.bottomMargin = param_icon.bottomMargin + ((TOP_ICON.getMeasuredHeight()-topl.getMeasuredHeight())/2);
//                topl.setLayoutParams(param_label);
//                addView(topl);
//                topl.startAnimation(anim_show);
                }
            }
        };

    private RelativeLayout.LayoutParams getLayoutParamRightBottom()
        {
        RelativeLayout.LayoutParams param_icon = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param_icon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param_icon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return param_icon;
        }


    TextView topl;
    ImageView []icons;
    TextView []labels;
    private void showButtons(boolean isShow)
        {
        if(isShow)
            {
            LinearLayout vertical = new LinearLayout(CONTEXT);
            vertical.setBackgroundColor(Color.parseColor("#99637262"));
            vertical.setOrientation(LinearLayout.VERTICAL);

            int[] location = new int[2];
            START_BUTTON.getLocationOnScreen(location);

            int start_button_margin_x = MARGIN;
            int start_button_margin_y = MARGIN;

            icons = new ImageView[buttons.size()];
            labels = new TextView[buttons.size()];

            for(int i=0;i<buttons.size();i++)
                {
                Data data = buttons.get(i);
                // 아이콘
                icons[i] = new ImageView(CONTEXT);
                ViewCompat.setTranslationZ(icons[i], buttons.size()-i);
                icons[i].setImageResource(data.IMAGE);

                RelativeLayout.LayoutParams param_icon = getLayoutParamRightBottom();
                param_icon.rightMargin = start_button_margin_x + ((START_BUTTON_WIDTH - TOP_ICON_WIDTH)/2);
                param_icon.bottomMargin = start_button_margin_y + gap + START_BUTTON_HEIGHT + ((TOP_ICON_HEIGHT+gap) * i);
                icons[i].setLayoutParams(param_icon);
                addView(icons[i]);
                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                anim_show.setStartOffset(70*i);
                icons[i].startAnimation(anim_show);

                // 라벨
                labels[i] = new TextView(CONTEXT);
                ViewCompat.setTranslationZ(labels[i], buttons.size()-i);
                labels[i].setText(data.NAME);
                labels[i].setTextColor(Color.WHITE);
                labels[i].measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                RelativeLayout.LayoutParams param_label = getLayoutParamRightBottom();
                param_label.rightMargin = param_icon.rightMargin + TOP_ICON_WIDTH + gap;
                param_label.bottomMargin = param_icon.bottomMargin + ((TOP_ICON_HEIGHT-labels[i].getMeasuredHeight())/2);
                labels[i].setLayoutParams(param_label);
                addView(labels[i]);
                labels[i].startAnimation(anim_show);
                }

            DIM.animate().scaleX(80f).scaleY(80f).alpha(1f).setDuration(DURATION).start();
            }
        else{
            for(int i=0;i<icons.length;i++)
                {
                final int reverse = icons.length-1-i;
                Animation anim_hide = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_hide);
                anim_hide.setStartOffset(70*i);
                anim_hide.setAnimationListener(new Animation.AnimationListener()
                    {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation)
                        {
                        removeView(icons[reverse]);
                        }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                    });
                icons[reverse].startAnimation(anim_hide);
                removeView(labels[reverse]);
                }
            DIM.animate().scaleX(1f).scaleY(1f).alpha(0f).setDuration(DURATION).start();
            }
        }

    private int getStatusBarHeight()
        {
        int height = 0;
        int idStatusBarHeight = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0)
            {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
            }

//        Log.i("####", "getTitleBarHeight()"+determineTitleBarHeight());

        return height + determineTitleBarHeight();
        }

    private int determineTitleBarHeight()
        {
        Window window = ((Activity) getContext()).getWindow();
        Rect windowRect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(windowRect);
        return windowRect.top;
        }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
        {
        if(START_BUTTON.isChecked()) START_BUTTON.performClick();
        return false;
        }

    public boolean isOpened()
        {
        return START_BUTTON.isChecked();
        }

    public void clickStart()
        {
        START_BUTTON.performClick();
        }
    }