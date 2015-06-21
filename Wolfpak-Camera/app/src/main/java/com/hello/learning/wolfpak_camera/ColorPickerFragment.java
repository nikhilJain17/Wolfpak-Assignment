package com.hello.learning.wolfpak_camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

/*
* This is a DialogFragment used to allow the user to select what color they want to draw with
* It also has a line width option
*
*
 */
public class ColorPickerFragment extends DialogFragment {

    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private View colorView;
    private int color;
    private View colorDialogView; // holds the layout of the DialogFragment

    @Override
    public Dialog onCreateDialog (Bundle bundle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose color");
        builder.setCancelable(true);
        colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color, null);
        builder.setView(colorDialogView);

        alphaSeekBar = (SeekBar) colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = (SeekBar) colorDialogView.findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) colorDialogView.findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) colorDialogView.findViewById(R.id.blueSeekBar);

        // attach listeners
        alphaSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangedListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangedListener);

        // button listener to accept color
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // @TODO
                // transfer the color through a SharedPreferences object here

            }
        });

        // initialize the tint bar to be reasonably dark
        // otherwise it is initialized to 0, and the color is white
        alphaSeekBar.setProgress(66);

        return builder.create();

    }

    // SeekBar listener
    private SeekBar.OnSeekBarChangeListener colorChangedListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            // user, not system, changed seekbar values
            if (fromUser) {

                // define the ARGB value for the color from the values of the seekbar
                // the seekbar goes from 0 to 127, and 127 * 2 = 255
                color = Color.argb(alphaSeekBar.getProgress() * 2, redSeekBar.getProgress() * 2,
                        greenSeekBar.getProgress() * 2, blueSeekBar.getProgress() * 2);

                colorDialogView.setBackgroundColor(color);
            }


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



}
