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
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 3),
                List.of("I'm toto", "I'm tata", "I'm titi"),
                List.of('a', 'b', 'c'),
                List.of(true, false, true)
        );
        filledDF = new DataFrame<>(index2, label2, values2);
    }

            /* ************************************** */
            /* Tests sur la construction de DataFrame */
            /* ************************************** */


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
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(9, 2, 3, 1),
                List.of("I'm truc", "I'm tata", "I'm titi", "I'm toto"),
                List.of('d', 'b', 'c', 'a'),
                List.of(false, false, true, true)
        );

        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }

    //Ce test vérifie que la création d'un Dataframe avec deux nom de labels identique fonctionne
    // et que le premier est écrasé par le second
    @Test
    public void testCreateDataFrameWithSameLabel() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 3),
                List.of(1.2, 3.4, 5.6),
                List.of("I'm toto", "I'm tata", "I'm titi"),
                List.of('a', 'b', 'c'),
                List.of(true, false, true)
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        assertEquals(filledDF.toString(), dataFrame.toString());
    }

            /* *********************************************** */
            /* Test sur les méthodes statistiques de DataFrame */
            /* *********************************************** */

        /* Test sur filledDF */

    //Ce test vérifie que la fonction getMin(L label) donne la valeur minimale du dataFrame de la colonne label
    @Test
    public void testGetMinDataFrame(){
        Integer min = 1;
        assertEquals(filledDF.getMin("colonne1"), min);
    }

    //Ce test vérifie que la fonction getMin(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMinOnStringDataFrame(){
        assertNull(filledDF.getMin("colonne2"));
    }

    //Ce test vérifie que la fonction getMin(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMinOnCharDataFrame(){

        assertNull(filledDF.getMin("colonne3"));
    }

    //Ce test vérifie que la fonction getMin(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMinOnBoolDataFrame(){
        assertNull(filledDF.getMin("colonne4"));
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie la valeur minimale même si elle est à la fin de la liste
    @Test
    public void testGetMinWithMinOnTheEndOfColumnDataFrame() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, -666)
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        Integer min = -666;
        assertEquals(dataFrame.getMin("colonne1"), min);
    }


    //Ce test vérifie que la fonction getMax(L label) renvoie la valeur minimale même si elle est dupliquée
    @Test
    public void testGetMinWithDuplicateMinDataFrame() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1");
        List<List<Object>> values2 = List.of(
                List.of(-666, -666, 3)
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        Integer min = -666;
        assertEquals(dataFrame.getMin("colonne1"), min);
    }

        //Test sur le max

    //Ce test vérifie que la fonction getMax(L label) donne la valeur maximale du dataFrame de la colonne label
    @Test
    public void testGetMaxDataFrame(){
        Integer max = 3;
        assertEquals(filledDF.getMax("colonne1"), max);
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMaxOnStringDataFrame(){
        assertNull(filledDF.getMax("colonne2"));
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMaxOnCharDataFrame(){

        assertNull(filledDF.getMax("colonne3"));
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie null car la colonne ne contient pas de nombre
    @Test
    public void testGetMaxOnBoolDataFrame(){
        assertNull(filledDF.getMax("colonne4"));
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie la valeur maximale même si elle est à la fin de la liste
    @Test
    public void testGetMaxWithMaxOnTheEndOfColumnDataFrame() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 666)
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        Integer max = 666;
        assertEquals(dataFrame.getMax("colonne1"), max);
    }

    //Ce test vérifie que la fonction getMax(L label) renvoie la valeur maximale même si elle est dupliquée
    @Test
    public void testGetMaxWithDuplicateMaxDataFrame() throws Exception {

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1");
        List<List<Object>> values2 = List.of(
                List.of(666, 666, 3)
        );
        dataFrame = new DataFrame<>(index2, label2, values2);

        Integer max = 666;
        assertEquals(dataFrame.getMax("colonne1"), max);
    }


        //Tests sur les autres méthodes statistiques


    //Ce test vérifie que la fonction getAverage(L label) donne la valeur moyenne du dataFrame de la colonne label
    @Test
    public void testGetAverageDataFrame(){
        double average = 2;
        assertEquals(filledDF.getAverage("colonne1").doubleValue(), average, 0.0001);
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
        assertEquals(filledDF.getSum("colonne1").doubleValue(), sum, 0.0001);
    }

    //Ce test vérifie que la fonction getAbsolute(L label) calcule la valeur absolue de somme des entrées non null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinDataFrame(){
        double absolute = 6;
        assertEquals(filledDF.getAbsolute("colonne1").doubleValue(), absolute, 0.0001);
    }

    //Ce test vérifie que la fonction getProduct(L label) calcule le produit des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetProductDataFrame(){
        double product = 6;
        assertEquals(filledDF.getProduct("colonne1").doubleValue(), product, 0.0001);
    }

        //test sur des colonnes non valide

    //Ce test vérifie que la fonction getAverage(L label) sur une mauvaise colonne donne null
    @Test
    public void testGetAverageOnWrongColumnDataFrame(){
        assertNull(filledDF.getAverage("colonneA"));
    }

    //Ce test vérifie que la fonction getCount(L label) sur une mauvaise colonne donne -1
    @Test
    public void testGetCountOnWrongColumnDataFrame(){
        int count = -1;
        assertEquals(filledDF.getCount("colonneA"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) sur une mauvaise colonne donne null
    @Test
    public void testGetSumOnWrongColumnDataFrame(){
        assertNull(filledDF.getSum("colonneA"));
    }

    //Ce test vérifie que la fonction getAbsolute(L label) sur une mauvaise colonne donne null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinOnWrongColumnDataFrame(){
        assertNull(filledDF.getAbsolute("colonneA"));
    }

    //Ce test vérifie que la fonction getProduct(L label) sur une mauvaise colonne donne null
    @Test
    public void testGetProductOnWrongColumnDataFrame(){
        assertNull(filledDF.getProduct("colonneA"));
    }

        //Test sur des String

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de String donne null
    @Test
    public void testGetAverageOnStringColumnDataFrame(){
        assertNull(filledDF.getAverage("colonne2"));
    }

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de String donne le bon résultat
    @Test
    public void testGetCountOnStringColumnDataFrame(){
        int count = 3;
        assertEquals(filledDF.getCount("colonne2"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) sur une colonne de String donne null
    @Test
    public void testGetSumOnStringColumnDataFrame(){
        assertNull(filledDF.getSum("colonne2"));
    }

    //Ce test vérifie que la fonction getAbsolute(L label) sur colonne de String donne null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinOnStringColumnDataFrame(){
        assertNull(filledDF.getAbsolute("colonne2"));
    }

    //Ce test vérifie que la fonction getProduct(L label) sur une colonne de String donne null
    @Test
    public void testGetProductOnStringColumnDataFrame(){
        assertNull(filledDF.getProduct("colonne2"));
    }


        //Test sur des char

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de char donne null
    @Test
    public void testGetAverageOnCharColumnDataFrame(){
        assertNull(filledDF.getAverage("colonne3"));
    }

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de char donne le bon résultat
    @Test
    public void testGetCountOnCharColumnDataFrame(){
        int count = 3;
        assertEquals(filledDF.getCount("colonne3"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) sur une colonne de char donne null
    @Test
    public void testGetSumOnCharColumnDataFrame(){
        assertNull(filledDF.getSum("colonne3"));
    }

    //Ce test vérifie que la fonction getAbsolute(L label) sur colonne de char donne null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinOnCharColumnDataFrame(){
        assertNull(filledDF.getAbsolute("colonne3"));
    }

    //Ce test vérifie que la fonction getProduct(L label) sur une colonne de char donne null
    @Test
    public void testGetProductOnCharColumnDataFrame(){
        assertNull(filledDF.getProduct("colonne3"));
    }


        //Test sur des boolean

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de Boolean donne null
    @Test
    public void testGetAverageOnBooleanColumnDataFrame(){
        assertNull(filledDF.getAverage("colonne4"));
    }

    //Ce test vérifie que la fonction getCount(L label) sur une colonne de Boolean donne le bon résultat
    @Test
    public void testGetCountOnBooleanColumnDataFrame(){
        int count = 3;
        assertEquals(filledDF.getCount("colonne4"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) sur une colonne de Boolean donne null
    @Test
    public void testGetSumOnBooleanColumnDataFrame(){
        assertNull(filledDF.getSum("colonne4"));
    }

    //Ce test vérifie que la fonction getAbsolute(L label) sur colonne de Boolean donne null
    // du dataFrame de la colonne label
    @Test
    public void testAbsoluteMinOnBooleanColumnDataFrame(){
        assertNull(filledDF.getAbsolute("colonne4"));
    }

    //Ce test vérifie que la fonction getProduct(L label) sur une colonne de Boolean donne null
    @Test
    public void testGetProductOnBooleanColumnDataFrame(){
        assertNull(filledDF.getProduct("colonne4"));
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
        assertNull(voidDF.getMax("colonne1"));
    }

    //Ce test vérifie que la fonction getAverage(L label) donne la valeur moyenne du dataFrame de la colonne label
    @Test
    public void testGetAverageEmptyDataFrame(){
        assertNull(voidDF.getAverage("colonne1"));
    }

    //Ce test vérifie que la fonction getCount(L label) donne le nombre d'entrée non null du dataFrame de la colonne label
    @Test
    public void testGetCountEmptyDataFrame(){
        int count = -1;
        assertEquals(voidDF.getCount("colonne1"), count);
    }

    //Ce test vérifie que la fonction getSum(L label) calcule la somme des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetSumEmptyDataFrame(){
        assertNull(voidDF.getSum("colonne1"));
    }

    //Ce test vérifie que la fonction getAbsolute(L label) calcule la valeur absolue de somme des entrées non null
    // du dataFrame de la colonne label
    @Test
    public void testAbsolutEmptyDataFrame(){
        assertNull(voidDF.getAbsolute("colonne1"));
    }

    //Ce test vérifie que la fonction getProduct(L label) calcule le produit des entrées non null du dataFrame de la colonne label
    @Test
    public void testGetProductEmptyDataFrame(){
        assertNull(voidDF.getProduct("colonne1"));
    }


            /* ******************************************** */
            /* Test sur les méthodes sélection de DataFrame */
            /* ******************************************** */


        /*Tests sur sélection de lignes*/

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingRows(K index) retourne le meme dataframe
    //que celui copié si on copie tous les index
    @Test
    public void testSelectIndexOnDataFrame() throws Exception {
        List<String> indexList = List.of("ligne1", "ligne2", "ligne3");
        DataFrame<String, String, Object> copyFilled = filledDF.constructNewDataFrameWithSelectingRows(indexList);
        assertEquals(filledDF.toString(), copyFilled.toString());
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingRows(K index) retourne un dataFrame
    //s'il y a des indices correcte et d'autres non
    @Test
    public void testSelectWrongIndexOnDataFrame() throws Exception {
        List<String> indexList = List.of("ligne1", "ligneB");
        DataFrame<String, String, Object> df = filledDF.constructNewDataFrameWithSelectingRows(indexList);

        List<String> index2 = List.of("ligne1");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(1),
                List.of("I'm toto"),
                List.of('a'),
                List.of(true)
        );

        DataFrame<String, String, Object> copyofFilleDF = new DataFrame<>(index2, label2, values2);

        assertEquals(copyofFilleDF.toString(), df.toString());
    }


    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingRows(K index) retourne null
    //s'il n'y a pas d'indice correcte à copier
    @Test
    public void testSelectAllWrongIndexOnDataFrame() throws Exception {
        List<String> indexList = List.of("LigneA", "LigneB");
        DataFrame<String, String, Object> nullDF = filledDF.constructNewDataFrameWithSelectingRows(indexList);
        assertNull(nullDF);
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingRows(K index) retourne null
    //si le dataFrame où copier les index est vide
    @Test
    public void testSelectIndexOnEmptyDataFrame() throws Exception {
        List<String> indexList = List.of("LigneA", "LigneB");
        DataFrame<String, String, Object> nullDF = voidDF.constructNewDataFrameWithSelectingRows(indexList);
        assertNull(nullDF);
    }


        /*Tests sur les sélection de colonnes*/

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingColumns(L label) retourne le meme dataframe
    //que celui copié si on copie tous les index
    @Test
    public void testSelectLabelOnDataFrame() throws Exception {
        List<String> columnList = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        DataFrame<String, String, Object> copyFilled = filledDF.constructNewDataFrameWithSelectingColumns(columnList);
        assertEquals(filledDF.toString(), copyFilled.toString());
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingColumns(L label) retourne un dataFrame
    //s'il y a des indices correcte et d'autres non
    @Test
    public void testSelectWrongLabelOnDataFrame() throws Exception {
        List<String> columnList = List.of("colonne1", "colonneB");
        DataFrame<String, String, Object> df = filledDF.constructNewDataFrameWithSelectingColumns(columnList);

        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1");
        List<List<Integer>> values2 = List.of(
                List.of(1, 2, 3)
        );

        DataFrame<String, String, Integer> copyofFilleDF = new DataFrame<>(index2, label2, values2);

        assertEquals(copyofFilleDF.toString(), df.toString());
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingColumns(L label) retourne null
    //s'il n'y a pas d'indice correcte à copier
    @Test
    public void testSelectAllWrongLabelOnDataFrame() throws Exception {
        List<String> columnList = List.of("ColonneA", "ColonneB");
        DataFrame<String, String, Object> nullDF = filledDF.constructNewDataFrameWithSelectingColumns(columnList);
        assertNull(nullDF);
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingColumns(L label) retourne null
    //si le dataFrame où copier les labels est vide
    @Test
    public void testSelectLabelOnEmptyDataFrame() throws Exception {
        List<String> columnList = List.of("ColonneA", "ColonneB");
        DataFrame<String, String, Object> nullDF = voidDF.constructNewDataFrameWithSelectingColumns(columnList);
        assertNull(nullDF);
    }



        /*Tests sur la sélection avancée d'après un interval */


    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max)
    // retourne le même dataFrame si le dataFrame où sélectionner des valeurs dans l'intervalle [min;max] si toutes les
    // valeurs sont dans l'intervalle
    @Test
    public void testSelectIntervalLabelOnDataFrame() throws Exception {
        DataFrame<String, String, Object> copyFilled = filledDF.constructNewDataFrameWithSelectingValuesOfColumns("colonne1",0,5);
        assertEquals(filledDF.toString(), copyFilled.toString());
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max)
    // retourne seulement les lignes du dataFrame où kes valeurs du label sont dans l'intervalle [min;max]
    @Test
    public void testSelectIntervalWith1ResOnDataFrame() throws Exception {

        DataFrame<String, String, Object> df = filledDF.constructNewDataFrameWithSelectingValuesOfColumns("colonne1",1,1);

        List<String> index2 = List.of("ligne1");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(1),
                List.of("I'm toto"),
                List.of('a'),
                List.of(true)
        );

        DataFrame<String, String, Object> copyofFilleDF = new DataFrame<>(index2, label2, values2);

        assertEquals(copyofFilleDF.toString(), df.toString());
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max)
    // retourne null si le dataFrame où sélectionner des valeurs dans l'intervalle [min;max] ne contient pas de valeur
    // dans cet intervalle
    @Test
    public void testSelectIntervalWithNoResultOnDataFrame() throws Exception {
        DataFrame<String, String, Object> nullDF = filledDF.constructNewDataFrameWithSelectingValuesOfColumns("Colonne1",0,0);
        assertNull(nullDF);
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max)
    // retourne null si le label passé en paramètre ne correspond pas à un label du dataFrame
    @Test
    public void testSelectIntervalWithWrongLabelOnDataFrame() throws Exception {
        DataFrame<String, String, Object> nullDF = filledDF.constructNewDataFrameWithSelectingValuesOfColumns("ColonneA",0,0);
        assertNull(nullDF);
    }

    //Ce test vérifie que la fonction constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max)
    // retourne null si le dataFrame où sélectionner des valeurs dans l'intervalle [min;max] est vide
    @Test
    public void testSelectIntervalOnEmptyDataFrame() throws Exception {
        DataFrame<String, String, Object> nullDF = voidDF.constructNewDataFrameWithSelectingValuesOfColumns("ColonneA", 0,0);
        assertNull(nullDF);
    }


                /* *************************************** */
                /* Tests on display methods of a dataFrame */
                /* *************************************** */

        /* Tests on auxiliary methods */

    @Test
    public void testAlignWithShortString(){
        String value = "I'm toto";
        assertEquals(value, filledDF.align(value));
    }

    @Test
    public void testAlignWithLongString(){
        String value = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String expected = "a".repeat(47) + "...";
        assertEquals(expected, filledDF.align(value));
    }

        /* Tests on voidDF */

        /* We verify that all display methods on an empty dataFrame will return an empty string */

    @Test
    public void testToStringOnVoidDF() {
        assertEquals(voidDF.toString(), "");
    }

    @Test
    public void testToStringFirstXElementsOnVoidDF_negative() {
        assertEquals(voidDF.toStringFirstXElements(-1), "");
    }

    @Test
    public void testToStringFirstXElementsOnVoidDF_zero() {
        assertEquals(voidDF.toStringFirstXElements(0), "");
    }

    @Test
    public void testToStringFirstXElementsOnVoidDF_positive() {
        assertEquals(voidDF.toStringFirstXElements(1), "");
    }

    @Test
    public void testToStringLastXElementsOnVoidDF_negative() {
        assertEquals(voidDF.toStringLastXElements(-1), "");
    }

    @Test
    public void testToStringLastXElementsOnVoidDF_zero() {
        assertEquals(voidDF.toStringLastXElements(0), "");
    }

    @Test
    public void testToStringLastXElementsOnVoidDF_positive() {
        assertEquals(voidDF.toStringLastXElements(1), "");
    }

        /* Tests on filledDF */

    // Verify that the dataFrame is correctly displayed (with no value changed, missed, ...)
    @Test
    public void testToStringOnFilledDF(){
        String res = "X\t" + "colonne1\t" + "colonne2\t" + "colonne3\t" + "colonne4\t\n"
                + "ligne1\t" + "1\t" + "I'm toto\t" + "a\t" + "true\t\n"
                + "ligne2\t" + "2\t" + "I'm tata\t" + "b\t" + "false\t\n"
                + "ligne3\t" + "3\t" + "I'm titi\t" + "c\t" + "true\t\n";

        assertEquals(res.replaceAll("\\s", ""), filledDF.toString().replaceAll("\\s", ""));
    }

    // Verify that this method has the same output as toString() when entering exact number of rows in the dataFrame
    @Test
    public void testToStringFirstXElementsOnFilledDF_exactNbRows(){
        assertEquals(filledDF.toStringFirstXElements(3), filledDF.toString());
    }

    // Verify that this method has the same output as toString() when entering number of rows larger than the actual
    // number of rows in the dataFrame
    @Test
    public void testToStringFirstXElementsOnFilledDF_moreNbRows(){
        assertEquals(filledDF.toStringFirstXElements(4), filledDF.toString());
    }

    // Verify that this method display only the X first rows of the dataFrame
    @Test
    public void testToStringFirstXElementsOnFilledDF(){
        String res = "X\t" + "colonne1\t" + "colonne2\t" + "colonne3\t" + "colonne4\t\n"
                + "ligne1\t" + "1\t" + "I'm toto\t" + "a\t" + "true\t\n"
                + "ligne2\t" + "2\t" + "I'm tata\t" + "b\t" + "false\t\n";

        assertEquals(res.replaceAll("\\s", ""), filledDF.toStringFirstXElements(2).replaceAll("\\s", ""));
    }

    // Verify that this method has the same output as toString() when entering exact number of rows in the dataFrame
    @Test
    public void testToStringLastXElementsOnFilledDF_exactNbRows(){
        assertEquals(filledDF.toStringLastXElements(3), filledDF.toString());
    }

    // Verify that this method has the same output as toString() when entering number of rows larger than the actual
    // number of rows in the dataFrame
    @Test
    public void testToStringLastXElementsOnFilledDF_moreNbRows(){
        assertEquals(filledDF.toStringLastXElements(4), filledDF.toString());
    }

    // Verify that this method display only the X last rows of the dataFrame
    @Test
    public void testToStringLastXElementsOnFilledDF(){
        String res = "X\t" + "colonne1\t" + "colonne2\t" + "colonne3\t" + "colonne4\t\n"
                + "ligne2\t" + "2\t" + "I'm tata\t" + "b\t" + "false\t\n"
                + "ligne3\t" + "3\t" + "I'm titi\t" + "c\t" + "true\t\n";

        assertEquals(res.replaceAll("\\s", ""), filledDF.toStringLastXElements(2).replaceAll("\\s", ""));
    }
}