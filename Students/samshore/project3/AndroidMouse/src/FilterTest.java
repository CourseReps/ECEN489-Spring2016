import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sam on 4/16/2016.
 */
public class FilterTest {

    public static void main(String args[]){
        List<Integer> data = new ArrayList<Integer>(Collections.nCopies(40,0));
        List<Float> filteredData = new ArrayList<Float>(Collections.nCopies(5,0f));

        /* print initial filteredData */
        for(int j=0; j<filteredData.size()-1; j++){
            System.out.print(filteredData.get(j) + " ");
        }
        System.out.println(filteredData.get(filteredData.size()-1));

        List<Float> f = makeFilter(5);

        /* filter data and print */
        for(int i = 0; i<20; i++){
            data.set(i, 90);
            filteredData.add(0,filter(f,data));

            for(int j=0; j<filteredData.size()-1; j++){
                System.out.print(filteredData.get(j) + " ");
            }
            System.out.println(filteredData.get(filteredData.size()-1));
        }

    }

    public static float filter(List<Float> f, List<Integer> data){
        float sum = 0;
        for(int i=0; i<f.size(); i++){
            sum = sum + f.get(i)*data.get(i);   // dot product of data and filter
        }
        return sum; // filtered data point
    }

    public static List<Float> makeFilter(int N){
        /* calculate denominator of filter coefficients */
        float denominator = 0f;
        for(int i = N; i>0; i--){
            denominator = denominator + i;
        }

        /* create filter */
        List<Float> f = new ArrayList<Float>();
        for(int i = N; i>0; i--){
            f.add((N-i),i/denominator);
        }
        return f;
    }
}
