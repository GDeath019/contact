package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowAllContact extends AppCompatActivity {
    ListView listContact;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_contact);
        btnBack = (Button) findViewById(R.id.btnBack);

        Uri uri = Uri.parse("content://contacts/people");
        ArrayList<String> strings = new ArrayList<>();
        CursorLoader loader = new CursorLoader(this,uri,null,null,null,null);
        Cursor cursor = loader.loadInBackground();
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String s="";
            String idColumnName = ContactsContract.Contacts._ID;
            int idIndex = cursor.getColumnIndex(idColumnName);
            s+=cursor.getString(idIndex)+"  --  ";
            String nameColumnName = ContactsContract.Contacts.DISPLAY_NAME;
            int idNameIndex = cursor.getColumnIndex(nameColumnName);
            s+=cursor.getString(idNameIndex)+"  --  ";
            String numberColumnIndex = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            int numberIndex = cursor.getColumnIndex(numberColumnIndex);
            if (Integer.valueOf(numberIndex)==1){
                Cursor number = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+ cursor.getString(idIndex),
                        null,
                        null);
                if (number.getCount() > 1){
                    int i=0;
                    String[] phoneNum = new String[number.getCount()];
                    while (number.moveToNext()) {
                        phoneNum[i] = number.getString(number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        s+=phoneNum[i];
                        i++;
                    }
                }else if (number.getCount()==1){
                    number.moveToFirst();
                    s+=number.getString(number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }else {
                Cursor number = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+ cursor.getString(idIndex),
                        null,
                        null);
                if (number.getCount() > 1){
                    int i=0;
                    String[] phoneNum = new String[number.getCount()];
                    while (number.moveToNext()) {
                        phoneNum[i] = number.getString(number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        s+="number:"+phoneNum[i]+"\r\n";
                        i++;
                    }
                }else if (number.getCount()==1){
                    number.moveToFirst();
                    s+=number.getString(number.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }
            cursor.moveToNext();
            strings.add(s);
        }
        listContact = (ListView) findViewById(R.id.lvContact);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, strings);
        listContact.setAdapter(arrayAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAllContact.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
