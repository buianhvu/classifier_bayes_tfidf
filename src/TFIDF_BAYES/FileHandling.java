package TFIDF_BAYES;

import org.jsoup.Jsoup;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.text.html.HTML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class FileHandling {
    String[] filename;
    HashSet<String> stop_words_set;
    public FileHandling(String[] file_name_arr) throws FileNotFoundException {
        this.filename = file_name_arr;
        this.stop_words_set = getSetOfStopWords();
    }
    public void fileHandle(Data data, int posOfTestSet) throws ParserConfigurationException, IOException, SAXException {
        HashSet<String> set_of_stop_words = getSetOfStopWords();
        data.set_of_stop_word = set_of_stop_words;
//        System.out.println("Start reading files ...");
        ArrayList<Class> list_of_classes = new ArrayList<>();

        for (int fileIndex = 0; fileIndex < filename.length; fileIndex++) {
            HashSet<String> temp_set_check_duplicate_doc = new HashSet<>();
//            System.out.println("Progressing file " + (fileIndex + 1));
            Class current_class = new Class();
            File inputFile = new File(filename[fileIndex]);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("row");
            int length_of_test = nList.getLength() / 5;
//            System.out.println("Length of test = " + length_of_test);
            int lowwer_bound = length_of_test * posOfTestSet;
            int upper_bound = lowwer_bound + length_of_test;
//            System.out.println("lower bound " + lowwer_bound);
//            System.out.println("upper bound " + upper_bound);
            for (int i = 0; i < nList.getLength(); i++) {
                if(i <= lowwer_bound || i >= upper_bound) {
                    Node nNode = nList.item(i);
                    Element eElement = (Element) nNode;
                    String text_of_a_doc = eElement.getElementsByTagName("field").item(1).getTextContent().toLowerCase();
                    text_of_a_doc = getRidOfHtml(text_of_a_doc);
                    Document current_doc = new Document(text_of_a_doc);
                    if (!temp_set_check_duplicate_doc.contains(text_of_a_doc)) {
                        temp_set_check_duplicate_doc.add(text_of_a_doc);
                        String[] words_in_text = text_of_a_doc.split(" ");
                        for (String word : words_in_text) {
                            if (!isStopWord(word)) {
                                current_doc.increaseTotalWords(1);
                                current_doc.updateWordTF(word);
                                data.set_of_unique_words_in_all_classes.add(word);
                            }
                        }
                        current_class.add_a_doc(current_doc);
                        data.increaseTotalNumOfDocs();
                    }
                }
            }
//            current_class.cal_weight_for_word_in_all_docs(data);
//            current_class.cal_total_weight_for_words_in_class();
            list_of_classes.add(current_class);
        }
        data.list_of_classes = list_of_classes;
        data.dataPrepare();
    }
    public HashSet<String> getSetOfStopWords() throws FileNotFoundException {
        HashSet<String> set_of_stop_words = new HashSet<>();
        File file = new File("SVTT_Dataset/stop_words.txt");
        Scanner sc = new Scanner(file);
        while(sc.hasNext()){
            String word = sc.nextLine();
            set_of_stop_words.add(word);
        }
        this.stop_words_set = set_of_stop_words;
        return set_of_stop_words;
    }
    public boolean isStopWord(String word){
        if(word.contains("http"))
            return true;
        if(word.length() <= 1 )
            return true;
        if(stop_words_set.contains(word))
            return true;
        return false;
    }
    public static String getRidOfHtml(String html) {
        return Jsoup.parse(html).text();
    }
}
