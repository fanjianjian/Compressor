package com.james.compressor.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.james.compressor.Compressor;
import com.james.compressor.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends AppCompatActivity {

    private TextView tvPicSize1, tvPicSize2;
    private ImageView imgPic1, imgPic2;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPicSize1 = (TextView) findViewById(R.id.tv_pic_size1);
        tvPicSize2 = (TextView) findViewById(R.id.tv_pic_size2);
        imgPic1 = (ImageView) findViewById(R.id.img_pic1);
        imgPic2 = (ImageView) findViewById(R.id.img_pic2);
        progress = (ProgressBar) findViewById(R.id.progress);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(MainActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE && data != null) {
            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

            File imgFile = new File(photos.get(0));
            int[] imgSize = Compressor.get(this).getImageSize(imgFile.getPath());
            tvPicSize1.setText(String.format(Locale.ENGLISH, "原图大小：%1$dk  尺寸：%2$d * %3$d"
                    , imgFile.length() / 1024, imgSize[0], imgSize[1]));

            Glide.with(this).load(imgFile).into(imgPic1);
            doCompress(imgFile);
        }
    }

    private void doCompress(File imgFile) {
        Compressor.get(this)
                .load(imgFile)
                .listener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        progress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(File file) {
                        progress.setVisibility(View.INVISIBLE);

                        int[] imgSize = Compressor.get(MainActivity.this).getImageSize(file.getPath());
                        tvPicSize2.setText(String.format(Locale.ENGLISH, "缩略图大小：%1$dk  尺寸：%2$d * %3$d"
                                , file.length() / 1024, imgSize[0], imgSize[1]));

                        Glide.with(MainActivity.this).load(file).into(imgPic2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.setVisibility(View.INVISIBLE);
                        tvPicSize2.setText("压缩失败了");
                    }
                }).start();
    }
}
