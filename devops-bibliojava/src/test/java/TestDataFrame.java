import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TestDataFrame {

    DataFrame<String, String, Integer> voidDF;
    DataFrame<String, String, Integer> filledDF;

    @Before
    public void init() throws Exception{
        List<String> index1 = List.of();
        List<String> label1 = List.of();
        List<List<Integer>> values1 = List.of();

        voidDF = new DataFrame<>(index1, label1, values1);


        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2),
                List.of(3, 4)
        );

        filledDF = new DataFrame<>(index2, label2, values2);
    }


    /* Tests sur la construction de DataFrame */

    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithNoValueInColumn() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2),
                List.of(5, 6)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithNoValueInLine() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2, 3)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithMoreLineThanRequire() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2, 0),
                List.of(3, 4, 0),
                List.of(5, 6, 0)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithMoreColumnThanRequire() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2 , 0, 1),
                List.of(3, 4, 0 , 1)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    @Test
    public void testCreateDataFrameWithSameIndex() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Integer>> values2 = List.of(
                List.of(0, 0),
                List.of(1, 2),
                List.of(3, 4)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }

    @Test
    public void testCreateDataFrameWithSameLabel() throws Exception {
        DataFrame<String, String, Integer> dataFrame;

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne2");
        List<List<Integer>> values2 = List.of(
                List.of(1, 0, 2),
                List.of(3, 0, 4)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }
    
}