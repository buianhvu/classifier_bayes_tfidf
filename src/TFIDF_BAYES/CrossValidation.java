package TFIDF_BAYES;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by BuiAnhVu on 8/18/2017.
 */
public class CrossValidation {
    double[] total_accuracy_for_each_cate = new double[10];
    String[] file_name = {"SVTT_Dataset/1.xml", "SVTT_Dataset/2.xml", "SVTT_Dataset/3.xml", "SVTT_Dataset/4.xml", "SVTT_Dataset/5.xml",
            "SVTT_Dataset/6.xml", "SVTT_Dataset/7.xml", "SVTT_Dataset/8.xml", "SVTT_Dataset/9.xml", "SVTT_Dataset/10.xml"};
    public String[] class_name = {"Xa hoi", "The gioi", "The thao", "Giao duc", "Kinh doanh", "Giai tri", "Du lich", "Phap luat", "Suc Khoe", "Cong Nghe"};
    public void test_kfold() throws IOException, SAXException, ParserConfigurationException {
        System.out.println("Divide total dataset into 5 parts, 4 for traindata, 1 for test");
        for(int posOfTest = 0; posOfTest < 5; posOfTest++){
            System.out.println("\nTIME " + (posOfTest + 1));
            System.out.println("");
            Data data = new Data();
            FileHandling fileHandling = new FileHandling(file_name);
            fileHandling.fileHandle(data, posOfTest); // train the data
            TfidfBayesClassifier classifier = new TfidfBayesClassifier(data);
            double[] arr_prob_for_each_cate = classifier.findAccuracy(file_name, posOfTest);
            for(int cateIndex = 0; cateIndex < 10; cateIndex++){
                total_accuracy_for_each_cate[cateIndex] += arr_prob_for_each_cate[cateIndex];
            }
        }
        System.out.println("- Summary: ");
        for(int cateIndex = 0; cateIndex < 10; cateIndex++){
            System.out.println("- Avarage for " + class_name[cateIndex] + ": " + total_accuracy_for_each_cate[cateIndex] / 5);
        }
        double trungbinh = 0;
        for (double v : total_accuracy_for_each_cate) {
            trungbinh += v;
        }
        System.out.println("AVARAGE = " + trungbinh/50);
    }
}