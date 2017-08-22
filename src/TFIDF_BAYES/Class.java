package TFIDF_BAYES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class Class {
    ArrayList<Document> list_of_docs = new ArrayList<>();
    Map<String, Double> map_of_word_and_total_weight_in_class = new HashMap<>(); // this map holds unique words and it's total weights
    double total_weight_of_class;
    public void add_a_doc(Document doc){
        this.list_of_docs.add(doc);
    }



    public void cal_weight_for_word_in_all_docs(Map<String, Integer> map_of_word_and_num_docs_contain_that_word_in_universe, int total_num_of_docs, HashSet<String> set_of_deleted_words){
        for(int docIndex = 0; docIndex < list_of_docs.size(); docIndex++){
            Document current_doc = list_of_docs.get(docIndex);
            Map<String, Double> map_of_weight = current_doc.map_word_vs_weight;
            Map<String, Integer> map_of_tf = current_doc.map_word_vs_tf;
            for(Map.Entry<String, Integer> entry : map_of_tf.entrySet()){
                String word = entry.getKey();
                double weight = 0;
                if(!set_of_deleted_words.contains(word)) {
                    if (!map_of_weight.containsKey(word)) {
                        double tf = (double) map_of_tf.get(word) / (double) current_doc.totalWords + 1;
                        double idf = Math.log((double) total_num_of_docs / (double) map_of_word_and_num_docs_contain_that_word_in_universe.get(word));
                        weight = tf * idf;
                        total_weight_of_class += weight;
                        map_of_weight.put(word, weight);
                    }
                    if (!map_of_word_and_total_weight_in_class.containsKey(word)) {
                        map_of_word_and_total_weight_in_class.put(word, weight);
                    } else {
                        double old_value = map_of_word_and_total_weight_in_class.get(word);
                        map_of_word_and_total_weight_in_class.replace(word, old_value + weight);
                    }
                }
                else{
                    map_of_tf.remove(entry);
                }
            }
        }
    }

    public Document getDoc(int index){
        return list_of_docs.get(index);
    }


}
