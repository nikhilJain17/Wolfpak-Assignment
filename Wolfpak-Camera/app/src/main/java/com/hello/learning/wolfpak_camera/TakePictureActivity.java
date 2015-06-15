package com.hello.learning.wolfpak_camera;

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class TakePictureActivity extends Activity {

    private Camera mCamera;
    private CameraView mView;
    Parameters parameters; // specify size of preview (prevents stretching)


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fullscreen code

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // end fullscreen code

        mCamera = Camera.open();
        mView = new CameraView(this);


        setContentView(mView);
    }

    // extending SurfaceView to render the camera images
    private class CameraView extends SurfaceView implements SurfaceHolder.Callback {
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

                mCamera.setDisplayOrientation(90); // set proper orientation (got rid of stretching)
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