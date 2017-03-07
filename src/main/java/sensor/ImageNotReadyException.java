package sensor;

/**
 * Created by scnaegl on 3/7/17.
 */
public class ImageNotReadyException extends RuntimeException {
  public ImageNotReadyException() { super(); }
  public ImageNotReadyException(String message) { super(message); }
  public ImageNotReadyException(String message, Throwable cause) { super(message, cause); }
  public ImageNotReadyException(Throwable cause) { super(cause); }
}
