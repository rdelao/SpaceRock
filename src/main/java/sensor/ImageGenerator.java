package sensor;

/**
 * Created by Divya on 2/18/2017.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static sensor.SensorSimulation.MAX_ASTEROIDS;
import static sensor.SensorSimulation.image_size;


public class ImageGenerator {

    private int elapsed_seconds;
    private ArrayList<Asteroid> asteroids;
    private Random random = new Random();


    public ImageGenerator(int elapsed_seconds, ArrayList<Asteroid> asteroids){
        this.elapsed_seconds = elapsed_seconds;
        this.asteroids = asteroids;
    }

    /**
     * Generate a imageView at the given time with the given zoom
     * @param time seconds since last picture
     * @param zoom scale 0-3
     * @return generated Picture object
     */
    Picture generateImage(int time, ZoomLevel zoom) {
        BufferedImage image = new BufferedImage(image_size[0], image_size[1], BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(java.awt.Color.black);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        System.out.format("Generating picture at time: %d seconds:\n", time);

        // Generate a starting set of asteroids at time = 0
        if (elapsed_seconds == 0) {
            asteroids.add(new Asteroid(new int[]{200, 200, 1000}, 2500, new int[]{5, 5, -5}, 0));
            generateRandomAsteroids();
        }

        // If we no longer have our max number of asteroids then generate some more
        if (asteroids.size() < MAX_ASTEROIDS) {
            generateRandomAsteroids();
        }

        // Move all asteroids and draw them on the imageView
        for (Asteroid asteroid : asteroids) {
            asteroid.move(time);
            System.out.println(asteroid.toString());
            drawAsteroidToImage(image, asteroid);
        }

        // Remove all asteroids that will never show up again
        removeOffWindowAsteroids();


        if (zoom != ZoomLevel.NONE) {
            BufferedImage sub_image = image.getSubimage(zoom.x, zoom.y, zoom.width, zoom.height);
            Image tmp = sub_image.getScaledInstance(image_size[0], image_size[1], Image.SCALE_SMOOTH);
            image = new BufferedImage(image_size[0], image_size[1], Image.SCALE_SMOOTH);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
        }
        generateNoise(image);

        return new Picture(image);
    }

    private void generateNoise(BufferedImage image) {
        for(int i = 0; i < image_size[0]; i++){
            for(int j = 0; j < image_size[1]; j++){
                if(random.nextInt(100) <= 10){
                    int colorValue = random.nextInt(255);
                    int color = new Color(colorValue, colorValue, colorValue).getRGB();
                    image.setRGB(i,j,color);
                }
            }
        }
    }

    private void removeOffWindowAsteroids() {
        ArrayList<Asteroid> asteroids_to_be_removed = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            // If the asteroid is behind us then no additional calculation is required
            if (asteroid.current_location[2] <= 0) {
                asteroids_to_be_removed.add(asteroid);
                //asteroids.remove(asteroid);
                continue;
            }

            int xmin = asteroid.current_location[0] - asteroid.current_radius;
            int xmax = asteroid.current_location[0] + asteroid.current_radius;
            int ymin = asteroid.current_location[1] - asteroid.current_radius;
            int ymax = asteroid.current_location[1] + asteroid.current_radius;

            // If the asteroid is currently out of bounds of the image_size then we know it is no longer going to show up.
            if (xmax < 0 || ymax < 0 || xmin > image_size[0] || ymin > image_size[1]) {
                //asteroids.remove(asteroid);
                asteroids_to_be_removed.add(asteroid);
            }
        }

        for (Asteroid asteroid : asteroids_to_be_removed) {
            System.out.format("Removing asteroid %d\n", asteroid.getId());
            asteroids.remove(asteroid);
        }
    }

    private void generateRandomAsteroids() {
        while(asteroids.size() <= MAX_ASTEROIDS) {
            asteroids.add(new Asteroid(image_size, elapsed_seconds));
        }
    }

    /**
     * Used for simulation testing only
     * @return entire imageView
     */

    private void drawAsteroidToImage(BufferedImage image, Asteroid asteroid) {
        if (asteroid.current_location[2] <= 0) {
            System.out.format("Asteroid %d is now behind us.\n", asteroid.getId());
            return;
        }

        Graphics2D g = image.createGraphics();
        // Uncomment these 2 lines to draw circles:
        //g.setColor(Color.gray);
        //g.fillOval(asteroid.current_location[0] - asteroid.current_radius, asteroid.current_location[1] - asteroid.current_radius, asteroid.current_radius, asteroid.current_radius);

        // Uncomment this line to use the images:
        g.drawImage(asteroid.getImage(), asteroid.current_location[0] - asteroid.current_radius, asteroid.current_location[1] - asteroid.current_radius, asteroid.current_radius, asteroid.current_radius, null);

        g.setColor(Color.green);
        g.drawString(Integer.toString(asteroid.getId()), asteroid.current_location[0], asteroid.current_location[1]);
    }



}
