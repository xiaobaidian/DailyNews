package zxzq.xiaobai.dailynews.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.BaseActivity;
import zxzq.xiaobai.dailynews.base.MyApplication;
import zxzq.xiaobai.dailynews.db.NewsDBManager;
import zxzq.xiaobai.dailynews.entity.News;

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back, iv_menu;
    private TextView tv_title;
    private ProgressBar pb_loading;
    private WebView wb_newsdetail;
    private News newsitem;//新闻对象
    private NewsDBManager manager;//新闻数据库的管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();//初始化控件id
        initEvent();//监听事件
        //使标题获取到焦点
        tv_title.setFocusable(true);
        newsitem = (News) getIntent().getSerializableExtra("newsitem");
        initWebView();//WebView的初始化和属性设置
        //注册shareSDK的key
        ShareSDK.initSDK(this,"1af231ae6e970");
    }

    /**
     * WebView的初始化和属性设置
     */
    private void initWebView() {
        wb_newsdetail.loadUrl(newsitem.getLink());
        //获取WebView的属性设置
        WebSettings webSettings = wb_newsdetail.getSettings();
        //设置WebView是否支持JavaScript脚本语言
        webSettings.setJavaScriptEnabled(true);
        //使网页加载内容适应屏幕宽度
        webSettings.setUseWideViewPort(true);
        //使用网页的内置缩放机制,包括屏幕上的控件具有手势缩放的功能
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        //使网页在WebView中显示
        wb_newsdetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //设置WebView的加载进度
        wb_newsdetail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb_loading.setProgress(newProgress);
                if (newProgress >= 100) {
                    pb_loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
            }
        });
    }

    /**
     * 监听事件
     */
    private void initEvent() {
        iv_back.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    /**
     * 初始化控件id
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        tv_title = (TextView) findViewById(R.id.tv_title);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        wb_newsdetail = (WebView) findViewById(R.id.wb_newsdetail);
        manager = new NewsDBManager(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_menu:
                showPop();
                break;
        }
    }

    /**
     * 显示PopupWindow
     */
    PopupWindow popupWindow;
    View view;
    TextView tv_savelocat, tv_comment, tv_share;

    private void showPop() {
        popupWindow = new PopupWindow(this);
        view = LayoutInflater.from(this).inflate(R.layout.pop_newsdetail_activity, null);
        tv_savelocat = (TextView) view.findViewById(R.id.tv_savelocat);
        tv_comment = (TextView) view.findViewById(R.id.tv_comment);
        tv_share = (TextView) view.findViewById(R.id.tv_share);
        //获取评论数量
        getCommentCount();
        popupWindow.setContentView(view);
        /**
         *  PopupWindow的属性设置
         */
        //宽度
        popupWindow.setWidth(wb_newsdetail.getWidth() / 3);
        //高度
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //获取到焦点
        popupWindow.setFocusable(true);
        //外部可触摸
        popupWindow.setOutsideTouchable(true);
        //设置外部触摸消失，需要设置背景
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_pop_user));
        //设置下拉
        popupWindow.showAsDropDown(iv_menu, 0, 9);
        /**
         * PopupWindow上对应item的监听事件
         */
        tv_savelocat.setOnClickListener(popListener);
        tv_comment.setOnClickListener(popListener);
        tv_share.setOnClickListener(popListener);
    }

    /**
     * 获取评论数量
     */
    private void getCommentCount() {
        //cmt_num?ver=版本号& nid=新闻编号
        String url="http://118.244.212.82:9092/newsClient/cmt_num?ver=1&nid="+newsitem.getNid();
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /*
                “status”:0,
	            “message”:”OK”,
	            “data”:评论数量
                 */
                try {
                    JSONObject object=new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        int comNum=object.getInt("data");
                        tv_comment.setText("("+comNum+")评论");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("获取评论数量失败,请检查网络!");
            }
        };
        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(this).add(request);
    }

    private View.OnClickListener popListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_savelocat:
                    if (manager.saveLoveNews(newsitem)) {
                        showToast("收藏成功！");
                    } else {
                        showToast("已收藏!");
                    }
                    break;
                case R.id.tv_comment:
                    //跳到评论界面
                    Bundle bundle=new Bundle();
                    bundle.putInt("nid",newsitem.getNid());
                    startActivity(CommentActivity.class,bundle);
                    break;
                case R.id.tv_share:
                    showShare();//一键分享
                    break;
            }
        }
    };

    /**
     * 一键分享
     */
    private void showShare() {
        OnekeyShare oks=new OnekeyShare();
        //关闭so授权
        oks.disableSSOWhenAuthorize();
        //设置标题 邮箱，微信，QQ...使用
        oks.setTitle(getString(R.string.app_name));
        //标题的网路连接 邮箱，微信，QQ...使用
        oks.setTitleUrl(newsitem.getLink());
        //分享文本，所有平台需要的摘要
        oks.setText(newsitem.getSummary());
        //设置图标（本地中有，就从本地获取）Linked获取
        oks.setImageUrl(newsitem.getIcon());
        //仅在微信中使用
        oks.setUrl(newsitem.getLink());
        //对该条新闻分享的评论 在人人网与QQ空间使用
        oks.setComment(newsitem.getSummary());
        //分享新闻的网站名称，仅QQ空间使用
        oks.setSite(getString(R.string.app_name));
        //分享新闻的网站网址
        oks.setSiteUrl(newsitem.getLink());
        //启动分享
        oks.show(this);
    }
}
