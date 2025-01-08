package com.bkacad.edu.nnt.contactsqlited03k15.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bkacad.edu.nnt.contactsqlited03k15.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact.db";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng
        String sql = "CREATE TABLE contacts(\n" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                " name TEXT NOT NULL,\n" +
                " phone TEXT NOT NULL,\n" +
                " email TEXT\n" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Co the de trong
    }

    public void insertContact(Contact contact) {
        String sql = "insert into contacts VALUES (NULL,'" + contact.getName() + "','" + contact.getPhone() + "','" + contact.getEmail() + "')";
        // Thuc thi
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public List<Contact> getAllContacts() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM contacts";
        // Thuc thi
        Cursor cursor = db.rawQuery(sql, null);

        List<Contact> contacts = new ArrayList<>();
        while(cursor.moveToNext()) {
            // Tao doi tuong contact -> ung voi ban ghi contact (row)
            Contact contact = new Contact();
            contact.setId(cursor.getLong(0));
            contact.setName(cursor.getString(1));
            contact.setPhone(cursor.getString(2));
            contact.setEmail(cursor.getString(3));

            contacts.add(contact);
        }
        cursor.close();

        return contacts;

    }


}
