package zxzq.xiaobai.dailynews.base;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import zxzq.xiaobai.dailynews.R;

/**
 * Created by 蔡传飞 on 2017-04-28.
 */

public class BaseFragment extends Fragment {
    private final String TAG=BaseFragment.this.getClass().getSimpleName();

    /**
     * 从Fragment跳转到Activity页面
     * @param mClass
     */
    public void startActivity(Class<?> mClass){
        Intent intent=new Intent(getActivity(),mClass);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.screen_in,R.anim.screen_out);
    }
    public RequestQueue queue;

    /**
     * 得到一个RequestQueue对象
     * 添加消息请求
     * @return
     */
    public RequestQueue getQueue(){
     if(queue==null){
         queue= Volley.newRequestQueue(getActivity());
     }return queue;
    }

    /**
     * 吐司
     * @param text
     */
    public void showToast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
