package main.java;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;


public class DataFrame<K, L, V> {

    private static final int MAX_CHARACTERS = 50;

    private Map<L, Map<K, V>> dataFrame = new LinkedHashMap<>();

    /**
     * Creates a dataFrame with initial indexes, labels and values. If those lists are empty, the dataFrame will remain
     * empty.
     *
     * @param index List of indexes representing rows of the dataFrame.
     * @param label List of labels representing columns of the dataFrame.
     * @param values List containing lists of Objects. Each sublist contains data of one column and then must have the
     *               same type. However, two lists can have different type.
     * @throws Exception If the size of indexes list does not match the size of sublists in values list or if the
     * size of labels list does not match the size of values list, the method will throw IndexOutOfBoundsException().
     */
    public DataFrame(List<K> index, List<L> label, List<List<V>> values) throws Exception {

        if(label.size() != values.size()){
            throw new IndexOutOfBoundsException("Label size is not equal to values size\n");
        }

        for (int i = 0; i < label.size(); i++) {
            List<V> columnValues = values.get(i);

            if(columnValues.size() != index.size()){
                throw new IndexOutOfBoundsException("Column size is not equal to values size\n");
            }

            Class<?> columnClass = columnValues.get(0).getClass();
            Map<K, V> currentColumn = new LinkedHashMap<>();

            for (int j = 0; j < index.size(); j++) {
                if (!columnValues.get(j).getClass().equals(columnClass)){
                    throw new IllegalArgumentException("Column values must have the same type");
                }

                if(currentColumn.containsKey(index.get(j))){
                    currentColumn.replace(index.get(j), columnValues.get(j));
                } else {
                    currentColumn.put(index.get(j), columnValues.get(j));
                }
            }

            dataFrame.put(label.get(i), currentColumn);
        }
    }


    public DataFrame(String csvFilename, char delimiter) throws FileNotFoundException {
        CSVParser<K, L> csvParser = new CSVParser<>();
        Vector<Vector<?>> resultList = csvParser.parseCSV(csvFilename, delimiter);

        List<K> index = (List<K>) resultList.get(0);
        List<L> label = (List<L>) resultList.get(1);
        List<List<V>> values = (List<List<V>>) resultList.get(2);

        if(label.size() != values.size()){
            throw new IndexOutOfBoundsException("Label size is not equal to values size\n");
        }

        for (int i = 0; i < label.size(); i++) {
            List<V> columnValues = values.get(i);

            if(columnValues.size() != index.size()){
                throw new IndexOutOfBoundsException("Column size is not equal to values size\n");
            }

            Class<?> columnClass = columnValues.get(0).getClass();
            Map<K, V> currentColumn = new LinkedHashMap<>();

            for (int j = 0; j < index.size(); j++) {
                if (!columnValues.get(j).getClass().equals(columnClass)){
                    throw new IllegalArgumentException("Column values must have the same type");
                }

                if(currentColumn.containsKey(index.get(j))){
                    currentColumn.replace(index.get(j), columnValues.get(j));
                } else {
                    currentColumn.put(index.get(j), columnValues.get(j));
                }
            }
            dataFrame.put(label.get(i), currentColumn);
        }
    }

  
            /* ************ AFFICHAGE *******/

    /**
     * Returns the string entered as parameter. It will be the string itself if its length is less than or equal to
     * 50 characters, else the string will be truncated so that the last 3 characters are 3 ellipsis.
     *
     * @param valueString A value from the dataFrame in the form of a string value.
     * @return Returns the string entered as parameter
     */
    public String align(String valueString){
        //String valueString = value.toString();
        if (valueString.length() > MAX_CHARACTERS){
            return valueString.substring(0, MAX_CHARACTERS - 3) + "...";
        } else {
            return valueString;
        }
    }

    /**
     * Returns a Map containing the maximum number of characters per column.
     *
     * @return Returns a Map containing the maximum number of characters per column
     */
    public Map<L, Integer> getMaxLengthPerColumn(){
        Map<L, Integer> mapWidth = new LinkedHashMap<>();
        for (L label : dataFrame.keySet()) {
            int max = label.toString().length();
            for (V value : dataFrame.get(label).values()) {
                String valueString = value.toString();
                max = Math.max(max, align(valueString).length());
            }
            mapWidth.put(label, max);
        }

        return mapWidth;
    }

    /**
     * This method returns the maximum length of all indexes starting from specified index.
     *
     * @param nbRows The number of indexes to compare
     * @param startIndex The first index of indexes to compare
     * @return Returns the maximum length of all indexes starting from specified index
     */
    public int getMaxWidthIndex(int nbRows, int startIndex){
        int maxLengthIndex = 0;
        int countRows = 0;
        int currentIndex = 0;

        Map<K, V> column1 = dataFrame.entrySet().iterator().next().getValue();
        for (K key : column1.keySet()) {
            if (startIndex <= currentIndex){
                if (countRows >= nbRows){
                    break;
                }

                int keyLength = key.toString().length();
                if (keyLength > maxLengthIndex){
                    maxLengthIndex = keyLength;
                }
                countRows++;
            }

            currentIndex++;
        }

        return Math.min(maxLengthIndex, MAX_CHARACTERS);
    }

    /**
     * Returns a well-aligned string representation of the dataFrame in the form of a two-dimensional array where each row is
     * displayed with its index and each column with its label. The method will return an empty string if the
     * dataFrame is empty.
     *
     * @return Returns a string representation of the dataFrame.
     */
    @Override
    public String toString() {

        if (dataFrame.isEmpty()){
            return "";
        }

        StringBuilder res = new StringBuilder();
        Map<L, Integer> mapLength = getMaxLengthPerColumn();
        int nbTotalRows = dataFrame.values().iterator().next().keySet().size();
        int maxLengthIndex = getMaxWidthIndex(nbTotalRows, 0);

        // On ajoute les labels
        res.append(String.format("%-" + maxLengthIndex + "s", "X\t"));
        for (L label : dataFrame.keySet()) {
            res.append(String.format("%" + mapLength.get(label) + "s", align(label.toString()))).append("\t");
        }
        res.append("\n");

        // Puis on ajoute les lignes
        for (K row : dataFrame.values().iterator().next().keySet()) {
            res.append(String.format("%-" + maxLengthIndex + "s", align(row.toString()))).append("\t");     // ajout de l'index
            for (L label : dataFrame.keySet()) {    // ajout des valeurs
                V value = dataFrame.get(label).get(row);
                res.append(String.format("%" + mapLength.get(label) + "s", align(value.toString()))).append("\t");
            }
            res.append("\n");
        }

        return res.toString();
    }

    /**
     * Returns a well-aligned string representation of the nbLinesToWrite first rows of the dataFrame. If the number of
     * rows to display is zero or lower or if the dataFrame is empty, the method will return an empty string. If it's
     * larger than the actual number of rows in the dataFrame, it will display all the dataFrame.
     *
     * @param nbLinesToWrite The number of rows to display
     * @return Returns a string representation of the nbLinesToWrite first rows of the dataFrame.
     */
    public String toStringFirstXElements(int nbLinesToWrite){

        // rien a ecrire
        if (nbLinesToWrite <= 0 || dataFrame.isEmpty()){
            return "";
        }

        StringBuilder res = new StringBuilder();
        Map<L, Integer> mapLength = getMaxLengthPerColumn();
        int maxLengthIndex = getMaxWidthIndex(nbLinesToWrite, 0);

        // On ajoute les labels
        res.append(String.format("%-" + maxLengthIndex + "s", "X\t"));
        for (L label : dataFrame.keySet()) {
            res.append(String.format("%" + mapLength.get(label) + "s", align(label.toString()))).append("\t");
        }
        res.append("\n");

        // Puis on ajoute les nbLinesToWrite lignes
        int countRows = 0;
        for (K row : dataFrame.values().iterator().next().keySet()) {
            if (countRows >= nbLinesToWrite){
                break;
            }

            res.append(String.format("%-" + maxLengthIndex + "s", align(row.toString()))).append("\t");     // ajout de l'index
            for (L label : dataFrame.keySet()) {    // ajout des valeurs
                V value = dataFrame.get(label).get(row);
                res.append(String.format("%" + mapLength.get(label) + "s", align(value.toString()))).append("\t");
            }
            res.append("\n");
            countRows++;
        }

        return res.toString();
    }

    /**
     * Returns a well-aligned string representation of the nbLinesToWrite last rows of the dataFrame. If the number of
     * rows to display is zero or lower or if the dataFrame is empty, the method will return an empty string. If it's
     * larger than the actual number of rows in the dataFrame, it will display all the dataFrame.
     *
     * @param nbLinesToWrite The number of rows to display
     * @return Returns a string representation of the nbLinesToWrite last rows of the dataFrame.
     */
    public String toStringLastXElements(int nbLinesToWrite){

        // rien a ecrire
        if (nbLinesToWrite <= 0 || dataFrame.isEmpty()){
            return "";
        }

        StringBuilder res = new StringBuilder();
        Map<L, Integer> mapLength = getMaxLengthPerColumn();
        int nbTotalRows = dataFrame.values().iterator().next().keySet().size();
        int startIndex = nbTotalRows - nbLinesToWrite;
        int maxLengthIndex = getMaxWidthIndex(nbLinesToWrite, startIndex);

        // On ajoute les labels
        res.append(String.format("%-" + maxLengthIndex + "s", "X\t"));
        for (L label : dataFrame.keySet()) {
            res.append(String.format("%" + mapLength.get(label) + "s", align(label.toString()))).append("\t");
        }
        res.append("\n");

        int currentIndex = 0;
        for (K row : dataFrame.values().iterator().next().keySet()) {
            if (currentIndex >= startIndex){
                res.append(String.format("%-" + maxLengthIndex + "s", align(row.toString()))).append("\t");   // ajout de l'index
                for (L label : dataFrame.keySet()) {    // ajout des valeurs
                    V value = dataFrame.get(label).get(row);
                    res.append(String.format("%" + mapLength.get(label) + "s", align(value.toString()))).append("\t");
                }
                res.append("\n");
            }
            currentIndex++;
        }

        return res.toString();
    }


            /* ************ STATISTIQUES *******/


    /**
     * Returns the smallest element of the specified column.
     *
     * @param label The label of the column where to search for the minimum.
     * @return The smallest element in the column, null if no element is comparable or if the column is empty.
     */
    public Number getMin(L label){
        Number min = null;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return null;
        }

        for(V value : dataFrame.get(label).values()){
            if(value instanceof Number){
                if(min == null){
                    min = (Number) value;
                } else if(((Comparable<Number>) value).compareTo(min) < 0){
                    min = (Number) value;
                }
            }
        }
        return min;
    }

    /**
     * Returns the largest element of the specified column.
     *
     * @param label The label of the column where to search for the maximum.
     * @return The largest element in the column, null if no element is comparable or if the column is empty.
     */
    public Number getMax(L label){
        Number max = null;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return null;
        }

        for(V value : dataFrame.get(label).values()){
            if(value instanceof Number)  {
                if(max == null){
                    max = (Number) value;
                } else if(((Comparable<Number>) value).compareTo(max) > 0){
                    max = (Number) value;
                }
            }
        }
        return max;
    }

    /**
     * Returns the average of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the average.
     * @return The average of the non-null elements in the column or 0 if the column is empty or null if the label isn't
     *         correct.
     */
    public Number getAverage(L label){

        if(dataFrame.get(label) == null ) {
            return null;
        } else if(dataFrame.get(label).isEmpty()) {
            return 0;

        }

        Number average = null;
        int i =0;

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    if(average == null){
                        average = 0;
                    }
                    average = average.doubleValue() + ((Number) value).doubleValue();
                    i++;
                }
            }
        }


        if(average == null){
            return null;
        } else if(average.doubleValue() == 0){ //éviter la div par 0
            return average;
        }

        return average.doubleValue() / i;
    }

    /**
     * Returns the number of elements of the specified column.
     *
     * @param label The label of the column where to count the elements.
     * @return The number of non-null elements in the column or 0 if the column is empty
     *         or -1 if the column doesn't exist.
     */
    public int getCount(L label){

        if(dataFrame.get(label) == null) {
            return -1;
        } else if (dataFrame.get(label).isEmpty()) {
            return 0;
        }

        int count = 0;

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                count ++;
            }
        }

        return count;
    }


    /**
     * Returns the sum of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the sum of the elements.
     * @return The sum of the non-null elements in the column or 0 if the column is empty or 0 if the column is empty or null if the label isn't
     *         correct.
     */
    public Number getSum(L label){

        if(dataFrame.get(label) == null ) {
            return null;
        } else if(dataFrame.get(label).isEmpty()) {
            return 0;
        }

        Number count = null;

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    if(count == null){
                        count = 0;
                    }
                    count = count.doubleValue() + ((Number) value).doubleValue();
                }
            }
        }
        return count;
    }

    /**
     * Returns the absolute sum of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the absolute sum of the elements.
     * @return The absolute sum of the non-null elements in the column or 0 if the column is empty or 0 if the column is empty or null if the label isn't
     *         correct.
     */
    public Number getAbsolute(L label){

        if(dataFrame.get(label) == null ) {
            return null;
        } else if(dataFrame.get(label).isEmpty()) {
            return 0;
        }

        Number count = null;

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    if(count == null){
                        count = 0;
                    }
                    count = count.doubleValue() + abs(((Number) value).doubleValue());
                }
            }
        }
        return count;
    }

    /**
     * Returns the product of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the product of the elements.
     * @return The product of the non-null elements in the column or 0 if the column is empty or 0 if the column is empty or null if the label isn't
     *         correct.
     */
    public Number getProduct(L label){

        if(dataFrame.get(label) == null ) {
            return null;
        } else if(dataFrame.get(label).isEmpty()) {
            return 0;
        }

        Number count = null;

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    if(count == null){
                        count = 1;
                    }
                    count = count.doubleValue() * ((Number) value).doubleValue();
                }
            }
        }
        return count;
    }

            /* *************** SELECTION ******/

    /**
     * Create a new DataFrame from the current object DataFrame by selecting lines with the parameter indexList.
     *
     * @param indexList A list of index to add to the new DataFrame.
     * @return A new DataFrame with index of indexList which are in the current object DataFrame.
     *         Return null if the current Object DataFrame is empty or if the list of parameter is empty
     *         or if none of the index of the indexList are in the current Object DataFrame.
     * @throws Exception If number of indexes does not match
     */
    public DataFrame<K, L, V> constructNewDataFrameWithSelectingRows(List<K> indexList) throws Exception {

        if(dataFrame.isEmpty() || indexList.isEmpty()){
            return null;
        }

        List<K> newListIndex = new ArrayList<>();
        List<L> newListLabel = new ArrayList<>();
        List<List<V>> newListValues = new ArrayList<>();

        //parcours de tout le dataframe
        for (Map.Entry<L, Map<K, V>> column : dataFrame.entrySet()) {

            List<V> value = new ArrayList<>();

            newListLabel.add(column.getKey());

            for(K index : indexList){
                if(column.getValue().containsKey(index)){
                    value.add(column.getValue().get(index));
                    if(!newListIndex.contains(index)){
                        newListIndex.add(index);
                    }
                }
            }
            newListValues.add(value);
        }

        if(newListIndex.isEmpty()){
            return null;
        }

        return new DataFrame<>(newListIndex, newListLabel, newListValues);
    }


    /**
     * Create a new DataFrame from the current object DataFrame by selecting lines with the parameter indexList.
     *
     * @param labelList A list of labels to add to the new DataFrame.
     * @return A new DataFrame with label of labelList which are in the current object DataFrame.
     *         Return null if the current Object DataFrame is empty or if the list in parameter is empty
     *         or if none of the labels of the labelList are in the current Object DataFrame.
     * @throws Exception If number of indexes does not match
     */
    public DataFrame<K, L, V> constructNewDataFrameWithSelectingColumns(List<L> labelList) throws Exception {
        if(dataFrame.isEmpty() || labelList.isEmpty()){
            return null;
        }

        List<K> newListIndex = new ArrayList<>();
        List<L> newListLabel = new ArrayList<>();
        List<List<V>> newListValues = new ArrayList<>();

        //pour remplir les labels une seule fois
        boolean isLabel = false;

        //parcours de tout le dataframe
        for (Map.Entry<L, Map<K, V>> column : dataFrame.entrySet()) {

            if(labelList.contains(column.getKey())){
                newListLabel.add(column.getKey());
                List<V> values = new ArrayList<>();

                //parcours de toutes les valeurs de la ligne
                for(Map.Entry<K,V> label : column.getValue().entrySet()){
                    if(!isLabel){ //si labels non remplis
                        newListIndex.add(label.getKey());
                    }
                    values.add(label.getValue());
                }

                newListValues.add(values);
                isLabel = true;
            }
        }

        if(newListIndex.isEmpty()){
            return null;
        }

        return new DataFrame<>(newListIndex, newListLabel, newListValues);
    }

    /**
     * Create a new DataFrame from the current object DataFrame by selecting lines which the values of the column label
     * are in the interval.
     *
     * @param label A label where the selection is applied.
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @return A new DataFrame with selecting lines where column are in the interval of the current object DataFrame.
     *         Return null if the current Object DataFrame is empty or if the list in parameter is empty
     *         or if none of the value of the label are in the interval of the current Object DataFrame.
     * @throws Exception If number of indexes does not match
     */
    public DataFrame<K, L, V> constructNewDataFrameWithSelectingValuesOfColumns(L label, double min, double max) throws Exception {
        if(dataFrame.isEmpty() || dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return null;
        }

        List<K> newListIndex = new ArrayList<>();

        //parcours de tout le dataframe
        for (Map.Entry<L, Map<K, V>> column : dataFrame.entrySet()) {

            //parcours de toutes les valeurs de la ligne
            for(Map.Entry<K,V> currentLabel : column.getValue().entrySet()){
                if(column.getKey().equals(label) && (currentLabel.getValue() instanceof Number)){
                    double value = ((Number) currentLabel.getValue()).doubleValue();
                    if(min <= value && value <= max){
                        newListIndex.add(currentLabel.getKey());
                    }
                }
            }
        }

        if(newListIndex.isEmpty()){
            return null;
        } else {
            return constructNewDataFrameWithSelectingRows(newListIndex);
        }
    }



            /* ************ MAIN *******/

    public static void main(String[] args) throws Exception {
        List<String> index = List.of("ligne1", "ligne2", "ligneaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa3");
        List<String> label = List.of("colonne1", "colonne2", "colonne3", "colonne4");

        // Chaque sous-listes represente les valeurs d'une colonne
        List<String> index2 = List.of("ligne1", "ligne2", "ligne3");
        List<String> label2 = List.of("colonne1", "colonne2", "colonne3", "colonne4");
        List<List<Object>> values2 = List.of(
                List.of(1, 2, 3),
                List.of("I'm toto", "I'm tata", "I'm titi"),
                List.of('a', 'b', 'c'),
                List.of(true, false, true)
        );

        DataFrame<String, String, Object> df = new DataFrame<>(index2, label2, values2);
        System.out.println(df.toString() + "\n");
        System.out.println(df.toStringFirstXElements(2));
        System.out.println(df.toStringLastXElements(2));

/*        System.out.println(df.getMin("colonne3"));
        System.out.println(df.getMax("colonne3"));
        System.out.println(df.getAverage("colonne3"));
        System.out.println(df.getCount("colonne3"));
        System.out.println(df.getSum("colonne3"));
        System.out.println(df.getAbsolute("colonne3"));
        System.out.println(df.getProduct("colonne3"));*/

    }

}
