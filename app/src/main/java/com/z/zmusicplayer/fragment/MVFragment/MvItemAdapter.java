package com.z.zmusicplayer.fragment.MVFragment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.z.zmusicplayer.R;
import com.z.zmusicplayer.bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by z on 2014/7/16.
 */

public class MvItemAdapter extends RecyclerView.Adapter<MvItemAdapter.MvItemViewHolder> {
    private List<VideoBean> videoBeanList = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;
    private int mWidth, mHeight;

    public MvItemAdapter(List<VideoBean> videoBeanList, Activity activity, int mWidth, int mHeight) {
        this.videoBeanList = videoBeanList;
        this.activity = activity;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public MvItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mvfragment_viewpager_item, parent, false);

        return new MvItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MvItemViewHolder holder, int position) {
        VideoBean videoBean = videoBeanList.get(position);
        ViewGroup.LayoutParams layoutParams = holder.mvfragmentIvPostimg.getLayoutParams();
        layoutParams.width = mWidth;
        layoutParams.height = mHeight;
        holder.mvfragmentIvPostimg.setLayoutParams(layoutParams);
        holder.viewbgs.setLayoutParams(layoutParams);
        Glide.with(activity).load(videoBean.getPosterPic()).into(holder.mvfragmentIvPostimg);
        holder.mvfragmentName.setText(videoBean.getArtistName());
        holder.mvfragmentPlaycount.setText(videoBean.getDescription());


    }

    @Override
    public int getItemCount() {
        int count = videoBeanList == null ? 0 : videoBeanList.size();
        return count;
    }

    class MvItemViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.mvfragment_iv_postimg)
        ImageView mvfragmentIvPostimg;
        @InjectView(R.id.mvfragment_name)
        TextView mvfragmentName;
        @InjectView(R.id.mvfragment_author)
        TextView mvfragmentAuthor;
        @InjectView(R.id.mvfragment_playcount)
        TextView mvfragmentPlaycount;
        @InjectView(R.id.relativelayout_item_rootview)
        RelativeLayout relativelayoutItemRootview;
        @InjectView(R.id.viewbgs)
        View viewbgs;

        public MvItemViewHolder(View itemView) {
            super(itemView);

            // ButterKnife.inject(activity,rootview);
            ButterKnife.inject(activity, itemView);
        }
    }
}
