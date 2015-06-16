package com.hello.learning.wolfpak_camera;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TakePictureActivity extends Activity implements View.OnTouchListener {

    private Camera mCamera;
    private CameraView mView;

    SurfaceView surfaceView;
    RelativeLayout layout;
    Button takePicture;

    Canvas canvas;

    int fileName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = 0;

        // fullscreen code

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        // end fullscreen code

        takePicture = new Button(this);

        mCamera = Camera.open();


     // add button code
        // init all views to be added

        surfaceView = new CameraView(this);
        surfaceView.setOnTouchListener(this);

         layout = (RelativeLayout) findViewById(R.id.layout); // master layout to hold all views

        if (layout != null) {
            Log.d("Layout: ", "Not null");
        }

        takePicture = (Button) findViewById(R.id.takePictureButton);


        // add the views and set their parameters
            // basically, instead of instantiating it in xml, we are doing it programmatically (like swing)

        //layout.addView(mView);
        //layout.addView(takePicture);


        setContentView(surfaceView);
        //setContentView(R.layout.activity_take_picture);
        //setContentView(layout);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // First, take the picture on a background thread
        // Then, start EditPicture activity

        TakePictureAsyncTask task = new TakePictureAsyncTask();
        task.execute();

        // launch editpicture
        Intent intent = new Intent(this, EditPicture.class);
        startActivity(intent);

        return false;
    }

    private class TakePictureAsyncTask extends AsyncTask <Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    // called as close to picture taking as possible
                    Log.d("Shutter: ", "Pic is being taken");
                }
            };

            Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                }
            };

            // takes the picture

            mCamera.takePicture(shutterCallback, rawPictureCallback, null, null);

//
//
//            //making a folder named picFolder to store pics taken by the camera using this application
//            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
//            File newdir = new File(dir);
//            newdir.mkdirs();
//
//            // the name of the pictures will be 1.jpeg, 2.jpeg, and so on
//            fileName++;
//
//            String file = dir + fileName + ".jpg";
//            File newfile = new File(file);
//
//            try {
//                newfile.createNewFile();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Uri outputFileUri = Uri.fromFile(newfile);
//
//            // take the picture
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//
//
//
//            startActivityForResult(cameraIntent, 0);
//
//
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if pic was saved properly
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Log.d("Camera", "Pic saved");
        }
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