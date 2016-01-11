package z.sye.space.library;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import z.sye.space.library.utils.DimenUtils;
import z.sye.space.loadingviewlibrary.R;
import z.sye.space.library.utils.PageState;
import z.sye.space.library.widget.MaterialProgress;
import z.sye.space.library.widget.RippleView;

/**
 * Created by Syehunter on 16/1/9.
 */
public class PageStateLayout extends FrameLayout implements PageStateListener {

    private Context mContext;

    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mSucceedView;
    private MaterialProgress mLoading;

    public PageStateLayout(Context context) {
        super(context);
        init();
        mContext = context;
    }

    public PageStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mContext = context;
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
    }

    @Override
    public void onLoading() {
        show(PageState.LOADING);
    }

    @Override
    public void onError() {
        show(PageState.ERROR);
    }

    @Override
    public void onEmpty() {
        show(PageState.EMPTY);
    }

    @Override
    public void onSucceed() {
        show(PageState.SUCCEED);
    }

    @Override
    public void onRequesting() {
        show(PageState.REQEUSTING);
    }

    private void show(PageState state) {
        if (null == mLoadingView) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        } else {
            if (mLoading instanceof MaterialProgress) {
                mLoading.reset();
            }
        }

        if (null == mErrorView) {
            mErrorView = getErrorView();
            addView(mErrorView);
        }

        if (null == mEmptyView) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }

        mLoadingView.setVisibility(state == PageState.LOADING || state == PageState.REQEUSTING ? VISIBLE : GONE);
        mErrorView.setVisibility(state == PageState.ERROR ? VISIBLE : GONE);
        mEmptyView.setVisibility(state == PageState.EMPTY ? VISIBLE : GONE);

        if (null != mSucceedView) {
            mSucceedView.setVisibility(state == PageState.SUCCEED || state == PageState.REQEUSTING ? VISIBLE : GONE);
        }
    }

    private View getLoadingView() {
        if (0 == Builder.loading) {
            mLoadingView = initLoadingView();
        } else {
            mLoadingView = LayoutInflater.from(mContext).inflate(Builder.loading, null, false);
        }

        return mLoadingView;
    }

    private View initLoadingView() {
        View loadingView = LayoutInflater.from(mContext).inflate(R.layout.loading, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DimenUtils.dip2px(mContext, 80),
                DimenUtils.dip2px(mContext, 80)
        );
        params.gravity = Gravity.CENTER;
        loadingView.setLayoutParams(params);

        mLoading = (MaterialProgress) loadingView.findViewById(R.id.loadingProgress);
        if (null != Builder.loadingColors) {
            mLoading.setColors(Builder.loadingColors);
        }
        return loadingView;
    }

    private View getErrorView() {
        if (0 == Builder.error) {
            mErrorView = initErrorView();
        } else {
            mErrorView = LayoutInflater.from(mContext).inflate(Builder.error, null, false);
        }

        return mErrorView;
    }

    private View initErrorView() {
        View errorView = LayoutInflater.from(mContext).inflate(R.layout.error, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        errorView.setLayoutParams(params);

        RippleView errorRipple = (RippleView) errorView.findViewById(R.id.ripple_error);
        if (0 != Builder.errorRippleColor) {
            errorRipple.setRippleColor(Builder.errorRippleColor);
        }

        ImageView errorImage = (ImageView) errorView.findViewById(R.id.iv_error);
        if (0 != Builder.errorImage) {
            errorImage.setBackgroundResource(Builder.errorImage);
        }

        TextView errorPromt = (TextView) errorView.findViewById(R.id.tv_error);
        if (!TextUtils.isEmpty(Builder.errorPromt)) {
            errorPromt.setText(Builder.errorPromt);
        }

        return errorView;
    }

    private View getEmptyView() {
        if (0 == Builder.empty) {
            mEmptyView = initEmptyView();
        } else {
            mEmptyView = LayoutInflater.from(mContext).inflate(Builder.empty, null, false);
        }
        return mEmptyView;
    }

    private View initEmptyView() {
        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.empty, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        emptyView.setLayoutParams(params);

        RippleView emptyRipple = (RippleView) emptyView.findViewById(R.id.ripple_empty);
        if (0 != Builder.emptyRippleColor) {
            emptyRipple.setRippleColor(Builder.emptyRippleColor);
        }

        ImageView emptyImage = (ImageView) emptyView.findViewById(R.id.iv_empty);
        if (0 != Builder.emptyImage) {
            emptyImage.setBackgroundResource(Builder.emptyImage);
        }

        TextView emptyPromt = (TextView) emptyView.findViewById(R.id.tv_empty);
        if (!TextUtils.isEmpty(Builder.emptyPromt)) {
            emptyPromt.setText(Builder.emptyPromt);
        }

        return emptyView;
    }

    /**
     * do something when click the empty image
     * @param onEmptyListener
     * @return
     */
    public PageStateLayout setOnEmptyListener(OnClickListener onEmptyListener) {
        if (null == mEmptyView) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        mEmptyView.setOnClickListener(onEmptyListener);
        return this;
    }

    /**
     * do something when click the error image
     * @param onErrorListener
     * @return
     */
    public PageStateLayout setOnErrorListener(OnClickListener onErrorListener) {
        if (null == mErrorView) {
            mErrorView = getErrorView();
            addView(mErrorView);
        }
        mErrorView.setOnClickListener(onErrorListener);
        return this;
    }

    /**
     * Load the container and succeedView defined by user
     * Display
     *
     * @param activity
     * @param succeedView
     */
    public void load(@NonNull Activity activity, @NonNull View succeedView) {
        if (null != mSucceedView) {
            removeView(mSucceedView);
        }
        mSucceedView = succeedView;
        addView(mSucceedView, 0);
        activity.setContentView(this);
    }

    /**
     * Load the container and succeedView defined by user
     *
     * @param parent
     * @param succeedView
     */
    public void load(@NonNull ViewGroup parent, @NonNull View succeedView) {
        if (null != mSucceedView) {
            removeView(mSucceedView);
        }
        mSucceedView = succeedView;
        addView(mSucceedView, 0);
        parent.addView(this);
    }

    /**
     * Load the succeedView defined by user without containers
     * @param succeedView
     */
    public void load(@NonNull View succeedView) {
        addView(mSucceedView, 0);
    }

    public static class Builder {

        private static final Builder mInstance = new Builder();

        private Builder() {

        }

        private static Integer loading = 0;
        private static Integer error = 0;
        private static Integer empty = 0;

        private static ArrayList<Integer> loadingColors;

        private static Integer emptyRippleColor = 0;
        private static Integer emptyImage = 0;
        private static String emptyPromt = "";

        private static Integer errorRippleColor = 0;
        private static Integer errorImage = 0;
        private static String errorPromt = "";

        /**
         * Set your loadingView
         * @param resId
         * @return
         */
        public static Builder setLoadingView(Integer resId) {
            loading = resId;
            return mInstance;
        }

        /**
         * Set your emptyView
         *
         * @param resId
         * @return
         */
        public static Builder setEmptyView(Integer resId) {
            empty = resId;
            return mInstance;
        }

        /**
         * Set your errorView
         *
         * @param resId
         * @return
         */
        public static Builder setErrorView(Integer resId) {
            error = resId;
            return mInstance;
        }

        /**
         * Set single color for MaterialProgress if you want
         *
         * @param color
         * @return
         */
        public static Builder setLoadingColor(Integer color) {
            if (null == loadingColors) {
                loadingColors = new ArrayList<>();
            }
            loadingColors.clear();
            loadingColors.add(color);
            return mInstance;
        }

        /**
         * Set the colors for MaterialProgress as your like
         *
         * @param colors
         * @return
         */
        public static Builder setLoadingColors(ArrayList<Integer> colors) {
            if (null == loadingColors) {
                loadingColors = new ArrayList<>();
            }
            loadingColors.clear();
            loadingColors.addAll(colors);
            return mInstance;
        }

        public static Builder setEmptyRippleColor(Integer color) {
            emptyRippleColor = color;
            return mInstance;
        }

        public static Builder setEmptyImage(Integer resId) {
            emptyImage = resId;
            return mInstance;
        }

        public static Builder setEmptyPromt(String promt) {
            emptyPromt = promt;
            return mInstance;
        }

        public static Builder setErrorRippleCor(Integer color) {
            errorRippleColor = color;
            return mInstance;
        }

        public static Builder setErrorImage(Integer resId) {
            errorImage = resId;
            return mInstance;
        }

        public static Builder setErrorPromt(String promt) {
            errorPromt = promt;
            return mInstance;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        PageStateSavedState ss = new PageStateSavedState(super.onSaveInstanceState());
        ss.mLoading = Builder.loading;
        ss.mEmpty = Builder.empty;
        ss.mError = Builder.error;
        ss.mLoadingColors = Builder.loadingColors;
        ss.mEmptyRippleColor = Builder.emptyRippleColor;
        ss.mEmptyImage = Builder.emptyImage;
        ss.mEmptyPromt = Builder.emptyPromt;
        ss.mErrorRippleColor = Builder.errorRippleColor;
        ss.mErrorImage = Builder.errorImage;
        ss.mErrorPromt = Builder.errorPromt;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof PageStateSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        PageStateSavedState ss = (PageStateSavedState) state;
        super.onRestoreInstanceState(ss);

        Builder.loading = ss.mLoading;
        Builder.empty = ss.mEmpty;
        Builder.error = ss.mError;
        Builder.loadingColors = ss.mLoadingColors;
        Builder.emptyRippleColor = ss.mEmptyRippleColor;
        Builder.emptyImage = ss.mEmptyImage;
        Builder.emptyPromt = ss.mEmptyPromt;
        Builder.errorRippleColor = ss.mErrorRippleColor;
        Builder.errorImage = ss.mErrorImage;
        Builder.errorPromt = ss.mErrorPromt;
    }

    static class PageStateSavedState extends BaseSavedState {

        Integer mLoading;
        Integer mError;
        Integer mEmpty;

        ArrayList<Integer> mLoadingColors;

        Integer mEmptyRippleColor;
        Integer mEmptyImage;
        String mEmptyPromt;

        Integer mErrorRippleColor;
        Integer mErrorImage;
        String mErrorPromt;

        private static final Parcelable.Creator<PageStateSavedState> mCreator = new Creator<PageStateSavedState>() {
            @Override
            public PageStateSavedState createFromParcel(Parcel source) {
                return new PageStateSavedState(source);
            }

            @Override
            public PageStateSavedState[] newArray(int size) {
                return new PageStateSavedState[size];
            }
        };

        PageStateSavedState(Parcelable superState) {
            super(superState);
        }

        private PageStateSavedState(Parcel source) {
            super(source);
            mLoading = source.readInt();
            mError = source.readInt();
            mEmpty = source.readInt();
            mLoadingColors = source.readArrayList(Integer.class.getClassLoader());
            mEmptyRippleColor = source.readInt();
            mEmptyImage = source.readInt();
            mEmptyPromt = source.readString();
            mErrorRippleColor = source.readInt();
            mErrorImage = source.readInt();
            mErrorPromt = source.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mLoading);
            out.writeInt(mError);
            out.writeInt(mEmpty);
            out.writeList(mLoadingColors);
            out.writeInt(mEmptyRippleColor);
            out.writeInt(mEmptyImage);
            out.writeString(mEmptyPromt);
            out.writeInt(mErrorRippleColor);
            out.writeInt(mErrorImage);
            out.writeString(mErrorPromt);
        }
    }
}
