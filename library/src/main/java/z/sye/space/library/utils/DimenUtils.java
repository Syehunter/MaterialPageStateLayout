package z.sye.space.library.utils;

import android.content.Context;

/**
 * Created by Syehunter on 16/1/9.
 */
public class DimenUtils {

    public static int dip2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
