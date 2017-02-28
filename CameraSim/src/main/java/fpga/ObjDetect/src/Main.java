package fpga.ObjDetect.src;
import java.io.IOException;

/**
 * Created by Rob on 2/25/2017.
 */

// controler for demo
// reads in imgs from res (real application will receive imgs from gausian filter module)
// converts image to byte array -- boolean there is debris @ (x,y) or there isn'
// code scans the array to determine objects (key features: size & location)
// outputs _______

public class Main {

    private int imgSize = 12;
    private byte[][] debrisMap;

    public static void main(String args[]) throws IOException
    {
        Main test = new Main();
        test.test1();
        test.test2();
    }

    private void test1() throws IOException
    {
        ImgToByteArray test = new ImgToByteArray(".\\res\\12xSample02.png", imgSize);
        debrisMap = test.parseMap();
        printArray();
    }

    private void test2()
    {
        DebrisScanner dSTest = new DebrisScanner(debrisMap, imgSize);
        dSTest.searchDebrisMap();
        dSTest.printList();
    }

    private void printArray()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < imgSize; i++)
        {
            for (int j = 0; j < imgSize; j++)
            {
                str.append(debrisMap[j][i]);
            }
            str.append("\n");
        }
        System.out.println(str.toString());
    }
}
