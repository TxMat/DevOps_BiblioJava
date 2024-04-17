import org.junit.After;
import org.junit.Test;

/**
 * These tests only check if the parsing handles errors correctly, they do <b>NOT</b> check for the correctness of the DataFrame.
 * This specifically checks for one-dimensional line CSV.
 */
//So we can't hit 100% code coverage with this test only.
public class TestCSVParsingOneDimensionalLine {

	private DataFrame<String, String, Object> dataFrameCSV;

	private static final String PATH_CORRECT = "src/test/resources/CSVOneDimLineCorrect.csv";

	@After
	public void endTest() {
		this.dataFrameCSV = null;
	}


	/**
	 * Check if no errors are thrown while parsing a correct one-dimensional column correct CSV.
	 */
	@Test
	public void testParsingCorrectCSVOneDimCol() throws Exception {
		dataFrameCSV = new DataFrame<>(PATH_CORRECT, ';');
	}

}
