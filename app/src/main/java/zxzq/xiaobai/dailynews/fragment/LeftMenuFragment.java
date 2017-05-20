package zxzq.xiaobai.dailynews.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import zxzq.xiaobai.dailynews.R;
import zxzq.xiaobai.dailynews.activity.MainActivity;
import zxzq.xiaobai.dailynews.base.BaseFragment;

public class LeftMenuFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout rl_news, rl_favorite, rl_local, rl_comment, rl_photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_left_menu, container, false);
        initView();
        initEvent();
        return view;
    }



    /**
     * 监听事件
     */
    private void initEvent() {
        rl_news.setOnClickListener(this);
        rl_favorite.setOnClickListener(this);
        rl_local.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
        rl_photo.setOnClickListener(this);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        rl_news = (RelativeLayout) view.findViewById(R.id.rl_news);
        rl_favorite = (RelativeLayout) view.findViewById(R.id.rl_favorite);
        rl_local = (RelativeLayout) view.findViewById(R.id.rl_local);
        rl_comment = (RelativeLayout) view.findViewById(R.id.rl_comment);
        rl_photo = (RelativeLayout) view.findViewById(R.id.rl_photo);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_news:
                Log.e("TAG", "onClick: onClick: onClick: onClick: onClick: ");
                ((MainActivity) getActivity()).showNewsListFragment();

                break;
            case R.id.rl_favorite:
                ((MainActivity) getActivity()).showFavoriteFragment();

                break;
            case R.id.rl_local:
                showToast("本地");
                break;
            case R.id.rl_comment:
                showToast("跟帖");

                break;
            case R.id.rl_photo:
                showToast("图片");

                break;
        }
    }

}
