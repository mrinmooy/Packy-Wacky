package com.salazarisaiahnoel.pacmanandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mp = MediaPlayer.create(this, R.raw.pacman_intro);

        ImageView iv1 = findViewById(R.id.iv1);

        Glide.with(this)
                .asGif()
                .load(R.drawable.pacman)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv1);

        Button play = findViewById(R.id.btnPlay);
//        Button settings = findViewById(R.id.btnSettings);
        Button about = findViewById(R.id.btnAbout);
        Button exit = findViewById(R.id.btnExit);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Play.class);
                startActivity(i);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), About.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Fullscreen.enableFullscreen(this);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
}