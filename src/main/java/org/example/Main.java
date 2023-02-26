package org.example;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main extends GLCanvas implements GLEventListener, KeyListener {

    private final ArrayList<Cube> cubes = new ArrayList<>();
    private final ArrayList<Shot> shots = new ArrayList<>();
    private int cubeDirection = -1;
    // --- SPACESHIP ---
    private Spaceship spaceship;
    // Movement
    private boolean left = false;
    private boolean right = false;
    // Shots
    private boolean shoot = false;
    private Instant lastShot = Instant.now();

    public Main() {
        this.addGLEventListener(this);
    }

    public static void main(String[] args) {
        Main canvas = new Main();
        canvas.setPreferredSize(new Dimension(1080, 720));

        final JFrame frame = new JFrame();
        frame.getContentPane().add(canvas);
        frame.setTitle("Shoot The Cubes");
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // add animator to set the frame rate
        new FPSAnimator(canvas, 60, true).start();
        frame.addKeyListener(canvas);
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // vider les valeurs du z-buffer
        gl.glClearDepth(1.0f);
        // activer le test de profondeur
        gl.glEnable(GL2.GL_DEPTH_TEST);
        // choisir le type de test de profondeur
        gl.glDepthFunc(GL2.GL_LEQUAL);
        // choix de la meilleure correction de perspective
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // Créer les cubes et les stocker

        int gap = 2;

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 6; x++) {
                // -2 et 3 pour ne pas être au centre
                Cube cube = new Cube(-2f + x * gap, 3f + y * gap);
                cubes.add(cube);
            }
        }

        // Créer le vaisseau et le stocker
        spaceship = new Spaceship();
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        // Initialisation des états
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);     // Clear The Screen And The Depth Buffer
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glLoadIdentity();

        // 2 rangés de 8 cubes
        // Chacun est indépendant (gl.glPushMatrix() avant)
        // Puis revenir à la matrice initiale (gl.glPopMatrix() après)

        // Classe Cube avec la position et le sens de rotation
        // Les cubes doivent être stockés dans une arraylist

        gl.glTranslatef(0, 0, -15);

        if (cubes.isEmpty()) {
            System.out.println("BRAVO !");
            return;
        }

        // --- CUBES ---
        // Dessiner
        for (Cube cube : cubes) {
            gl.glPushMatrix();
            cube.draw(gl);
            gl.glPopMatrix();
        }

        // Rotation + déplacement cubes
        Cube.increaseRot();

        Cube mostLeft = cubes.stream().reduce((cube, cube2) -> cube.getX() <= cube2.getX() ? cube : cube2).get();
        Cube mostRight = cubes.stream().reduce((cube, cube2) -> cube.getX() >= cube2.getX() ? cube : cube2).get();
        if (mostLeft.getX() < -8.35)
            cubeDirection = 1;
        if (mostRight.getX() > 8.35)
            cubeDirection = -1;

        for (Cube cube : cubes) {
            cube.move(Cube.SPEED * cubeDirection);
        }

        // --- VAISSEAU ---
        // Dessiner
        gl.glPushMatrix();
        spaceship.draw(gl);
        gl.glPopMatrix();

        // Mouvement
        if (left && spaceship.getX() > -7.5f) {
            spaceship.move(-Spaceship.SPEED);
        } else if (right && spaceship.getX() < 7.5f) {
            spaceship.move(Spaceship.SPEED);
        }

        // --- TIR ---
        Instant halfASecondLater = lastShot.plus(500, ChronoUnit.MILLIS);
        boolean isHalfASecondLater = halfASecondLater.isBefore(Instant.now());
        if (shoot && isHalfASecondLater) {
            lastShot = Instant.now();

            // Ajoute le nouveau tir
            Shot shot = new Shot(spaceship.getX());
            shots.add(shot);
        }

        // Bouge tous les tirs
        for (Shot shot : shots) {
            gl.glPushMatrix();

            if (shot.getY() > 10) {
                shots.remove(shot);
                break;
            }

            for (Cube cube : cubes) {
                if (shot.touchesCube(cube)) {
                    cubes.remove(cube);
                    shots.remove(shot);
                    return;
                }
            }

            shot.move();
            shot.draw(gl);
            gl.glPopMatrix();
        }

        // Nettoyage de la pipeline
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl);

        float aspect = (float) width / height;
        // Set the view port (display area)
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection,
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);
        // Enable the model-view transform
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); // reset

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shoot = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shoot = false;
        }
    }
}
