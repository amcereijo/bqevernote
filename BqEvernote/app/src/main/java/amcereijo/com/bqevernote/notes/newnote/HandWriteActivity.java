package amcereijo.com.bqevernote.notes.newnote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import amcereijo.com.bqevernote.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_hand_write)
public class HandWriteActivity extends RoboActivity implements View.OnTouchListener {

    public final static int RESULT_CODE = 1024;

    private ImageView imageView;
    private Canvas canvas;
    private Paint paint;
    private List<Float> points = new ArrayList<Float>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) this.findViewById(R.id.handwrite_imageview);

        createPaint();
        generateEmptyCanvas();

        imageView.setOnTouchListener(this);
    }

    private void createPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(16);
    }


    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                points.add(event.getX());
                points.add(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                points.add(event.getX());
                points.add(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                points.add(event.getX());
                points.add(event.getY());
                float[] ps = new float[points.size()];
                for(int i=0; i<points.size();i++){
                    ps[i] = points.get(i);
                }
                canvas.drawPoints(ps, 0, ps.length - 1, paint);
                imageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private void generateEmptyCanvas(){
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        currentDisplay.getSize(point);
        Bitmap bitmap = Bitmap.createBitmap(point.x, point.y,
                Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);

        imageView.setImageBitmap(bitmap);
    }

    public void processHandWrite(View v){
        //TODO get text from draw
        Bundle conData = new Bundle();
            conData.putString("text", "Thanks Thanks");
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void clearHandWrite(View v){
        generateEmptyCanvas();
    }

    public void cancelHandWrite(View v){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
