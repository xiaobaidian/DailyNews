package zxzq.xiaobai.dailynews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.adapter.LoginLogAdapter;
import zxzq.xiaobai.dailynews.base.BaseActivity;
import zxzq.xiaobai.dailynews.base.MyApplication;
import zxzq.xiaobai.dailynews.entity.UserInfo;
import zxzq.xiaobai.dailynews.utils.LoadImage;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back,iv_photo;
    private TextView tv_name,tv_integral,tv_comment_count,tv_cover;
    private ListView lv_loginLog;
    private Button bt_exit;
    private SharedPreferences sp;
    private String token;
    private Bundle bundle;
    private UserInfo info;
    private String photoPath;
    private Bitmap bitmap;
    private LoginLogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initTitle("用户详情");
        initView();
        initEvent();
        sp=getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        token=sp.getString("token",null);//获取用户令牌

        bundle=getIntent().getExtras();//初始化Bundle
        info= (UserInfo) bundle.getSerializable("userInfo");

        photoPath=sp.getString("photoPath",null);
        bitmap= BitmapFactory.decodeFile(photoPath);
        if (info!=null){
            initData();//初始化用户信息
            adapter=new LoginLogAdapter(this);
            adapter.addAll(info.getLoginlog());
            lv_loginLog.setAdapter(adapter);
        }
    }
    private void initEvent() {
        iv_photo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_photo= (ImageView) findViewById(R.id.iv_photo);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_integral= (TextView) findViewById(R.id.tv_integral);
        tv_comment_count= (TextView) findViewById(R.id.tv_comment_count);
        tv_cover= (TextView) findViewById(R.id.tv_cover);
        lv_loginLog= (ListView) findViewById(R.id.lv_loginLog);
        bt_exit= (Button) findViewById(R.id.bt_exit);
    }
    /**
     * 初始化用户信息
     */
    private void initData() {
        if (bitmap!=null){
            iv_photo.setImageBitmap(bitmap);
        }else {
            LoadImage loadmage=new LoadImage(this, new LoadImage.ImageLoadListener() {
                @Override
                public void imageLoadOk(Bitmap bitmap, String url) {
                    iv_photo.setImageBitmap(bitmap);
                }
            });
            loadmage.geBitmap(info.getPortrait(),iv_photo);
        }
        tv_name.setText(info.getUid());
        tv_integral.setText("积分："+info.getIntegration());
        tv_comment_count.setText(info.getComnum()+"");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                photoPath=sp.getString("photoPath",null);
                if (photoPath!=null){
                    setResult(3);//返回上个页面
                    finish();
                    return;
                }
                finish();
                break;

            case R.id.bt_exit:
                setResult(2);//退出登陆
                finish();
                break;

            case R.id.iv_photo:
                showPop();//更改头像
                break;
        }
    }


    /**
     * 更改用户头像
     */
    private View popView;
    private TextView tv_camera,tv_photo;
    private PopupWindow popupWindow;
    private void showPop() {
        popView= LayoutInflater.from(this).inflate(R.layout.pop_user_activity,null);

        popupWindow=new PopupWindow(this);
        popupWindow.setContentView(popView);
        tv_camera= (TextView) popView.findViewById(R.id.tv_camera);
        tv_photo= (TextView) popView.findViewById(R.id.tv_photo);
        tv_camera.setOnClickListener(popListener);
        tv_photo.setOnClickListener(popListener);
        /**
         * 设置popupWindow的属性
         */
        popupWindow.setWidth(300);//宽度
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//高度
        popupWindow.setFocusable(true);//获取焦点
        popupWindow.setOutsideTouchable(true);//外部可触摸
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_pop_user));//设置背景资源
        popupWindow.showAtLocation(lv_loginLog, Gravity.CENTER,0,0);//显示位置
        tv_cover.setVisibility(View.VISIBLE);//可见
        tv_cover.setAlpha(0.5f);//设置为半透明状态
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_cover.setVisibility(View.GONE);
            }
        });
    }

    /**
     * PopupWindow中的监听事件
     */
    private View.OnClickListener popListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_camera:
                    popupWindow.dismiss();
                    takePhoto();//调用系统的拍照功能
                    break;

                case R.id.tv_photo:
                    popupWindow.dismiss();
                    selectPhoto();//从相册选择
                    break;
            }
        }


    };

    private static final int PHOTO=2;//选择照片的请求码
    private static final int CAMERA=3;//拍照的请求码

    /**
     * 从相册选择
     */
    private void selectPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK,null);
        intent.setType("image/*");//设置图片类型
        intent.putExtra("crop","true");//设置裁剪功能
        intent.putExtra("aspectX",1);//设置宽高的比例
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",70);//设置宽高的值
        intent.putExtra("outputY",70);
        intent.putExtra("return-data",true);//裁剪的结果
        startActivityForResult(intent,PHOTO);
    }

    /**
     * 调用系统的拍照功能
     */
    private void takePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PHOTO){//从相册选择照片
            if (resultCode== Activity.RESULT_OK){
                Bundle bundle=data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                Bitmap b=getClipBitmap(bitmap);
                iv_photo.setImageBitmap(b);
            }
        }

        if (requestCode==CAMERA){//调用系统的拍照功能
            if (resultCode==Activity.RESULT_OK){
                Bundle bundle=data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                Bitmap b=getClipBitmap(bitmap);
                iv_photo.setImageBitmap(b);
            }
        }
    }

    /**
     * 裁剪图片
     * @param bitmap
     * @return
     */
    Bitmap b;
    private Bitmap getClipBitmap(Bitmap bitmap) {
        Bitmap bitmapType=BitmapFactory.decodeResource(getResources(),R.mipmap.userbg);//图片类型
        int w=bitmapType.getWidth();//宽度
        int h=bitmapType.getHeight();//高度
        b=Bitmap.createScaledBitmap(bitmap,w,h,true);//裁剪过后的照片
        Canvas c=new Canvas(b);
        Paint p=new Paint();
        c.drawBitmap(b,new Matrix(),p);
        //使两张图片相互交叉显示
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        c.drawBitmap(bitmapType,new Matrix(),p);
        savePhoto(b);//保存裁剪过后的照片
        return b;
    }

    /**
     * 保存裁剪过后的照片
     * @param b
     */
    private void savePhoto(Bitmap b) {
        File fileDir=new File(Environment.getExternalStorageDirectory(),"photo");
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file=new File(fileDir,"userphoto.jpg");
        try {
            FileOutputStream fos=new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG,100,fos);
            sp.edit().putString("photoPath",file.getPath()).commit();
            requestUpdateUserPhoto(file.getPath());//从文件路径中请求图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件路径中请求图片
     * @param filePath
     */
    private void requestUpdateUserPhoto(String filePath) {
        //user_image?token=用户令牌& portrait =头像
        String url="http://118.244.212.82:9092/newsClient/user_image?token="+token+"&portrait="+filePath;
        Response.Listener<String > listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    /**
                     * {
                     “status”:0,
                     “message”:”OK”,
                     “data”:{
                     “result”:0，
                     “explain”:”上传成功”
                     }
                     */
                    JSONObject object=new JSONObject(s);
                    if (object.getInt("status")==0){
                        object=object.getJSONObject("data");
                        if (object.getInt("result")==0){
                            showToast("图片上传成功！");
                            iv_photo.setImageBitmap(b);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("图片上传失败，请检查网络链接");
            }
        };

        StringRequest request=new StringRequest(url,listener,errorListener);
        MyApplication.getQueue(this).add(request);
    }
}
