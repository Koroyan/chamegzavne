package com.example.chamegzavne.MoreFunctions;

import android.graphics.Color;
import android.os.Handler;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

public class Fader implements Runnable {
    private float radius, initialRadius, maxRadius;
    private int baseColor, color, initialColor;
    private Handler h;
    private Circle c;
    private float radiusJump = 400;
    int numIncrements, alphaIncrement;
    private CircleOptions co;
    private GoogleMap mMap;

    public Fader(Handler h, Circle c, float initialRadius, float maxRadius, int initialColor, CircleOptions co,GoogleMap mMap) {
        this.initialRadius = initialRadius;
        this.initialColor = initialColor;
        this.maxRadius = maxRadius;
        this.h = h;
        this.c = c;
        this.co = co;
        this.mMap=mMap;
        reset();
    }

    private void reset() {
        radius = initialRadius;
        this.color = initialColor;
        this.baseColor = initialColor;
        numIncrements = (int)((maxRadius - initialRadius) / radiusJump);
        alphaIncrement = 0x100 / numIncrements;
        if (alphaIncrement <= 0) alphaIncrement = 1;
    }

    public void run() {
        int alpha = Color.alpha(color);
        radius = radius + radiusJump;
        c.setRadius(radius);
        alpha -= alphaIncrement;
        color = Color.argb(alpha, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor));
        c.setFillColor(color);
        c.setStrokeColor(color);

        if (radius < maxRadius) {
            h.postDelayed(this, 25);
        } else {
            c.remove();
            reset();
            c = mMap.addCircle(co);
            h.postDelayed(this, 2000);
        }

        //done
    }
}
