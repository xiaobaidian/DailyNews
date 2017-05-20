package zxzq.xiaobai.dailynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.MyBaseAdapter;
import zxzq.xiaobai.dailynews.entity.UserInfo;


/**
 * Created by Administrator on 2017/5/16.
 */

public class LoginLogAdapter extends MyBaseAdapter<UserInfo.Loginlog> {
    public LoginLogAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        HoldView holdView = null;
        if (convertView == null) {
            holdView = new HoldView();
            convertView = inflater.inflate(R.layout.loginlog_item, null);
            holdView.login_time = (TextView) convertView.findViewById(R.id.login_time);
            holdView.login_address = (TextView) convertView.findViewById(R.id.login_address);
            holdView.login_type = (TextView) convertView.findViewById(R.id.login_type);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        UserInfo.Loginlog loginlog = list.get(position);
        holdView.login_time.setText(loginlog.getTime());
        holdView.login_address.setText(loginlog.getAddress());
        holdView.login_type.setText(loginlog.getDevice().equals("0") ? "手机" : "电脑");
        return convertView;
    }

    class HoldView {
        TextView login_time, login_address, login_type;
    }
}
