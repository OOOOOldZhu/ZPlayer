package com.z.zmusicplayer.fragment.mainfragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.VideoBean;

import java.util.List;

/**
 * Created by z on 2014/7/2.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.MyViewHolder> {
    private List<VideoBean> videoBeanList = null;
    private Activity activity;
    private LayoutInflater inflater;
    private int mWidth, mHeight;

    public MainFragmentAdapter(List<VideoBean> videoBeanList, Activity activity, int mWidth, int mHeight) {
        this.videoBeanList = videoBeanList;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.activity =activity;
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.homepage_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VideoBean videoBean = videoBeanList.get(position);
        ViewGroup.LayoutParams layoutParams = holder.ivContentimg.getLayoutParams();
        layoutParams.width = mWidth;
        layoutParams.height = mHeight;
        holder.ivContentimg.setLayoutParams(layoutParams);
        holder.viewbg.setLayoutParams(layoutParams);

        holder.tvTitle.setText(videoBean.getTitle());
        holder.tvDescription.setText(videoBean.getDescription());
        String posterPic = videoBean.getPosterPic();
        ImageView iv = holder.ivContentimg;
        if(activity !=null ){
            Log.d("activity",activity.toString());
        }else {
            Log.d("activity","不存在");
        }

        Glide.with(activity).load(posterPic).into(iv);
       // holder.ivContentimg.setImageResource(R.mipmap.home_page_project);
        final int tag;
        String type = videoBean.getType();

        if (type.equals("ACTIVITY")) {
            tag = 0;
            holder.ivType.setImageResource(R.mipmap.home_page_activity);
        } else if (type.equalsIgnoreCase("VIDEO")) {
            tag = 1;
            holder.ivType.setImageResource(R.mipmap.home_page_video);
        } else if (type.equalsIgnoreCase("WEEK_MAIN_STAR")) {
            tag = 2;
            holder.ivType.setImageResource(R.mipmap.home_page_star);
        } else if (type.equalsIgnoreCase("PLAYLIST")) {
            tag = 3;
            holder.ivType.setImageResource(R.mipmap.home_page_playlist);
        } else if (type.equalsIgnoreCase("AD")) {
            tag = 4;
            holder.ivType.setImageResource(R.mipmap.home_page_ad);
        } else if (type.equalsIgnoreCase("PROGRAM")) {
            tag = 5;
            holder.ivType.setImageResource(R.mipmap.home_page_program);
        } else if (type.equalsIgnoreCase("bulletin")) {
            tag = 6;
            holder.ivType.setImageResource(R.mipmap.home_page_bulletin);
        } else if (type.equalsIgnoreCase("fanart")) {
            tag = 7;
            holder.ivType.setImageResource(R.mipmap.home_page_fanart);
        } else if (type.equalsIgnoreCase("live")) {
            tag = 8;
            holder.ivType.setImageResource(R.mipmap.home_page_live);
        } else if (type.equalsIgnoreCase("LIVENEW") || type.equalsIgnoreCase("livenewlist")) {
            tag = 9;
            holder.ivType.setImageResource(R.mipmap.home_page_live_new);
        } else if (type.equalsIgnoreCase("LIVENTORY") || type.equalsIgnoreCase(videoBean.getType())) {
            tag = 10;
            holder.ivType.setImageResource(R.mipmap.home_page_project);
        } else {
            tag  = -100;
            holder.ivType.setImageResource(0);
        }

    }


    @Override
    public int getItemCount() {
        return videoBeanList== null ?0:videoBeanList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView ivitemLogo;
        private ImageView ivContentimg;
        private final View viewbg;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final RelativeLayout rlItemRootVIew;
        private final ImageView ivType;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivitemLogo = (ImageView) itemView.findViewById(R.id.iv_item_logo);
            ivContentimg = (ImageView) itemView.findViewById(R.id.iv_contentimg);
            viewbg = itemView.findViewById(R.id.viewbg);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            rlItemRootVIew = (RelativeLayout) itemView.findViewById(R.id.rl_item_rootView);
            ivType = (ImageView) itemView.findViewById(R.id.iv_type);
        }
    }
}
