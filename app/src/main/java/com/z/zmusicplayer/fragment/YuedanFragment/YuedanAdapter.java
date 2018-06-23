package com.z.zmusicplayer.fragment.YuedanFragment;

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
import com.z.zmusicplayer.bean.YueDanBean;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by z on 2017/7/9.
 */

public class YuedanAdapter extends RecyclerView.Adapter<YuedanAdapter.YuedanHolder> {
    List<YueDanBean.PlayListsBean> list;
    private Activity activity;
    private int mWidth, mHeight;
    private LayoutInflater inflater;

    public YuedanAdapter(List<YueDanBean.PlayListsBean> list, Activity activity, int mWidth, int mHeight) {
        this.list = list;
        this.activity = activity;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public YuedanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.yuedan_recycler_item, parent,false);
        return new YuedanHolder(view);
    }

    @Override
    public void onBindViewHolder(YuedanHolder holder, int position) {
        YueDanBean.PlayListsBean playListsBean = list.get(position);
        ViewGroup.LayoutParams layoutParams = holder.yuedanItemPostimg.getLayoutParams();
        layoutParams.height=mHeight;
        layoutParams.width=mWidth;
        holder.yuedanItemBgView.setLayoutParams(layoutParams);

        holder.title.setText(playListsBean.getTitle());
        holder.author.setText(playListsBean.getCreator().getNickName());
        holder.yuedanItemPlayCount.setText("共收录MV"+playListsBean.getVideoCount()+"首");
        Glide.with(activity).load(playListsBean.getPlayListBigPic()).into(holder.yuedanItemPostimg);
        Glide.with(activity).load(playListsBean.getThumbnailPic()).into(holder.yuedanItemCivImageview);

    }

    @Override
    public int getItemCount() {
        int count = list == null ? 0 : list.size();
        return count;
    }


    class YuedanHolder extends RecyclerView.ViewHolder {


        // @InjectView(R.id.yuedan_item_postimg)
        ImageView yuedanItemPostimg;
        // @InjectView(R.id.yuedan_item_bgView)
        View yuedanItemBgView;
        // @InjectView(R.id.yuedan_item_civ_imageview)
        CircleImageView yuedanItemCivImageview;
        // @InjectView(R.id.title)
        TextView title;
        // @InjectView(R.id.author)
        TextView author;
        // @InjectView(R.id.yuedan_item_play_count)
        TextView yuedanItemPlayCount;
        // @InjectView(R.id.yuedan_item_rootView)
        RelativeLayout yuedanItemRootView;

        public YuedanHolder(View itemView) {
            super(itemView);
            // itemView.findViewById()
            yuedanItemPostimg = (ImageView) itemView.findViewById(R.id.yuedan_item_postimg);
            yuedanItemBgView=itemView.findViewById(R.id.yuedan_item_bgView);
            yuedanItemCivImageview=(CircleImageView)itemView.findViewById(R.id.yuedan_item_civ_imageview);
            title=(TextView)itemView.findViewById(R.id.title);
            author=(TextView)itemView.findViewById(R.id.author);
            yuedanItemPlayCount=(TextView)itemView.findViewById(R.id.yuedan_item_play_count);
            yuedanItemRootView=(RelativeLayout)itemView.findViewById(R.id.yuedan_item_rootView);

            // ButterKnife.inject(activity,itemView);

        }


    }
}
