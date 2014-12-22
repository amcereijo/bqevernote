package amcereijo.com.bqevernote.notes.newnote.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import amcereijo.com.bqevernote.R;

/**
 * Created by medianet on 15/12/2014.
 */
public class DrawView extends View {
    private Bitmap mBitmapBrush;
    private Point mBitmapBrushDimensions;

    private List<Point> mPositions = new ArrayList<Point>(100);

    public DrawView(Context context) {
        super(context);

        mBitmapBrush = BitmapFactory.decodeResource(context.getResources(), R.drawable.brush);
        mBitmapBrushDimensions = new Point(mBitmapBrush.getWidth(), mBitmapBrush.getHeight());

        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point pos : mPositions) {
            canvas.drawBitmap(mBitmapBrush, pos.x, pos.y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final float posX = event.getX();
                final float posY = event.getY();
                Point p = new Point();
                p.set((int)posX, (int)posY);
                mPositions.add(new Point((int)posX - mBitmapBrushDimensions.x / 2, (int)posY - mBitmapBrushDimensions.y / 2));
                invalidate();
        }

        return true;
    }
}
