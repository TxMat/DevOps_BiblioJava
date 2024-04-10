package main.java;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataFrame<K, L, V> {

    private Map<K, Map<L, V>> dataFrame = new LinkedHashMap<>();

    public DataFrame(List<K> index, List<L> label, List<List<V>> values) throws Exception {

        if(index.size() != values.size()){
            throw new IndexOutOfBoundsException();
        }

        for (int i = 0; i < index.size(); i++) {
            List<V> value = values.get(i);

            Map<L, V> currentColumn = new LinkedHashMap<>();
            for (int j = 0; j < label.size(); j++) {
                if(currentColumn.containsKey(label.get(j))){
                    currentColumn.replace(label.get(j), value.get(j));
                }else {
                    currentColumn.put(label.get(j), value.get(j));
                }
            }

            if(value.size() != label.size()){
                throw new IndexOutOfBoundsException();
            }

            dataFrame.put(index.get(i), currentColumn);
        }
    }

    /************* AFFICHAGE *******/


    //affiche le dataFrame en entier
    public String toString(){
        StringBuilder res = new StringBuilder();

        boolean first = false;

        for (Map.Entry<K, Map<L, V>> index : dataFrame.entrySet()) {

            Map<L, V> column = index.getValue();

            //Pour la première ligne uniquement on affiche le nom des colonnes
            if(!first){
                res.append("X \t");
                for(L label : column.keySet()){
                    res.append(label).append("\t");
                }
                res.append("\n");
                first = true;
            }

            res.append(index.getKey()).append("\t");

            for(V value : column.values()){
                res.append(value).append("\t");
            }
            res.append("\n");
        }
        return res.toString();
    }

    /************* MAIN *******/

    public static void main(String[] args) throws Exception {
        List<String> index = List.of("ligne1", "ligne2", "ligne3", "ligne4", "ligne5");
        List<String> label = List.of("Colonne1", "Colonne2");
        List<List<Integer>> values = List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6), List.of(7, 8), List.of(9, 10));

        DataFrame<String, String, Integer> df = new DataFrame<>(index, label, values);

    }

}
