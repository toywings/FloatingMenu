package kr.co.toywings.floatingbutton;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by schneider on 2017. 11. 6..
 */

public class V_FloatingMenu extends RelativeLayout implements View.OnTouchListener
    {
    public Context CONTEXT;
    public ArrayList<Data> buttons;

    public ImageView DIM;
    public V_CheckImageView START_BUTTON;
    public ImageView TOP;

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;
    public final int MARGIN = 50;      // 버튼 바깥여백
    public final int BOUNDARY = 30;    // 터치,클릭 구분 범위
    public final float ANGLE = 135f;   // 버튼회전각도
    public final int DURATION = 500;   // 버튼회전시간
    public float dX, dY;
    public float rawX, rawY;
    public int gapX, gapY;
    public int gap;
    public int START_BUTTON_WIDTH;
    public int START_BUTTON_HEIGHT;
    public int TOP_ICON_WIDTH;
    public int TOP_ICON_HEIGHT;
    public int NAVIGATION_BAR_HEIGHT;
    public int START_X;
    public int START_Y;
    public int TOP_X;
    public int TOP_Y;

    public V_FloatingMenu(Context context)
        {
        super(context);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu(Context context, AttributeSet attrs)
        {
        super(context, attrs);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu(Context context, AttributeSet attrs, int defStyleAttr)
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


        }

    private void initViews()
        {
        LayoutParams params_start = getLayoutParamRightBottom();
        params_start.rightMargin = MARGIN;
        params_start.bottomMargin = MARGIN;

        LayoutParams params_dim = getRelativeLayoutParam();
        params_dim.width = 100;
        params_dim.height = 100;
        params_dim.rightMargin = 0;
        params_dim.bottomMargin = 0;
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

        START_BUTTON.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        DIM.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        START_BUTTON_WIDTH = START_BUTTON.getMeasuredWidth();
        START_BUTTON_HEIGHT = START_BUTTON.getMeasuredHeight();

        gapX = (START_BUTTON_WIDTH-params_dim.width)/2;
        gapY = (START_BUTTON_HEIGHT-params_dim.height)/2;
//        params_dim.rightMargin = MARGIN + gapX;
//        params_dim.bottomMargin = MARGIN + gapY;

        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            {
            NAVIGATION_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId);
            Log.i("####", "네비게이션바 높이 : "+NAVIGATION_BAR_HEIGHT);
            }

        }

    public void setButtons(ArrayList<Data> list, ImageView Top)
        {
        removeView(TOP);
        removeView(START_BUTTON);

        buttons = list;
        TOP = Top;

        int top_width = (int)getResources().getDimension(R.dimen.dp40);
        int top_height = (int)getResources().getDimension(R.dimen.dp40);
        LayoutParams param_icon = getRelativeLayoutParam();
        param_icon.width = top_width;
        param_icon.height = top_height;
        TOP.setLayoutParams(param_icon);

        TOP_ICON_WIDTH = top_width;
        TOP_ICON_HEIGHT = top_height;
        START_BUTTON = new V_CheckImageView(CONTEXT);
        START_BUTTON.setImageResource(R.drawable.floating_home);

        addView(DIM);
        addView(TOP);
        addView(START_BUTTON);
        START_X = SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN;
//        START_Y = SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN-NAVIGATION_BAR_HEIGHT;
        START_Y = SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN;
        START_BUTTON.animate().x(START_X).y(START_Y).setDuration(0).start();
        TOP_X = START_X +((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2);
        TOP_Y = START_Y -TOP_ICON_HEIGHT-gap;

        TOP.animate().x(TOP_X).y(START_Y -TOP_ICON_HEIGHT-gap).setDuration(0).start();

        TOP.layout(TOP_X, TOP_Y, TOP_X + TOP.getMeasuredWidth(), TOP_Y + TOP.getMeasuredHeight());

//        ObjectAnimator translateX = ObjectAnimator.ofFloat(TOP, "translationX", TOP_X);
//        ObjectAnimator translateY = ObjectAnimator.ofFloat(TOP, "translationY", START_Y-TOP_ICON_HEIGHT-gap);
//        translateX.setDuration(0);
//        translateY.setDuration(0);
//        translateX.start();
//        translateY.start();

        setListeners();
        }

    private LayoutParams getRelativeLayoutParam()
        {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }

    private void setListeners()
        {
        START_BUTTON.setOnClickListener(new OnClickListener()
            {
            @Override
            public void onClick(View view)
                {
                if(buttons==null||buttons.size()==0) Toast.makeText(CONTEXT, "리스트가 없습니다.", Toast.LENGTH_SHORT).show();
                START_BUTTON.setEnabled(false);
                new Handler().postDelayed(new Runnable()
                    {
                    @Override
                    public void run()
                        {
                        START_BUTTON.setEnabled(true);
                        }
                    }, 1500);
                START_BUTTON.toggle();
                ViewPropertyAnimator vpa = view.animate();
                if(START_BUTTON.isChecked())
                    {
                    vpa.rotation(ANGLE); // 한번돌아간 상태면 안돌림 By는 현재상태에서 돌림
                    vpa.setInterpolator(new AnticipateInterpolator());

                    START_BUTTON.setAlpha(0f);
                    START_BUTTON.animate().x(SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN).y(SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN).setDuration(0).start();
                    START_BUTTON.animate().alpha(1f).setDuration(300).start();

                    removeView(TOP);
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

        START_BUTTON.setOnCheckedChangeListener(new V_CheckImageView.OnCheckedChangeListener()
            {
            @Override
            public void onCheckedChanged(View checkableView, boolean isChecked)
                {
//                if(!isChecked) START_BUTTON.performClick();
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
                        START_BUTTON.bringToFront();
                        float x = event.getRawX() + dX;
                        float y = event.getRawY() + dY;
                        int right = SCREEN_WIDTH - view.getWidth() - MARGIN;
                        int bottom = SCREEN_HEIGHT - view.getHeight() - MARGIN;
                        if(x<MARGIN) x=MARGIN;  // Left     Limit
                        int top = MARGIN + TOP_ICON_HEIGHT + gap;
                        if(y<top) y=top;        // Top      Limit
                        if(x>right) x=right;    // Right    Limit
                        if(y>bottom) y=bottom;  // Bottom   Limit
                        TOP.animate().x(x+((START_BUTTON_WIDTH - TOP_ICON_WIDTH)/2)).y(y-gap-TOP_ICON_HEIGHT).setDuration(0).start();
                        view.animate().x(x).y(y).setDuration(0).start();
                        DIM.animate().x(x+gapX).y(y+gapY).setDuration(0).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Math.abs(rawX-event.getRawX())<BOUNDARY&&Math.abs(rawY-event.getRawY())<BOUNDARY||START_BUTTON.isChecked())
                            {
                            // 클릭이벤트 발생 false;
                            return false;
                            }
                        else{
                        return true;
                        }
                    }
                return false;
                }
            });

        DIM.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view)
            {
            if(START_BUTTON.isChecked()) START_BUTTON.performClick();
            }
        });

        ((Activity)CONTEXT).getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener()
            {
            @Override
            public void onSystemUiVisibilityChange(int visibility)
                {
//                RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//                setLayoutParams(param);
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                SCREEN_HEIGHT = displayMetrics.heightPixels - getStatusBarHeight();
//
////                TOP.animate().x(x+((START_BUTTON_WIDTH - TOP_ICON_WIDTH)/2)).y(y-gap-TOP_ICON_HEIGHT).setDuration(0).start();
//                START_BUTTON.animate().x(SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN).y(SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN).setDuration(0).start();
//
//
//                if (visibility == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//                    {
//                    // 네비게이션바 투명
//                    BOTTOM_HEIGHT = 0;
//                    }
//                else if (visibility == View.SYSTEM_UI_FLAG_VISIBLE)
//                    {
//                    // 네비게이션바 보이기
//                    BOTTOM_HEIGHT = NAVIGATION_BAR_HEIGHT;
//                    }
//
//                // 스타트버튼 및 버튼들 위치 리프레쉬

//                TOP.animate()
//                    .x(SCREEN_WIDTH-MARGIN-TOP_ICON_WIDTH-((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2))
//                    .y(SCREEN_HEIGHT-MARGIN-TOP_ICON_WIDTH-START_BUTTON_HEIGHT-gap)
//                    .setDuration(0).start();

//                int[] location = new int[2];
//                START_BUTTON.getLocationOnScreen(location);
//                TOP.animate()
//                        .x(location[0]+((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2))
//                        .y(location[1]-(START_BUTTON_WIDTH/2)-gap-TOP_ICON_WIDTH)
//                        .setDuration(0).start();
//                TOP.bringToFront();

                }
            });
        }

    Handler show_top_button = new Handler()
        {
        @Override
        public void handleMessage(Message msg)
            {
            super.handleMessage(msg);

            if(START_BUTTON.isChecked())
                {
                removeView(TOP);
                }
            else{
                removeView(TOP);
                addView(TOP);
                int[] location = new int[2];
                START_BUTTON.getLocationOnScreen(location);
                TOP.animate()
                        .x(location[0]+((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2))
                        .y(location[1]-(START_BUTTON_WIDTH/2)-gap-TOP_ICON_WIDTH)
                        .setDuration(0).start();
                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                TOP.startAnimation(anim_show);

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

    private LayoutParams getLayoutParamRightBottom()
        {
        LayoutParams param_icon = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param_icon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param_icon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return param_icon;
        }
    private LayoutParams getLayoutParamRight()
        {
        LayoutParams param_icon = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param_icon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return param_icon;
        }

    TextView topl;
    TextView[]labels;
    private void showButtons(boolean isShow)
        {
        if(isShow)
            {
            LinearLayout vertical = new LinearLayout(CONTEXT);
            vertical.setBackgroundColor(Color.parseColor("#99637262"));
            vertical.setOrientation(LinearLayout.VERTICAL);

            int[] location = new int[2];
            START_BUTTON.getLocationOnScreen(location);
            labels = new TextView[buttons.size()];

            for(int i=0;i<buttons.size();i++)
                {
                Data data = buttons.get(i);
                // 아이콘
                if(data.ICON==null)
                    {
                    data.BUTTON.setImageResource(data.IMAGE);
                    }
                else{
                data.BUTTON.setImageBitmap(data.ICON);
                }

                LayoutParams param_icon = getRelativeLayoutParam();

                param_icon.width = (int)getResources().getDimension(R.dimen.dp40);
                param_icon.height = (int)getResources().getDimension(R.dimen.dp40);
                data.BUTTON.setLayoutParams(param_icon);;

                data.BUTTON.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addView(data.BUTTON);
//                data.BUTTON.animate().x(TOP_X).y(SCREEN_HEIGHT-(START_Y + ((TOP_ICON_HEIGHT+gap) * i))).setDuration(0).start();
                Log.i("####", "#### 스크린 하이트 : "+(START_BUTTON_HEIGHT+MARGIN));
                data.BUTTON.animate().x(TOP_X).y(SCREEN_HEIGHT-(START_BUTTON_HEIGHT + MARGIN + ((TOP_ICON_HEIGHT+gap) * (i+1)))).setDuration(0).start();
                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                anim_show.setStartOffset(70*i);
                data.BUTTON.startAnimation(anim_show);

                // 라벨
                labels[i] = new TextView(CONTEXT);
                labels[i].setText(data.NAME+"  ");
                labels[i].setTextColor(Color.WHITE);
                labels[i].measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                int ditance = (int)getResources().getDimension(R.dimen.dp150);
                LayoutParams param_label = new LayoutParams(ditance, LayoutParams.WRAP_CONTENT);
                labels[i].setGravity(Gravity.RIGHT);
                labels[i].setLayoutParams(param_label);
                addView(labels[i]);
                labels[i].animate().x(TOP_X -ditance).y(((TOP_ICON_HEIGHT-labels[i].getMeasuredHeight())/2)+SCREEN_HEIGHT-(START_BUTTON_HEIGHT + MARGIN + ((TOP_ICON_HEIGHT+gap) * (i+1)))).setDuration(0).start();
                labels[i].startAnimation(anim_show);
                }

            DIM.animate().scaleX(80f).scaleY(80f).alpha(1f).setDuration(DURATION).start();
            }
        else{
        for(int i=0;i<buttons.size();i++)
            {
            final int reverse = buttons.size()-1-i;
            Animation anim_hide = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_hide);
            anim_hide.setStartOffset(70*i);
            anim_hide.setAnimationListener(new Animation.AnimationListener()
                {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation)
                    {
                    removeView(buttons.get(reverse).BUTTON);
                    }
                @Override
                public void onAnimationRepeat(Animation animation) {}
                });
            buttons.get(reverse).BUTTON.startAnimation(anim_hide);
            removeView(labels[reverse]);
            }
        int time = buttons.size() * 80 + 200;
        removeviews.sendEmptyMessageDelayed(0, time);
        DIM.animate().scaleX(1f).scaleY(1f).alpha(0f).setDuration(DURATION).start();
        }
        }

    Handler removeviews = new Handler()
        {
        @Override
        public void handleMessage(Message msg)
            {
            super.handleMessage(msg);
            for(int i=0;i<buttons.size();i++)
                {
                if(buttons.get(i).BUTTON!=null) removeView(buttons.get(i).BUTTON);
                if(labels[i]!=null)             removeView(labels[i]);
                }
            }
        };

    private int getStatusBarHeight()
        {
        int height = 0;
        int idStatusBarHeight = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0)
            {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
            }
        return height;
        }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
        {
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

    public void clickStart(boolean bool)
        {
        START_BUTTON.setChecked(false);
        }
    public void clearTopIcon()
        {
        removeView(TOP);
        }

    public void resetLocation()
        {
        removeView(TOP);
        LayoutParams param_icon = getLayoutParamRightBottom();
        param_icon.rightMargin = MARGIN + ((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2);
        param_icon.bottomMargin = START_BUTTON_HEIGHT + gap + MARGIN;
        if(TOP!=null)
            {
            TOP.setLayoutParams(param_icon);
            addView(TOP);
            }
        START_BUTTON.setVisibility(View.VISIBLE);
        START_BUTTON.animate().x(SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN).y(SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN).setDuration(0).start();
        START_BUTTON.invalidate();
        }

    }