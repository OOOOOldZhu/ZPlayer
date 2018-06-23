package com.z.zmusicplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.z.zmusicplayer.R;

/**
 * Created by z on 2014/6/27.
 */
//CursorAdapter也是继承的baseadapter
public class MusicAdapter extends CursorAdapter {

    //baseadapter中基本的四个参数 getCount() getId() getItemid() getView()
    /*public MusicAdapter(Context context, Cursor c) {
        super(context, c);
    }*/
    //自动重新查询
    public MusicAdapter(Context context, Cursor c, boolean autoRequery) {
        //最后一个参数 自动重新查询 当设置为true  cursor的内容发生变化的时候
        //会自动调用 requery方法更新界面 不推荐传递true
        //也不推荐使用这个构造,而是使用下面的构造  最后一个参数传递 flag_register_content_observer
        super(context, c, autoRequery);
    }

    public MusicAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //getView() 细分了一下，成下面两个方法 ,
    //getVeiw(){
// if(convertView==null){
// v=newView(mContext,mCursor,parent)}
// else{
// v=convertView;}
//   bindVeiw();
// }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //判断了convertView是否为空， 如果为空会执行这个方法
        View view = LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    //把数据设置到item 上
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //在getView之中调用，cursor存储了数据集合 ,是传进来的
        //cursor.get
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            return;
        }
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        Long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        String fileSize = Formatter.formatFileSize(context, size);
        /*if(cursor.getString(0).contains("kgmusic")){
           return;
        }*/
        if (title != null) {
            if (title.length() > 10) {
                title = title.substring(0, 9);
            }
            viewHolder.title.setText(title);
        }

        if (artist != null) {
            viewHolder.artist.setText(artist);
        }
        if (size != 0 && fileSize != null) {
            viewHolder.size.setText(fileSize);
        }

    }


    static class ViewHolder {

        private final TextView title;
        private final TextView artist;
        private final TextView size;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.tv_title);
            artist = (TextView) view.findViewById(R.id.tv_artist);
            size = (TextView) view.findViewById(R.id.tv_size);
        }
    }
}
