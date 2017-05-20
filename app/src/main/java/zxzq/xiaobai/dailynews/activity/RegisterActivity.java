package zxzq.xiaobai.dailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.base.BaseActivity;
import zxzq.xiaobai.dailynews.base.MyApplication;
import zxzq.xiaobai.dailynews.utils.CommonUtil;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_email, et_name, et_passWord;
    private Button bt_register;
    private CheckBox cb;
    public static final int TO_LOGIN_RESULT=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTitle("注册");
        initView();
        initEvent();
    }

    private void initEvent() {
        bt_register.setOnClickListener(this);
    }
    /**
     * 初始化控件
     */
    private void initView() {
        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);
        et_passWord = (EditText) findViewById(R.id.et_passWord);
        bt_register = (Button) findViewById(R.id.bt_register);
        cb = (CheckBox) findViewById(R.id.cb);
    }

    @Override
    public void onClick(View v) {
        String email = et_email.getText().toString().trim();
        final String name = et_name.getText().toString().trim();
        final String pwd = et_passWord.getText().toString().trim();
        //验证邮箱
        if (email.length() == 0) {
            showToast("请输入邮箱");
            return;
        }

        if (!CommonUtil.verifyEmail(email)) {
            showToast("请输入正确的邮箱格式");
            return;
        }

        //验证用户名
        if (name.length() == 0) {
            showToast("请输入用户名");
            return;
        }

        if (name.length() < 6 || name.length() > 24) {
            showToast("用户名的长度应该在6~24位之间");
            return;
        }

        //验证密码
        if (pwd.length() == 0) {
            showToast("请输入密码");
            return;
        }

        if (!CommonUtil.verifyPassword(pwd)) {
            showToast("密码格式不正确");
            return;
        }

        //CheckBox
        if (!cb.isChecked()){
            showToast("没有同意条款");
            return;
        }
        //user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
        String url = "http://118.244.212.82:9092/newsClient/user_register?ver=1&uid=" + name + "&email=" + email + "&pwd=" + pwd;
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /**
                 * {	“status”:0,
                 “message”:”OK”
                 “data”:{
                 “result”:0
                 “explain”:”注册成功”
                 “token”:用户令牌
                 }
                 }
                 */
                try {
                    JSONObject object=new JSONObject(s);
                    if (object.getString("message").equals("OK")){
                        object=object.getJSONObject("data");
                        int i=object.getInt("result");
                        switch (i){
                            case 0:
                                Intent intent=new Intent();
                                Bundle bundle=new Bundle();
                                bundle.putString("name",name);
                                bundle.putString("pwd",pwd);
                                intent.putExtras(bundle);
                                setResult(TO_LOGIN_RESULT,intent);
                                showToast("注册成功！");
                                finish();
                                break;

                            case -1:
                                showToast("服务器不允许注册(用户数量已满)");
                                break;

                            case -2:
                                showToast("用户名重复");
                                break;

                            case -3:
                                showToast("邮箱重复");
                                break;
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
                showToast("注册失败");
            }
        };

        StringRequest request=new StringRequest(url,listener,errorListener);
        MyApplication.getQueue(this).add(request);
    }
}

