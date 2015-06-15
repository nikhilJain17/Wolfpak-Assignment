package com.hello.learning.wolfpak_camera;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class TakePictureActivity extends Activity {

    String logTag;

    Camera camera;
    SurfaceView surfaceView; // where the camera displays stuff on the screen

    CameraView cameraView; // used to get camera preview

    SurfaceHolder surfaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        // for logging stuff
        logTag = "TakePictureActivity";

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        // make the app full screen
        ActionBar actionBar = getActionBar();
        try {
            actionBar.hide();
        }

        catch (NullPointerException e) {
            e.printStackTrace();
        }
//
        //cameraView = new CameraView(this);

      //  setContentView(cameraView);
//
//        try {
//            // null object error....
//            surfaceHolder = surfaceView.getHolder();
//        }
//        catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            camera.setPreviewDisplay(surfaceHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onResume () {

        cameraView = new CameraView(this);
        setContentView(cameraView);
//
//        try {
//            // null object error....
//            surfaceHolder = surfaceView.getHolder();
//        }
//        catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            camera.setPreviewDisplay(surfaceHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }



private class CameraView extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;

    public CameraView(Context context) {
        super(context);

        mHolder = surfaceView.getHolder();
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
            camera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            camera.release();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        camera.stopPreview();
        camera.release();

        }

    }
}
