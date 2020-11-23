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

    private int selectedColor;
    private int selectedWidth = 8;

    private float zoom = 1.0f;

    private PointF translate = new PointF(-200, -300);

    private boolean multitouch = false;

    private double oldDistance = 0.0;

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

        // salvo le trasformazioni correnti
        canvas.save();

        // applico la scalatura
        canvas.scale(this.getZoom(), this.getZoom());

        // applico la traslazione del punto di vista
        canvas.translate(getTranslate().x, getTranslate().y);


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


        //
        if(selectedIndex >= 0 && selectedIndex < percent.size()){
            paint.setColor(selectedColor);
            paint.setStrokeWidth(selectedWidth);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(enclosing, selectedStartAngle, percent.get(selectedIndex)*p2a, true, paint);
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // coordinate tocco utente (evento)
        float tx = event.getX();
        float ty = event.getY();

        float x = (tx / getZoom()) - getTranslate().x;
        float y = (ty / getZoom()) - getTranslate().y;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getPointerCount() == 1){
                    selectedIndex = this.pickCorrelation(x,y);

                    this.invalidate();

                    this.previousTouch.x = tx;
                    this.previousTouch.y = ty;

                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                //la gesture è la seguente: l'utente sta premendo la schermata e contemporaneamente muove il dito
                switch(event.getPointerCount()){
                    //un dito
                    case 1:

                        if(multitouch){ //multitouch significa zoom
                            // usciamo immediatamente se l'utente ha sollevato un dito dopo
                            // l'interazione multitouch
                            return true;
                        }

                        // recuperiamo il delta fra la posizione corrente e quella
                        // precedente. Dobbiamo dividere per il fattore di scala
                        // per avere la distanza nel sistema di riferimento
                        // originario (1.0f)

                        float dx = (tx - this.previousTouch.x) / this.zoom;
                        float dy = (ty - this.previousTouch.y) / this.zoom;

                        this.previousTouch.x = tx; // le vecchie coordinate diventano le nuove
                        this.previousTouch.y = ty;

                        // aggiorniamo la traslazione spostandola di dx sulle x
                        // e di dy sulle y
                        this.translate.set(
                                this.translate.x + dx,
                                this.translate.y + dy
                        );
                        this.invalidate(); //aggiorno
                        return true;

                    case 2:
                        // qui gestiamo il pinch: questo case ha lo scopo di settare la variabile zoom che verrà chiamata in canvas.scale

                        // teniamo traccia del fatto che l'utente abbia iniziato un pinch
                        multitouch = true;

                        // recuperiamo la posizione corrente del tocco 1 e del tocco 2, creo due elementi di tipo PointerCoords
                        MotionEvent.PointerCoords touch1 = new MotionEvent.PointerCoords();
                        MotionEvent.PointerCoords touch2 = new MotionEvent.PointerCoords();

                        event.getPointerCoords(0, touch1); //prende le coordinate di touch1, 0 è l'indice dato da getPointerCount
                        event.getPointerCoords(1, touch2);

                        // calcoliamo la distanza corrente tra i due touch
                        double distance = Math.sqrt(
                                Math.pow(touch2.x - touch1.x, 2) +
                                        Math.pow(touch2.y - touch1.y, 2));

                        // confrontiamo con la precedente (oldDistance inizialmente è 0.0)
                        if (distance - oldDistance > 0) {
                            // ingrandisco la vista
                            zoom += 0.03;
                            this.invalidate();
                        }

                        if (distance - oldDistance < 0) {
                            // rimpicciolisco la vista
                            zoom -= 0.03;
                            this.invalidate();
                        }

                        oldDistance = distance;

                        return true;
                }

            case MotionEvent.ACTION_UP:
                // reset delle variabili di stato in modo tale da averle per la successiva interazione
                this.previousTouch.x = 0.0f;
                this.previousTouch.y = 0.0f;
                multitouch = false;
                oldDistance = 0.0f;
                return true;
        }

        return false;
    }

    /**
     * Restituisce l'indice della fetta di torta che contiene il punto di coordinate (x,y)
     * @param x L'ascissa del punto
     * @param y L'ordinata del punto
     * @return l'indice della fetta di torta
     */
    private int pickCorrelation(float x, float y){
        // controlliamo che il tocco sia dentro il rettangolo della vista
        if(enclosing.contains(x,y)){

            // calcoliamo la distanza del tocco dal centro in x ed y
            // ottengo la distanza effettiva dal centro (distanza Euclidea)
            float dx = x - center.x;
            float dy = y - center.y;
            float r = (float) Math.sqrt(dx*dx + dy*dy);

            float cos = dx/r;
            float sin = -dy/r;

            // calcoliamo l'angolo che va da -180 a 180 tra il centro e il punto cliccato
            double angle = Math.toDegrees(Math.atan2(sin, cos));

            if(angle > 90){
                angle = angle -360;
            }

            // la ricerca parte da 90
            float alpha = 90.0f;
            // fetta successiva
            float alpha1;

            float p2a = 360.0f / 100.0f;
            float p;

            for(int i = 0; i < percent.size(); i++){
                p = percent.get(i);

                // estremo finale della fetta calcolato tramite p
                alpha1 = alpha - p*p2a;

                // controllo se angle fa parte della fetta corrente
                if(angle > alpha1 && angle < alpha){
                    return i;
                }

                alpha = alpha1;
            }

        }
        else{
            // il tocco è fuori dal piechart,
            return -1;
        }

        return 1;
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

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getSelectedWidth() {
        return selectedWidth;
    }

    public void setSelectedWidth(int selectedWidth) {
        this.selectedWidth = selectedWidth;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public PointF getTranslate() {
        return translate;
    }

    public void setTranslate(PointF translate) {
        this.translate = translate;
    }

    public boolean isMultitouch() {
        return multitouch;
    }

    public void setMultitouch(boolean multitouch) {
        this.multitouch = multitouch;
    }
}
