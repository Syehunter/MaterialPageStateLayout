/**
 * @(#) z.sye.space.loadingview 2016/1/11;
 * <p/>
 * Copyright (c), 2009 深圳孔方兄金融信息服务有限公司（Shenzhen kfxiong
 * Financial Information Service Co. Ltd.）
 * <p/>
 * 著作权人保留一切权利，任何使用需经授权。
 */
package z.sye.space.loadingview;

import android.app.Application;

import z.sye.space.library.PageStateLayout;

/**
 * Created by Syehunter on 2016/1/11.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        PageStateLayout.Builder.setLoadingView(R.layout.custom_layout_loading)
//                .setEmptyView(R.layout.custom_layout_empty)
//                .setErrorView(R.layout.custom_layout_error);
    }
}
