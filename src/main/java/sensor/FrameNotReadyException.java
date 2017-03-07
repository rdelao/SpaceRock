package sensor;

/**
 * Created by scnaegl on 3/7/17.
 */
public class FrameNotReadyException extends RuntimeException {
  public FrameNotReadyException() { super(); }
  public FrameNotReadyException(String message) { super(message); }
  public FrameNotReadyException(String message, Throwable cause) { super(message, cause); }
  public FrameNotReadyException(Throwable cause) { super(cause); }
}
