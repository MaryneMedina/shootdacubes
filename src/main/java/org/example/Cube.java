package org.example;

import com.jogamp.opengl.GL2;

public class Cube {

    public static final int ROTATION_SPEED = 5;
    public static final float SPEED = .05f;
    private static float rot = 0;
    private final float y;
    private float x;

    public Cube(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static void increaseRot() {
        rot += ROTATION_SPEED;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void move(float step) {
        this.x += step;
    }

    public void draw(GL2 gl) {
        gl.glTranslatef(x, y, 0);
        gl.glScalef(.5f, .5f, .5f);
        gl.glRotatef(rot, 0, 1, 0);

        gl.glBegin(GL2.GL_QUADS);
        // Front
        gl.glColor3d(0.184314, 0.184314, 0.309804);
        gl.glVertex3d(-1, -1, 1);
        gl.glVertex3d(1, -1, 1);
        gl.glVertex3d(1, 1, 1);
        gl.glVertex3d(-1, 1, 1);
        // Back
        gl.glColor3d(0.137255, 0.137255, 0.556863);
        gl.glVertex3d(-1, -1, -1);
        gl.glVertex3d(1, -1, -1);
        gl.glVertex3d(1, 1, -1);
        gl.glVertex3d(-1, 1, -1);
        // Left
        gl.glColor3d(0.258824, 0.258824, 0.435294);
        gl.glVertex3d(-1, -1, -1);
        gl.glVertex3d(-1, -1, 1);
        gl.glVertex3d(-1, 1, 1);
        gl.glVertex3d(-1, 1, -1);
        // Right
        gl.glColor3d(0.196078, 0.8, 0.6);
        gl.glVertex3d(1, -1, -1);
        gl.glVertex3d(1, -1, 1);
        gl.glVertex3d(1, 1, 1);
        gl.glVertex3d(1, 1, -1);
        // Bottom
        gl.glColor3d(0.22, 0.69, 0.87);
        gl.glVertex3d(-1, -1, 1);
        gl.glVertex3d(1, -1, 1);
        gl.glVertex3d(1, -1, -1);
        gl.glVertex3d(-1, -1, -1);
        // Up
        gl.glColor3d(0.372549, 0.623529, 0.623529);
        gl.glVertex3d(-1, 1, 1);
        gl.glVertex3d(1, 1, 1);
        gl.glVertex3d(1, 1, -1);
        gl.glVertex3d(-1, 1, -1);
        gl.glEnd();
    }

}
