package com.artyushin.hw132;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private String fileName = "data.txt";
    private String login = "";
    private String password = "";
    private String readLogin = "";
    private String readPassword = "";

    private int storageLocation;
    private SharedPreferences prefs;
    private final static String PREF_NAME = "ST_PREF";
    private final static String PREF_KEY = "ST_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        prefs = getSharedPreferences (PREF_NAME, MODE_PRIVATE);
        storageLocation = prefs.getInt (PREF_KEY, 0);

        EditText etLogin = findViewById (R.id.login);
        EditText etPassword = findViewById (R.id.password);
        Button bLogin = findViewById (R.id.bLogin);
        Button bReg = findViewById (R.id.bReg);
        CheckBox checkBox = findViewById (R.id.checkBox);

        if (storageLocation == 1) { checkBox.setChecked (true); }

        checkBox.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked ( )) {
                    storageLocation = 1;
                } else {
                    storageLocation = 0;
                }
                prefs.edit ( ).putInt (PREF_KEY, storageLocation).apply ( );
            }
        });

        bLogin.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                login = etLogin.getText ( ).toString ( );
                password = etPassword.getText ( ).toString ( );

                loadFile(pathFile());

                if (!login.equals (readLogin)) {
                    Toast.makeText (MainActivity.this, "Invalid login!", Toast.LENGTH_LONG).show ( );
                } else if (!password.equals (readPassword)) {
                    Toast.makeText (MainActivity.this, "Invalid password!", Toast.LENGTH_LONG).show ( );
                } else {
                    Toast.makeText (MainActivity.this,
                            "Successfully username and password match the data!", Toast.LENGTH_LONG).show ( );
                }
            }
        });

        bReg.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                login = etLogin.getText ( ).toString ( );
                password = etPassword.getText ( ).toString ( );

                String strData = login + "\n" + password;
                writeDataFile(strData, pathFile());

//                Toast.makeText (MainActivity.this, "Registered!", Toast.LENGTH_LONG).show ( );
            }
        });
    }

    public String pathFile() {
        File directory;
        if (storageLocation == 1) {
            directory = getExternalFilesDir(null);
        } else {
            directory = getFilesDir();
        }
        String path = directory.getAbsolutePath() + "/" + fileName;
        return path;
    }

    public void writeDataFile(String strData, String path){

        if (login.equals ("") || password.equals ("")) {
            Toast.makeText (MainActivity.this, "No data entered!", Toast.LENGTH_LONG).show ( );
        } else {
            File file = new File(path);

            Toast.makeText (MainActivity.this, "Save to file: \n"
                    + file.toString (), Toast.LENGTH_LONG).show ( );

            FileWriter fw = null;
            try {
                fw = new FileWriter (file);
                fw.write (strData);
            } catch (Exception ex) {
                ex.printStackTrace ( );
            } finally {
                try {
                    fw.close ();
                } catch (IOException e) {
                    e.printStackTrace ( );
                }
            }
        }
    }

    public void loadFile(String path) {
        if (login.equals ("") || password.equals ("")) {
            Toast.makeText (MainActivity.this, "No data entered!", Toast.LENGTH_LONG).show ( );
        } else {
            try {
                File file = new File(path);

                Toast.makeText (MainActivity.this, "Read file: \n"
                        + file.toString (), Toast.LENGTH_LONG).show ( );

                FileInputStream fIn = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader (fIn));
                readLogin = reader.readLine();
                readPassword = reader.readLine();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}