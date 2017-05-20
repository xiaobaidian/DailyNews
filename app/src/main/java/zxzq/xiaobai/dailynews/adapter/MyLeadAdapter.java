package zxzq.xiaobai.dailynews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔡传飞 on 2017-04-27.
 */

public class MyLeadAdapter extends PagerAdapter {
    private List<View> list;

    public List<View> getList() {
        return list;
    }

    public void setList(List<View> list) {
        this.list = list;
    }

    public MyLeadAdapter() {
        setList(new ArrayList<View>());
    }
    public void add(View v){
        list.add(v);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = list.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = list.get(position);
        container.removeView(v);
    }
}
