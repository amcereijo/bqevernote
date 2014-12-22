package amcereijo.com.bqevernote.notes.newnote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.inject.Inject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import amcereijo.com.bqevernote.R;
import amcereijo.com.bqevernote.notes.newnote.views.DrawView;
import amcereijo.com.bqevernote.ocr.OCRServiceAPI;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_hand_write)
public class HandWriteActivity extends RoboActivity {

    public final static int RESULT_CODE = 1024;
    public final static String TAG = HandWriteActivity.class.getName();

    private Bitmap bitmap;
    private DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDrawView();
    }

    private void createDrawView() {
        drawView = new DrawView(this);
        ((LinearLayout)findViewById(R.id.handwrite_drawview)).addView(drawView);
        drawView.setDrawingCacheEnabled(true);
    }

    private void createBitmap(){
        bitmap = drawView.getDrawingCache();
    }

    @Inject
    private OCRServiceAPI ocrServiceAPI;

    public void processHandWrite(View v){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.title_activity_notes));
        dialog.setMessage(getString(R.string.notes_note_handwrite_recog));
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File fTemp =  File.createTempFile("canvas", ".png");
                    FileOutputStream fos = new FileOutputStream(fTemp);
                    createBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    if(ocrServiceAPI.convertToText("es", fTemp.getAbsolutePath()) &&
                            ocrServiceAPI.getResponseCode() == 200){
                        String text = ocrServiceAPI.getResponseText();
                        Bundle conData = new Bundle();
                        conData.putString("text", text);
                        Intent intent = new Intent();
                        intent.putExtras(conData);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(HandWriteActivity.this, "No se ha podido guardar el texto", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    Toast.makeText(HandWriteActivity.this, "No se ha podido guardar el texto", Toast.LENGTH_LONG).show();
                } finally {
                    dialog.dismiss();
                }
            }
        }).start();

    }

    public void clearHandWrite(View v){
        ((LinearLayout)findViewById(R.id.handwrite_drawview)).removeAllViews();
        createDrawView();
    }

    public void cancelHandWrite(View v){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
