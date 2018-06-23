package com.z.zmusicplayer.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.z.zmusicplayer.R;
import com.z.zmusicplayer.fragment.BaseFragment;
import com.z.zmusicplayer.fragment.MVFragment.MVFragment;
import com.z.zmusicplayer.fragment.mainfragment.MainFragment;
import com.z.zmusicplayer.fragment.VbangFragment;
import com.z.zmusicplayer.fragment.YuedanFragment.YuedanFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.container)
    FrameLayout container;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.bottomBar)
    BottomBar bottomBar;
    private SparseArray<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        initFragment();//在bottombar使用之前初始化 几个fragment
        initBottombar();

    }

    private void initFragment() {
        //android 中特定hashmap的替换者 ，key只能是int类型, key int类型可以省内存,key是对象的话，占用内存比较大
        //插入和hashmap差不多，查找的时候是二分法查找，几万条以下的和hashmap没什么区别
        //稀疏数组存起来
        fragments = new SparseArray<>();
        fragments.append(R.id.tab_main,new MainFragment());
        fragments.append(R.id.tab_mv,new MVFragment());
        fragments.append(R.id.tab_vbang,new VbangFragment());
        fragments.append(R.id.tab_yuedan,new YuedanFragment());


    }

    private void initBottombar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                //BaseFragment baseFragment = fragments.get(tabId);
                //bottombar会帮助先点击第一个条目 .
                FragmentManager manager=MainActivity.this.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container,fragments.get(tabId));
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Toast.makeText(this, "设置被点击了....", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        //toobar里面 的title默认显示工程默认的title
        toolbar.setTitle("");
        title.setText("音乐首页");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.textcolor));
        //toolbar.setTitle("音乐");
        setSupportActionBar(toolbar);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults[0]== PermissionChecker.PERMISSION_GRANTED){
            //initData();
            VbangFragment vbangFragment = (VbangFragment) fragments.get(R.id.tab_vbang);
            vbangFragment.initData();

        }else{
            Toast.makeText(getApplicationContext(), "没有读取sd卡的权限....", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
