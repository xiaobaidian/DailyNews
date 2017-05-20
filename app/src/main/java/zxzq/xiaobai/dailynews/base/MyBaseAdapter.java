package zxzq.xiaobai.dailynews.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔡传飞 on 2017-05-03.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
//设置快速滑动
    public boolean isFling=false;
    //保存对象的集合
    protected List<T> list;
    //布局加载器
    protected LayoutInflater inflater;
    //上下文对象
    protected Context context;

    public MyBaseAdapter(Context context) {
        this.context=context;
        list=new ArrayList<T>();
        inflater=LayoutInflater.from(context);
    }

    /**
     * 添加单条数据
     * @param t
     */
    public void add(T t){
        list.add(t);
    }

    /**
     * 添加一个集合数据
     * @param tlist
     */
    public void addAll(List<T> tlist){
       list.addAll(tlist);
        notifyDataSetChanged();
    }

    /**
     * 获取集合数据的方法
     * @return
     */
    public List<T> getList(){
        return list;
    }

    /**
     * 删除单条数据
     */
    public void remove(T t){
        list.remove(t);
    }

    /**
     * 删除所有数据
     * @param tlist
     */
    public void removeAll(List<T> tlist){
        list.removeAll(tlist);
        notifyDataSetChanged();
    }

    /**
     * 清空所有数据
     */
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 设置缓存图片
     */
    public static LruCache<String,Bitmap> lruCache=new LruCache<String,Bitmap>(5*1024*1024){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };
    //设置是否快速滑动
    public void setFling(boolean isFling) {
        this.isFling = isFling;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
