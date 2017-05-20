package zxzq.xiaobai.dailynews.base;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 蔡传飞 on 2017-05-17.
 */

public class MyApplication extends Application {
    private static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    public static RequestQueue getQueue(Context context){
        if (queue == null) {
            queue= Volley.newRequestQueue(context);
        }
        return queue;
    }
}
