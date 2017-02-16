/**
 * WARNING: NOT YET TESTED.
 */

package Processing;

import Commands.Asteroid;

import java.util.HashSet;

/**
 * This class saves the three most recent configurations of asteroid positions
 * and layouts, as received from the satellite system.  Using these, it
 * attempts to assign ids to asteroids believed to be the same across the
 * three configurations.
 * 
 * Relevant assumptions:
 * 
 * The debris are roughly spherical in shape.
 * The debris have approximately constant velocity.
 * The "location" field in the Asteroid class denotes the upper-left coordinate
 * of the asteroid, in screen coordinates.
 * The "size" field denotes the diameter of the asteroid as detected by the
 * camera, also in screen coordinates.
 */
public class DebrisProcessor
{
  public static final int NUM_CONFIGURATIONS = 3;
  public static final double CROSS_ERROR = 0.1;

  private Asteroid [] [] configurations;
  private int curIdx;
  private int currentId;
  
  public DebrisProcessor()
  {
    curIdx = 0;
    currentId = 1;
    configurations = new Asteroid [NUM_CONFIGURATIONS] [];
  }
  
  /**
   * Add the most recent configuration to the set of tracked configurations,
   * evicting the least recent.  If at least three configurations have been
   * added, id numbers will be assigned to the asteroids upon the method's
   * completion.
   * @param configuration The most current asteroid configuration to be added.
   * @return true if labels are assigned to the asteroids, false otherwise.
   */
  public boolean addAndAssign(Asteroid [] configuration)
  {
    if (curIdx < NUM_CONFIGURATIONS - 1)
    {
      configurations[curIdx++] = configuration;
      return false;
    }
    
    // We update in a circular queue-like manner.  The first entry in
    // configurations holds the least recent configuration, the last entry
    // holds the most recent.
    
    for (int i = 1; i < NUM_CONFIGURATIONS; i++)
    {
      configurations[i - 1] = configurations[i];
    }
    
    configurations[NUM_CONFIGURATIONS - 1] = configuration;
    
    assignLabels();
    return true;
  }
  
  /**
   * Using the newest configuration of asteroids, assign labels based on their
   * behavior in this and the past two configurations.
   * 
   * Given the three asteroids a1, a2, and a3 in three separate configurations,
   * where a1 is least recent and a3 is the most recent, they are considered to
   * be the same asteroid and given the same label if:
   * 
   * The centers of a1, a2, and a3 are situated on a straight line relative to
   * the viewing plane.
   * 
   * a1, a2, and a3 are displaced progressively leftward or rightward from each
   * other (in other words, the asteroid moves solely to the left or to the
   * right as time advances, and doesn't seem to "turn around" in the middle of
   * its trajectory).
   * 
   * a1, a2, and a3 are displaced progressively upward or downward from each
   * other.
   * 
   * a1, a2, and a3 grow progressively larger or smaller.
   */
  public void assignLabels()
  {
    HashSet<Long> usedIndices = new HashSet<>();
    
    // For every triplet of asteroids in the three configurations:
    
    for (int i = 0; i < configurations[0].length; i++)
    {
      Asteroid a1 = configurations[0][i];
      
      double x1 = a1.location.getX() + a1.size / 2;
      double y1 = a1.location.getY() + a1.size / 2;
      
      for (int j = 0; j < configurations[1].length; j++)
      {
        Asteroid a2 = configurations[1][j];
        
        double x2 = a2.location.getX() + a2.size / 2;
        double y2 = a2.location.getY() + a2.size / 2;
        
        Vector2 v1 = new Vector2(x2 - x1, y2 - y1);
        
        for (int k = 0; k < configurations[2].length; k++)
        {
          Asteroid a3 = configurations[2][k];
          
          double x3 = a3.location.getX() + a3.size / 2;
          double y3 = a3.location.getY() + a3.size / 2;
          
          Vector2 v2 = new Vector2(x3 - x2, y3 - y2);
          
          // Verify their centers are along a straight line, approximately
          // (the cross product of their vectors is roughly zero).
          double errCross = Math.abs(v1.cross(v2));
          
          // Check that this asteroid moves only to the left or only to the
          // right.
          boolean rightward = x1 < x2 && x2 < x3;
          boolean leftward = x1 > x2 && x2 > x3;
          
          // Check that this asteroid moves only upward or downward. 
          boolean upward = y1 > y2 && y2 > y3;
          boolean downward = y1 < y2 && y2 < y3;
          
          // Check that this asteroid only moves forward or backward.
          boolean forward = a1.size < a2.size && a2.size < a3.size;
          boolean backward = a1.size > a2.size && a2.size > a3.size;
          
          if (errCross < CROSS_ERROR && (rightward || leftward) &&
              (upward || downward) && (forward || backward) &&
              !usedIndices.contains(a2.id))
          {
            // These asteroids are considered the same.  We will now assign it
            // an id.
            
            if (a2.id == 0)
            {
              // This is the first time we've begun to track this asteroid (we
              // haven't given it an id previously) - assign it its own unique
              // label.
              
              a1.id = currentId;
              a2.id = currentId;
              a3.id = currentId++;
            }
            else
            {
              // We've already been tracking this asteroid - simply assign the
              // most recent version of this asteroid the id of its
              // predecessor.

              a3.id = a2.id;
            }
            // Finally, track the id so we don't end up giving two asteroids
            // the same id if there are two possible candidates for the third
            // asteroid for one existing asteroid path.
            
            usedIndices.add(a3.id);
          }
        }
      }
    }
  }
  
  /**
   * Utility vector class for use in assigning labels to the asteroids.
   */
  private class Vector2
  {
    double x;
    double y;
    
    Vector2(double x, double y)
    {
      this.x = x;
      this.y = y;
    }
    
    double cross(Vector2 other)
    {
      return x * other.y - other.x * y;
    }
  }
}
