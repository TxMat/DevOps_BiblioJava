import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CSVParser<IndexType, LabelType, ValType> {


    public Vector<Vector<?>> parseCSV(String csvFileName, final char delimiter) throws FileNotFoundException{
        Scanner csvScanner = new Scanner(new FileInputStream(csvFileName));

        Vector<IndexType> indexList = new Vector<>();
        Vector<LabelType> labelList = new Vector<>();
        Vector<Vector<Object>> valueList = new Vector<>();


        String columnName = csvScanner.nextLine();

        //Sometimes the first column name may be empty, especially in two-dimensional arrays, we'll just shave off the delimiter
        if (columnName.charAt(0) == delimiter){
            columnName = columnName.substring(1);
        }

        String[] columnNames = columnName.split(String.valueOf(delimiter));

        int columnCount = columnNames.length;

        for (String column : columnNames){
            if (column.isBlank()){
                throw new IllegalArgumentException("CSV File contains an empty column");
            }
            //TODO: Labels & Indexes are always String ?? In that case cast remove the cast
            labelList.add((LabelType) column);
        }

        //Add the lists containing the values. There should be as many lists as the number of columns
        for (int i = 0; i < columnCount; i++) {
            valueList.add(new Vector<Object>());
        }

        Vector<ValueType> valuesType = new Vector<ValueType>();

        //At this point we got the columns and they're valid
        //FIXME: This implies two-dimensional arrays, fix it for one-dimensional or make another function
        String line;
        String[] lines;
        String[] values;
        String[] indexes;
        boolean firstIterationFlag = true;

        do{
            line = csvScanner.nextLine();
            lines = line.split(String.valueOf(delimiter));
            values = Arrays.copyOfRange(lines, lines.length-columnCount , lines.length);
            indexes = Arrays.copyOfRange(lines, 0, (lines.length-columnCount));


            //Check if the dimensions are correct:

            //Throws if the dimension of values and labels are different
            if (values.length != columnCount){
                throw new IllegalArgumentException("Inconsistent number of columns, expected " + columnCount + " got " + values.length);
            }

            //FIXME: This may be dead code, inconsistent length of indexes implies different values length, thus throwing before reaching this
            //TODO: Verify with tests
            if (indexes.length == 0){
                throw new IllegalArgumentException("Inconsistent number of columns, expected " + values.length + " got " + indexes.length);
            }


            //Check if the types are correct :
            for (int i = 0; i < values.length; i++) {
                if (firstIterationFlag){
                    valuesType.add(findType(values[i]));
                }else{
                    // Else we check the type of every value
                    if (! checkType(valuesType.get(i), values[i])){
                        throw new IllegalArgumentException("The value " + values[i] + " has unexpected type, expected type was " + valuesType.get(i));
                    }
                }
            }


            //Append the indexes
            for (String index : indexes){
                indexList.add((IndexType) index);
            }

            for (int i = 0; i < values.length; i++) {
                switch (valuesType.get(i)){
                    case STRING:
                        valueList.get(i).add((String) values[i]);
                        break;
                    case INTEGER:
                        valueList.get(i).add(Integer.parseInt(values[i]));
                        break;
                    case DOUBLE:
                        valueList.get(i).add(Double.parseDouble(values[i]));
                        break;
                    case BOOLEAN:
                        valueList.get(i).add(Boolean.valueOf(values[i]));
                        break;
                    default:
                        System.err.println("Forgot to add cases for the type " + valuesType.get(i));
                        System.err.println("Defaulting to String");
                        valueList.get(i).add((String) values[i]);
                        break;
                }
            }

            firstIterationFlag = false;

        }while(csvScanner.hasNextLine());

        Vector<Vector<?>> resultVector = new Vector<>();
        resultVector.add(indexList);
        resultVector.add(labelList);
        resultVector.add(valueList);

        return resultVector;
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

        //Built-in boolean parser is different, returns false if value is different than "true", ignoring case
        //So we make our own parser
        if ((value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("false"))){
            return ValueType.BOOLEAN;
        }

        //We default to String if other types are not supported
        return ValueType.STRING;
    }


    //FIXME: If the value is a String that is either "true" or "false", it will false-negative
    //Potential fix : Be permissive and return ((valuesType == ValueType.BOOLEAN) || (valuesType == valueType.STRING))
    private static boolean checkType(ValueType valuesType, String value){

        try{
            Integer.parseInt(value);
            return valuesType == ValueType.INTEGER;
        }catch (NumberFormatException ignored){}

        try{
            Double.parseDouble(value);
            return valuesType == ValueType.DOUBLE;
        }catch (NumberFormatException ignored){}


        if ((value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("false"))){
            return valuesType == ValueType.BOOLEAN;
        }

        return valuesType == ValueType.STRING;
    }
}
