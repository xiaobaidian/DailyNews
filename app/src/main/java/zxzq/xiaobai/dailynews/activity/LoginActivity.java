package zxzq.xiaobai.dailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name, et_passWord;

    private Button bt_login, bt_forgetPass, bt_register;

    public static final int TO_REGISTER_ACTIVITY = 1;
    public static final int TO_RIGHT_FRAGMENT_RESULT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initEvent() {
        bt_login.setOnClickListener(this);
        bt_forgetPass.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_passWord = (EditText) findViewById(R.id.et_passWord);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_forgetPass = (Button) findViewById(R.id.bt_forgetPass);
        bt_register = (Button) findViewById(R.id.bt_register);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==TO_REGISTER_ACTIVITY){
            if (resultCode==RegisterActivity.TO_LOGIN_RESULT){
                Bundle bundle=data.getExtras();
                et_name.setText(bundle.getString("name"));
                et_passWord.setText(bundle.getString("pwd"));
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                requestLogin();//登陆
                break;

            case R.id.bt_forgetPass:
                startActivity(ForgetActivity.class);
                break;

            case R.id.bt_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, TO_REGISTER_ACTIVITY);
                break;
        }
    }

    /**
     * 请求登陆
     */
    private void requestLogin() {
        String name = et_name.getText().toString().trim();
        String psw = et_passWord.getText().toString().trim();
        if (name.length() == 0) {
            showToast("用户名不能为空！");
            return;
        }

        if (psw.length() == 0) {
            showToast("密码不能为空！");
            return;
        }

        if (psw.length() < 6 || psw.length() > 24) {
            showToast("密码的长度应该在6~24位之间");
            return;
        }

        if (!CommonUtil.verifyPassword(psw)) {
            showToast("密码要由英文字母，下划线和数字组成！");
            return;
        }

//        user_login?ver=版本号&uid=用户名&pwd=密码&device=0
        String url = "http://118.244.212.82:9092/newsClient/user_login?ver=1&uid=" + name + "&pwd=" + psw + "&device=0";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                /**
                 * {	“status”:0,
                 “message”:”OK”
                 “data”:{
                 “result”:登陆状态
                 “explain”:登陆成功
                 “token”:用户令牌
                 }
                 }
                 */
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("message").equals("OK")) {
                        object = object.getJSONObject("data");
                        String token = object.getString("token");
                        Intent intent = new Intent();
                        intent.putExtra("token", token);
                        setResult(TO_RIGHT_FRAGMENT_RESULT, intent);
                        finish();
                    } else {
                        showToast("登陆异常");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showToast("登陆异常，请检查您的网络！");
            }
        };

        StringRequest request = new StringRequest(url, listener, errorListener);
        MyApplication.getQueue(this).add(request);
    }
}
