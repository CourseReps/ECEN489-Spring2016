package example.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    /* Encrypts the message before send-off*/
    public String Encrypt(String msg, int shift){
        String s = "";
        int len = msg.length();
        for(int x = 0; x < len; x++){
            char c = (char)(msg.charAt(x) + shift);
            if (c > 'z')
                s += c-26;
            else
                s += c;
        }
        return s;
    }

    /* This is simply used for decryption of the scanned message */
    public String Decrypt(String msg, int shift){
        String s = "";
        int len = msg.length();
        for(int x = 0; x < len; x++){
            char c = (char)(msg.charAt(x) - shift);
            if (c < 'a')
                s += c+26;
            else
                s += c;
        }
        return s;
    }

    /* This method will turn the QR image into a Byte Array for transport over http */
    private String readyPhoto(Bitmap bitmapm) {
        String output = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] byteArrayImage = baos.toByteArray();
            output = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Button;
        final EditText Message, Name, Receviers;
        final ImageView imageView = (ImageView) findViewById(R.id.qrCode);

        //String qrData = "Data I want to encode in QR code";
        Button = (Button) findViewById(R.id.createButton);
        Message = (EditText) findViewById(R.id.message);
        Name = (EditText) findViewById(R.id.name);
        Receviers = (EditText) findViewById(R.id.recipients);

        /* This is a click Listener that generates the JSON object from user input, encrypts the
        message, and then generates the QR messsage on the screen
         */
        assert Button != null;
        Button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        int qrCodeDimention = 500;

                        assert Message!= null;
                        assert Name!= null;
                        assert Receviers!= null;

                        JSONObject finalMessage = new JSONObject();
                        JSONArray recArr = new JSONArray();

                        for (String retval : Receviers.getText().toString().split(" ")){
                            recArr.put(retval);
                        }
                        recArr.put(Name.getText().toString());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String currentDateandTime = sdf.format(new Date());

                        String encoded = Encrypt(Message.getText().toString(),5);
                        try {
                            finalMessage.put("Sender", Name.getText());
                            finalMessage.put("Receivers", recArr);
                            finalMessage.put("Message", encoded);
                            finalMessage.put("Time", currentDateandTime);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println(finalMessage.toString());
                        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(finalMessage.toString(), null,
                                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

                        try {
                            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                            assert imageView != null;
                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }


                        Thread t = new Thread(){
                            public void run()
                            {
                                uploadThread();
                            }
                        };
                        t.start();
                    }
                });
    }

    /* This is a NOT FULLY WORKING method to add the generated QR message to the web application */

    private void uploadThread(String imagePath) throws ParseException, IOException {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://localhost:3306/chat");

        FileBody filebodyImage = new FileBody(new File(imagePath));
        StringBody title = new StringBody("Filename: " + imagePath);
        StringBody description = new StringBody("This is a description of the image");

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("imageFile", filebodyVideo);
        reqEntity.addPart("title", title);
        reqEntity.addPart("description", description);
        httppost.setEntity(reqEntity);

        // DEBUG
        System.out.println( "executing request " + httppost.getRequestLine( ) );
        HttpResponse response = httpclient.execute( httppost );
        HttpEntity resEntity = response.getEntity( );

        // DEBUG
        System.out.println( response.getStatusLine( ) );
        if (resEntity != null) {
            System.out.println( EntityUtils.toString( resEntity ) );
        } // end if

        if (resEntity != null) {
            resEntity.consumeContent( );
        } // end if

        httpclient.getConnectionManager( ).shutdown( );
    } // end of uploadVideo( )

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void scanBarcodeCustomLayout(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan something");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void scanBarcodeFrontCamera(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        integrator.initiateScan();
    }

    public void scanContinuous(View view) {
        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
        startActivity(intent);
    }

    public void scanToolbar(View view) {
        new IntentIntegrator(this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
    }

    public void scanCustomScanner(View view) {
        new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();
    }

    public void scanMarginScanner(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.initiateScan();
    }


    /* This method will display that scanned message IF THE USER IS AUTHORIZED. This method only
    checks the user's name from a hard-coded string. THERE IS NO SQL OR OTHER DATABASE backing
    this up like there is for the Web Application.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        EditText Name = (EditText) findViewById(R.id.name);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                JSONObject oJSON = null;
                JSONArray aJSON = null;
                boolean match = false;
                try {
                    oJSON = new JSONObject(result.getContents());
                    aJSON = oJSON.getJSONArray("Receivers");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < aJSON.length(); i++) {
                    System.out.println(String.valueOf(match));
                    try {
                        if (aJSON.getString(i).equals(Name.getText().toString()) ) {
                            match = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (match) {
                    try {
                        Toast.makeText(this, "Scanned: " + Decrypt(oJSON.getString("Message"),3), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Unauthorized", Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    /**
     * Sample of scanning from a Fragment
     */
    public static class ScanFragment extends Fragment {
        private String toast;

        public ScanFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            displayToast();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanFromFragment();
                }
            });
            return view;
        }

        public void scanFromFragment() {
            IntentIntegrator.forSupportFragment(this).initiateScan();
        }

        private void displayToast() {
            if(getActivity() != null && toast != null) {
                Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
                toast = null;
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    toast = "Cancelled from fragment";
                } else {
                    toast = "Scanned from fragment: " + result.getContents();
                }

                // At this point we may or may not have a reference to the activity
                displayToast();
            }
        }
    }
}
