package org.example;

import com.jogamp.opengl.GL2;

public class Spaceship {

    public static final float SPEED = .2f;

    private float x = 0;

    public float getX() {
        return x;
    }

    public void move(float step) {
        this.x += step;
    }

    public void draw(GL2 gl) {
        gl.glTranslatef(x, -5f, 0);
        gl.glScalef(.8f, -.8f, .8f);

        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glEnd();

        // Draw the second cube
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-1.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glVertex3f(-1.5f, 0.5f, 0.5f);
        gl.glEnd();

        // Draw the third cube
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(1.5f, -0.5f, 0.5f);
        gl.glVertex3f(1.5f, 0.5f, 0.5f);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glEnd();

        // Draw the fourth cube
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.5f, -1.5f, 0.5f);
        gl.glVertex3f(0.5f, -1.5f, 0.5f);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glEnd();
    }

}
