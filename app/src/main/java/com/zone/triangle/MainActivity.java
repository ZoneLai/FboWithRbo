package com.zone.triangle;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {
    private GLSurfaceView mGLSurfaceView;
    private TriangleRenderer mTriangleRenderer;
    private ImageView mImageView;
    private Button mButton;
    private String mImgPath;

    TriangleSurfaceView mTriangleSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initView();
        mTriangleSurfaceView = new TriangleSurfaceView(this, null);
        setContentView(mTriangleSurfaceView);
    }

    private void initView() {
        mGLSurfaceView = findViewById(R.id.gl_view);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mTriangleRenderer = new TriangleRenderer(this);
        mGLSurfaceView.setRenderer(mTriangleRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mImageView = findViewById(R.id.iv_photo);
        mButton = findViewById(R.id.bt_select);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            mImgPath = c.getString(columnIndex);
            Bitmap bmp = BitmapFactory.decodeFile(mImgPath);
//            mTriangleRenderer.setBitmap(bmp);
            mGLSurfaceView.requestRender();
            c.close();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTriangleSurfaceView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTriangleSurfaceView.onDestroy();
    }
}
