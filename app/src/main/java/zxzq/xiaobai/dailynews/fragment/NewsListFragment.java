package zxzq.xiaobai.dailynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.activity.NewsDetailActivity;
import zxzq.xiaobai.dailynews.adapter.NewsListAdapter;
import zxzq.xiaobai.dailynews.adapter.NewsTypeAdapter;
import zxzq.xiaobai.dailynews.base.BaseFragment;
import zxzq.xiaobai.dailynews.entity.News;
import zxzq.xiaobai.dailynews.entity.NewsType;
import zxzq.xiaobai.dailynews.utils.CommonUtil;
import zxzq.xiaobai.dailynews.view.HorizontalListView;
import zxzq.xiaobai.dailynews.view.xllistview.XListView;


public class NewsListFragment extends BaseFragment implements View.OnClickListener {
    View view;
    HorizontalListView hl;
    XListView xl;
    ImageView iv_moretype;
    NewsTypeAdapter hlAdapter;//水平适配器
    NewsListAdapter xlAdapter;//垂直适配器
    private int contentSubid;//新闻类型对应的位置


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_list, container, false);
        hl = (HorizontalListView) view.findViewById(R.id.hl);
        xl = (XListView) view.findViewById(R.id.xl);
        iv_moretype = (ImageView) view.findViewById(R.id.iv_moretype);
        hlAdapter = new NewsTypeAdapter(getActivity());
        hl.setAdapter(hlAdapter);
        xlAdapter = new NewsListAdapter(getActivity());
        xl.setAdapter(xlAdapter);
        //设置可以刷新
        xl.setPullRefreshEnable(true);
        //设置查看更多
        xl.setPullLoadEnable(true);
        //水平listview的监听
        hl.setOnItemClickListener(hlItemListener);
        //垂直方向listview的监听  刷新
        xl.setXListViewListener(RefreshListeren);
        //垂直方向listview的监听  点击
        xl.setOnItemClickListener(xlItemListener);
        //moretype的监听
        iv_moretype.setOnClickListener(this);
        //获取新闻类型的数据
        requestNewsTypeData();
        return view;
    }

    /**
     * 获取新闻的类型
     */
    private void requestNewsTypeData() {
        //news_sort?ver=版本号&imei=手机标识符
        String url = "http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=2";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    /*
                    “status”:0,
	                “message”:”OK”,
	                “data”:
                     */
                    if (object.get("message").equals("OK")) {
                        JSONArray array = object.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            NewsType type = gson.fromJson(object.toString(), NewsType.class);
                            hlAdapter.addAll(type.getSubgrp());
                        }
                        int subId = hlAdapter.getList().get(0).getSubid();
                        contentSubid = subId;
                        requestNewsData(subId, false);//获取竖直方向的数据
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("获取新闻类型失败，请检查您的网路连接");
            }
        };
        //添加到消息队列中
        Request request = new StringRequest(url, listener, errorListener);
        getQueue().add(request);
    }

    /**
     * 请求新闻列表数据
     *
     * @param subId
     * @param isLoadMore
     */
    private void requestNewsData(int subId, boolean isLoadMore) {
//news_list?ver=版本号&subid=分类名&dir=1&nid=新闻id&stamp=20140321&cnt=20
        int dir = 1;
        int nid = 1;
        if (isLoadMore) {
            dir = 2;
            if (xlAdapter.getCount() > 0) {
                nid = xlAdapter.getList().get(xlAdapter.getCount() - 1).getNid();
            }
        }
        String stamp = null;
        int cnt = 20;
        String url = "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=" + subId + "&dir=" + dir + "&nid=" + nid + "&stamp=" + stamp + "&cnt=" + cnt;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    /*
            “type”:1,
			“nid”:新闻编号,
			“stamp":新闻时间戳,
			“icon”:图标路径,
			“title”:,新闻标题
			“summary”:,新闻摘要
			“link”:新闻链接
                     */
                    if (object.get("message").equals("OK")) {
                        JSONArray array = object.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            News news = gson.fromJson(object.toString(), News.class);
                            xlAdapter.add(news);
                        }
                        xlAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("获取新闻列表失败，请检查您的网路连接");
            }
        };
        Request request = new StringRequest(url, listener, errorListener);
        getQueue().add(request);
    }


    /**
     * 水平listview的监听
     */
    private AdapterView.OnItemClickListener hlItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hlAdapter.setSelectedPosttion(position);
            xlAdapter.clear();
            contentSubid=hlAdapter.getList().get(position).getSubid();
            requestNewsData(contentSubid,false);
        }
    };
    /**
     * 垂直方向listview的监听  刷新
     */
    private XListView.IXListViewListener RefreshListeren = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            xlAdapter.clear();//清空适配器
            requestNewsData(contentSubid, false);
            xl.stopLoadMore();
            xl.stopRefresh();
            xl.setRefreshTime(CommonUtil.getSystime());
        }

        @Override
        public void onLoadMore() {
            requestNewsData(contentSubid, true);
            xl.stopLoadMore();
            xl.stopRefresh();
        }
    };
    /**
     * 垂直方向listview的监听  点击
     */
    private AdapterView.OnItemClickListener xlItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            News news = (News) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("newsitem", news);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        showToast("完善中，尽情期待...");
    }
}
