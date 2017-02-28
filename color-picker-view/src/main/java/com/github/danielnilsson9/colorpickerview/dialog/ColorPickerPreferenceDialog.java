package com.github.danielnilsson9.colorpickerview.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.github.danielnilsson9.colorpickerview.R;
import com.github.danielnilsson9.colorpickerview.view.ColorWheelView;


public class ColorPickerPreferenceDialog  extends DialogFragment implements ColorWheelView.OnColorChangedListener{

    public interface ColorPickerDialogListener {
        void onColorSelected(int color);
    }


    private int mSelectedColorValue;
    private int mNewSelectedColorValue;

    private ColorPickerDialogListener mColorPickerDialogListener;


    public static ColorPickerPreferenceDialog newInstance(int selectedColorValue) {
        Bundle args = new Bundle();
        args.putInt("SELECTED_COLOR", selectedColorValue);


        ColorPickerPreferenceDialog dialog = new ColorPickerPreferenceDialog();
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mSelectedColorValue = args.getInt("SELECTED_COLOR", 0);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.colorpickerview__dialog_color_picker, null);


        ColorWheelView wheel = (ColorWheelView) dialogView.findViewById(R.id.colorpickerview__color_wheel);

        if (mSelectedColorValue == 0){
            wheel.setOldCenterColor(Color.parseColor("#FF9900"));
            wheel.setColor(Color.parseColor("#FF9900"));
        } else {
            wheel.setOldCenterColor(mSelectedColorValue);
            wheel.setColor(mSelectedColorValue);
        }

        wheel.setOnColorChangedListener(this);


        return new AlertDialog.Builder(getActivity())
                .setTitle("Select a Color")
                .setView(dialogView)
                .setPositiveButton("OK", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mColorPickerDialogListener.onColorSelected(mNewSelectedColorValue);
                                dismiss();
                            }
                        })
                .setNegativeButton("Cancel", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        })
                .create();
    }


    @Override
    public void onColorChanged(int color) {
        mNewSelectedColorValue = color;
        System.out.println(color);
    }

    public void setColorPickerDialogListener(ColorPickerDialogListener colorPickerDialogListener){
        this.mColorPickerDialogListener = colorPickerDialogListener;
    }
}
