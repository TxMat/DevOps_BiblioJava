package main.java;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DataFrame<K, L, V> {

    private Map<K, Map<L, V>> dataFrame = new LinkedHashMap<>();

    /**
     * Constructeur pour la création d'une nouvelle instance de DataFrame avec des listes d'objets.
     *
     * @param index Une liste d'indices pour les lignes du DataFrame. Chaque indice doit être unique.
     * @param label Une liste des labels pour les colonnes du DataFrame.
     * @param values Une liste de listes de valeurs, où chaque sous-liste correspond aux valeurs d'une ligne.
     * @throws Exception Si le nombre d'indices ne correspond pas au nombre de lignes de valeurs,
     *                   ou si le nombre de valeurs dans une ligne ne correspond pas au nombre de labels.
     */
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

    /**
     * Retourne une représentation textuelle du DataFrame, sous forme d'un tableau à deux dimensions où chaque ligne
     * est affichée avec son index et les valeurs des colonnes
     *
     * La première ligne affiche le nom des colonnes
     *
     * @return String représentant le DataFrame
     */
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
