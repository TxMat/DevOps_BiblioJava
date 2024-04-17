import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestCSVTwoDimensional {

    private Scanner csvScanner;

    @Before
    public void initTest() throws FileNotFoundException {
        this.csvScanner = new Scanner(new FileInputStream("devops-bibliojava/src/main/resources/CSVTwoDimCorrect.csv"));
    }

    @After
    public void endTest() {
        this.csvScanner.close();
    }

}
