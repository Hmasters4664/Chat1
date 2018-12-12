package com.example.olivier.businessapp.views;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);

    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        init();
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getAttributes(attrs);
        init();
    }

    private void getAttributes(AttributeSet attrs) {

    }
    protected void init() {
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }

}