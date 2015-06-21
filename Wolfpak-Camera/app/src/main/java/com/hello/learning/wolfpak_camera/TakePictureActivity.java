package com.hello.learning.wolfpak_camera;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TakePictureActivity extends Activity implements View.OnTouchListener {

    private Camera mCamera;
    private CameraView mView; // object that holds the preview of the camera

    SurfaceView surfaceView;
    RelativeLayout layout;
    Button takePicture;

    // DELETE
    int fileName;

    SharedPreferences pictureSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = 0;

        // init the sharedpreferences
        pictureSharedPrefs = getSharedPreferences("picture", MODE_PRIVATE);

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


        return false;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public void setUpEditing (Bitmap bitmap) {
        setContentView(R.layout.activity_edit_picture);



        ImageView imageView = (ImageView) findViewById(R.id.picHolder);
        imageView.setImageBitmap(bitmap);



    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    private class TakePictureAsyncTask extends AsyncTask <Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {



            Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {


                    Bitmap pictureBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // Create a rotated bitmap
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    Bitmap rotatedBitmap = Bitmap.createBitmap(pictureBitmap , 0, 0, pictureBitmap.getWidth(),
                            pictureBitmap.getHeight(), matrix, true);


//                    mutablePicture.setHeight(getWallpaperDesiredMinimumHeight() / 2);
//                    mutablePicture.setWidth(getWallpaperDesiredMinimumWidth() / 2);
                    setUpEditing(rotatedBitmap);


                    // To pass data through intents, the bitmap must be scaled down
//                   pictureBitmap = scaleDownBitmap(pictureBitmap, 50, getApplicationContext());
//
//                    Intent intent = new Intent(getApplicationContext(), EditPicture.class);
//                    intent.putExtra("pictureKey,", pictureBitmap); // add the picture data here
//                    startActivity(intent);

                }
            };

            // takes the picture

            mCamera.takePicture(null, null, null, jpegPictureCallback);
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



////////////////////////////////////////////////////////////////////////////////////////////////////

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