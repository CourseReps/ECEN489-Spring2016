import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 4/20/2016.
 */
public class Test {
    public static void main(String args[]){
        byte[] b = createByteMessage(0xFF, 90, 180);
        List<Integer> results = decodeByteMessage(b);
        System.out.println(results.get(0) + " " + results.get(1) + " " + results.get(2));

    }

    public static List<Integer> decodeByteMessage(byte[] b)
    {
        List<Integer> list = new ArrayList<Integer>();

        int i0 = b[0] & 0xFF;
        int i1 = b[1] & 0xFF;
        int i2 = b[2] & 0xFF;

        list.add(i0);
        list.add(i1);
        list.add(i2);

        return list;
    }

    public static byte[] createByteMessage(int mouseMessage, int xRot, int yRot)
    {
        return new byte[] {
                (byte) (mouseMessage & 0xFF),
                (byte) (xRot & 0xFF),
                (byte) (yRot & 0xFF)
        };
    }


}
