package it.polimi.ingsw.is25am22new.Client.View.GUI;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class GalaxyBackground extends Canvas {

    private static final int STAR_COUNT = 200;
    private Star[] stars;
    private AnimationTimer timer;

    public GalaxyBackground() {
        this(800, 600);
    }

    public GalaxyBackground(double width, double height) {
        super(width, height);

        GraphicsContext gc = getGraphicsContext2D();
        stars = generateStars(width, height);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw(gc, width, height);
            }
        };
        timer.start();
    }

    private Star[] generateStars(double width, double height) {
        Random random = new Random();
        Star[] stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(
                    random.nextDouble() * width,
                    random.nextDouble() * height,
                    3.5 + random.nextDouble() * 1.5,
                    random.nextDouble() * 0.10 + 0.01
            );
        }
        return stars;
    }

    private void draw(GraphicsContext gc, double width, double height) {
        // Black background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // Stars
        for (Star star : stars) {
            star.update();
            double alpha = 0.5 + 0.5 * Math.sin(star.brightness);
            gc.setFill(Color.rgb(255, 255, 255, alpha));
            gc.fillOval(star.x, star.y, star.radius, star.radius);
        }
    }

    public void stopAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    private static class Star {
        double x, y;
        double radius;
        double brightness;
        double speed;

        public Star(double x, double y, double radius, double speed) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.speed = speed;
            this.brightness = Math.random() * 2 * Math.PI;
        }

        public void update() {
            brightness += speed;
        }
    }
}