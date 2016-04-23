import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 4/20/2016.
 */
public class Test {
    public static void main(String args[]){
        byte[] b = createByteMessage(-90, -180);
        List<Integer> results = decodeByteMessage(b);
        System.out.println(results.get(0) + " " + results.get(1));

    }

    public static List<Integer> decodeByteMessage(byte[] b)
    {
        List<Integer> list = new ArrayList<Integer>();

        int i1 = (b[0] & 0xFF) << 8 |
                (b[1] & 0xFF);

        int i2 = (b[2] & 0xFF) << 8 |
                (b[3] & 0xFF);

        list.add(i1);
        list.add(i2);

        return list;
    }

    public static byte[] createByteMessage(int a, int b) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF),
                (byte) ((b >> 8) & 0xFF),
                (byte) (b & 0xFF)
        };
    }

}
