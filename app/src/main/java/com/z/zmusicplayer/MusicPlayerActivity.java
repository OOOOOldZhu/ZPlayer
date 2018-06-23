package com.z.zmusicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.z.zmusicplayer.bean.MusicItem;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        ArrayList<MusicItem> musics = (ArrayList<MusicItem>) getIntent().getSerializableExtra("musics");
        int position = this.getIntent().getIntExtra("position",0);//position
        //int position = Integer.parseInt();
        Toast.makeText(this,musics.get(position).title, Toast.LENGTH_SHORT).show();


    }


}
