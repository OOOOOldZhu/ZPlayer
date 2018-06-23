package com.z.zmusicplayer.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.z.zmusicplayer.bean.MusicItem;
import com.z.zmusicplayer.MusicPlayerActivity;
import com.z.zmusicplayer.R;
import com.z.zmusicplayer.adapter.MusicAdapter;
import com.z.zmusicplayer.db.MyAsyncQueryHandler;

import java.util.ArrayList;

/**
 * Created by z on 2017/6/25.
 */

public class VbangFragment extends BaseFragment {
    private MusicAdapter adapter;
    private ListView listView;
    //recyclerview 官方不支持 cusor adapter

    @Override
    protected int getLayoutId() {
        return R.layout.vbang;
    }

    @Override
    protected void init() {
        adapter = new MusicAdapter(getContext(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView = (ListView) rootView.findViewById(R.id.iv_music_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取游标，把游标数据转换成集合 传递给下一个activity
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(-1);
                ArrayList<MusicItem> musicItemList=new ArrayList<MusicItem>();
                while (cursor.moveToNext()){
                    musicItemList.add(MusicItem.getMusicFromCursor(cursor));

                }
                Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                intent.putExtra("musics",musicItemList);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(VbangFragment.this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                //用户没给权限及会挂掉
                return;
            }
        }
        initData();
    }
    //如果在fragment中编写回调，用户拒绝的时候不会走吐司的方法，必须在activity中获取回调函数
    /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
            initData();
        }else{
            Toast.makeText(getContext(), "没有读取sd卡的权限....", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    public void initData() {
        ContentResolver contentResolver = getContext().getApplicationContext().getContentResolver();
        //uri 系统已经封装好了
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        /*public final @Nullable Cursor query(@RequiresPermission.Read @NonNull Uri uri,
                @Nullable String[] projection, @Nullable String selection,
                @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            return query(uri, projection, selection, selectionArgs, sortOrder, null);
        }*/
        //Media._ID,这个必须查， 不查不然会报错
        //之前是同步的查询，在主线程中属于耗时操作，可能导致ANR
        //Cursor cursor = contentResolver.query(uri, new String[]{Media._ID,Media.DATA, Media.ARTIST, Media.SIZE, Media.DURATION, Media.TITLE}, null, null, null);


        //开启一个异步查询，
        MyAsyncQueryHandler handler = new MyAsyncQueryHandler(contentResolver);
        handler.startQuery(1, adapter, uri, new String[]{Media._ID, Media.DATA, Media.ARTIST, Media.SIZE, Media.DURATION, Media.TITLE}, null, null, null);
        //CursorUtils.printCusor(cursor);
        //adapter.changeCursor(cursor); //把之前的cursor关闭了
        //adapter.swapCursor(cursor)  //之前的cursor不会关闭
    }


}
