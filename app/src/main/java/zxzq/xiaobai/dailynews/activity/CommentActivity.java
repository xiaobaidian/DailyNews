package zxzq.xiaobai.dailynews.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.adapter.CommentAdapter;
import zxzq.xiaobai.dailynews.base.BaseActivity;
import zxzq.xiaobai.dailynews.base.MyApplication;
import zxzq.xiaobai.dailynews.entity.CommentInfo;
import zxzq.xiaobai.dailynews.utils.SystemUtils;
import zxzq.xiaobai.dailynews.view.xllistview.XListView;

public class CommentActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    private ImageView iv_back, iv_send;
    private XListView xListView;
    private EditText et_comment;
    private CommentAdapter adapter;
    private Bundle bundle;
    private SharedPreferences sp;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initTitle("评论");
        initView();
        initEvent();
        bundle = getIntent().getExtras();

        sp = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        token = sp.getString("token", null);

        adapter = new CommentAdapter(this);
        xListView.setAdapter(adapter);
        //加载评论数据
        requestCommentData(false);
        //使xlistview可以刷新
        xListView.setPullRefreshEnable(true);
        //使xlistview可以加载更多
        xListView.setPullLoadEnable(true);
        //设置xlistview的监听
        xListView.setXListViewListener(this);
    }

    /**
     * 监听事件
     */
    private void initEvent() {
        iv_back.setOnClickListener(this);
        iv_send.setOnClickListener(this);
    }

    /**
     * 请求评论数据
     *
     * @param isLoadMore
     */
    private void requestCommentData(boolean isLoadMore) {
        String stamp = null;
        int cid = 0;
        int dir = 1;
        if (isLoadMore) {
            dir = 2;
            cid = adapter.getList().get(adapter.getCount() - 1).getCid();
        }
        String url = "http://118.244.212.82:9092/newsClient/cmt_list?ver=1&nid=" + bundle.getInt("nid") + "&type=1&stamp=" + stamp + "&cid=" + cid + "&dir=" + dir + "&cnt=20";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        JSONArray array = object.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            CommentInfo info = gson.fromJson(object.toString(), CommentInfo.class);
                            adapter.add(info);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("获取评论数据失败，请检查网络！");
            }
        };
        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(this).add(request);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        xListView = (XListView) findViewById(R.id.lv_comment);
        et_comment = (EditText) findViewById(R.id.et_comment);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        adapter.clear();
        requestCommentData(false);
        xListView.stopLoadMore();
        xListView.stopRefresh();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        requestCommentData(true);
        xListView.stopLoadMore();
        xListView.stopRefresh();
    }

    /**
     * 监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_send:
                requestSendComment();
                break;
        }
    }

    /**
     * 发布评论
     */
    private void requestSendComment() {

        if (token == null) {
            showToast("请先登陆！");
            return;
        }
        String comment = et_comment.getText().toString().trim();//输入框中的内容
        if (comment.length() == 0) {
            showToast("请输入评论内容！");
            return;
        }
        //cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
        String url = "http://118.244.212.82:9092/newsClient/cmt_commit?ver=1&nid=" + bundle.getInt("nid") + "&token=" + token + "&imei=" + SystemUtils.getIMEI(this) + "&ctx=" + comment;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /**
                 * {
                 “status”:0,
                 “message”:”OK”,
                 “data”:{
                 “result”:0,
                 “explain”：“发布成功”
                 }
                 }
                 */
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getInt("status") == 0) {
                        adapter.clear();
                        requestCommentData(false);//获取评论数据
                        et_comment.setText("");
                    } else {
                        showToast("请重新登陆！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("数据请求失败，请检查您的网络！");
            }
        };

        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(this).add(request);
    }
}
