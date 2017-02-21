/**
 WARNING: NOT YET TESTED - MAY STILL NEED SOME SUBSTANTIAL WORK.
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
 * The debris are approximable as spheres in shape.
 * The debris have approximately linear trajectories.
 * The "location" field in the Asteroid class denotes the center of the
 * asteroid, in screen coordinates.
 * The "size" field denotes the diameter of the asteroid as detected by the
 * camera, also in screen coordinates.
 */
public class DebrisProcessor {
    public static final int NUM_CONFIGURATIONS = 3;
 
    /**
     Currently an arbitrary constant value - may have to be tweaked in
     accordance with the sensitivity of the chosen coordinate system.
     If the computed cross product of vectors from the centers of three
     asteroids is less than this value, they pass the cross product test for
     being along a straight line.
     */
    public static final double CROSS_ERROR = 0.1;

    private Asteroid[][] configurations;
    private int curIdx;
    private int currentId;


    public DebrisProcessor() {
        curIdx = 0;
        currentId = 1;
        configurations = new Asteroid[NUM_CONFIGURATIONS][];
    }


    /**
     Add the most recent configuration to the set of tracked configurations,
     evicting the least recent.  If at least three configurations have been
     added, id numbers will be assigned to the asteroids upon the method's
     completion.

     @param configuration The most current asteroid configuration to be added.

     @return true if labels are assigned to the asteroids, false otherwise.
     */
    public boolean addAndAssign(Asteroid[] configuration) {
     
        // Add the newest configuration to the working set and evict the
        // least recent.
     
        if (curIdx < NUM_CONFIGURATIONS - 1) {
            configurations[curIdx++] = configuration;
            return false;
        } else if (curIdx == NUM_CONFIGURATIONS - 1) {
            configurations[curIdx++] = configuration;
        } else {

            // We update in a circular queue-like manner.  The first entry in
            // configurations holds the least recent configuration, the last entry
            // holds the most recent.

            for (int i = 1; i < NUM_CONFIGURATIONS; i++) {
                configurations[i - 1] = configurations[i];
            }

            configurations[NUM_CONFIGURATIONS - 1] = configuration;
        }

        assignLabels();
        return true;
    }


    /**
     Using the newest configuration of asteroids, assign labels based on their
     behavior in this and the past two configurations.
     <p>
     Given the three asteroids a1, a2, and a3 in three separate configurations,
     where a1 is least recent and a3 is the most recent, they are considered to
     be the same asteroid and given the same label if:
     <p>
     The centers of a1, a2, and a3 trace out a straight line relative to the
     viewing plane (this should be the case if the trajectories are linear, even
     in three dimensions).
     <p>
     a1, a2, and a3 are displaced progressively leftward or rightward from each
     other (in other words, the asteroid moves solely to the left or to the
     right as time advances, and doesn't seem to "turn around" in the middle of
     its trajectory).
     <p>
     a1, a2, and a3 are displaced progressively upward or downward from each
     other.
     <p>
     a1, a2, and a3 grow progressively larger or smaller.
     <p>
     Note that this only assigns labels to those asteroids deemed to be the
     same.  It's possible that some asteroids that aren't added to a triplet
     will be left unassigned.  This may just mean that we haven't yet collected
     enough images of that particular one to assign an id quite yet.  (It's
     also possible that an asteroid that ends up hidden by another overlapping
     asteroid will end up having to be reassigned by the processor.  This is
     unfortunate, but ideally shouldn't be a huge issue.)
     */
    public void assignLabels() {
        HashSet<Long> usedIndices = new HashSet<>();

        // For every triplet of asteroids in the three configurations:

        for (int i = 0; i < configurations[0].length; i++) {
            Asteroid a1 = configurations[0][i];

            // We get the centerpoint of each asteroid.  Note that I'm
            // assuming here that getX() and getY() return the center
            // of each asteroid by default.
         
            double x1 = a1.location.getX();
            double y1 = a1.location.getY();

            for (int j = 0; j < configurations[1].length; j++) {
                Asteroid a2 = configurations[1][j];

                double x2 = a2.location.getX();
                double y2 = a2.location.getY();

                Vector2 v1 = new Vector2(x2 - x1, y2 - y1);

                for (int k = 0; k < configurations[2].length; k++) {
                    Asteroid a3 = configurations[2][k];

                    double x3 = a3.location.getX();
                    double y3 = a3.location.getY();

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
                        !usedIndices.contains(a2.id)) {
                        // This triplet is considered the same.  We will now assign it an
                        // id.
                     
                        // TODO: Note that these criteria are very strict.  If an asteroid
                        // is measured to move even a little bit in an unexpected direction,
                        // the entire thing will fail.  It might be best to do something
                        // similar to the cross product above, and permit a certain amount
                        // of "error" into the calculation before rejecting it as opposed to
                        // using the unforgiving booleans above.

                        if (a2.id == 0) {
                            // This is the first time we've begun to track this asteroid (we
                            // haven't given it an id previously) - assign it a unique
                            // label.

                            a1.id = currentId;
                            a2.id = currentId;
                            a3.id = currentId++;
                        } else {
                            // We've already been tracking this particular asteroid - this
                            // is just the next step in its motion.  So, just give it the
                            // id of its predecessor.

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
     TODO: Assign threat level.  Should hopefully not be terrible to do.  My idea was to
     measure the average amount that the size changes with respect to time for each
     growing asteroid.  The timestamps will be needed to get the average increase in
     radius, but as objects closer to us appear to "grow" faster, you can get a good
     idea of how close you are to colliding with the asteroid most likely by comparing
     the rates of growth of each asteroid.  If they're increasing, and increasing by a
     significant margin, collision is probably going to be pretty imminent, and that'd
     correspond to a more "dangerous" threat level.
    */
 
    /**
     TODO: Assign velocity.  I'm not sure there's a good way to do it.  A two-dimensional
     image of a three-dimensional view with no distances known makes it pretty much
     impossible to assign exact values as there's so much ambiguity.  I.e. if you see a
     tiny rock on your screen flit past your screen quickly, that could be either a
     dime-sized rock moving a couple of inches away from the lens (in which case, the
     velocity of that particular asteroid is low) or a rock the size of the Sun moving
     several million kilometers away (in which case, the velocity of that particular
     asteroid would be ridiculously high) due to the fact that the camera can't emulate
     depth perception.  Maybe there's some way it can be estimated.
    */
 
    /**
     Utility vector class for use in assigning labels to the asteroids.
     */
    private class Vector2 {
        double x;
        double y;


        Vector2(double x, double y) {
            this.x = x;
            this.y = y;
        }


        double cross(Vector2 other) {
            return x * other.y - other.x * y;
        }
    }
}
