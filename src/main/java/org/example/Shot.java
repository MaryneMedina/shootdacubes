package org.example;

import com.jogamp.opengl.GL2;

public class Shot {

    public static final float RADIUS = .25f;
    private static final float SPEED = .3f;

    private final float x;
    private float y = -4.5f;

    public Shot(float x) {
        this.x = x;
    }

    public void move() {
        y += SPEED;
    }

    public float getY() {
        return y;
    }

    public boolean touchesCube(Cube cube) {
        return x - RADIUS >= cube.getX() - 1f && x + RADIUS <= cube.getX() + 1f
                && y - RADIUS >= cube.getY() - 1f && y + RADIUS <= cube.getY() + 1f;
    }

    public void draw(GL2 gl) {
        // Beauté du cercle
        int vertices = 20;

        gl.glTranslatef(x, y, 0);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3d(.8f, .2f, .2f);

        double angle;
        double increment = 2 * Math.PI / vertices;
        for (int i = 0; i < vertices; i++) {
            angle = i * increment;
            double x = RADIUS * Math.cos(angle);
            double y = RADIUS * Math.sin(angle);
            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }
}
