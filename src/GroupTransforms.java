import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by sahba on 9/22/16.
 * The class is based on the Xform class with the aim of facilitating applying transformations
 * to the objects
 */
public class GroupTransforms extends Group

{
  private final double ROTATION_AMOUNT;
  private final Rotate ry;
  private final Rotate rx;
  private final Translate tz;
  private final double MOVE_AMOUNT;

  /**
   * constructor
   *
   * @param rotationAmount the amount of rotaion for the rotation methods
   * @param moveAmount     the amount of move for the move methods
   */
  public GroupTransforms(double rotationAmount, double moveAmount)
  {
    super();

    ROTATION_AMOUNT = rotationAmount;
    MOVE_AMOUNT = moveAmount;
    ry = new Rotate();
    ry.setAxis(Rotate.Y_AXIS);
    ry.setPivotX(4);
    ry.setPivotY(4);
    ry.setPivotZ(4);

    rx = new Rotate();
    rx.setAxis(Rotate.X_AXIS);
    rx.setPivotX(4);
    rx.setPivotY(4);
    rx.setPivotZ(4);

    tz = new Translate();

    this.getTransforms().addAll(rx, ry, tz);
  }

  /**
   * adds the ROTATION_AMOUNT to the current rotation in along Y axis
   */
  public void addRotateY()
  {
    ry.setAngle(ry.getAngle() + ROTATION_AMOUNT);

  }

  /**
   * adds the ROTATION_AMOUNT to the current rotation in along X axis
   */
  public void addRotateX()
  {

    rx.setAngle(rx.getAngle() + ROTATION_AMOUNT);
  }

  /**
   * moves the objects in the positive Z direction
   */
  public void movePositiveZ()
  {

    tz.setZ(tz.getZ() + MOVE_AMOUNT);
  }

  /**
   * moves the objects in the negative Z direction
   **/
  public void moveNegativeZ()

  {

    tz.setZ(tz.getZ() - MOVE_AMOUNT);
  }

  /**
   * sets the z position of the objects in the group
   *
   * @param position (Z)
   */
  public void setPositionZ(Double position)

  {

    tz.setZ(position);
  }

  /**
   * sets the Y position of the object in the group
   *
   * @param position (the y position)
   */
  public void setPositionY(Double position)
  {
    tz.setY(position);
  }
}
