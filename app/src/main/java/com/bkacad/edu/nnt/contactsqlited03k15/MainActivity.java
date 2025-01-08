package com.bkacad.edu.nnt.contactsqlited03k15;

import android.content.DialogInterface;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bkacad.edu.nnt.contactsqlited03k15.database.ContactDBHelper;
import com.bkacad.edu.nnt.contactsqlited03k15.dialog.ContactDialog;
import com.bkacad.edu.nnt.contactsqlited03k15.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtSearch;
    private Button btnSearch;
    private ListView lvContacts;
    // Dữ liệu và adapter
    private List<String> data;

    private List<Contact> contacts; // DB
    private ArrayAdapter adapter;

    // Tao them dialog
    private ContactDialog contactDialog = null;

    // Contact DBHelper
    private ContactDBHelper dbHelper;

    private FloatingActionButton floatingActionButton;

    private AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Bind Id
        lvContacts = findViewById(R.id.lv_contacts);
        btnSearch = findViewById(R.id.btn_search);
        edtSearch = findViewById(R.id.edt_search);
        floatingActionButton = findViewById(R.id.btn_add);
        // Dữ liệu
        data = new ArrayList<>();

        // Du lieu can lay tu SQLite
        dbHelper = new ContactDBHelper(MainActivity.this);
        contacts = dbHelper.getAllContacts();
        // Thêm dữ liệu mặc định nếu SQLite chưa có dữ liệu
        if(contacts.size() == 0){
            dbHelper.insertContact(new Contact("Contact 1", "19182828"));
            dbHelper.insertContact(new Contact("Contact 2", "19182829"));
            dbHelper.insertContact(new Contact("Contact 3", "191828210"));

            contacts = dbHelper.getAllContacts();
        }
        // Chuyen sang list string de hien thi listview
        for (Contact contact: contacts) {
            data.add(contact.getName()+" - " + contact.getPhone());
        }

        // Adapter
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,data);

        lvContacts.setAdapter(adapter);

        // Setonclick
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactDialog == null){
                    contactDialog = new ContactDialog(MainActivity.this) {
                        @Override
                        public void insertContact(String name, String phone) {
                            // B1: insert vao db
                            dbHelper.insertContact(new Contact(name, phone));
                            // B2: reset data
                            data.clear();
                            // B3: lay du lieu tu db -> them vao data
                            List<Contact> contacts = dbHelper.getAllContacts();
                            for (Contact contact: contacts) {
                                data.add(contact.getName()+" - " + contact.getPhone());
                            }
                            // B4: Thong bao cho adapter biet du lieu thay doi
                            adapter.notifyDataSetChanged();
                            dismiss();
                        }
                    };
                }
                contactDialog.show();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // B1: Lay du lieu tu editText
                String kw = edtSearch.getText().toString();
//
                // B2: Truy van CSDL
                contacts = dbHelper.getContactsWithCondition(kw);
                // B3: Hien Thi
                data.clear();
                for (Contact contact: contacts) {
                    data.add(contact.getName()+" - " + contact.getPhone());
                }
                adapter.notifyDataSetChanged();

            }
        });

        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(alertDialog == null){
                    alertDialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Cảnh báo")
                            .setMessage("Bạn có muốn xoá?")
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contact contact = contacts.get(position);
                                    dbHelper.deleteContact(contact.getId());
                                    // Lay nguoc du lieu tu db
                                    contacts = dbHelper.getAllContacts();
                                    data.clear();
                                    for (Contact c: contacts) {
                                        data.add(c.getName()+" - " + c.getPhone());
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            })
                            .create();
                }
                alertDialog.show();

                return false;
            }
        });

    }
}