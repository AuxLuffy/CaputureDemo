package com.example.sunzh.caputuredemo;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.sunzh.caputuredemo.utils.ScreenUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView mSv;
    private Button mBtnCamera;
    private Camera mCamera;
    private SurfaceHolder sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        setListeners();
    }

    private void setListeners() {
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前相机参数
                Camera.Parameters parameters = mCamera.getParameters();
                //设置相片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置预览大小
                parameters.setPreviewSize(ScreenUtils.getScreenWidth(CameraActivity.this), ScreenUtils.getScreenHeight(CameraActivity.this));
                //设置对焦方式，这里设置为自动对焦
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        //判断是否对焦成功
                        if (b) {
                            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(byte[] bytes, Camera camera) {
                                    File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "img" + File.separator + System.currentTimeMillis() + ".jpg");
                                    try {
                                        file.createNewFile();
                                        FileOutputStream fos = new FileOutputStream(file);
                                        try {
                                            fos.write(bytes);
                                            fos.close();
                                            Intent intent = new Intent(CameraActivity.this, PhotoActivity.class);
                                            intent.putExtra("path", file.getAbsolutePath());
                                            Log.e("TAG", "拍照完成");
                                            startActivity(intent);
                                            finish();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        mSv = (SurfaceView) findViewById(R.id.sv);
        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mCamera = getCamera();
        sh = mSv.getHolder();
        sh.addCallback(this);
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 预览
     *
     * @param camera
     * @param holder
     */
    private void showViews(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //系统相机默认是横屏的，我们要旋转90度
            camera.setDisplayOrientation(90);
            //开始预览
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        showViews(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        showViews(mCamera, sh);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 释放相机
     */
    private void clearCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera();
            if (sh != null) {
                showViews(mCamera, sh);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearCamera();
    }
}
