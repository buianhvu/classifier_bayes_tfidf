package TFIDF_BAYES;

import org.w3c.dom.css.DocumentCSS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class Data {
    ArrayList<Class> list_of_classes = new ArrayList<>();
    HashSet<String> set_of_unique_words_in_all_classes = new HashSet<>();
    HashSet<String> set_of_stop_word;
    Map<String, Integer> map_of_word_and_num_docs_contain_that_word_in_universe = new HashMap<>();
    HashSet<String> set_of_deleted_words = new HashSet<>();
    public double sumOfWeightOfAllClasses;
    public Class getClass(int index){
        return list_of_classes.get(index);
    }
    int total_num_of_docs;

    public void increaseTotalNumOfDocs(){
        total_num_of_docs++;
    }
    public void dataPrepare(){
        updateMapNumOfDocsContainWord();
        delete_word_low_df();
        nomarlizeDocs(set_of_deleted_words);
        calWeightForWordsInAllClasses();
        setSumOfWeightOfAllClasses();
    }
    public void delete_word_low_df(){
        for(Map.Entry<String, Integer> entry : map_of_word_and_num_docs_contain_that_word_in_universe.entrySet()){
            if(entry.getValue() <= 0 || entry.getValue() > total_num_of_docs*0.5) {
                set_of_deleted_words.add(entry.getKey());
                set_of_unique_words_in_all_classes.remove(entry.getKey());
                map_of_word_and_num_docs_contain_that_word_in_universe.remove(entry);
            }
        }
    }

    private void calWeightForWordsInAllClasses(){
//        System.out.println("Calculating weight for all words in all classes ...");
        for(int classIndex = 0; classIndex < list_of_classes.size(); classIndex++)
            getClass(classIndex).cal_weight_for_word_in_all_docs(map_of_word_and_num_docs_contain_that_word_in_universe, total_num_of_docs,set_of_deleted_words);
    }

    private void updateMapNumOfDocsContainWord(){
//        System.out.println("Calculating number of docs contains words ...");
        for(int classIndex = 0; classIndex < list_of_classes.size(); classIndex++){
            Class current_class = list_of_classes.get(classIndex);
            for( int docIndex = 0; docIndex < current_class.list_of_docs.size(); docIndex++){
                Document current_doc = current_class.getDoc(docIndex);
                for(Map.Entry entry : current_doc.map_word_vs_tf.entrySet()){
                    String word = (String) entry.getKey();
                    addToMapNumOfDocsContainWord(word);
                }
            }
        }
    }
    private void addToMapNumOfDocsContainWord(String word){
        if(!map_of_word_and_num_docs_contain_that_word_in_universe.containsKey(word))
            map_of_word_and_num_docs_contain_that_word_in_universe.put(word, 1);
        else {
            int old_value = map_of_word_and_num_docs_contain_that_word_in_universe.get(word);
            map_of_word_and_num_docs_contain_that_word_in_universe.replace(word, old_value + 1);
        }
    }
    private void nomarlizeDocs(HashSet<String> set_of_deleted_words){
        for(int classIndex = 0; classIndex < list_of_classes.size(); classIndex++){
            Class current_class = list_of_classes.get(classIndex);
            for(int docIndex = 0; docIndex < current_class.list_of_docs.size(); docIndex++){
                Document current_doc = current_class.getDoc(docIndex);
                current_doc.nomarlize(set_of_deleted_words);
            }
        }
    }

//    public void calWeightForWordInAllClasses(){
//        System.out.println("Calculating weight for words in all classes ...");
//        for (int classIndex = 0; classIndex < list_of_classes.size(); classIndex++){
//            TFIDF_BAYES.Class currentClass = list_of_classes.get(classIndex);
//            currentClass.cal_weight_for_word_in_all_docs(map_of_word_and_num_docs_contain_that_word_in_universe, total_num_of_docs);
//            currentClass.cal_total_weight_for_words_in_class();
//        }
//    }
    public void setSumOfWeightOfAllClasses(){
        for(int classIndex = 0; classIndex < list_of_classes.size(); classIndex++){
            sumOfWeightOfAllClasses = list_of_classes.get(classIndex).total_weight_of_class;
        }
    }


}
