package com.test;

/**
 * Created by Chaance on 4/28/2016.
 */

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.widget.Button;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.widget.EditText;

public class MainActivity extends Activity {

        Intent clickedactivity;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            Button camera = (Button) findViewById(R.id.button);
            camera.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CameraProjectActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }



            public void openCamera(View view){
                Intent intent = new Intent(this, CameraProjectActivity.class);
                EditText editText = (EditText) findViewById(R.id.open_camera);
            }
           // public boolean onCreateOptionsMenu(Menu menu){
                // Inflate the menu
               // getMenuInflater().inflate(R.menu.main, menu);
               // return true;
           // }
    }

