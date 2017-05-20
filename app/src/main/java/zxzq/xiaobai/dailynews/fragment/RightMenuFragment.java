package zxzq.xiaobai.dailynews.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.activity.LoginActivity;
import zxzq.xiaobai.dailynews.activity.UserActivity;
import zxzq.xiaobai.dailynews.base.BaseFragment;
import zxzq.xiaobai.dailynews.base.MyApplication;
import zxzq.xiaobai.dailynews.entity.UserInfo;
import zxzq.xiaobai.dailynews.utils.LoadImage;
import zxzq.xiaobai.dailynews.utils.SystemUtils;


public class RightMenuFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView iv_photo, iv_share_friends, iv_share_qq, iv_share_wx, iv_share_weibo;
    private TextView tv_update_version, tv_name;
    private RelativeLayout rl_unlogin, rl_logined;

    private SharedPreferences sp;
    private boolean isLogined;//登陆状态
    private String token;//用户token来获取用户信息
    private SharedPreferences.Editor editor;
    private Bundle bundle;
    private UserInfo info;
    private Bitmap bitmap;
    private String photoPath;

    private static final int TO_LOGIN_ACTIVITY = 1;
    private static final int TO_USER_ACTIVITY = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_right_menu, null);

        sp = getActivity().getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        editor = sp.edit();

        photoPath = sp.getString("photoPath", null);
        bitmap = BitmapFactory.decodeFile(photoPath);

        initView(view);
        initEvent();
        return view;
    }

    private void initEvent() {
        tv_update_version.setOnClickListener(this);

        iv_share_wx.setOnClickListener(this);
        iv_share_qq.setOnClickListener(this);
        iv_share_friends.setOnClickListener(this);
        iv_share_weibo.setOnClickListener(this);

        rl_unlogin.setOnClickListener(this);
        rl_logined.setOnClickListener(this);
        changeView();
    }

    private void initView(View view) {
        tv_update_version = (TextView) view.findViewById(R.id.tv_update_version);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
        iv_share_wx = (ImageView) view.findViewById(R.id.iv_share_wx);
        iv_share_qq = (ImageView) view.findViewById(R.id.iv_share_qq);
        iv_share_friends = (ImageView) view.findViewById(R.id.iv_share_friends);
        iv_share_weibo = (ImageView) view.findViewById(R.id.iv_share_weibo);
        rl_unlogin = (RelativeLayout) view.findViewById(R.id.rl_unlogin);
        rl_logined = (RelativeLayout) view.findViewById(R.id.rl_logined);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_update_version:
                checkNewApk();//版本更新
                break;

            case R.id.iv_share_wx:
                showToast("登陆微信");
                break;
            case R.id.iv_share_qq:
                showToast("登陆QQ");
                break;
            case R.id.iv_share_friends:
                showToast("登陆朋友圈");
                break;
            case R.id.iv_share_weibo:
                showToast("登陆微博");
                break;

            case R.id.rl_unlogin:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, TO_LOGIN_ACTIVITY);
                break;

            case R.id.rl_logined:
                intent = new Intent(getActivity(), UserActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("userInfo", info);
                intent.putExtras(bundle);
                startActivityForResult(intent, TO_USER_ACTIVITY);
                break;
        }
    }

    /**
     * 带结果的返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_LOGIN_ACTIVITY) {//跳到登陆界面
            if (resultCode == LoginActivity.TO_RIGHT_FRAGMENT_RESULT) {
                String token = data.getStringExtra("token");
                editor.putString("token", token).commit();
                editor.putBoolean("isLogined", true).commit();
                changeView();//登陆成功后进入RightFragment更改界面
            }
        }

        if (requestCode == TO_USER_ACTIVITY) {//跳到用户详情界面
            if (resultCode == 2) {//退出登陆
                editor.clear().commit();
                changeView();
            }

            if (resultCode == 3) {
                photoPath = sp.getString("photoPath", null);
                bitmap = BitmapFactory.decodeFile(photoPath);
                iv_photo.setImageBitmap(bitmap);
            }
        }
    }


    /**
     * 更改界面
     */
    private void changeView() {
        isLogined = sp.getBoolean("isLogined", false);
        if (isLogined) {//登陆状态
            token = sp.getString("token", null);
            if (token != null) {//如果token不为空，请求获取用户信息
                requestUserData(token);
            }
            rl_unlogin.setVisibility(View.GONE);
            rl_logined.setVisibility(View.VISIBLE);
        } else {//不是登陆状态
            rl_unlogin.setVisibility(View.VISIBLE);
            rl_logined.setVisibility(View.GONE);
        }
    }

    /**
     * 请求用户信息
     *
     * @param token
     */
    private void requestUserData(String token) {
//        user_home?ver=版本号&imei=手机标识符&token =用户令牌
        String url = "http://118.244.212.82:9092/newsClient/user_home?ver=1&imei=" + SystemUtils.getIMEI(getActivity()) + "&token=" + token;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /**
                 * {	“status”:0,
                 “message”:”OK”,
                 “data”:{
                 “uid”:用户名
                 “portrait”:用户图标
                 “integration”:用户积分票总数
                 “comnum”:评论总数
                 “loginlog”:[
                 {
                 “time”:登录时间
                 “address”:北京市朝阳区
                 “device”:0
                 }
                 ……
                 ]
                 }
                 */
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        object = object.getJSONObject("data");
                        Gson gson = new Gson();
                        info = gson.fromJson(object.toString(), UserInfo.class);
                        if (bitmap != null) {//如果保存的图片存在，直接设置
                            iv_photo.setImageBitmap(bitmap);
                        } else {//如果保存的图片，就要去下载
                            LoadImage.ImageLoadListener loadImagListener = new LoadImage.ImageLoadListener() {
                                @Override
                                public void imageLoadOk(Bitmap bitmap, String url) {
                                    iv_photo.setImageBitmap(bitmap);
                                }
                            };
                            LoadImage loagImage = new LoadImage(getActivity(), loadImagListener);
                            loagImage.geBitmap(info.getPortrait(), iv_photo);
                        }
                        tv_name.setText(info.getUid());

                    } else {
                        if (object.getInt("status") == -3) {
                            showToast(s);
                            editor.clear().commit();
                            changeView();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("数据请求失败，请检查您的网络");
            }
        };

        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(getActivity()).add(request);
    }

    /**
     * 版本更新
     */
    private void checkNewApk() {
        String url = "http://118.244.212.82:9092/newsClient/update?imei=" + SystemUtils.getIMEI(getActivity()) + "&pkg=" + getActivity().getPackageName() + "&ver=1";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String version = object.getString("version");
                    int intVersion = Integer.parseInt(version);//转为整型
                    if (intVersion > 1) {
                        showToast("有新版本");
                    } else {
                        showToast("无新版本");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("网络请求失败，请检查网链接");
            }
        };
        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(getActivity()).add(request);
    }
}
