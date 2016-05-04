package com.example.fanchaozhou.project3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static com.googlecode.javacv.cpp.opencv_core.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by Fanchao Zhou on 4/2/2016.
 */

public class ImageFragment extends Fragment implements SensorEventListener{

    public static final int REQUEST_TYPE_LIST_DIALOG = 3;
    public static final int REQUEST_ADDING_TYPES_DIALOG = 2;            //The request code for returning user's choice from dialog
    public static final int REQUEST_IMAGE_CAPTURE = 1;                  //The request code for taking a photo
    private static final float LIGHT_THRESHOLD = 1.0f;
    private static final String NO_CAMERA_HINT = "Camera NOT Avalaible";   //The hint for an unavalaible camera
    private static final String ADD_TYPE_DIALOG_TAG = "Adding Types Dialog";//Tag for the dialog when switching from running to training
    private static final String TYPE_LIST_DIALOG_TAG = "Type List Dialog";
    private static final int MIN_PHOTOS = 7;
    private static final int MAX_TX = 10;
    private static final int FACE_LEN = 650;
    private Uri photoURI;
    private Bitmap thumbnailImage;
    private RecordDBHelper dbHelper;
    private SensorManager sensorManager;
    private Sensor lightSensor = null;
    private float illumination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);      //Allows to create menu on this fragment
        dbHelper = new RecordDBHelper(getActivity());
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        final Switch runTrainSwitch = (Switch)rootView.findViewById(R.id.switch_traing_or_run);
        if(runTrainSwitch != null){
            String mode = MainActivity.isInTraining?getString(R.string.training_on):getString(R.string.running_on);
            runTrainSwitch.setChecked(!MainActivity.isInTraining);                  //By default, the app is in training mode
            runTrainSwitch.setText(mode); //Set the summary of the switch
            runTrainSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch runTrainSwitch = (Switch)v;
                    int cnt2;
                    int cnt1;
                    int cntRecs;
                    boolean isEnough = true;

                    if (runTrainSwitch.isChecked()) {  //If the app is switched to running mode
                        ArrayList<Integer> indexArray = new ArrayList<>();
                        ArrayList<Integer> cntsArray = new ArrayList<Integer>();

                        for(cnt1 = 0; cnt1 < MainActivity.typeList.size(); cnt1++){
                            cntRecs = 0;
                            for(cnt2 = 0; cnt2<MainActivity.recordList.size(); cnt2++){
                                if(MainActivity.recordList.get(cnt2).typeID == MainActivity.typeList.get(cnt1).typeID){
                                    cntRecs++;
                                }
                            }
                            if(cntRecs < MIN_PHOTOS){  //If some type does not have enough samples, save the index
                                indexArray.add(cnt1);
                                cntsArray.add(cntRecs);
                                isEnough = false;
                            }
                        }

                        if(!isEnough){  //There exists a type that does not have enough samples
                            //Switch Back to training mode, and pops out a toast as a hint to not enough training samples
                            String hint = "MINIMUM # of Samples for each Type: "+MIN_PHOTOS+"\nNo ENOUGH Samples for:";
                            for(int cnt = 0; cnt < indexArray.size(); cnt++){
                                hint += ('\n'+MainActivity.typeList.get(indexArray.get(cnt)).typeName+"(Only has "+
                                        cntsArray.get(cnt)+" Samples)");
                            }
                            runTrainSwitch.setChecked(false);
                            runTrainSwitch.setText(getString(R.string.training_on));
                            Toast toast = Toast.makeText(getActivity(), hint, Toast.LENGTH_LONG);
                            toast.show();
                        } else if(cnt1 == 0) {  //The type list is empty
                            runTrainSwitch.setChecked(false);
                            runTrainSwitch.setText(getString(R.string.training_on));
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.no_type_info), Toast.LENGTH_LONG);
                            toast.show();
                        } else {  //If the app is switching to the running mode, then first the training needs to be done
                            //Start a new progress dialog that shows the training is on going.
                            final ProgressDialog trainingProgressDialog = new ProgressDialog(getContext());
                            trainingProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            trainingProgressDialog.setTitle("Warning");
                            trainingProgressDialog.setMessage("Training is on-going, please wait...");
                            trainingProgressDialog.setCancelable(false);
                            trainingProgressDialog.show();
                            Thread trainingThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences trainingPrefsFile = getActivity()
                                            .getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
                                    int curTypeListHash = MainActivity.typeList.hashCode();
                                    int curRecListHash = MainActivity.recordList.hashCode();
                                    int preTypeListHash = trainingPrefsFile
                                            .getInt(getString(R.string.pref_type_list_hashval), Integer.MIN_VALUE);
                                    int preRecListHash = trainingPrefsFile
                                            .getInt(getString(R.string.pref_record_list_hashval), Integer.MIN_VALUE);
                                    if(curTypeListHash!=preTypeListHash || curRecListHash!=preRecListHash){//Compare the hash values
                                        SharedPreferences.Editor editor = trainingPrefsFile.edit();
                                        editor.putBoolean(getString(R.string.pref_is_trained), false);
                                        editor.apply();
                                        MainActivity.faceRecognizer.train(MainActivity.recordList);   //Train the model

                                        File trainingXmlFile = new File(getActivity().getFilesDir(), getString(R.string.fr_file_name));
                                        MainActivity.faceRecognizer.save(trainingXmlFile.getPath());
                                        editor.putBoolean(getString(R.string.pref_is_trained), true);
                                        editor.putInt(getString(R.string.pref_record_list_hashval), curRecListHash);
                                        editor.putInt(getString(R.string.pref_type_list_hashval), curTypeListHash);
                                        editor.apply();
                                    }
                                    trainingProgressDialog.dismiss();
                                }
                            });
                            trainingThread.start();
                            runTrainSwitch.setText(getString(R.string.running_on));
                            MainActivity.isInTraining = false;
                        }
                    } else {                         //If the app is switched to training mode
                        MainActivity.isInTraining = true;
                        runTrainSwitch.setText(getString(R.string.training_on));
                    }
                }
            });
        }

        Button photoButton = (Button)rootView.findViewById(R.id.button_photos);
        if(photoButton != null){
            //Handler: Button for taking a photo
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(illumination > ImageFragment.LIGHT_THRESHOLD){// If the light condition is good
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Create an intent for taking photots
                        File imageFile = null;

                        if(runTrainSwitch != null){
                            if(runTrainSwitch.isChecked()){  //The app is in the running mode
                                try{
                                    imageFile = createImageFile(getString(R.string.running_on));
                                } catch (Exception e){
                                    System.out.println(e);
                                }
                            } else {                          //The app is in the training mode
                                if(MainActivity.typeList.size() == 0){
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.no_type_info), Toast.LENGTH_SHORT);
                                    toast.show();
                                    return;
                                }
                                try{
                                    imageFile = createImageFile(getString(R.string.training_on));
                                } catch (Exception e){
                                    System.out.println(e);
                                }
                            }

                            if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
                                //If no camera is available, give a hint to the user and return
                                Toast toast = Toast.makeText(getActivity(), NO_CAMERA_HINT, Toast.LENGTH_LONG);
                                toast.show();
                            } else if(imageFile != null){
                                photoURI = Uri.fromFile(imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);  //Start the camera app to take a photo
                            }
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.dim_light), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }

        Button addTypesButton = (Button) rootView.findViewById(R.id.button_add_types);
        if(addTypesButton!=null && runTrainSwitch!=null) {
            final Fragment curFragment = this;
            addTypesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (runTrainSwitch.isChecked()) {  //If the app is switched to running mode
                        //Gives a hint that adding a new type is disallowed in the running mode
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.no_adding_types_when_running), Toast.LENGTH_LONG);
                        toast.show();
                    } else {                         //If the app is switched to training mode
                        //Pops out a new dialog to prompt the user to enter a new type name
                        AddTypesDialogFragment addTypesDialog = new AddTypesDialogFragment();
                        addTypesDialog.setTargetFragment(curFragment, REQUEST_ADDING_TYPES_DIALOG);
                        addTypesDialog.show(getActivity().getSupportFragmentManager(), ADD_TYPE_DIALOG_TAG);
                    }
                }
            });
        }

        return rootView;
    }

    private File createImageFile(String mode) throws IOException {
        // Create an image file name
        String imageFileName = getImageFileName(mode);

        File storageDir;
        if(mode.equals(getString(R.string.training_on))){
            storageDir = getActivity().getExternalFilesDir(null);
        } else {
            storageDir = getActivity().getExternalCacheDir();
        }

        File image;
        image = new File(storageDir, imageFileName+".jpg");

        return image;
    }

    private String getImageFileName(String mode){
        String newMacAddress = getWifiMac();

        String photoTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = newMacAddress + mode + photoTime + "_";

        return imageFileName;
    }

    private String getWifiMac(){
        WifiManager manager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        String newMacAddress = "";
        for(int cnt = 0; cnt < macAddress.length(); cnt++){
            if(macAddress.charAt(cnt) != ':'){
                newMacAddress += macAddress.charAt(cnt);
            }
        }

        return newMacAddress;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_LIGHT){
            TextView lightTextView = (TextView) getActivity().findViewById(R.id.textView_light);
            if(lightTextView != null){
                illumination = event.values[ 0 ];
                lightTextView.setText("Light Condition(in lx): "+illumination);
                if(illumination <= LIGHT_THRESHOLD){
                    lightTextView.append("\nLight Too DIM!");
                } else {
                    lightTextView.append("\nNormal");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Nothing to Do;
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this, lightSensor);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Switch switchTrainRun = (Switch) getActivity().findViewById(R.id.switch_traing_or_run);
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            //If the requestCode matches the code for photo request and the resultCode is 'OK'

            final int width = 290;  //The width of the thumbnail
            final int height = 390; //The height of the thumbnail
            final String imageFilePath = photoURI.getPath();
            final File imageFile = new File(imageFilePath);
            final ImageFragment curFragment = this;

            Bitmap tempPhoto = BitmapFactory.decodeFile(imageFilePath);
            final Bitmap photo = Bitmap.createScaledBitmap(tempPhoto, 960, 1280, false);
            try{
                //Resize the photo first
                FileOutputStream fileOS = new FileOutputStream(imageFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
                fileOS.close();
            } catch(Exception e) {
                System.out.println(e);
            }

            if(!switchTrainRun.isChecked()){   //The app is currently in the training mode.
                //Open a dialog for the user to choose the class of the object in the photo just taken
                final TextView resultText = (TextView)getActivity().findViewById(R.id.nameTextView);
                final ImageView imageView = (ImageView)getActivity().findViewById(R.id.objImageView);

                resultText.post(new Runnable() {
                    @Override
                    public void run() {
                        thumbnailImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFilePath), width, height);
                        CvRect face = MainActivity.faceDetector.detectFace(imageFile);      //Get the location of the Face
                        if (face.height() == 0 && face.width() == 0) {
                            resultText.setText(getString(R.string.no_face_detected));
                            imageFile.delete();
                        } else {
                            resultText.setText(getString(R.string.face_detected));
                            try {
                                //Extract the face from the image
                                BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(imageFilePath, false);
                                Bitmap originFacePhoto = Bitmap.createBitmap(regionDecoder.decodeRegion(
                                        new Rect(face.x(), face.y(), face.x() + face.width(), face.y() + face.height()), null));
                                //Normalize the size of the face photo
                                final Bitmap facePhoto = Bitmap.createScaledBitmap(originFacePhoto, FACE_LEN, FACE_LEN, false);
                                //Overwrite the face photo into the original file
                                FileOutputStream fileOS = new FileOutputStream(imageFile);
                                facePhoto.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
                                fileOS.close();

                                //final Bitmap photo = facePhoto;
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(facePhoto);
                                    }
                                });
                            } catch (Exception e) {
                                System.out.println(e);
                            }

                            TypeListDialogFragment classesDialog = new TypeListDialogFragment();
                            classesDialog.setTargetFragment(curFragment, REQUEST_TYPE_LIST_DIALOG);
                            classesDialog.show(getActivity().getSupportFragmentManager(), TYPE_LIST_DIALOG_TAG);
                        }
                    }
                });

            } else {                           //The app is currently in the running mode.
                final TextView resultTextView = (TextView) getActivity().findViewById(R.id.nameTextView);
                try{
                    final ImageView imageView = (ImageView)getActivity().findViewById(R.id.objImageView);
                    System.out.println(photo.getHeight()+ " " +photo.getWidth());
                    imageView.post(new Runnable() {
                        //Start a new thread to set the Image on the main page and start the computation
                        @Override
                        public void run() {
                            imageView.setImageBitmap(photo);
                        }
                    });
                } catch(Exception e){
                    System.out.println(e);
                }

                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        int cnt;
                        long label = -1;
                        final String name;
                        CvRect face = MainActivity.faceDetector.detectFace(imageFile);      //Get the location of the Face in the image

                        if(face.height()==0 && face.width()==0){    // No face is detected in the photo
                            resultTextView.setText(getString(R.string.no_face_detected));
                            System.out.println(imageFile.delete());
                        } else {                                    //A face is detected in the photo
                            try{
                                //Copy the original photo into a new file
                                final File transPhotoFile = createImageFile("Uploading");
                                FileOutputStream transPhotoOS = new FileOutputStream(transPhotoFile);
                                Bitmap transPhoto = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                                transPhoto.compress(Bitmap.CompressFormat.JPEG, 100, transPhotoOS);

                                //Extract the face from the image
                                BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(imageFilePath, false);
                                Bitmap facePhoto = regionDecoder.decodeRegion(
                                        new Rect(face.x(), face.y(), face.x() + face.width(), face.y() + face.height()), null);
                                facePhoto = Bitmap.createScaledBitmap(facePhoto, FACE_LEN, FACE_LEN, false);
                                FileOutputStream fileOS = new FileOutputStream(imageFile);
                                facePhoto.compress(Bitmap.CompressFormat.JPEG, 100, fileOS);
                                fileOS.close();

                                //Run the classification algorithm
                                label = MainActivity.faceRecognizer.predict(imageFile);
                                imageFile.delete();
                                for(cnt = 0; cnt < MainActivity.typeList.size(); cnt++){
                                    if(label == MainActivity.typeList.get(cnt).typeID){
                                        break;
                                    }
                                }
                                if(cnt < MainActivity.typeList.size()){  //The face is recognized as an existing type
                                    name = MainActivity.typeList.get(cnt).typeName;
                                } else {                                 //The face is not recognized as any existing type
                                    name = "FACE NOT RECOGNIZED";
                                }
                                resultTextView.setText(name);

                                //Upload the photo and the recognition result to the server
                                Thread sendPhotoThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int cnt = 0;
                                        boolean isSuccessful = false;
                                        while(cnt<MAX_TX && !isSuccessful){
                                            try{
                                                System.out.println(0);
                                                ///////// Read the image into a byte array ///////////
                                                FileInputStream fileInputStream = new FileInputStream(transPhotoFile);
                                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                                byte[] buffer = new byte[ 1024 ];
                                                while (fileInputStream.read(buffer) != -1) {
                                                    bos.write(buffer);
                                                }
                                                byte[] faceByteArray = bos.toByteArray();
                                                fileInputStream.close();
                                                bos.close();
                                                ///////////////////////////////////////////////////////

                                                System.out.println(2);
                                                ////////// Set up an HTTP connection to the server and send the String /////
                                                String serverIP = ((EditText)getActivity().findViewById(R.id.editText_server_ip)).getText().toString();
                                                String serverPort = ((EditText)getActivity().findViewById(R.id.editText_server_port)).getText().toString();
                                                String webAppName = getString(R.string.webapp_name);
                                                String paraName = getString(R.string.photo_data);
                                                URL url = new URL("http://"+serverIP+":"+serverPort+webAppName);

                                                System.out.println(3);
                                                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                                connection.setRequestMethod("POST");
                                                connection.setConnectTimeout(9243);
                                                connection.setDoOutput(true);

                                                System.out.println(4);
                                                OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
                                                ////////// Encode the image and its name into a JSON string /////////
                                                String faceString = Base64.encodeToString(faceByteArray, Base64.URL_SAFE | Base64.NO_WRAP);
                                                String fileName = getWifiMac();
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put(getString(R.string.key_type), getString(R.string.key_type_upload));
                                                jsonObject.put(getString(R.string.key_file_name), fileName);
                                                jsonObject.put(getString(R.string.key_name), name);
                                                jsonObject.put(getString(R.string.key_image), faceString);
                                                System.out
                                                        .println
                                                                (jsonObject.getString(getString(R.string.key_type)) + " " +
                                                                        fileName + " " +
                                                                        name + " " +
                                                                        faceString.length());
                                                output.write(paraName + "=" + jsonObject.toString());
                                                output.flush();

                                                System.out.println(5);
                                                InputStream input = connection.getInputStream();
                                                byte[] inputBytes = new byte[ 1 ];
                                                StringBuffer buf = new StringBuffer();
                                                while (input.read(inputBytes) != -1) {
                                                    buf.append(new String(inputBytes));
                                                }

                                                System.out.println(6);
                                                output.close();
                                                input.close();
                                                connection.disconnect();

                                                isSuccessful = true;
                                                ////////////////////////////////////////////////////////////////////////////

                                            } catch (Exception e) {
                                                System.out.println(e);
                                                cnt++;
                                            }
                                        }

                                        transPhotoFile.delete();
                                    }
                                });

                                sendPhotoThread.start();

                            } catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    }
                });
            }
        } else if(requestCode == REQUEST_ADDING_TYPES_DIALOG) {
            //If the requestCode is for the type adding dialog
            Bundle typeNameBundle = data.getExtras();
            String typeName = typeNameBundle.getString(AddTypesDialogFragment.TYPE_NAME);
            boolean hasDup = false;

            for(DBType type:MainActivity.typeList){
                if(type.typeName.equals(typeName)){
                    hasDup = true;
                    break;
                }
            }
            if(!hasDup){
                DBType type  = new DBType();
                type.typeName = typeName;
                MainActivity.typeList.add(type);
                dbHelper.addOneType(type);
            }
        } else if(requestCode == REQUEST_TYPE_LIST_DIALOG){
            //If the requestCode is for the list of types dialog
            final DBRecord dbRecord = new DBRecord();
            dbRecord.typeName = MainActivity.typeList.get(resultCode).typeName;
            dbRecord.typeID = MainActivity.typeList.get(resultCode).typeID;
            dbRecord.fullsizePhotoUri = photoURI.toString();
            dbRecord.thumbnailPhoto = thumbnailImage.copy(thumbnailImage.getConfig(), true);

            int cnt;
            for(cnt = MainActivity.recordList.size()-1; cnt>=0 && MainActivity.recordList.get(cnt).typeID>dbRecord.typeID; cnt--);
            MainActivity.recordList.add(cnt + 1, dbRecord);
            dbHelper.addOneRec(dbRecord);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_image_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name_list) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, new NameListFragment())
                    .commit();

            return true;
        } else if (id == R.id.action_sample_photos) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container, new SamplesFragment())
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
