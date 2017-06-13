/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list1 = new CircularLinkedList();
    private CircularLinkedList list2 = new CircularLinkedList();
    private CircularLinkedList list3 = new CircularLinkedList();
    private List<CircularLinkedList> lists = new ArrayList<>();

    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStrokeWidth(10);
        int i = 0;
        for(CircularLinkedList list:lists) {
            if(i==1){
                pointPaint.setColor(Color.GREEN);
            }
            if(i==2){
                pointPaint.setColor(Color.BLUE);
            }
            Point last = null;
            Point start = null;
            for (Point p : list) {
                if (last == null) {
                    last = p;
                    start = p;
                }
                canvas.drawLine(last.x, last.y, p.x, p.y, pointPaint);
                canvas.drawCircle(p.x, p.y, 20, pointPaint);
                last = p;
            }
            if (start != null && last != null) {
                canvas.drawLine(start.x, start.y, last.x, last.y, pointPaint);
            }
            i++;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean allSelected = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list1.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list1.insertSmallest(p);
                } else if (insertMode.equals("Beginning")){
                    list1.insertBeginning(p);
                }
                else{
                    list1.insertBeginning(p); //red
                    list2.insertNearest(p); // green
                    list3.insertSmallest(p); // blue
                    allSelected = true;
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                TextView smallestText = (TextView) ((Activity) getContext()).findViewById(R.id.smallest_text);
                TextView closestText = (TextView) ((Activity) getContext()).findViewById(R.id.nearest_text);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", list1.totalDistance()));
                    if(allSelected) {
                        message.setText(String.format("Beginning length is now %.2f", list1.totalDistance()));
                        message.setTextColor(Color.RED);
                        closestText.setText(String.format("Nearest length is now %.2f", list2.totalDistance()));
                        closestText.setTextColor(Color.GREEN);
                        smallestText.setText(String.format("Smallest length is now %.2f", list3.totalDistance()));
                        smallestText.setTextColor(Color.BLUE);
                    }
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list1.reset();
        list2.reset();
        list3.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
