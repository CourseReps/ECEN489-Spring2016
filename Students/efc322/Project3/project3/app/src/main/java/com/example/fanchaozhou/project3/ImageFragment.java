package com.example.fanchaozhou.project3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.googlecode.javacv.cpp.opencv_core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Fanchao Zhou on 4/2/2016.
 */

public class ImageFragment extends Fragment {

    public static final int REQUEST_TYPE_LIST_DIALOG = 3;
    public static final int REQUEST_ADDING_TYPES_DIALOG = 2;            //The request code for returning user's choice from dialog
    public static final int REQUEST_IMAGE_CAPTURE = 1;                  //The request code for taking a photo
    private static final String NO_CAMERA_HINT = "Camera NOT Avalaible";   //The hint for an unavalaible camera
    private static final String ADD_TYPE_DIALOG_TAG = "Adding Types Dialog";//Tag for the dialog when switching from running to training
    private static final String TYPE_LIST_DIALOG_TAG = "Type List Dialog";
    private static final int MIN_PHOTOS = 8;
    private static final int FACE_LEN = 650;
    private Uri photoURI;
    private Bitmap thumbnailImage;
    private RecordDBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new RecordDBHelper(getActivity());
        setHasOptionsMenu(true);      //Allows to create menu on this fragment
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
                                    MainActivity.faceRecognizer.train(MainActivity.recordList);   //Train the model
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
        WifiManager manager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        String photoTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = macAddress+mode + "_JPG " + photoTime + "_";

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
                                Bitmap facePhoto = regionDecoder.decodeRegion(
                                        new Rect(face.x(), face.y(), face.x() + face.width(), face.y() + face.height()), null);
                                //Normalize the size of the face photo
                                facePhoto = Bitmap.createScaledBitmap(facePhoto, FACE_LEN, FACE_LEN, false);
                                //Overwrite the face photo into the original file
                                FileOutputStream fileOS = new FileOutputStream(imageFile);
                                facePhoto.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
                                fileOS.close();

                                final Bitmap photo = BitmapFactory.decodeStream(new FileInputStream(new File(imageFilePath)));
                                //final Bitmap photo = facePhoto;
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(photo);
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
                    final Bitmap photo = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                    final ImageView imageView = (ImageView)getActivity().findViewById(R.id.objImageView);
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
                        CvRect face = MainActivity.faceDetector.detectFace(imageFile);      //Get the location of the Face in the image

                        if(face.height()==0 && face.width()==0){
                            resultTextView.setText(getString(R.string.no_face_detected));
                        } else {
                            long label = -1;
                            try{
                                //First extract the face from the image
                                BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(imageFilePath, false);
                                Bitmap facePhoto = regionDecoder.decodeRegion(
                                        new Rect(face.x(), face.y(), face.x() + face.width(), face.y() + face.height()), null);
                                facePhoto = Bitmap.createScaledBitmap(facePhoto, FACE_LEN, FACE_LEN, false);
                                FileOutputStream fileOS = new FileOutputStream(imageFile);
                                facePhoto.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
                                fileOS.close();

                                //Run the classification algorithm
                                label = MainActivity.faceRecognizer.predict(imageFile);
                            } catch(Exception e){
                                System.out.println(e);
                            }
                            for(cnt = 0; cnt < MainActivity.typeList.size(); cnt++){
                                if(label == MainActivity.typeList.get(cnt).typeID){
                                    break;
                                }
                            }
                            if(cnt < MainActivity.typeList.size()){  //The face is recognized as an existing type
                                resultTextView.setText(MainActivity.typeList.get(cnt).typeName);;
                            } else {                                 //The face is not recognized as any existing type
                                resultTextView.setText("FACE NOT RECOGNIZED");;
                            }

                            imageFile.delete();
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
            DBRecord dbRecord = new DBRecord();
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
