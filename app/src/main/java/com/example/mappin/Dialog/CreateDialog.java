package com.example.mappin.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mappin.R;

public class CreateDialog extends Dialog {
    Button btn_create = null;
    Button btn_cancel = null;
    EditText txt_address = null;
    TextView txt_id = null;

    public CreateDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.create_dialog_layout);
        btn_create = findViewById(R.id.btn_create);
        btn_cancel = findViewById(R.id.btn_cancel);
        txt_address = findViewById(R.id.edit_create_address);
        txt_id = findViewById(R.id.txt_create_id);
    }

    public void setCreateClickListener(View.OnClickListener listener){
        btn_create.setOnClickListener(listener);
    }

    public void setCancelClickListener(View.OnClickListener listener){
        btn_cancel.setOnClickListener(listener);
    }

    public String getTxt_address() {
        return txt_address.getText().toString();
    }

    public void setTxt_address(String s) {
        this.txt_address.setText(s);
    }

    public String getTxt_id() {
        return txt_id.toString();
    }

    public void setTxt_id(String s) {
        this.txt_id.setText(s);
    }
}
