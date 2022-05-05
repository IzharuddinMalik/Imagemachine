package com.imagemachine.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imagemachine.R;

public class CustomToast {

    public void customToast(Context context, String message) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View customLayout = inflater.inflate(R.layout.custom_toast, null);
        TextView textView = customLayout.findViewById(R.id.tvToastMessage);
        textView.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(customLayout);
        toast.show();
    }
}
