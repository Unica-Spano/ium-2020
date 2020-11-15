package com.example.lezione3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class PieChartView extends View {

    // sfondo oggetto paint
    private int backgroundColor = Color.WHITE;

    // lista delle percentuali delle fette del piechart
    private List<Float> percent;

    // lista dei colori delle fette del piechart
    private List<Integer> segmentColor;

    // rettangolo contenitore del piechart
    private RectF enclosing = new RectF();

    // centro della canvas
    private PointF center = new PointF();

    // raggio del piechart
    private int radius = 100;

    // colore del bordo delle fette non selezionate
    private int strokeColor;

    // spessore del bordo delle fette non selezionate
    private int strokeWidth;

    // indice della fetta selezionata nella lista delle percentuali
    private int selectedIndex = 2;

    // variabile di appoggio evidenziare la fetta selezionata
    private float selectedStartAngle = 0.0f;

    // posizione precedente del tocco per implementare la traslazione e pan della vista
    private PointF previousTouch = new PointF(0,0);

    protected void onDraw(Canvas canvas){

        Paint paint = new Paint();

        // abilitiamo l'antialiasing
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //
        paint.setColor(this.getBackgroundColor());

        // l'elemento all'interno della canvas dove disegneremo
        Rect rect = new Rect();
        rect.left = 0;
        rect.right = getWidth();
        rect.top = 0;
        rect.bottom = getHeight();

        // aggiungiamo il rettangolo alla canvas
        canvas.drawRect(rect, paint);


        // percentuale della singola fetta
        float p;
        // colore della singola fetta
        int c;

        // centro della canvas
        center.x = canvas.getWidth() / 2;
        center.y = canvas.getHeight() /2;

        // estremi del rettangolo contenitore del piechart
        enclosing.top = center.y - radius;
        enclosing.bottom = center.y + radius;
        enclosing.left = center.x - radius;
        enclosing.right = center.x + radius;

        // angolo di partenza per disegnare le fette
        float alpha = -90.0f;

        // passo dell'1% convertendolo da pigreco a radianti
        float p2a = 360.0f / 100.0f;

        // riempimento fette del piechart (colore)
        for(int i = 0; i < percent.size(); i++){

            p = percent.get(i);
            c = segmentColor.get(i);

            // imposto colore della fetta e stile di disegno (FILL)
            paint.setColor(c);
            paint.setStyle(Paint.Style.FILL);

            // drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
            canvas.drawArc(enclosing, alpha, p*p2a, true, paint);

            // il nuovo punto iniziale sarà quello del punto precedente sommato al punto finale (p*p2a),
            // moltiplicando la percentuali in radianti ottengo la fine dell'angolo
            alpha += p*p2a;
        }

        // angolo di partenza per disegnare le fette
        alpha = -90.0f;

        // disegnare i bordi delle fette
        for(int i = 0; i < percent.size(); i++){

            p = percent.get(i);
            c = segmentColor.get(i);

            // imposto colore della fetta e stile di disegno (STROKE)
            paint.setColor(strokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);

            // contorno della fetta selezionata
            // avrà un colore diverso, lo vedremo meglio nella parte 2
            if(i == selectedIndex){
                selectedStartAngle = alpha;
            }

            // drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
            canvas.drawArc(enclosing, alpha, p*p2a, true, paint);

            alpha += p*p2a;
        }






    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float tx = event.getX();
        float ty = event.getY();

        //float x = (tx / getZoom()) - getTranslate().x;
        //float y = (ty / getZoom()) - getTranslate().y;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getPointerCount()==1){
                    //selectedIndex = this.pickCorrelation(x,y);

                    this.invalidate();

                    this.previousTouch.x = tx;
                    this.previousTouch.y = ty;

                    return true;
                }
                break;
        }

        return false;
    }


    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<Float> getPercent() {
        return percent;
    }

    public void setPercent(List<Float> percent) {
        this.percent = percent;
    }

    public List<Integer> getSegmentColor() {
        return segmentColor;
    }

    public void setSegmentColor(List<Integer> segmentColor) {
        if(segmentColor.size() != percent.size()){
            throw new IllegalArgumentException(
                    "La lista dei colori e delle percentuali devono avere la stessa dimensione"
            );
        }

        this.segmentColor = segmentColor;
    }

    public PointF getCenter() {
        return center;
    }

    public void setCenter(PointF center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public float getSelectedStartAngle() {
        return selectedStartAngle;
    }

    public void setSelectedStartAngle(float selectedStartAngle) {
        this.selectedStartAngle = selectedStartAngle;
    }
}
