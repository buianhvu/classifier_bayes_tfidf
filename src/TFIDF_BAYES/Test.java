package TFIDF_BAYES;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class Test {
    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        CrossValidation testing = new CrossValidation();
        testing.test_kfold();
    }
}
