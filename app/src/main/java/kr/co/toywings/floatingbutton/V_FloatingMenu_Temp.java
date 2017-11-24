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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by schneider on 2017. 11. 6..
 */

public class V_FloatingMenu_Temp extends RelativeLayout implements View.OnTouchListener
    {
    public Context CONTEXT;
    public ArrayList<Data> buttons;

    public ImageView DIM;
    public V_CheckImageView START_BUTTON;
    public ImageView TOP;

    public final int MARGIN = 50;      // 버튼 바깥여백
    public final int BOUNDARY = 30;    // 터치,클릭 구분 범위
    public final float ANGLE = 135f;   // 버튼회전각도
    public final int DURATION = 500;   // 버튼회전시간

    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;
    public float dX, dY;
    public float rawX, rawY;
    public int gapX, gapY;
    public int gap;
    public int START_BUTTON_WIDTH;
    public int START_BUTTON_HEIGHT;
    public int TOP_ICON_WIDTH;
    public int TOP_ICON_HEIGHT;
    public int NAVIGATION_BAR_HEIGHT;
    public int 스타트버튼_X좌표;
    public int 스타트버튼_Y좌표;
    public int 탑버튼_X좌표;
    public int 탑버튼_Y좌표;

    public V_FloatingMenu_Temp(Context context)
        {
        super(context);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu_Temp(Context context, AttributeSet attrs)
        {
        super(context, attrs);
        CONTEXT = context;
        init();
        }

    public V_FloatingMenu_Temp(Context context, AttributeSet attrs, int defStyleAttr)
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
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        Log.i("####", "스크린 높이 : "+SCREEN_HEIGHT);
        gap = (int)getResources().getDimension(R.dimen.dp10);
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            {
            NAVIGATION_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId);
            }

        initViews();
        setListeners();
        addView(DIM);
        }

    private void initViews()
        {
        LayoutParams param_dim = new LayoutParams(50, 50);
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(Color.parseColor("#b3000000"));
        DIM = new ImageView(CONTEXT);
        DIM.setImageDrawable(gd);
        DIM.setLayoutParams(param_dim);
        DIM.setAlpha(0f);
        START_BUTTON = new V_CheckImageView(CONTEXT);
        START_BUTTON.setImageResource(R.drawable.floating_home);
        START_BUTTON.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        DIM.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        START_BUTTON_WIDTH = START_BUTTON.getMeasuredWidth();
        START_BUTTON_HEIGHT = START_BUTTON.getMeasuredHeight();
        gapX = (START_BUTTON_WIDTH-50)/2;
        gapY = (START_BUTTON_HEIGHT-50)/2;
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

        addView(TOP);
        addView(START_BUTTON);
        스타트버튼_X좌표 = SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN;
        스타트버튼_Y좌표 = SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN-NAVIGATION_BAR_HEIGHT;
        START_BUTTON.animate().x(스타트버튼_X좌표).y(스타트버튼_Y좌표).setDuration(0).start();
        탑버튼_X좌표 = 스타트버튼_X좌표+((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2);
        탑버튼_Y좌표 = 스타트버튼_Y좌표-TOP_ICON_HEIGHT-gap;
        TOP.animate().x(탑버튼_X좌표).y(스타트버튼_Y좌표-TOP_ICON_HEIGHT-gap).setDuration(0).start();
        }

    private void setListeners()
        {
//        START_BUTTON.setOnClickListener(new OnClickListener()
//            {
//            @Override
//            public void onClick(View view)
//                {
//                Toast.makeText(CONTEXT, "스타트버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
//                if(buttons==null||buttons.size()==0) Toast.makeText(CONTEXT, "리스트가 없습니다.", Toast.LENGTH_SHORT).show();
//                START_BUTTON.setEnabled(false);
//                new Handler().postDelayed(new Runnable()
//                    {
//                    @Override
//                    public void run()
//                        {
//                        START_BUTTON.setEnabled(true);
//                        }
//                    }, 1500);
//                START_BUTTON.toggle();
//                ViewPropertyAnimator vpa = view.animate();
//                if(START_BUTTON.isChecked())
//                    {
//                    vpa.rotation(ANGLE); // 한번돌아간 상태면 안돌림 By는 현재상태에서 돌림
//                    vpa.setInterpolator(new AnticipateInterpolator());
//
//                    START_BUTTON.setAlpha(0f);
//                    START_BUTTON.animate().x(SCREEN_WIDTH-START_BUTTON_WIDTH-MARGIN).y(SCREEN_HEIGHT-START_BUTTON_HEIGHT-MARGIN).setDuration(0).start();
//                    START_BUTTON.animate().alpha(1f).setDuration(300).start();
//
//                    removeView(TOP);
//                    }
//                else{
//                    vpa.rotationBy(-ANGLE);
//                    vpa.setInterpolator(new OvershootInterpolator());
//                    show_top_button.sendEmptyMessageDelayed(0, 500);
//                    }
//
//                showButtons(START_BUTTON.isChecked());
//                vpa.setDuration(DURATION);
//                vpa.start();
//                }
//            });

        // 스타트버튼 터치이동
        START_BUTTON.setOnTouchListener(new OnTouchListener()
            {
            @Override
            public boolean onTouch(View view, MotionEvent event)
                {
                Log.i("####", "22324");
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

//        DIM.setOnClickListener(new OnClickListener()
//            {
//            @Override
//            public void onClick(View view)
//                {
//                if(START_BUTTON.isChecked()) START_BUTTON.performClick();
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
                removeView(TOP);
                }
            else{
                removeView(TOP);
                addView(TOP);

                int[] location = new int[2];
                START_BUTTON.getLocationOnScreen(location);
                int posiy = location[1]-(START_BUTTON_WIDTH/2)-gap-TOP_ICON_WIDTH;
                TOP.animate()
                        .x(location[0]+((START_BUTTON_WIDTH-TOP_ICON_WIDTH)/2))
                        .y(posiy)
                        .setDuration(0).start();

                Log.i("####", posiy+"좌표");

                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                TOP.startAnimation(anim_show);

            }
            }
        };

    private LayoutParams getRelativeLayoutParam()
        {
        LayoutParams param_icon = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        return param_icon;
        }

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
                param_icon.rightMargin = MARGIN + ((START_BUTTON_WIDTH - param_icon.width)/2);

//                param_icon.bottomMargin = NAVIGATION_BAR_HEIGHT + start_button_margin_y + gap + START_BUTTON_HEIGHT + ((TOP_ICON_HEIGHT+gap) * i);
//                if(C.MAIN.NOW_FRAGMENT == MainActivity.FN.WEBVIEW) param_icon.bottomMargin = start_button_margin_y + gap + START_BUTTON_HEIGHT + ((TOP_ICON_HEIGHT+gap) * i);
//                else param_icon.bottomMargin = NAVIGATION_BAR_HEIGHT + start_button_margin_y + gap + START_BUTTON_HEIGHT + ((TOP_ICON_HEIGHT+gap) * i);

                param_icon.bottomMargin = START_BUTTON_HEIGHT + gap + MARGIN + ((TOP_ICON_HEIGHT+gap) * i);

                data.BUTTON.setLayoutParams(param_icon);
                data.BUTTON.setScaleType(ImageView.ScaleType.FIT_CENTER);
                addView(data.BUTTON);
                Animation anim_show = AnimationUtils.loadAnimation(CONTEXT, R.anim.floating_show);
                anim_show.setStartOffset(70*i);
                data.BUTTON.startAnimation(anim_show);

                // 라벨
                labels[i] = new TextView(CONTEXT);
                labels[i].setText(data.NAME);
                labels[i].setTextColor(Color.WHITE);
                labels[i].measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                LayoutParams param_label = getRelativeLayoutParam();
//                param_label.rightMargin = param_icon.rightMargin + TOP_ICON_WIDTH + gap;
//                param_label.bottomMargin = param_icon.bottomMargin + ((TOP_ICON_HEIGHT-labels[i].getMeasuredHeight())/2);
                labels[i].setLayoutParams(param_label);
                addView(labels[i]);
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

    }