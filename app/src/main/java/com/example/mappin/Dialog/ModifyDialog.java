package com.example.mappin.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mappin.R;

public class ModifyDialog extends Dialog {
    Button btn_update = null;
    Button btn_delete = null;
    EditText txt_address = null;
    TextView txt_id = null;

    public ModifyDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.modify_dialog_layout);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        txt_address = findViewById(R.id.edit_modify_address);
        txt_id = findViewById(R.id.txt_modify_id);
    }

    public void setUpdateClickListener(View.OnClickListener listener){
        btn_update.setOnClickListener(listener);
    }

    public void setDeleteClickListener(View.OnClickListener listener){
        btn_delete.setOnClickListener(listener);
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
