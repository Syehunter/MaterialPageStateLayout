package z.sye.space.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

import z.sye.space.loadingviewlibrary.R;

/**
 * Created by Syehunter on 16/1/8.
 */
public class MaterialProgress extends View {

    private int mStartColor = 0xFF5588FF;
    private int mMidColor = 0xFFEE8834;
    private int mEndColor = 0xFF773355;
    private int mRadius = 30;
    private int mStrokeWidth = 5;

    private Paint mGapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ArrayList<Integer> mColors = new ArrayList<>();
    private int mColorIndex = 0;
    private boolean mColorChange = true;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private float mProgress = 0;
    private float mSpeed = 230f;
    private long mLastTimeAnimated = 0;
    private double mTimeStartUpdating = 0;
    private double mBarSpinningTime = 460;
    private float mBarExtraLength = 0;
    private boolean mBarSpinFromStart = true;
    private long mPausedWithoutUpdatingTime = 0;
    private final int mBarLength = 16;
    private final int mBarMaxLength = 270;
    private final long mPausedUpdateTime = 200;

    public MaterialProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttrs(context.obtainStyledAttributes(attrs, R.styleable.MaterialProgress));
    }

    public MaterialProgress(Context context) {
        super(context);
    }

    private void parseAttrs(TypedArray a) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        mStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mStrokeWidth, metrics);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mRadius, metrics);

        mStartColor = a.getColor(R.styleable.MaterialProgress_mp_startColor, mStartColor);
        mMidColor = a.getColor(R.styleable.MaterialProgress_mp_midColor, mMidColor);
        mEndColor = a.getColor(R.styleable.MaterialProgress_mp_endColor, mEndColor);
        mRadius = (int) a.getDimension(R.styleable.MaterialProgress_mp_radius, mRadius);
        mStrokeWidth = (int) a.getDimension(R.styleable.MaterialProgress_mp_strokeWidth, mStrokeWidth);
        mSpeed = a.getFloat(R.styleable.MaterialProgress_mp_speed, mSpeed);
        a.recycle();

        mColors.add(mStartColor);
        mColors.add(mMidColor);
        mColors.add(mEndColor);

        mLastTimeAnimated = SystemClock.uptimeMillis();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        mWidth = mRadius + mPaddingLeft + mPaddingRight;
        mHeight = mRadius + mPaddingTop + mPaddingBottom;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(mWidth, widthSize);
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(mHeight, heightSize);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(this.toString(), "[w]: " + w + " && [h]: " + h);

        initPaint();

        initRectF();

        invalidate();
    }

    private void initRectF() {
        //Calculate circleDiameter
        int minTemp = Math.min(
                mWidth - mPaddingLeft - mPaddingRight,
                mHeight - mPaddingTop - mPaddingBottom
        );
        int circleDiameter = Math.min(minTemp, (mRadius - mStrokeWidth) * 2);

        int xOffset = (mWidth - mPaddingLeft - mPaddingRight - circleDiameter) / 2 + mPaddingLeft;
        int yOffset = (mHeight - mPaddingTop - mPaddingBottom - circleDiameter) / 2 + mPaddingTop;

        mRectF = new RectF(xOffset + mStrokeWidth,
                yOffset + mStrokeWidth,
                xOffset + circleDiameter - mStrokeWidth,
                yOffset + circleDiameter - mStrokeWidth);
    }

    private void initPaint() {
        mGapPaint.setColor(0x00FFFFFF);
        mGapPaint.setStyle(Paint.Style.STROKE);
        mGapPaint.setStrokeWidth(mStrokeWidth);

        mBarPaint.setColor(0xFF5588FF);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, 360, 360, false, mGapPaint);
        long deltaTime = (SystemClock.uptimeMillis() - mLastTimeAnimated);
        float deltaNormalized = deltaTime * mSpeed / 1000.0f;

        updateBarLength(deltaTime);

        mProgress += deltaNormalized;
        if (mProgress > 360) {
            mProgress -= 360f;

        }
        mLastTimeAnimated = SystemClock.uptimeMillis();

        float from = mProgress - 90;
        float length = mBarLength + mBarExtraLength;

        if (isInEditMode()) {
            from = 0;
            length = 135;
        }


        if (length <16.5f) {
            if (mColorChange) {
                if (mColorIndex == mColors.size()) {
                    mColorIndex = 0;
                }
                mBarPaint.setColor(mColors.get(mColorIndex));
                mColorIndex++;
                mColorChange = !mColorChange;
            }
        } else {
            mColorChange = true;
        }

        canvas.drawArc(mRectF, from, length, false, mBarPaint);

        invalidate();
    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (mPausedWithoutUpdatingTime >= mPausedUpdateTime) {
            mTimeStartUpdating += deltaTimeInMilliSeconds;

            if (mTimeStartUpdating > mBarSpinningTime) {
                //Has spined a round
                mTimeStartUpdating -= mBarSpinningTime;
                mPausedWithoutUpdatingTime = 0;
                mBarSpinFromStart = !mBarSpinFromStart;
            }

            float distance =
                    (float) Math.cos((mTimeStartUpdating / mBarSpinningTime + 1) * Math.PI) / 2 + 0.5f;
            float destLength = (mBarMaxLength - mBarLength);

            if (mBarSpinFromStart) {
                mBarExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (mBarExtraLength - newLength);
                mBarExtraLength = newLength;
            }
        } else {
            mPausedWithoutUpdatingTime += deltaTimeInMilliSeconds;
        }
    }

    /**
     * Set single color for MaterialProgress if you want
     *
     * @param color
     */
    public void setColor(int color) {
        mColors.clear();
        mColors.add(color);
    }

    /**
     * Set the colors for MaterialProgress as your like
     *
     * @param colors
     */
    public void setColors(ArrayList<Integer> colors) {
        mColors.clear();
        mColors.addAll(colors);
    }

    public void reset() {
        mColorIndex = 0;
        mColorChange = true;
        mLastTimeAnimated = 0;
        mTimeStartUpdating = 0;
        mBarSpinningTime = 460;
        mBarExtraLength = 0;
        mBarSpinFromStart = true;
        mPausedWithoutUpdatingTime = 0;
        mProgress = 0;
    }
}
