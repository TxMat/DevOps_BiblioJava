import org.junit.Test;
import java.util.List;

import main.java.DataFrame;

import static org.junit.Assert.*;

/**
 * These tests check if the two DataFrames created differently (with Lists and with CSV) are the same
 */
public class TestCSVDataframeIntegration {

	DataFrame<String, String, Object> dataFrameList;
	DataFrame<String, String, Object> dataFrameCSV;

	private static final String PATH_CORRECT = "src/test/resources/CSVTwoDimCorrect.csv";

	@Test
	public void compareDataframes() throws Exception{
		dataFrameCSV = new DataFrame<>(PATH_CORRECT, ';');

		List<String> index = List.of("Ligne1", "Ligne2");
		List<String> label = List.of("Colonne1", "Colonne2", "Colonne3", "Colonne4");
		List<List<Object>> values = List.of(
				List.of(-1, 2),
				List.of(3.1, 4.2),
				List.of("Hello World", "Goodbye World"),
				List.of("a", "b"));

		dataFrameList = new DataFrame<String, String, Object>(index, label, values);

		assertTrue(dataFrameCSV.equals(dataFrameList));

		//Interestingly enough, assertEquals does not pass, it doesn't seem to use the .equals() method but comparing the .toString()
		//Still weird because the String representation is the same, byte to byte
		//assertEquals(dataFrameCSV, dataFrameList);
	}
}
