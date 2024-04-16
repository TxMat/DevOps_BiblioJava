package main.java;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;


public class DataFrame<K, L, V> {

    private Map<L, Map<K, V>> dataFrame = new LinkedHashMap<>();

    /**
     * Creates a dataFrame with initial indexes, labels and values
     *
     * @param index List of indexes representing rows of the dataFrame.
     * @param label List of labels representing columns of the dataFrame.
     * @param values List containing lists of Objects. Each list contains data of one column and then must have the same type. However, two lists can have different type.
     * @throws Exception If number of indexes does not match Si le nombre d'indices ne correspond pas au nombre de lignes de valeurs,
     *                   ou si le nombre de valeurs dans une ligne ne correspond pas au nombre de labels.
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

            /* ************ AFFICHAGE *******/

    /**
     * Returns a textual representation of the dataFrame in the form of a two-dimensional array where each row is
     * displayed with its index and each column with its label.
     *
     * @return Returns a string representation of the dataFrame.
     */
    //TODO: Aligner les valeurs avec les colonnes
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        // On ajoute les labels
        res.append("X\t");
        for (L label : dataFrame.keySet()) {
            res.append(label).append("\t");
        }
        res.append("\n");

        // Puis on ajoute les lignes
        for (K row : dataFrame.values().iterator().next().keySet()) {
            res.append(row).append("\t");   // ajout de l'index
            for (Map<K, V> columnValue : dataFrame.values()) {  // ajout des valeurs
                V value = columnValue.get(row);
                res.append(value).append("\t");
            }
            res.append("\n");
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
        List<String> index = List.of("ligne1", "ligne2");
        List<String> label = List.of("Colonne1", "Colonne2", "colonne3", "colonne4");

        /* Chaque sous-listes represente les valeurs d'une colonne */
        List<List<Object>> values = List.of(
                List.of(-1, 2),
                List.of(3.1, 4.2),
                List.of("Hello World", "Goodbye World"),
                List.of('a', 'b'));

        DataFrame<String, String, Object> df = new DataFrame<>(index, label, values);
        System.out.println(df.toString());

        System.out.println(df.getMin("colonne3"));
        System.out.println(df.getMax("colonne3"));
        System.out.println(df.getAverage("colonne3"));
        System.out.println(df.getCount("colonne3"));
        System.out.println(df.getSum("colonne3"));
        System.out.println(df.getAbsolute("colonne3"));
        System.out.println(df.getProduct("colonne3"));
        
    }

}
