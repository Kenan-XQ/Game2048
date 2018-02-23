package newsimooc.imooc.com.game2048;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by user on 2017/10/27.
 */

public class Card extends FrameLayout {

    private TextView mTextView;
    private int mNum;

    public Card(Context context) {
        super(context);
        mTextView = new TextView(getContext());
        mTextView.setTextSize(32);
        mTextView.setBackgroundColor(0xffCCCCCC);
        //设置加粗
        //mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTextView.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(25, 25, 0, 0);
        addView(mTextView, lp);

        setNum(0);
    }

    public void setNum(int num) {
        this.mNum = num;
        if (num > 0) {
            mTextView.setText(num + "");
        } else {
            mTextView.setText("");
        }
        judgeFont(num);
        judgeColor(num);
    }

    public int getNum() {
        return mNum;
    }

    //卡片对应字体大小
    public void judgeFont(int num) {
        switch (num) {
            case 2:
                mTextView.setTextSize(38);
                break;
            case 4:
                mTextView.setTextSize(38);
                break;
            case 8:
                mTextView.setTextSize(38);
                break;
            case 16:
                mTextView.setTextSize(36);
                break;
            case 32:
                mTextView.setTextSize(36);
                break;
            case 64:
                mTextView.setTextSize(36);
                break;
            case 128:
                mTextView.setTextSize(34);
                break;
            case 256:
                mTextView.setTextSize(34);
                break;
            case 512:
                mTextView.setTextSize(34);
                break;
            case 1024:
                mTextView.setTextSize(32);
                break;
            case 2048:
                mTextView.setTextSize(32);
                break;
            case 4096:
                mTextView.setTextSize(32);
                break;
            case 8192:
                mTextView.setTextSize(32);
                break;
            case 16384:
                mTextView.setTextSize(28);
                break;
            case 32768:
                mTextView.setTextSize(28);
                break;
        }
    }

    //卡片配色
    public void judgeColor(int num) {
        switch (num) {
            case 0:
                mTextView.setBackgroundColor(0xffCCCCCC);
                break;
            case 2:
                mTextView.setBackgroundColor(0xffFDF5E6);
                break;
            case 4:
                mTextView.setBackgroundColor(0xffFFE4B5);
                break;
            case 8:
                mTextView.setBackgroundColor(0xffEEB422);
                break;
            case 16:
                mTextView.setBackgroundColor(0xffEE9A00);
                break;
            case 32:
                mTextView.setBackgroundColor(0xffEE7600);
                break;
            case 64:
                mTextView.setBackgroundColor(0xffEE4000);
                break;
            case 128:
                mTextView.setBackgroundColor(0xffC1FFC1);
                break;
            case 256:
                mTextView.setBackgroundColor(0xffC0FF3E);
                break;
            case 512:
                mTextView.setBackgroundColor(0xff43CD80);
                break;
            case 1024:
                mTextView.setBackgroundColor(0xff00EE00);
                break;
            case 2048:
                mTextView.setBackgroundColor(0xffFFFF00);
                break;
            case 4096:
                mTextView.setBackgroundColor(0xff87CEFA);
                break;
            case 8192:
                mTextView.setBackgroundColor(0xff836FFF);
                break;
            case 16384:
                mTextView.setBackgroundColor(0xff00FFFF);
                break;
            case 32768:
                mTextView.setBackgroundColor(0xff009ACD);
                break;
            case 65536:
                mTextView.setBackgroundColor(0xffFFFF00);
                break;

        }
    }

    /**
     * 设置数字合并时的动画效果
     */
    public void getCardAnimation() {
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 0.1f);
//        alphaAnimation.setDuration(100);
//        alphaAnimation.setRepeatCount(1);
//        //重复方式
//        alphaAnimation.setRepeatMode(Animation.REVERSE);
//        mTextView.startAnimation(alphaAnimation);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        mTextView.startAnimation(animation);
    }

    /**
     * 数字移动时的动作效果
     */
    public void getCardTranslateAnimation(float fromX, float toX, float fromY, float toY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        translateAnimation.setDuration(200);  //设置动画时间
        translateAnimation.setFillAfter(false);  //设置动画结束时会停留在当前位置
        //设置插值器，可以理解为用于改变运动形式的东西
        //（现在设置的运动形式类似于自由落体，会有弹跳效果）
        translateAnimation.setInterpolator(new BounceInterpolator());
        mTextView.startAnimation(translateAnimation);
    }



    public boolean equals(Card card) {
        return mNum == card.getNum();
    }
}
