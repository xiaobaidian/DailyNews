package zxzq.xiaobai.dailynews.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.BaseActivity;
import zxzq.xiaobai.dailynews.fragment.LeftMenuFragment;
import zxzq.xiaobai.dailynews.fragment.NewsListFragment;
import zxzq.xiaobai.dailynews.fragment.RightMenuFragment;
import zxzq.xiaobai.dailynews.fragment.SaveFragment;
import zxzq.xiaobai.dailynews.view.slidingmenu.slidingmenu.SlidingMenu;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_set, iv_user;
    private NewsListFragment newsListFragment;
    private SaveFragment saveFragment;
    public static SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initSlidingMenu();//侧滑菜单的相关设置
        showNewsListFragment();
    }

    /**
     * 侧滑菜单的相关设置
     */
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //设置模式
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置触发模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏可以触摸触发
        //设置偏移的距离
        slidingMenu.setBehindOffset(200);
        //设置显示的Activity
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        //设置左侧布局
        slidingMenu.setMenu(R.layout.menu_left);//默认左边
        //设置右侧布局
        slidingMenu.setSecondaryMenu(R.layout.menu_right);
        //实例化两边的Fragment
        Fragment leftMenuFragment=new LeftMenuFragment();
        Fragment rightMenuFragment=new RightMenuFragment();
        //将两边边的Fragment设置到布局中
        getFragmentManager().beginTransaction().replace(R.id.ll_menu_left,leftMenuFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.ll_menu_right,rightMenuFragment).commit();

    }

    /**
     * 监听事件
     */
    private void initEvent() {
        iv_set.setOnClickListener(this);
        iv_user.setOnClickListener(this);
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_set = (ImageView) findViewById(R.id.iv_set);
        iv_user = (ImageView) findViewById(R.id.iv_user);

    }

    /**
     * 显示新闻列表的Fragment
     */
    public void showNewsListFragment() {
        setTitle("资讯");
        slidingMenu.showContent();
        if (newsListFragment==null){
            newsListFragment=new NewsListFragment();
        }
showFragment(R.id.layout_content,newsListFragment);
    }

    /**
     * 显示收藏界面Fragment
     */
    public void showFavoriteFragment() {
        setTitle("收藏");
        slidingMenu.showContent();
        if (saveFragment == null) {
            saveFragment=new SaveFragment();
        }
        if (saveFragment.isHidden()) {
            saveFragment.setRefresh();
        }
        showFragment(R.id.layout_content,saveFragment);
    }

    /**
     * 更换当前界面的标题
     * @param title
     */
    public void setTitle(String title){
tv_title.setText(title);
}


    /**
     * 左右两边的监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_set:
                if (slidingMenu != null && slidingMenu.isMenuShowing()) {
                    slidingMenu.showContent();
                } else if (slidingMenu != null) {
                    slidingMenu.showMenu();
                }
                    break;

            case R.id.iv_user:
                if (slidingMenu != null && slidingMenu.isMenuShowing()) {
                    slidingMenu.showContent();
                } else if (slidingMenu != null) {
                    slidingMenu.showSecondaryMenu();
                }
                break;
        }
    }
    /**
     * 两次返回退出应用
     */
    boolean isFirstExit=true;
    public void exitTwice(){
        if (isFirstExit) {
            showToast("再按一次退出");
            isFirstExit = false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.showContent();
        } else {
            exitTwice();
        }
    }
}
