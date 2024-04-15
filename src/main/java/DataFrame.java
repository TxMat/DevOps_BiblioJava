package main.java;

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

            /************* AFFICHAGE *******/

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


            /************* STATISTIQUES *******/


    /**
     * Returns the smallest element of the specified column.
     *
     * @param label The label of the column where to search for the minimum.
     * @return The smallest element in the column, null if no element is comparable or if the column is empty.
     */
    public V getMin(L label){
        V min = null;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return null;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(min == null){
                    min = value;
                } else if(((Comparable<V>) value).compareTo(min) < 0){
                    min = value;
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
    public V getMax(L label){
        V max = null;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return null;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(max == null){
                    max = value;
                } else if(((Comparable<V>) value).compareTo(max) > 0){
                    max = value;
                }
            }
        }
        return max;
    }

    /**
     * Returns the average of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the average.
     * @return The average of the non-null elements in the column or 0 if the column is empty.
     */
    public double getAverage(L label){
        double average = 0;
        int i =0;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return 0;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    average += ((Number) value).doubleValue();
                    i++;
                }
            }
        }

        //éviter la div par 0
        if(average == 0){
            return average;
        }

        return average / i;
    }

    /**
     * Returns the number of elements of the specified column.
     *
     * @param label The label of the column where to count the elements.
     * @return The number of non-null elements in the column or 0 if the column is empty.
     */
    public int getCount(L label){
        int count = 0;


        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return 0;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    count ++;
                }
            }
        }
        return count;
    }


    /**
     * Returns the sum of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the sum of the elements.
     * @return The sum of the non-null elements in the column or 0 if the column is empty.
     */
    public double getSum(L label){
        double count = 0;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return 0;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    count += ((Number) value).doubleValue();
                }
            }
        }
        return count;
    }

    /**
     * Returns the absolute sum of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the absolute sum of the elements.
     * @return The absolute sum of the non-null elements in the column or 0 if the column is empty.
     */
    public double getAbsolute(L label){
        double count = 0;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return 0;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(value instanceof Number){
                    count += abs(((Number) value).doubleValue());
                }
            }
        }
        return count;
    }

    /**
     * Returns the product of the elements of the specified column.
     *
     * @param label The label of the column where to calculate the product of the elements.
     * @return The product of the non-null elements in the column or 0 if the column is empty.
     */
    public double getProduct(L label){
        double count = 0;

        if(dataFrame.get(label) == null || dataFrame.get(label).isEmpty()){
            return 0;
        }

        for(V value : dataFrame.get(label).values()){
            if(value != null){
                if(count == 0){
                    count = 1;
                }
                if(value instanceof Number){
                    count = count * ((Number) value).doubleValue();
                }
            }
        }
        return count;
    }



            /************* MAIN *******/

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

        System.out.println(df.getMin("Colonne1"));
        System.out.println(df.getMax("Colonne1"));
        System.out.println(df.getAverage("Colonne1"));
        System.out.println(df.getCount("Colonne1"));
        System.out.println(df.getSum("Colonne1"));
        System.out.println(df.getAbsolute("Colonne1"));
        System.out.println(df.getProduct("Colonne1"));
        
    }

}
