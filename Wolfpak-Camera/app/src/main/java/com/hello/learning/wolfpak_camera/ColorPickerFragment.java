package com.hello.learning.wolfpak_camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

    @Override
    public Dialog onCreateDialog (Bundle bundle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    }



}
