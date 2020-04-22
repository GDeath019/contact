package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnAllContact, btnCallLog, btnMediaStore, btnBookMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        btnAllContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowAllContact.class);
                startActivity(intent);
            }
        });
        btnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] projection = new String[]{
                        CallLog.Calls.DATE,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.DURATION
                };
                Uri uri = CallLog.Calls.CONTENT_URI;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    Cursor cursor = getContentResolver().query(
                            uri,
                            projection,
                            CallLog.Calls.DURATION + "<?", new String[]{"30"},
                            CallLog.Calls.DATE + "asc"
                    );
                        cursor.moveToFirst();
                        String s = "";
                        while (cursor.isAfterLast() == false) {
                            for (int i = 0; i < cursor.getColumnCount(); i++) {
                                s += cursor.getString(i) + " --  ";
                            }
                            s += "\n";
                            cursor.moveToNext();
                        }
                        cursor.close();
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "NULL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnMediaStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] projection = new String[]{
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.DATE_ADDED,
                        MediaStore.MediaColumns.MIME_TYPE
                };
                CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                if(cursor.getCount()==0){
                    Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();
                String s = "";
                while (!cursor.isAfterLast()){
                    for (int i=0;i<cursor.getColumnCount();i++){
                        s+= cursor.getString(i)+"  --  ";
                    }
                    s+="\n";
                    cursor.moveToNext();
                }
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        });
        btnBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void anhXa() {
        btnAllContact = (Button) findViewById(R.id.allContact);
        btnCallLog = (Button) findViewById(R.id.callLog);
        btnMediaStore = (Button) findViewById(R.id.mediaStore);
        btnBookMark = (Button) findViewById(R.id.bookMarks);
    }
}
