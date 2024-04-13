package test.java;

import static org.junit.Assert.*;
import main.java.DataFrame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TestDataFrame {

    DataFrame<String, String, Object> voidDF;
    DataFrame<String, String, Object> filledDF;

    DataFrame<String, String, Object> dataFrame;

    //Initialisation de deux DataFrame : un vide et un rempli
    @Before
    public void init() throws Exception{
        List<String> index1 = List.of();
        List<String> label1 = List.of();
        List<List<Object>> values1 = List.of();
        voidDF = new DataFrame<>(index1, label1, values1);


        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 3),
                List.of("I'm toto", "I'm tata", "I'm titi")
        );
        filledDF = new DataFrame<>(index2, label2, values2);
    }


    /* Tests sur la construction de DataFrame */


    //Ce test vérifie qu'une exception est levée si le nombre de valeurs dans une colonne n'est pas valide
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithNoValueInColumn() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Object>> values2 = List.of(
                List.of(1, 2),
                List.of(5, 6)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    //Ce test vérifie qu'une exception est levée si le nombre de valeurs dans une ligne n'est pas valide
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithNoValueInLine() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Object>> values2 = List.of(
                List.of(1, 2),
                List.of('a', 'b'),
                List.of("c")
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    //Ce test vérifie qu'une exception est levée si le nombre de lignes est plus grand que le nombre d'indices
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithMoreLineThanRequire() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3");
        List<List<Object>> values2 = List.of(
                List.of(1, 2),
                List.of("I'm toto", "I'm tata", "I'm titi"),
                List.of(5.4, 6.2)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    //Ce test vérifie qu'une exception est levée si le nombre de colonnes est plus grand que le nombre de labels
    @Test (expected = IndexOutOfBoundsException.class)
    public void testCreateDataFrameWithMoreColumnThanRequire() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Object>> values2 = List.of(
                List.of(1, 2),
                List.of(-3, -4),
                List.of("I'm toto", "I'm tata")
        );

        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    /* This test verify that an exception is thrown when a column has elements of different types */
    @Test (expected = IllegalArgumentException.class)
    public void testCreateDataFrameWithDifferentTypesInColumn() throws Exception {
        List<String> index2 = List.of("ligne1", "ligne2");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Object>> values2 = List.of(
                List.of(1, 2),
                List.of("a", 'a')
        );
        dataFrame = new DataFrame<>(index2, label2, values2);
    }

    //Ce test vérifie que la création d'un Dataframe avec deux nom d'indice identique fonctionne
    // et que le premier est écrasé par le second
    @Test
    public void testCreateDataFrameWithSameIndex() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3", "ligne1");
        List<String> label2 = List.of("colonne1", "colonne2");
        List<List<Object>> values2 = List.of(
                List.of(9, 2, 3, 1),
                List.of("I'm truc", "I'm tata", "I'm titi", "I'm toto")
        );

        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }

    //Ce test vérifie que la création d'un Dataframe avec deux nom de labels identique fonctionne
    // et que le premier est écrasé par le second
    @Test
    public void testCreateDataFrameWithSameLabel() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne2");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 3),
                List.of(1.2, 3.4, 5.6),
                List.of("I'm toto", "I'm tata", "I'm titi")
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }
    
}
