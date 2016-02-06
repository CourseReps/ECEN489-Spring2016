// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		MainActivity.java
// Version:			1.0.0
// Date:			January 29, 2016
// Description:	    Task #3 - Java JDBC on Android
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           January 29, 2016  	         Initial Release
//  --------------------------------------------------------------------------------------------------------------------


//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------
package com.lusherengineering.sqldatabaseapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.database.sqlite.*;
import android.content.Context;
import android.app.AlertDialog.Builder;
import android.view.View.OnClickListener;
import android.database.Cursor;

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    MainActivity
//  Description:	MainActivity class for project
//  --------------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements OnClickListener {

    //
    // Create instance of database, edit fields, and buttons
    SQLiteDatabase db;
    EditText editAuthorID,editLastName,editFirstName;
    Button btnAdd, btnView;

    @Override
    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     onCreate (OVERRIDE)
    //      Inputs:	    savedInstanceState
    //     Outputs:	    none
    //  Description:    onCreate Event
    //	----------------------------------------------------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Add Button
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        // View Data
        btnView=(Button)findViewById(R.id.btnView);
        btnView.setOnClickListener(this);

        // Edit Text Boxes
        editAuthorID=(EditText)findViewById(R.id.editAuthorID);
        editLastName=(EditText)findViewById(R.id.editLastName);
        editFirstName=(EditText)findViewById(R.id.editFirstName);


        // Hello Button
        final Button button = (Button) findViewById(R.id.hellobutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView t = (TextView)findViewById(R.id.textViewhello);
                t.setText("Hello, this is a test");
            }
        });

        db=openOrCreateDatabase("testDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS authors(authorID smallint, firstName varchar(30), lastName varchar(30));");

    }

    //	----------------------------------------------------------------------------------------------------------------
    //    Function:     onClick
    //      Inputs:	    View
    //     Outputs:	    none
    //  Description:    onClick Event
    //	----------------------------------------------------------------------------------------------------------------
    public void onClick(View view)
    {
        if (view == btnAdd) {
            if (editAuthorID.getText().toString().trim().length() == 0 ||
                    editLastName.getText().toString().trim().length() == 0 ||
                    editFirstName.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO authors VALUES('" + editAuthorID.getText() + "','" + editFirstName.getText() +
                    "','" + editLastName.getText() + "');");
            showMessage("Success", "Record added");
            clearText();
        }
        else if (view == btnView) {
            Cursor c = db.rawQuery("SELECT authorID, firstName, lastName FROM authors;", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("ID: "+c.getString(0)+"\n");
                buffer.append("First Name: "+c.getString(1)+"\n");
                buffer.append("Last Name: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText()
    {
        editAuthorID.setText("");
        editLastName.setText("");
        editFirstName.setText("");
        editAuthorID.requestFocus();
    }
}
