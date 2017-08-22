package TFIDF_BAYES;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class TfidfBayesClassifier {
    Data data;
    HashSet<String> stop_words_set = new HashSet<>();
    public TfidfBayesClassifier(Data data) throws FileNotFoundException {
        this.data = data;
        stop_words_set = getSetOfStopWords();
    }
    public String[] class_name = {"Xa hoi", "The gioi", "The thao", "Giao duc", "Kinh doanh", "Giai tri", "Du lich", "Phap luat", "Suc Khoe", "Cong Nghe"};

    public int classify(String text){
        double array_of_result[] = new double[data.list_of_classes.size()];
        for(int classIndex = 0; classIndex < data.list_of_classes.size(); classIndex++){
            double prob = 0 + Math.log((double) data.getClass(classIndex).list_of_docs.size() / (double)data.total_num_of_docs);
//            double prob = 0;
            double total_weight_of_class = data.getClass(classIndex).total_weight_of_class;
            String[] words_in_text = text.toLowerCase().split(" ");
            for(String word : words_in_text){
               if(!isStopWord(word)){
                   if(data.getClass(classIndex).map_of_word_and_total_weight_in_class.get(word) != null){
                       double total_weight_of_word = data.getClass(classIndex).map_of_word_and_total_weight_in_class.get(word);
                       double singleProb = Math.log((total_weight_of_word + 1.0) / (total_weight_of_class + (double) data.set_of_unique_words_in_all_classes.size()));
                       prob = prob + singleProb;
                   }
                   else{
                       double singleProb = Math.log((  1.0) / (total_weight_of_class + (double) data.set_of_unique_words_in_all_classes.size()));
                       prob = prob + singleProb;
                   }
               }
            }
            array_of_result[classIndex] = prob;
        }
//        for(int i = 0; i <= 9 ; i++){
//            System.out.print(i+ " " + array_of_result[i] + " ");
//        }
        return (findMaxOfArray(array_of_result) + 1);
    }

    public double[] findAccuracy(String[] file_name, int posOfTestSet) throws ParserConfigurationException, IOException, SAXException {
        double total = 0;
        double[] accuracy_of_cate_arr = new double[10];
        for(int classIndex = 0; classIndex < data.list_of_classes.size(); classIndex++){
            System.out.println("Num of docs " + class_name[classIndex] + " : " + data.getClass(classIndex).list_of_docs.size());
        }
        System.out.println("Finding accuracy: ");
        for (int fileIndex = 0; fileIndex < file_name.length; fileIndex++) {
            ArrayList<Boolean> array_to_cal_accuracy_rate = new ArrayList<>();
            File inputFile = new File(file_name[fileIndex]);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("row");
            int length_of_test = nList.getLength() / 5;
            int lowwer_bound = length_of_test * posOfTestSet;
            int upper_bound = lowwer_bound + length_of_test;
            for (int i = 0; i < nList.getLength(); i++) {
                if(i >= lowwer_bound && i<= upper_bound){
                    Node nNode = nList.item(i);
                    Element eElement = (Element) nNode;
                    String textContent = eElement.getElementsByTagName("field").item(1).getTextContent().toLowerCase();
                    int num_of_class_text_belong_to = classify(textContent);
                    String num_of_class_classified = eElement.getElementsByTagName("field").item(0).getTextContent(); // that text already classified
                    if (num_of_class_text_belong_to == Integer.parseInt(num_of_class_classified)) {
                        array_to_cal_accuracy_rate.add(true);
                    } else
                        array_to_cal_accuracy_rate.add(false);
                }
            }
            int count = 0;
            for(int index = 0; index < array_to_cal_accuracy_rate.size(); index++){
                if(array_to_cal_accuracy_rate.get(index))
                    count++;
            }
            double rate = (double) count / (double) array_to_cal_accuracy_rate.size();
            total += rate;
            System.out.println("Accuracy for " + class_name[fileIndex] + " : " + rate);
            accuracy_of_cate_arr[fileIndex] = rate;
        }
        System.out.println("Avarage = " + total/10);
        return accuracy_of_cate_arr;
    }
    private int findMaxOfArray(double array_class_prob[]) {
        double max = -1000000;
        int maxIndex = -100;
        for (int i = 0; i < array_class_prob.length; i++) {
//            System.out.println("i = " + i + " = " + array_class_prob[i]);
            if (array_class_prob[i] > max) {
                max = array_class_prob[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public boolean isStopWord(String word){
        if(data.set_of_deleted_words.contains(word))
            return true;
        if(word.contains("http"))
            return true;
        if(word.length() <= 1 || word.length() >25 )
            return true;
        if(stop_words_set.contains(word))
            return true;
        return false;
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
}
