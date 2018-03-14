package com.weeznn.weeji.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.weeznn.weeji.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlterDialogFragment extends DialogFragment {
    private static final String TAG=AlterDialogFragment.class.getSimpleName();

    private TextInputEditText name;
    private RadioGroup type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_alter_dialog,container,false);
        name=view.findViewById(R.id.dialog_name);
        type=view.findViewById(R.id.radioGroup);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_alter_dialog,null))
                .setPositiveButton(R.string.dialog_postive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=getActivity().getPackageManager()
                                .getLaunchIntentForPackage("com.weeznn.baidu_speech");
                        intent.putExtra("FILE_TYPE",name.getText());
                        switch (type.getCheckedRadioButtonId()){
                            case R.id.dialog_note:
                                intent.putExtra("FILE_NAME","NOTE");
                                break;
                            case R.id.dialog_diary:
                                intent.putExtra("FILE_NAME","DIARY");
                                break;
                            case R.id.dialog_meeting:
                                default:
                                    intent.putExtra("FILE_NAME","MEETING");
                        }

                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.dialog_nagitive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
