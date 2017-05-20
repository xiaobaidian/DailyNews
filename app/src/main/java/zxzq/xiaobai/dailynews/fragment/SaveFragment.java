package zxzq.xiaobai.dailynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.activity.NewsDetailActivity;
import zxzq.xiaobai.dailynews.adapter.NewsListAdapter;
import zxzq.xiaobai.dailynews.base.BaseFragment;
import zxzq.xiaobai.dailynews.db.NewsDBManager;
import zxzq.xiaobai.dailynews.entity.News;

public class SaveFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {
    private View view;
    private ListView lv_save;
    private NewsListAdapter adapter;
    private NewsDBManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_save, container, false);
        lv_save = (ListView) view.findViewById(R.id.lv_save);
        adapter = new NewsListAdapter(getActivity());
        manager = new NewsDBManager(getActivity());
        ArrayList<News> newsList = manager.querryLoveNews();
        adapter.addAll(newsList);
        lv_save.setAdapter(adapter);
        lv_save.setOnItemClickListener(this);
        lv_save.setOnItemLongClickListener(this);
        return view;
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        adapter.clear();
        adapter.addAll(manager.querryLoveNews());
        adapter.notifyDataSetChanged();
    }

    /**
     * 从收藏的新闻中跳到新闻详情界面
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = (News) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("newsitem", news);
        startActivity(intent);
    }

    /**
     * 收藏新闻长按显示对话框
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showPop(position);
        return true;
    }

    /**
     * 长按显示的对话框
     */
    private View popview;
    private PopupWindow popupWindow;
    private TextView tv_deleteone,tv_deleteall;
    private int currentPosition;
    private void showPop(int position) {
        currentPosition=position;
        popupWindow=new PopupWindow(getActivity());
        popview=LayoutInflater.from(getActivity()).inflate(R.layout.pop_save_fragment,null);
        tv_deleteone= (TextView) popview.findViewById(R.id.tv_deleteone);
        tv_deleteall= (TextView) popview.findViewById(R.id.tv_deleteall);
        popupWindow.setContentView(popview);
        /**
         * 设置popupWindow的属性
         */
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(lv_save, Gravity.CENTER,0,0);//显示位置
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_pop_user));
        /**
         * popupWindow的监听
         */
        tv_deleteone.setOnClickListener(this);
        tv_deleteall.setOnClickListener(this);
    }
    /**
     * popupWindow的监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_deleteone:
                boolean isDelete =manager.deletNews((News) adapter.getItem(currentPosition));
                if (isDelete) {
                    setRefresh();//刷新
                    popupWindow.dismiss();//对话框消失
                }
                break;
            case R.id.tv_deleteall:
                boolean deleteAll=manager.deleteAllNews();
                if (deleteAll) {
                    setRefresh();//刷新
                    popupWindow.dismiss();//对话框消失
                }
                break;
        }
    }
}
