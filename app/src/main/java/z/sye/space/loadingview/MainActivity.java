package z.sye.space.loadingview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import z.sye.space.library.PageStateLayout;
import z.sye.space.library.utils.PageState;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOADING = 0;
    private static final int EMPTY = 1;
    private static final int ERROR = 2;
    private static final int SUCCEED = 3;
    private static final int REQUESTING = 4;
    private long msgDelayed = 4000;

    private PageStateLayout mLayout;

    private PageState currentState = PageState.SUCCEED;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    mLayout.onLoading();
                    break;
                case EMPTY:
                    mLayout.onEmpty();
                    currentState = PageState.EMPTY;
                    break;
                case ERROR:
                    mLayout.onError();
                    currentState = PageState.ERROR;
                    break;
                case SUCCEED:
                    mLayout.onSucceed();
                    currentState = PageState.SUCCEED;
                    break;
                case REQUESTING:
                    mLayout.onRequesting();
                    currentState = PageState.REQEUSTING;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrameLayout mParent = (FrameLayout) findViewById(R.id.fl_parent);

        View succeedView = LayoutInflater.from(this).inflate(R.layout.layout_succeed, null, false);
        mLayout = new PageStateLayout(this);
        mLayout.setOnEmptyListener(mOnEmptyListener)
                .setOnErrorListener(mOnErrorListener)
                .load(mParent, succeedView);

        mLayout.onLoading();
        Message msg = new Message();
        msg.what = SUCCEED;
        mHandler.sendMessageDelayed(msg, msgDelayed);

        Button emptyBtn = (Button) succeedView.findViewById(R.id.btn_empty);
        Button errorBtn = (Button) succeedView.findViewById(R.id.btn_error);
        Button requestBtn = (Button) succeedView.findViewById(R.id.btn_requesting);
        emptyBtn.setOnClickListener(this);
        errorBtn.setOnClickListener(this);
        requestBtn.setOnClickListener(this);

    }

    private View.OnClickListener mOnEmptyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Do something when empty", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener mOnErrorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "Do something when error", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Message msg = new Message();
        switch (v.getId()) {
            case R.id.btn_empty:
                mLayout.onLoading();//simulate network loading
                msg.what = EMPTY;
                mHandler.sendMessageDelayed(msg, msgDelayed);
                break;
            case R.id.btn_error:
                mLayout.onLoading();//simulate network loading
                msg.what = ERROR;
                currentState = PageState.ERROR;
                mHandler.sendMessageDelayed(msg, msgDelayed);
                break;
            case R.id.btn_requesting:
                mLayout.onRequesting();
                msg.what = SUCCEED;//show Succeed View when Requesting end
                currentState = PageState.REQEUSTING;
                mHandler.sendMessageDelayed(msg, msgDelayed);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentState != PageState.SUCCEED) {
            mLayout.onSucceed();
            currentState = PageState.SUCCEED;
        } else {
            super.onBackPressed();
        }
    }
}
