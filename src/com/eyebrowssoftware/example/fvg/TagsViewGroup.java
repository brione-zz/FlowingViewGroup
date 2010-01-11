/**
 * 
 */
package com.eyebrowssoftware.example.fvg;

import java.util.Stack;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * @author Brion Emde
 *
 */
public class TagsViewGroup extends ViewGroup {
	@SuppressWarnings("unused")
	private static final String TAG = "TagsAdapterView";
	
	private static final int MIN_ROWS = 2;
	
	private ListAdapter mAdapter;
	private int mHorizontalSpacing = 0;
	private int mVerticalSpacing = 0;
	private int mViewHeight = -1;
	private int mPaddingTop;
	private int mPaddingBottom;
	private int mPaddingLeft;
	private int mPaddingRight;
	private int mMinRows;

	
	private ViewGroup.MarginLayoutParams params = 
		new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); 

	public TagsViewGroup(Context context) {
		super(context);
		initTagsViewGroup();
	}
	
	public TagsViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTagsViewGroup();
		initFromAttributes(context, attrs, R.style.tags);
	}

	public TagsViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initTagsViewGroup();
		initFromAttributes(context, attrs, defStyle);
	}
	
	public void initTagsViewGroup() {
	}
	
	public void initFromAttributes(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagsViewGroup, 0, defStyle);

		final int N = a.getIndexCount();
		for(int i = 0; i < N; ++i) {
			int attr = a.getIndex(i);
			int value;
			switch (attr) {
			case R.styleable.TagsViewGroup_horizontalSpacing:
				value = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_horizontalSpacing, mHorizontalSpacing);
				if(value > 0)
					setHorizontalSpacing(value);
				break;
			case R.styleable.TagsViewGroup_verticalSpacing:
				value = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_verticalSpacing, mVerticalSpacing);
				if(value > 0)
					setVerticalSpacing(value);
				break;
			case R.styleable.TagsViewGroup_minRows:
				value = a.getInt(R.styleable.TagsViewGroup_minRows, MIN_ROWS);
				if(value > 0)
					setMinimumRows(value);
				break;
			default:
				break;
			}
		}
		
		a.recycle();
	}
	
	public void setHorizontalSpacing(int hSpacing) {
		if(hSpacing < 0)
			hSpacing = 0;
		mHorizontalSpacing = hSpacing;
	}
	
	public void setVerticalSpacing(int vSpacing) {
		if(vSpacing < 0)
			vSpacing = 0;
		mVerticalSpacing = vSpacing;
	}
	
	public void setMinimumRows(int minRows) {
		if(minRows > 0)
			mMinRows = minRows;
	}
	
	public ListAdapter getAdapter() {
		return mAdapter;
	}
	
	private Stack<View> tvs = new Stack<View>();

	public void setAdapter(ListAdapter adapter) {
		if((mAdapter = adapter) == null) return;
		
		// Cache any pre-existing children, from being recycled.
		int ccount = this.getChildCount(); 
		for(int i = 0; i < ccount; i++) {
			tvs.push(this.getChildAt(i));
		}
		removeAllViews();
		int items = mAdapter.getCount();
		// Log.d(TAG, "Adapter count: " + String.valueOf(items));
		for(int i = 0; i < items; ++i) {
			View tv = mAdapter.getView(i, (tvs.empty()) ? null : tvs.pop(), this);
			if(mViewHeight < 0) {
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				mViewHeight = tv.getMeasuredHeight();
			}
			this.addView(tv, i, params);
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		// Log.d(TAG, "-----------------------------------------");
		// Log.d(TAG, "Measure Dimensions: widthSize = " + String.valueOf(widthSize) 
		//		+ " heightSize: " + String.valueOf(heightSize));

		
		mPaddingTop = getPaddingTop();
		mPaddingBottom = getPaddingBottom();
		mPaddingLeft = getPaddingLeft();
		mPaddingRight = getPaddingRight();
		
		// Log.d(TAG, "Measure Padding: Top = " + String.valueOf(mPaddingTop) + "Bottom: " + String.valueOf(mPaddingBottom) 
		// 		+ " Left = " + String.valueOf(mPaddingLeft) + " Right = " + String.valueOf(mPaddingRight));
		
		int measuredHeight = 0;
		int measuredWidth= 0;
		int children_height = 0;
		int children_width = 0;
		
		int suggestedMinWidth = this.getSuggestedMinimumWidth();
		int suggestedMinHeight = this.getSuggestedMinimumHeight();

		@SuppressWarnings("unused")
		int height = 0;
		int width = 0;
		
		if(widthMode == MeasureSpec.UNSPECIFIED) {
			// Log.d(TAG, "Unspecified");
			// we'll have a single row of tags, respecing the padding.
			int ccount = this.getChildCount();
			for(int i = 0; i < ccount; ++i) {
				View tv = this.getChildAt(i);
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				height = tv.getMeasuredHeight();
				width = tv.getMeasuredWidth();
				// Log.d(TAG, "Unspecified Simple: width = " + String.valueOf(width) + " height: " + String.valueOf(height));
				children_width += tv.getMeasuredWidth();
				if((i+1) < ccount)
					children_width += mHorizontalSpacing;
			}
			// Log.d(TAG, "-----------------------------------------");
			measuredHeight = mPaddingTop + mViewHeight + mPaddingBottom;
			measuredWidth = children_width + mPaddingLeft + mPaddingRight;
		} else {	// MeasureSpec.AT_MOST or MeasureSpec.EXACTLY 
			// Log.d(TAG, "Other");
			measuredWidth = widthSize; // use the max allowed
			children_width = widthSize - mPaddingLeft - mPaddingRight;
			int pos = 0;
			int row = 0;
			int ccount = this.getChildCount();
			for(int i = 0; i < ccount; ++i) {
				View tv = this.getChildAt(i);
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				height = tv.getMeasuredHeight();
				width = tv.getMeasuredWidth();
				// Log.d(TAG, "Other Simple: width = " + String.valueOf(width) + " height: " + String.valueOf(height));
				if(pos + width + mHorizontalSpacing >= children_width) {
					// Log.d(TAG, "Starting new row, next_post = " + String.valueOf(pos));
					pos = 0;
					++row; 
				}
				pos += width + mHorizontalSpacing;
				// Log.d(TAG, "Extending row, next_post = " + String.valueOf(pos));
			}
			// Log.d(TAG, "-----------------------------------------");
			
			int rows_written = ++row;
			switch(heightMode){
			case MeasureSpec.AT_MOST:
				// Log.d(TAG, "heightMode: AT_MOST");
				children_height = rows_written * (mViewHeight + mVerticalSpacing) - mVerticalSpacing;
				measuredHeight = children_height + mPaddingTop + mPaddingBottom;
				measuredHeight = (measuredHeight > heightSize) ? heightSize : measuredHeight;
				break;
			case MeasureSpec.EXACTLY:
				// Log.d(TAG, "heightMode: EXACTLY");
				measuredHeight = heightSize;
				break;
			case MeasureSpec.UNSPECIFIED:
				// Log.d(TAG, "heightMode: UNSPECIFIED");
				if(rows_written < mMinRows)
					rows_written = mMinRows;
				children_height = rows_written * (mViewHeight + mVerticalSpacing) - mVerticalSpacing;
				measuredHeight = children_height + mPaddingTop + mPaddingBottom;
				break;
			default:
				break;
			}
		}
		if(measuredHeight < suggestedMinHeight)
			measuredHeight = suggestedMinHeight;
		if(measuredWidth < suggestedMinWidth)
			measuredWidth = suggestedMinWidth;
		setMeasuredDimension(measuredWidth, measuredHeight);
		// Log.d(TAG, "Measurement: width = " + String.valueOf(measuredWidth) + " height: " + String.valueOf(measuredHeight));
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		// Log.d(TAG, "Layout Dimensions: Top = " + String.valueOf(t) + "Bottom: " + String.valueOf(b) 
		//		+ " Left = " + String.valueOf(l) + " Right = " + String.valueOf(r));

		// Log.d(TAG, "Layout Padding: Top = " + String.valueOf(mPaddingTop) + "Bottom: " + String.valueOf(mPaddingBottom) 
		//		+ " Left = " + String.valueOf(mPaddingLeft) + " Right = " + String.valueOf(mPaddingRight));
		
		int pos = mPaddingLeft;
		int row = 0;
		r -= mPaddingRight;
		for(int i = 0; i < this.getChildCount(); ++i) {
			View tv = this.getChildAt(i);
			int width = tv.getMeasuredWidth();
			int height = tv.getMeasuredHeight();
			if((pos + width + mHorizontalSpacing) >= r) {
				pos = mPaddingLeft;
				++row; 
			}
			int child_top = mPaddingTop + row * (mViewHeight + mVerticalSpacing);
			tv.layout(pos, child_top, pos + width, child_top + height);
			// Log.d(TAG, "child: Top = " + String.valueOf(child_top) + "Bottom: " + String.valueOf(child_top+height) 
			//		+ " Left = " + String.valueOf(pos) + " Right = " + String.valueOf(pos + width));
			pos += width + mHorizontalSpacing;
		}
	}
}
