import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

public class CSVParser<IndexType, LabelType> {


    /**
     * Parse a CSV File, checking for dimensions. <p>
     * Some CSV uses quotes to wrap strings, these are currently not supported. <p>
     * Not all types are supported, content will be defaulted to string if no alternative.
     * @param csvFileName The CSV filename.
     * @param delimiter The delimiter of the CSV.
     * @return A vector containing 3 vectors, respectively list of index, labels and values.
     * @throws FileNotFoundException Throws if the CSV File is not found.
     * @throws IllegalArgumentException Throws if one dimension is incorrect.
     */
    public Vector<Vector<?>> parseCSV(String csvFileName, final char delimiter) throws FileNotFoundException, IllegalArgumentException {
        Scanner csvScanner = new Scanner(new FileInputStream(csvFileName));

        Vector<IndexType> indexList = new Vector<>();
        Vector<LabelType> labelList = new Vector<>();
        Vector<Vector<Object>> valueList = new Vector<>();

        //Parse the first line, containing the columns name
        String columnName = csvScanner.nextLine();

        boolean onlyOneColumnFlag = (columnName.split(String.valueOf(delimiter)).length == 1);
        boolean onlyOneLineFlag = (! csvScanner.hasNextLine());

        //Band-aid fix for CSV with one column dimension
        if ((! columnName.contains(String.valueOf(delimiter))) && (!onlyOneColumnFlag)){
            throw new IllegalArgumentException("The CSV doesn't contains the delimiter \"" + delimiter + "\"");
        }


        //This is bad, need to refractor this
        if (onlyOneLineFlag){
            String[] line;
            String[] values;
            String[] index; //If there's one line there's only one index

            line = columnName.split(String.valueOf(delimiter));
            values = Arrays.copyOfRange(line, 1, line.length);
            index = Arrays.copyOfRange(line, 0, 1);

            Vector<ValueType> valueType = new Vector<>();
            for (String value : values){
                valueType.add(findType(value));
            }


            for (int i = 0; i < values.length; i++) {
                valueList.add(new Vector<Object>());
            }

            indexList.add((IndexType) index[0]);

            appendValues(valueList, valueType, values);

            //You can have a one dimensional CSV, like this "Ligne1;1;true;hello"
            //But you can't have a one dimensional DataFrame
            //We add an empty string for columns name

            try{
                for (int i = 0; i < values.length; i++) {
                    labelList.add((LabelType) "");
                }
            }catch (ClassCastException castException){
                //If one said that Labels are integer they (probably ?) can't be made from an empty string
                //We'll just throw
                throw new IllegalArgumentException("Cannot handle empty labels with the label given type, pass a String as a label type");
            }

            Vector<Vector<?>> resultVector = new Vector<>();
            resultVector.add(indexList);
            resultVector.add(labelList);
            resultVector.add(valueList);

            return resultVector;
        }

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
        String[] indexes = new String[0]; //Initialize just to comply with the compiler, doesn't matter

        long fieldsCount = 0;
        boolean firstIterationFlag = true;

        do{
            line = csvScanner.nextLine();
            lines = line.split(String.valueOf(delimiter));
            values = Arrays.copyOfRange(lines, lines.length - columnCount, lines.length);

            if (! onlyOneColumnFlag) {
                indexes = Arrays.copyOfRange(lines, 0, (lines.length - columnCount));
            }

            //Count the number of field separated by the delimiter, throw if it's not the same as the number of columns
            fieldsCount = line.chars().filter(ch -> ch == delimiter).count();
            if ((columnCount != fieldsCount) && (!onlyOneColumnFlag)){
                throw new IllegalArgumentException("Inconsistent number of columns, expected " + columnCount + " got " + fieldsCount);
            }


            //Check if the types are correct :
            for (int i = 0; i < values.length; i++) {
                if (firstIterationFlag) {
                    valuesType.add(findType(values[i]));
                } else {
                    // Else we check the type of every value
                    if (!checkType(valuesType.get(i), values[i])) {
                        throw new IllegalArgumentException("The value " + values[i] + " has unexpected type, expected type was " + valuesType.get(i));
                    }
                }
            }

            if (! onlyOneColumnFlag) {
                this.appendIndexes(indexList, indexes);
            }else{
                //Same case whe there's no labels. We still need to have some labels on the DataFrame so we add the empty string
                this.appendIndexes(indexList, new String[]{""});
            }

            this.appendValues(valueList, valuesType, values);

            firstIterationFlag = false;

        } while (csvScanner.hasNextLine());

        Vector<Vector<?>> resultVector = new Vector<>();
        resultVector.add(indexList);
        resultVector.add(labelList);
        resultVector.add(valueList);

        return resultVector;
    }


    /**
     * Append the indexes on the index list of the DataFrame.
     * @param indexList The index list of the DataFrame.
     * @param indexes The indexes list parsed from the CSV.
     */
    private void appendIndexes(Vector<IndexType> indexList, String[] indexes) {
        //Append the indexes
        for (String index : indexes) {
            indexList.add((IndexType) index);
        }
    }


    /**
     * Append the values on the value list of the DataFrame.
     * @param valueList The value list of the DataFrame.
     * @param valuesType The list containing the types of the values parsed.
     * @param values The values list parsed from the CSV.
     */
    private void appendValues(Vector<Vector<Object>> valueList, Vector<ValueType> valuesType, String[] values) {
        for (int i = 0; i < values.length; i++) {
            switch (valuesType.get(i)) {
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
                case DATE:
                    valueList.get(i).add(LocalDate.parse(values[i]));
                    break;
                default:
                    System.err.println("Forgot to add cases for the type " + valuesType.get(i));
                    System.err.println("Defaulting to String");
                    valueList.get(i).add((String) values[i]);
                    break;
            }
        }
    }

    /**
     * Try to guess the type of the parsed value. <p>
     * If no type can be determined, defaults to String.
     * @param value The string representation of the value to guess the type.
     * @return The type guessed.
     */
    private static ValueType findType(String value){

        try{
            Integer.parseInt(value);
            return ValueType.INTEGER;
        }catch (NumberFormatException ignored){};

        try{
            Double.parseDouble(value);
            return ValueType.DOUBLE;
        }catch (NumberFormatException ignored){};

        //Built-in boolean parser is different, returns false if value is different from "true", ignoring case
        //So we make our own parser
        if ((value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("false"))){
            return ValueType.BOOLEAN;
        }

        try{
            LocalDate.parse(value);
            return ValueType.DATE;
        }catch (DateTimeException ignored){}


        //We default to String if other types are not supported
        return ValueType.STRING;
    }


    /**
     * Check if the type of the value is still the same type found previously (on the first line of the CSV).
     * @param valuesType  The list containing the types of the values parsed.
     * @param value The string representation of the value to guess the type.
     * @return true if the type is the same, false otherwise.
     */
    //FIXME: If the value is a String that is either "true" or "false", it will false-negative
    //Potential fix : Be permissive and return ((valuesType == ValueType.BOOLEAN) || (valuesType == valueType.STRING))
    private boolean checkType(ValueType valuesType, String value){

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

        try{
            LocalDate.parse(value);
            return valuesType == ValueType.DATE;
        }catch (DateTimeException ignored){}

        return valuesType == ValueType.STRING;
    }
}
