package com.fixpapa.ffixpapa.EngineerPart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fixpapa.ffixpapa.R;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;

import static com.fixpapa.ffixpapa.Services.Rest.Utility.showToast;

public class DigitalSugnature extends AppCompatActivity {

    Button clearButton, doneButton;
    SignatureView signature_view;
    ImageView finishImage;
    Bitmap bitmapSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_sugnature);
        finishImage = (ImageView) findViewById(R.id.finishImage);
        signature_view = (SignatureView) findViewById(R.id.signature_view);
        doneButton = (Button) findViewById(R.id.doneButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        finishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_view.clearCanvas();

            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signature_view.isBitmapEmpty()) {
                    showToast(DigitalSugnature.this, "please sign Firstly");
                } else {
                    bitmapSignature = signature_view.getSignatureBitmap();
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    bitmapSignature.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("imageC", byteArray);
                    setResult(99,intent);
                    finish();


                }
            }
        });

    }

}
