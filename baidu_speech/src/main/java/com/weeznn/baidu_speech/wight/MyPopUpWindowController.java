package com.weeznn.baidu_speech.wight;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by weeznn on 2018/2/6.
 */

public class MyPopUpWindowController {
    private static final String TAG=MyPopUpWindowController.class.getSimpleName();

    private int layoutResId;
    private Context context;
    private PopupWindow popupWindow;
    View mPopUpView;
    private View mView;
    private Window window;

    MyPopUpWindowController(Context context,PopupWindow popupWindow){
        this.context=context;
        this.popupWindow=popupWindow;
    }

    /**
     * 设置布局
     * @param layoutResId
     */
    public void setView(int layoutResId){
        mView=null;
        this.layoutResId=layoutResId;
        installContent();
    }

    public void installContent() {
        if (layoutResId!=0){
            mPopUpView= LayoutInflater.from(context).inflate(layoutResId,null);
        }else if (mView!=null){
            popupWindow.setContentView(mView);
        }
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    /**
     * 设置宽高
     * @param width
     * @param height
     */
    public void setWidthAndHeight(int width,int height){
        if (width==0||height==0){
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }

    /**
     * 设置背景灰色程度
     * @param level
     */
    public void setBackGroundGreyLevel(float level){
        window=((Activity)context).getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        params.alpha=level;
        window.setAttributes(params);
    }

    /**
     * 设置动画类型
     * @param animationStype
     */
    public void setAnimationStyle(int animationStype){
        popupWindow.setAnimationStyle(animationStype);
    }

    /**
     * 设置outside是否可点击
     * @param touchable
     */
    public void setOutsideTouchAble(boolean touchable){
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置透明背景
        popupWindow.setOutsideTouchable(touchable);
        popupWindow.setFocusable(touchable);
    }

    static class PopUpParams{
        public int layoutResId;
        public Context context;
        public int mWidth,mHeigh;
        public boolean isShowBg,isShowAnim;
        public float bgGreyLevel;
        public int animationStyle;
        public View view;
        public boolean isTouchAble=false;

        public PopUpParams(Context context){
            this.context=context;
        }

        public void apply(MyPopUpWindowController controller){
            if (view!=null){
                controller.setView(view);
            }else if (layoutResId!=0){
                controller.setView(layoutResId);
            }else {
                throw new IllegalArgumentException("popupwindow's view is null");
            }
            controller.setWidthAndHeight(mWidth,mHeigh);
            controller.setOutsideTouchAble(isTouchAble);
            if (isShowBg){
                controller.setBackGroundGreyLevel(bgGreyLevel);
            }
            if (isShowAnim){
                controller.setAnimationStyle(animationStyle);
            }
        }
    }
}
