import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVParser<IndexType, LabelType, ValType> {

    @Deprecated
    public void parseCSV(Map<IndexType, Map<LabelType, ValType>> hashMap, String csvFileName, final char delimiter) throws FileNotFoundException, ClassCastException{
        Scanner csvScanner = new Scanner(new FileInputStream(csvFileName));

        Vector<IndexType> indexList = new Vector<>();
        Vector<LabelType> labelList = new Vector<>();
        Vector<Object> valueList = new Vector<>();


        String columnName = csvScanner.nextLine();

        //Sometimes the first column name may be empty, especially in two-dimensional arrays, we'll just shave of the delimiter
        if (columnName.charAt(0) == delimiter){
            columnName = columnName.substring(1);
        }

        String[] columnNames = columnName.split(String.valueOf(delimiter));
        int columnCount = columnNames.length;

        for (String column : columnNames){
            if (column.isBlank()){
                throw new IllegalArgumentException("CSV File contains an empty column");
            }
            labelList.add((LabelType) column);
        }

        Vector<ValueType> valuesType = new Vector<ValueType>();

        //At this point we got the columns and they're valid
        //FIXME: This implies two-dimensional arrays, fix it for one-dimensional or make another function
        String line;
        String indexName;
        String[] lines;
        String[] values;
        boolean firstIterationFlag = true;

        do{
            Map<LabelType, Object> labelToValueHashMap = new LinkedHashMap<>();
            line = csvScanner.nextLine();
            lines = line.split(String.valueOf(delimiter));
            values = Arrays.copyOfRange(lines, lines.length-columnCount , lines.length);

            //-1 to account for the index name
            if ((lines.length-1) != columnCount){
                throw new IllegalArgumentException("Inconsistent number of columns, expected " + columnCount + " got " + (lines.length-1));
            }

            for (int i = 0; i < lines.length; i++) {
                //First we get the index name, this is NOT a value
                if (i == 0){
                    indexName = lines[0];
                    indexList.add((IndexType) indexName);
                    continue; //Nothing else to do
                }


                // Then we get to the values
                // If this is the first iteration (of the while), we have to determine the values type
                // The values in the next lines have to be the same type as the value above them
                if (firstIterationFlag){
                    valuesType.add(findType(lines[i]));
                }else{
                    // Else we check the type of every value, -1 to account for the index names
                    if (! checkType(valuesType.get(i-1), values[i-1])){
                        throw new IllegalArgumentException("The value " + lines[i] + "has unexpected type, expected type was" + valuesType.get(i-1));
                    }
                }

                //labelToValueHashMap.put((LabelType) lines[i], values[i]);
                //TODO: Add (columnName[i(+1 ??)], value) to the Map<Label, Value>
            }

            firstIterationFlag = false;

        }while(csvScanner.hasNextLine());

    }

    @Deprecated
    public Map<IndexType, Map<LabelType, ValType>> parseCSV_NoDynamicType(Map<IndexType, Map<LabelType, ValType>> hashMap, String csvFileName, final char delimiter) throws FileNotFoundException, ClassCastException{
        Scanner csvScanner = new Scanner(new FileInputStream(csvFileName));

        Vector<IndexType> indexList = new Vector<>();
        Vector<LabelType> labelList = new Vector<>();
        Vector<Vector<ValType>> valueList = new Vector<>();

        String columnName = csvScanner.nextLine();

        //Sometimes the first column name may be empty, especially in two-dimensional arrays, we'll just shave of the delimiter
        if (columnName.charAt(0) == delimiter){
            columnName = columnName.substring(1);
        }

        String[] columnNames = columnName.split(String.valueOf(delimiter));
        int columnCount = columnNames.length;

        for (String column : columnNames){
            if (column.isBlank()){
                throw new IllegalArgumentException("CSV File contains an empty column");
            }
            valueList.add(new Vector<ValType>());
            labelList.add((LabelType) column);
        }

        String line;
        String[] lines;
        ValType[] values;


        do{
            line = csvScanner.nextLine();
            int indexCount = 0, valueCount = 0;
            lines = line.split(String.valueOf(delimiter));
            values = (ValType[]) new Object[columnCount];


            //Get the values of the current line
            for (int valueIndex = lines.length - columnCount, idx = 0; valueIndex <= columnCount; valueIndex++, idx++) {
                values[idx] = (ValType) lines[valueIndex];
            }


            for (int idx = 0; idx < lines.length; idx++) {
                if (idx < lines.length-columnCount){
                    //We are in the index;
                    indexCount++;
                    indexList.add((IndexType) lines[idx]);
                }else{
                    //We are in the values
                    valueCount++;
                    valueList.get(idx-indexCount).add((ValType) lines[idx]);
                }
            }

            if (valueCount != columnCount){
                throw new IllegalArgumentException("Inconsistent number of columns, expected " + columnCount + " got " + (lines.length-columnCount));
            }

        }while(csvScanner.hasNextLine());

        for (int i = 0; i < indexList.size(); i++) {
            Vector<ValType> value = valueList.get(i);

            Map<LabelType, ValType> currentColumn = new LinkedHashMap<>();
            for (int j = 0; j < labelList.size(); j++) {
                currentColumn.put(labelList.get(j), value.get(j));
            }
            hashMap.put(indexList.get(i), currentColumn);
        }
        System.out.println("Success???");
        return hashMap;
    }


    /*
     *                  Index1   Index2      *                                      * Label1 : 123456
     * Label1, Label2 : 123456 | String      * Label1, Label2 : 123456 | Coffee     * Label2 : 678910
     * Label3, Label4 : 678910 | Coffee      *                                      * Label3 : 111213
     */


    //FIXME: Dirty? Maybe find another way?
    private static ValueType findType(String value){

        try{
            Integer.parseInt(value);
            return ValueType.INTEGER;
        }catch (NumberFormatException ignored){};

        try{
            Double.parseDouble(value);
            return ValueType.DOUBLE;
        }catch (NumberFormatException ignored){};

        //We default to String if other types are not supported
        return ValueType.STRING;
    }


    private static boolean checkType(ValueType valuesType, String value){

        try{
            Integer.parseInt(value);
            return valuesType == ValueType.INTEGER;
        }catch (NumberFormatException ignored){}

        try{
            Double.parseDouble(value);
            return valuesType == ValueType.DOUBLE;
        }catch (NumberFormatException ignored){}

        return valuesType == ValueType.STRING;
    }
}
