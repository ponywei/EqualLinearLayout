package com.example.pony.equallinearlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * desc : 等分Layout
 * author : WeiHao
 * email : ponywei@foxmail.com
 * version : xx
 * date : 17/1/12
 */

public class EqualLinearLayout extends ViewGroup {
	private static final int LAYOUT_SPAN_COUNT_DEFAULT = 4;
	private static final int LAYOUT_SPAN_MARGIN_DEFAULT = 15;
	private static final int LAYOUT_LINE_MARGIN_DEFAULT = 15;

	//Layout Attributes
	//每行子视图个数
	private int spanCount;
	//子视图间距
	private int spanMargin;
	//行间距
	private int lineMargin;

	//平均子视图宽度
	private int avgChildViewWidth;

	public EqualLinearLayout(Context context) {
		super(context);
	}

	public EqualLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public EqualLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	private void init(AttributeSet attributeSet) {
		TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.EqualLinearLayout);
		try {
			spanCount = array.getInteger(R.styleable.EqualLinearLayout_ellSpanCount, LAYOUT_SPAN_COUNT_DEFAULT);
			spanMargin = array.getDimensionPixelSize(R.styleable.EqualLinearLayout_ellSpanMargin, UIUtils.dp2px(getContext(),
					LAYOUT_SPAN_MARGIN_DEFAULT));
			lineMargin = array.getDimensionPixelSize(R.styleable.EqualLinearLayout_ellLineMargin, UIUtils.dp2px(getContext(),
					LAYOUT_LINE_MARGIN_DEFAULT));
		} finally {
			array.recycle();
		}

		if (spanCount == 0) {
			throw new IllegalArgumentException("Span Count must NOT be ZERO!");
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);


		int childCount = getChildCount();
		int height = getPaddingTop() + getPaddingBottom();

		int lineCount = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
		if (lineCount == 0 || childCount == 0) {
			return;
		}

		//平均每个ChildView的宽度
		avgChildViewWidth = (widthSize - getPaddingLeft() - getPaddingRight() - (spanCount - 1) * spanMargin) / spanCount;

		//逐个Measure Child View
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			LayoutParams params = childView.getLayoutParams();
			measureChild(childView,
					getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), avgChildViewWidth + getPaddingLeft() +
                            getPaddingRight()),
					getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), params.height));
		}

		//总高度
		height += getChildAt(0).getMeasuredHeight() * lineCount + lineMargin * (lineCount - 1);

		setMeasuredDimension(widthSize, (heightMode == MeasureSpec.EXACTLY) ? heightSize : height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int leftPosition;
		int topPosition;
		int rightPosition;
		int bottomPosition;

		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);

			leftPosition = getPaddingLeft() + (i % spanCount) * (avgChildViewWidth + spanMargin);
			topPosition = getPaddingTop() + (i / spanCount) * (childView.getMeasuredHeight() + lineMargin);
			rightPosition = leftPosition + avgChildViewWidth;
			bottomPosition = topPosition + childView.getMeasuredHeight();

			childView.layout(leftPosition, topPosition, rightPosition, bottomPosition);
		}
	}
}
