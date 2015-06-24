package com.hello.learning.wolfpak_camera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
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
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

    int color; // argb value of color to draw with

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

        // @TODO GET VIDEO WORKING PLS
        // Picture
        TakePictureAsyncTask task = new TakePictureAsyncTask();
        task.execute();
//

        // video
//        TakeVideoAsyncTask task = new TakeVideoAsyncTask();
//        task.execute();


        return false;
    }



    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    * @TODO
    * Move this editing stuff into its own class
    *
    * Unfortunately, because the binder for transferring the picture is too small,
    * the editing stuff can't be in its own activity. Even when the picture is compressed.
    *
    * = (
     */


    public void setUpEditing (Bitmap bitmap) {
        setContentView(R.layout.activity_edit_picture);



        ImageView imageView = (ImageView) findViewById(R.id.picHolder);
        imageView.setImageBitmap(bitmap);



    }

    public void onColorButtonClick (View view) {

        ColorPickerFragment fragment = new ColorPickerFragment();
        fragment.show(getFragmentManager().beginTransaction(), "");

    }

    public void setColor(int colorArg) {
        color = colorArg;
        Log.d("Color: ", Integer.toString(color));
    }

    public void onSaveButtonClick (View view) throws Exception {

        Toast.makeText(this, "Saving picture", Toast.LENGTH_SHORT).show();
        getOutputMediaFile(1); // 1 = pics, 2 = video



    }


    private File getOutputMediaFile(int type){

        File mediaStorageDir = new File(getApplicationContext().getFilesDir(), "Wolfpak Pics");

        if (!mediaStorageDir.mkdir()) {
            mediaStorageDir.mkdir();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
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


    private class TakeVideoAsyncTask extends AsyncTask<Void, Void, Void> {

        private File getOutputMediaFile(int type){
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == 1){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");
            } else if(type == 2) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_"+ timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }



        @Override
        protected Void doInBackground(Void... params) {

            mCamera.unlock();

            MediaRecorder mediaRecorder = new MediaRecorder();
            mediaRecorder.setCamera(mCamera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
            mediaRecorder.setOutputFile(getOutputMediaFile(2).toString());
//            mediaRecorder.setPreviewDisplay(surfaceView);

            try {
                mediaRecorder.prepare();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            mediaRecorder.start();

//            record for 10 seconds
//            try {
//                wait(10000);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            mediaRecorder.stop();
            mediaRecorder.release();
            mCamera.lock();




            return null;
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