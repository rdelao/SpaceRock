package fpga.objectdetection;
/**
 * Created by Rob on 2/26/2017.
 */
public enum Dir
{
    NORTH
        { public int deltaX() {return  0;}
            public int deltaY() {return -1;}
        },

    EAST
            { public int deltaX() {return  1;}
                public int deltaY() {return  0;}
            },

    SOUTH
            { public int deltaX() {return  0;}
                public int deltaY() {return  1;}
            },


    WEST
            { public int deltaX() {return -1;}
                public int deltaY() {return  0;}
            };


    public abstract int deltaX();
    public abstract int deltaY();
    public static final int SIZE = values().length;
}
