package tamu.edu.kyle.secrectmessenger12;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButton;
        final EditText mEdit;
        final ImageView imageView = (ImageView) findViewById(R.id.qrCode);

        //String qrData = "Data I want to encode in QR code";
        mButton = (Button) findViewById(R.id.createButton);
        mEdit = (EditText) findViewById(R.id.message);

        assert mButton != null;
        mButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        int qrCodeDimention = 500;

                        assert mEdit != null;
                        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(mEdit.getText().toString(), null,
                                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

                        try {
                            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                            assert imageView != null;
                            imageView.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
