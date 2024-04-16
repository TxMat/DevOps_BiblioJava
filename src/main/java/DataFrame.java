import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DataFrame<K, L, V> {

    private Map<L, Map<K, V>> dataFrame = new LinkedHashMap<>();


    /* DO NOT COMMIT THIS CONSTRUCTOR
    *  This was taken from an advanced branch
    */

    public DataFrame(List<K> index, List<L> label, List<List<V>> values) {

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
            CSVParser<K, L, V> csvParser = new CSVParser<>();
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


    public static void main(String[] args) throws Exception {
        List<String> index = List.of("ligne1", "ligne2");
        List<String> label = List.of("Colonne1", "Colonne2", "colonne3", "colonne4");

        /* Chaque sous-listes represente les valeurs d'une colonne */
        List<List<Object>> values = List.of(
                List.of(-1, 2),
                List.of(3.1, 4.2),
                List.of("Hello World", "Goodbye World"),
                List.of('a', 'b'));

        System.out.println("Working Directory = " + System.getProperty("user.dir"));


        DataFrame<String, String, Object> dataframeList = new DataFrame<>(index, label, values);

        DataFrame<String, String, Object> dataframeCSV = new DataFrame<>("src/main/resources/Classeur1.csv", ';');
    }

}
