package zxzq.xiaobai.dailynews.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Timer;
import java.util.TimerTask;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.adapter.MyLeadAdapter;
import zxzq.xiaobai.dailynews.base.BaseActivity;

public class LeadActivity extends BaseActivity {
    ViewPager viewpager;
    MyLeadAdapter adapter;
    RadioGroup radioGroup;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否是第一次启动
        sp = getSharedPreferences("setting", MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            init();
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            enter();
        }
    }

    private void init() {
        setContentView(R.layout.activity_lead);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new MyLeadAdapter();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ImageView t0 = new ImageView(this);
        ImageView t1 = new ImageView(this);
        ImageView t2 = new ImageView(this);
        t0.setImageResource(R.mipmap.welcome);
        t1.setImageResource(R.mipmap.bd);
        t2.setImageResource(R.mipmap.wy);
        adapter.add(t0);
        adapter.add(t1);
        adapter.add(t2);
        viewpager.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.radio1:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.radio2:
                        viewpager.setCurrentItem(2);
                        Timer timer=new Timer();
                        TimerTask task=new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(LogoActivity.class);
                            }
                        };
                        timer.schedule(task,3000);

                        break;



                }
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                RadioButton r=null;
                switch (i) {
                    case 0:
                        r=(RadioButton) findViewById(R.id.radio0);
                        break;
                    case 1:
                        r=(RadioButton) findViewById(R.id.radio1);
                        break;
                    case 2:
                        r=(RadioButton) findViewById(R.id.radio2);
                        break;



                }
                r.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    public void enter(){
        startActivity(new Intent(LeadActivity.this,LogoActivity.class));
        finish();
    }
}