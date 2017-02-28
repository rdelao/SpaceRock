package memory;

/**
 * Created by ststromberg, erparks, and christiaan on 2/26/17.
 * Represents the zoom level of the camera.
 */
public enum ZoomLevel {
    TWO(2), FOUR(4), EIGHT(8);

    private final int zoomLevel;

    ZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }
}