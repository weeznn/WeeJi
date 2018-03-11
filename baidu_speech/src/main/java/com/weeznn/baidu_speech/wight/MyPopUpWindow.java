package com.weeznn.baidu_speech.wight;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by weeznn on 2018/2/6.
 * https://github.com/crazyqiang/AndroidStudy/blob/master/app/src/main/java/org/ninetripods/mq/study/popup/PopupWindow/CommonPopupWindow.java
 */

public class MyPopUpWindow extends PopupWindow {
    private static final String TAG=MyPopUpWindow.class.getSimpleName();
    private MyPopUpWindowController controller;

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        controller.setBackGroundGreyLevel(1.0f);
    }


    public interface ViewINterface{
        void getChildView(View view,int layoutResId);
    }

    private MyPopUpWindow(Context context){
        controller=new MyPopUpWindowController(context,this);
    }

    public static class Builder{
        private  MyPopUpWindowController.PopUpParams popUpParams;
        private ViewINterface listener;

        public Builder(Context context){
            popUpParams=new MyPopUpWindowController.PopUpParams(context);
        }

        /**
         * 设置布局
         * @param layoutResId
         * @return
         */
        public Builder setView(int layoutResId){
            popUpParams.view=null;
            popUpParams.layoutResId=layoutResId;
            return this;
        }
        public Builder setView(View view){
            popUpParams.view=view;
            popUpParams.layoutResId=0;
            return this;
        }

        /**
         * 设置事件监听
         * @param listener
         * @return
         */
        public Builder setViewOnclickListener(ViewINterface listener){
            this.listener=listener;
            return this;
        }

        /**
         * 设置宽高,默认wrap_content
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width,int height){
            popUpParams.mHeigh=height;
            popUpParams.mWidth=width;
            return this;
        }

        /**
         * 设置背景灰色程度
         * @param level
         * @return
         */
        public Builder setBackGroundGreyLevel(float level){
            popUpParams.isShowBg=true;
            popUpParams.bgGreyLevel=level;
            return this;
        }

        /**
         * 设置outside是否可点击
         * @param touchable
         * @return
         */
        public Builder setOutsideTouchAble(boolean touchable ){
            popUpParams.isTouchAble=true;
            return this;
        }

        /**
         * 设置动画
         * @param animationType
         * @return
         */
        public Builder setAnimationType(int animationType){
            popUpParams.isShowAnim=true;
            popUpParams.animationStyle=animationType;
            return this;
        }
        public MyPopUpWindow creat(){
            final MyPopUpWindow popUpWindow=new MyPopUpWindow(popUpParams.context);
            popUpParams.apply(popUpWindow.controller);
            if (listener!=null&&popUpParams.layoutResId!=0){
                listener.getChildView(popUpWindow.controller.mPopUpView,popUpParams.layoutResId);
            }
            int widthMeasureSpec=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
            popUpWindow.controller.mPopUpView.measure(widthMeasureSpec,heightMeasureSpec);
            return popUpWindow;
        }
    }

}
