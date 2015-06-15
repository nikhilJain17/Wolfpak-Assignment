package com.hello.learning.wolfpak_camera;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TakePictureActivity extends Activity {

    private Camera mCamera;
    private CameraView mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCamera = Camera.open();
        mView = new CameraView(this);

        setContentView(mView);
    }

    // extending SurfaceView to render the camera images
    private class CameraView extends SurfaceView implements SurfaceHolder.Callback{
        private SurfaceHolder mHolder;

        public CameraView(Context context) {
            super(context);

            mHolder = this.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            setFocusable(true);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                mCamera.release();
            }
            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            mCamera.stopPreview();
            mCamera.release();

        }

    }

}