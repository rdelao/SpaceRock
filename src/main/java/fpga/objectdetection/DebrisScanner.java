package fpga.objectdetection;
import java.util.ArrayList;

/**
 * Created by Rob on 2/25/2017.
 */

// NOTE: IS BOARDER RELIABLE DATA -- set variable for this?
// gausian filter does not provide reliable boarder data (cause it has no information to pull from edges)
class DebrisScanner {

    private byte[][] searchedArray;
    private byte[][] debrisMap;
    private int imgSize;
    // SOME LIST OF DEBRIS -- not sure how this fits into greater scope
    private ArrayList<Debris> foundObjects = new ArrayList<>();

    private boolean validDebris; // if on boarder, then dont know total scope --> invalid
    private int debrisSize;

    DebrisScanner(byte[][] debrisMap, int imgSize)
    {
        this.debrisMap = debrisMap;
        this.imgSize = imgSize;
        searchedArray = new byte[imgSize][imgSize];
        zeroSearchedArray();
    }

    private void zeroSearchedArray()
    {
        for (int i = 0; i < imgSize; i++)
        {
            for (int j = 0; j < imgSize; j++)
            {
                searchedArray[i][j] = 0;
            }
        }
    }

    // SOME LIST OF DEBRIS searchDebrisMap()
    // Scan line by line (ignoring edges -- gausian filter unreliable)
    // Once we find any debris, BFS for its entirety
    // if it reaches edge, ignore
    // if it is enclosed in frame, record
    ArrayList<Debris> searchDebrisMap()
    {
        for (int j = 0; j < imgSize; j++)
        {
            for (int i = 0; i < imgSize; i++)
            {
                checkSpace(i, j);
            }
        }
        return foundObjects;
    }

    private void checkSpace(int i, int j)
    {
        if( searchedArray[i][j] == 1 ) { return; }
        searchedArray[i][j] = 1;
        if( debrisMap[i][j] == 0 ) { return; }
        startDebrisSearch(i, j);
    }

    private boolean startDebrisSearch(int i, int j)
    {
        validDebris = !(onBoarder(i, j));
        debrisSize = 1;
        for( Dir d : Dir.values() )
        {
            search(i + d.deltaX(), j + d.deltaY() );
        }
        if( validDebris )
        {
            foundObjects.add( new Debris(i, j, debrisSize) );
            return true;
        }
        return false;
    }

    private SearchValue search(int i, int j)
    {
        if( outOfBounds(i, j) ) { return SearchValue.OUT_OF_BOUNDS; }
        if( searchedArray[i][j] == 1 ) { return SearchValue.ALREADY_SEARCHED; }
        searchedArray[i][j] = 1;
        if( debrisMap[i][j] == 0 ) { return SearchValue.NO_DEBRIS; }
        if( onBoarder(i, j) )
        {
            validDebris = false;
        }
        for( Dir d : Dir.values() )
        {
            search(i + d.deltaX(), j + d.deltaY() );
        }
        debrisSize++;
        return SearchValue.DEBRIS_NORMAL;
    }

    private boolean outOfBounds(int x, int y)
    {
        if( x < 0 || x >= imgSize ) { return true; }
        if( y < 0 || y >= imgSize ) { return true; }
        return false;
    }

    private boolean onBoarder(int x, int y)
    {
        if( x == 0 || x == (imgSize - 1) ) { return true; }
        if( y == 0 || y == (imgSize - 1) ) { return true; }
        return false;
    }

    void printList()
    {
        System.out.println("Found " + foundObjects.size() + " 'valid' debris");
        int counter = 1;
        for( Debris d : foundObjects)
        {
            System.out.println("Object " + counter + " found @ (" + d.frameX + ", " + d.frameY + ") with size " + d.size);
            counter++;
        }
    }
}
