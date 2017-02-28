package operator.commands;

import java.awt.*;
import java.io.Serializable;

/**
 @author David Ringo created: 2017.02.16 class:   cs351 project: spacerock-gui description: */
public interface ImageResponse extends Serializable {
    int getXOffset();

    int getYOffset();

    int[] getPixels();

    int getHeight();

    int getWidth();

    long getAsteroidID();

    Image getImage();
}
