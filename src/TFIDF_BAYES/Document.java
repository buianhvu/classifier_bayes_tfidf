package TFIDF_BAYES;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by BuiAnhVu on 8/10/2017.
 */
public class Document {
    public String textContent;
    public int totalWords;
    public Document(String textContent){
        this.textContent = textContent;
        this.totalWords = 0;
    }
    public void increaseTotalWords(int num){
        totalWords += num;
    }
    public void decreaseTotalWords(int num){
        totalWords -= num;
    }
    public HashMap<String, Double> map_word_vs_weight = new HashMap<>(); // map of unique word and it's weight in a đoc
    public HashMap<String, Integer> map_word_vs_tf = new HashMap<>();

    public void updateWordTF(String word){
        if(!map_word_vs_tf.containsKey(word)){
            map_word_vs_tf.put(word, 1);
        }
        else {
            int old_value = map_word_vs_tf.get(word);
            map_word_vs_tf.replace(word, old_value + 1);
        }
    }
    public void nomarlize(HashSet<String> setOfDeletedWords){
        double mauso = 0;
        for(Map.Entry<String, Double> entry : map_word_vs_weight.entrySet()){
            if(setOfDeletedWords.contains(entry.getKey())) {
                decreaseTotalWords(map_word_vs_tf.get(entry));
                map_word_vs_weight.remove(entry);
                map_word_vs_tf.remove(entry.getKey());
            }
            else
                mauso += Math.pow(entry.getValue(), 2);
        }
//        for(Map.Entry<String, Double> entry : map_word_vs_weight.entrySet()){
//            double old_value = entry.getValue();
//            entry.setValue(old_value / mauso);
//        }
    }
}
