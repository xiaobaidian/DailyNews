package zxzq.xiaobai.dailynews.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.utils.LogUtil;

/**
 * Created by 蔡传飞 on 2017-04-26.
 */

public class BaseActivity extends FragmentActivity {
    /**
     * 吐司信息
     */
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    Fragment currentFragment;//当前显示的Fragment

    /**
     * Fragment之间跳转的方法
     *
     * @param target
     * @param toFragment
     */
    public void showFragment(int target, Fragment toFragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        //如果当前显示的Fragment不为空，就隐藏起来
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        //如果需要显示的Fragment存在，才能显示
        String toClassName = toFragment.getClass().getSimpleName();
        if (manager.findFragmentByTag(toClassName) != null) {
            fragmentTransaction.show(toFragment);
        } else {
            fragmentTransaction.add(target, toFragment, toClassName);
        }
        fragmentTransaction.commit();//提交
        currentFragment = toFragment;//给当前的Fragment赋值
    }

    /**
     * @param mClass   要跳转的Activity
     * @param isFinish 是否结束当前的Activity
     */
    public void startActivity(Class<?> mClass, boolean isFinish) {
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
        if (isFinish) {
            this.finish();//结束当前Activity
        }
    }

    /**
     *
     * @param mClass 要跳转的Activity
     */
    public void startActivity(Class<?> mClass) {
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
    }

    /**
     * 存在bundle跳转的情况
     * @param mClass
     * @param bundle
     */
    public void startActivity(Class<?> mClass,Bundle bundle) {
        Intent intent = new Intent(this, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.screen_in, R.anim.screen_out);
    }


    /**
     * 初始化标题
     *
     * @param title
     */
    public void initTitle(String title) {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("title");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();//匿名内部类  所以通过类名来找
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(getClass().getSimpleName(), "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(getClass().getSimpleName(), "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(getClass().getSimpleName(), "onRestart");
    }
}
