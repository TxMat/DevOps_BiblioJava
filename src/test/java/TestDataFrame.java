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



            /* Test sur les méthodes statistiques de DataFrame */

        /* Test sur filledDF */

    //Ce test vérifie que la fonction getMin(L label) donne la valeur minimale du dataFrame de la colonne label
    @Test
    public void testGetMinDataFrame(){
        Integer min = 1;
        assertEquals(filledDF.getMin("colonne1"), min);
    }

    //Ce test vérifie que la fonction getMax(L label) donne la valeur maximale du dataFrame de la colonne label
    @Test
    public void testGetMaxDataFrame(){
        Integer max = 3;
        assertEquals(filledDF.getMax("colonne1"), max);
    }

    //Ce test vérifie que la fonction getAverage(L label) donne la valeur moyenne du dataFrame de la colonne label
    @Test
    public void testGetAverageDataFrame(){
        double average = 2;
        assertEquals(filledDF.getAverage("colonne1"), average, 0.0001);
    }

    //Ce test vérifie que la fonction getCount(L label) donne le nombre d'entrée non null du dataFrame de la colonne label
    @Test
    public void testGetCountDataFrame(){
        int count = 3;
        assertEquals(filledDF.getCount("colonne1"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) calcule la somme des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetSumDataFrame(){
        double sum = 6;
        assertEquals(filledDF.getSum("colonne1"), sum, 0.0001);
    }

    //Ce test vérifie que la fonction getAbsolute(L label) calcule la valeur absolue de somme des entrées non null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinDataFrame(){
        double absolute = 6;
        assertEquals(filledDF.getAbsolute("colonne1"), absolute, 0.0001);
    }

    //Ce test vérifie que la fonction getProduct(L label) calcule le produit des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetProductDataFrame(){
        double product = 6;
        assertEquals(filledDF.getProduct("colonne1"), product, 0.0001);
    }

        /*Test sur voidDF */

    //Ce test vérifie que la fonction getMin(L label) donne la valeur minimale du dataFrame de la colonne label
    @Test
    public void testGetMinEmptyDataFrame(){
        assertNull(voidDF.getMin("colonne1"));
    }

    //Ce test vérifie que la fonction getMax(L label) donne la valeur maximale du dataFrame de la colonne label
    @Test
    public void testGetMaxEmptyDataFrame(){
        Integer max = 0;
        assertNull(voidDF.getMax("colonne1"));
    }

    //Ce test vérifie que la fonction getAverage(L label) donne la valeur moyenne du dataFrame de la colonne label
    @Test
    public void testGetAverageEmptyDataFrame(){
        double average = 0;
        assertEquals(voidDF.getAverage("colonne1"), average, 0.0001);
    }

    //Ce test vérifie que la fonction getCount(L label) donne le nombre d'entrée non null du dataFrame de la colonne label
    @Test
    public void testGetCountEmptyDataFrame(){
        int count = 0;
        assertEquals(voidDF.getCount("colonne1"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) calcule la somme des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetSumEmptyDataFrame(){
        double sum = 0;
        assertEquals(voidDF.getSum("colonne1"), sum, 0.0001);
    }

    //Ce test vérifie que la fonction getAbsolute(L label) calcule la valeur absolue de somme des entrées non null
    // du dataFrame de la colonne label
    @Test
    public void testAbsolutEmptyDataFrame(){
        double absolute = 0;
        assertEquals(voidDF.getAbsolute("colonne1"), absolute, 0.0001);
    }

    //Ce test vérifie que la fonction getProduct(L label) calcule le produit des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetProductEmptyDataFrame(){
        double product = 0;
        assertEquals(voidDF.getProduct("colonne1"), product, 0.0001);
    }


}
