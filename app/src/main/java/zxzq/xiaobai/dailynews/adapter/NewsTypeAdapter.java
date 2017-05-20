package zxzq.xiaobai.dailynews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.MyBaseAdapter;
import zxzq.xiaobai.dailynews.entity.NewsType;

/**
 * Created by 蔡传飞 on 2017-05-03.
 */

public class NewsTypeAdapter extends MyBaseAdapter<NewsType.SubType> {


    private int selectedPosttion;//当前选中的位置
    public NewsTypeAdapter(Context context) {
        super(context);
    }

    public NewsTypeAdapter(Context context, int selectedPosttion) {
        super(context);
        this.selectedPosttion = selectedPosttion;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_newstype, null);
            holdView = new HoldView();
            holdView.tv_newstype = (TextView) convertView.findViewById(R.id.tv_newstype);
            convertView.setTag(holdView);
        } else {
            holdView= (HoldView) convertView.getTag();
        }
        NewsType.SubType nt=list.get(position);
        String group=nt.getSubgroup();//分类名
        holdView.tv_newstype.setText(group);
        if (selectedPosttion == position) {
            holdView.tv_newstype.setTextColor(Color.RED);
        } else {
            holdView.tv_newstype.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    class HoldView{
        TextView tv_newstype;
    }
    public void setSelectedPosttion(int selectedPosttion) {
        this.selectedPosttion = selectedPosttion;
        notifyDataSetChanged();
    }
}
