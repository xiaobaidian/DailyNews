package zxzq.xiaobai.dailynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.MyBaseAdapter;
import zxzq.xiaobai.dailynews.entity.News;
import zxzq.xiaobai.dailynews.utils.LoadImage;

/**
 * Created by 蔡传飞 on 2017-05-03.
 */

public class NewsListAdapter extends MyBaseAdapter<News> {
    private Bitmap defaultBitmap;//默认图片
    public NewsListAdapter(Context context) {
        super(context);
        defaultBitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.defaultpic);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_news, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView= (HoldView) convertView.getTag();
        }
        News news=list.get(position);
        holdView.iv_icon.setImageBitmap(defaultBitmap);
        holdView.tv_title.setText(news.getTitle());
        holdView.tv_content.setText(news.getSummary());
        holdView.tv_time.setText(news.getStamp());
        String url=news.getIcon();
        holdView.iv_icon.setTag(url);
        final HoldView finalHoldView=holdView;
        LoadImage loadImage=new LoadImage(context, new LoadImage.ImageLoadListener() {
            @Override
            public void imageLoadOk(Bitmap bitmap, String url) {
finalHoldView.iv_icon.setImageBitmap(bitmap);
            }
        });
        loadImage.geBitmap(url,holdView.iv_icon);
        return convertView;
    }

    class HoldView {
        ImageView iv_icon;
        TextView tv_title, tv_content, tv_time;

        public HoldView(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.imageView1);
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_content = (TextView) view.findViewById(R.id.textView2);
            tv_time = (TextView) view.findViewById(R.id.textView3);

        }
    }
}
