/**
 * 
 */
package com.eyebrowssoftware.example.fvg;

import junit.framework.Assert;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * @author Brion Emde
 *
 */
public class TagsViewGroup extends ViewGroup {
	private static final String TAG = "TagsAdapterView";
	
	private static final int MIN_ROWS = 2;
	
	private ListAdapter mAdapter;
	private int mHorizontalSpacing = 0;
	private int mVerticalSpacing = 0;
	private int mTextHeight = -1;
	private int mTopPadding;
	private int mBottomPadding;
	private int mLeftPadding;
	private int mRightPadding;
	private int mMinRows;

	
	private static final ViewGroup.LayoutParams PARAMS = 
		new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); 

	public TagsViewGroup(Context context) {
		super(context);
	}
	
	public TagsViewGroup(Context context, AttributeSet attrs) {
		
		this(context, attrs, R.style.tags);
	}

	public TagsViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagsViewGroup, 0, defStyle);
		int hSpacing = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_horizontalSpacing, mHorizontalSpacing);
		setHorizontalSpacing(hSpacing);
		int vSpacing = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_verticalSpacing, mVerticalSpacing);
		setVerticalSpacing(vSpacing);
		int padding = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_padding, 0);
		if(padding > 0) {
			mTopPadding = mBottomPadding = mRightPadding = mLeftPadding = padding;
		}
		padding = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_paddingLeft, 0);
		if(padding > 0) {
			mLeftPadding = padding;
		}
		padding = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_paddingRight, 0);
		if(padding > 0) {
			mRightPadding = padding;
		}
		padding = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_paddingTop, 0);
		if(padding > 0) {
			mTopPadding = padding;
		}
		padding = a.getDimensionPixelOffset(R.styleable.TagsViewGroup_paddingBottom, 0);
		if(padding > 0) {
			mBottomPadding = padding;
		}
		this.setPadding(mLeftPadding, mTopPadding, mRightPadding, mBottomPadding);

		padding = a.getInt(R.styleable.TagsViewGroup_minRows, MIN_ROWS);
		if(padding > 0) {
			mMinRows = padding;
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

	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		
		this.removeAllViewsInLayout();
		if(mAdapter == null) return ;
		
		int items = mAdapter.getCount();
		// Log.d(TAG, "Adapter count: " + String.valueOf(items));
		for(int i = 0; i < items; ++i) {
			TextView context = null;
			TextView tv = (TextView) mAdapter.getView(i, context, this);
			if(mTextHeight < 0) {
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				mTextHeight = tv.getMeasuredHeight();
			}
			Assert.assertTrue(this.addViewInLayout(tv, i, PARAMS, true));
		}
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int measuredHeight = 0;
		int measuredWidth= 0;
		int children_height = 0;
		int children_width = 0;
		
		int suggestedMinWidth = this.getSuggestedMinimumWidth();
		int suggestedMinHeight = this.getSuggestedMinimumHeight();
		
		if(widthMode == MeasureSpec.UNSPECIFIED) {
			// we'll have a single row of tags, respecing the padding.
			int ccount = this.getChildCount();
			for(int i = 0; i < ccount; ++i) {
				TextView tv = (TextView) this.getChildAt(i);
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				children_width += tv.getMeasuredWidth();
				if((i+1) < ccount)
					children_width += mHorizontalSpacing;
			}
			measuredHeight = mTopPadding + mTextHeight + mBottomPadding;
			measuredWidth = children_width + mLeftPadding + mRightPadding;
		} else {	// MeasureSpec.AT_MOST or MeasureSpec.EXACTLY 
			measuredWidth = widthSize; // use the max allowed
			int right = widthSize - mRightPadding -1; 
			int next_pos = mLeftPadding;
			int row = 0;
			int width = 0;
			int ccount = this.getChildCount();
			for(int i = 0; i < ccount; ++i) {
				TextView tv = (TextView) this.getChildAt(i);
				tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				width = tv.getMeasuredWidth();
				if(next_pos + width >= right) {
					next_pos = mLeftPadding;
					++row; 
				}
				next_pos += width + mHorizontalSpacing;
			}
			int rows_written = ++row;
			switch(heightMode){
			case MeasureSpec.AT_MOST:
				// Log.d(TAG, "heightMode: AT_MOST");
				children_height = rows_written * (mTextHeight + mVerticalSpacing) - mVerticalSpacing;
				measuredHeight = children_height + mTopPadding + mBottomPadding;
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
				children_height = row * (mTextHeight + mVerticalSpacing) - mVerticalSpacing;
				measuredHeight = children_height + mTopPadding + mBottomPadding;
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
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int pos = mLeftPadding;
		int row = 0;
		r -= mRightPadding;
		for(int i = 0; i < this.getChildCount(); ++i) {
			TextView tv = (TextView) this.getChildAt(i);
			int width = tv.getMeasuredWidth();
			if((pos + width) >= r) {
				pos = mLeftPadding;
				++row; 
			}
			int child_top = mTopPadding + row * (mTextHeight + mVerticalSpacing);
			tv.layout(pos, child_top, pos + width, child_top + mTextHeight);
			pos += width + mHorizontalSpacing;
		}
	}
}
