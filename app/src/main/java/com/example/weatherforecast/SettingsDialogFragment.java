package com.example.weatherforecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.weatherforecast.modelCurrentWeather.OnDialogListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingsDialogFragment extends BottomSheetDialogFragment{

    private OnDialogListener dialogListener;

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_dialogfragment, container,
                false);

        setCancelable(false);

        view.findViewById(R.id.btnC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (dialogListener != null) dialogListener.onDialogC();
            }
        });

        view.findViewById(R.id.btnF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (dialogListener != null) dialogListener.onDialogF();
            }
        });

        return view;

    }

    public void setOnDialogListener(OnDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

}

