package zxzq.xiaobai.dailynews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.MyBaseAdapter;
import zxzq.xiaobai.dailynews.entity.CommentInfo;
import zxzq.xiaobai.dailynews.utils.LoadImage;

/**
 * Created by 蔡传飞 on 2017-05-17.
 */

public class CommentAdapter extends MyBaseAdapter<CommentInfo> {
    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView=null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_xlist_comment, null);
            holdView = new HoldView();
            holdView.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            holdView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holdView.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holdView.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            convertView.setTag(holdView);
        } else {
            holdView= (HoldView) convertView.getTag();
        }
        CommentInfo commentInfo=list.get(position);
        //把信息设置上
        holdView.tv_name.setText(commentInfo.getUid());
        holdView.tv_time.setText(commentInfo.getStamp());
        holdView.tv_detail.setText(commentInfo.getContent());
        final HoldView finalHoldView = holdView;
        LoadImage.ImageLoadListener imageLoadListener=new LoadImage.ImageLoadListener() {
            @Override
            public void imageLoadOk(Bitmap bitmap, String url) {
               finalHoldView.iv_photo.setImageBitmap(bitmap);
            }
        };
        LoadImage loadImage=new LoadImage(context,imageLoadListener);
        loadImage.geBitmap(commentInfo.getPortroit(),holdView.iv_photo);
        return convertView;
    }
    class HoldView{
        ImageView iv_photo;
        TextView tv_name,tv_time,tv_detail;
    }
}
