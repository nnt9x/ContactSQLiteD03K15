package com.bkacad.edu.nnt.contactsqlited03k15.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bkacad.edu.nnt.contactsqlited03k15.R;

public abstract class ContactDialog extends Dialog {

    // View
    private EditText edtName, edtPhone;
    private Button btnAdd;
    public ContactDialog(@NonNull Context context) {
        super(context);
    }

    public abstract void insertContact(String name, String phone);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_contact);
        // Bind ID
        edtName = findViewById(R.id.edt_dialog_add_name);
        edtPhone = findViewById(R.id.edt_dialog_add_phone);
        btnAdd = findViewById(R.id.btn_dialog_add);
        // Bat su kien onclick
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lay du lieu tu editxt => gui ve main
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                insertContact(name, phone);
            }
        });
    }
}
