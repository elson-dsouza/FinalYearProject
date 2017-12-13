package tweet4analysis;

import opennlp.tools.lemmatizer.Lemmatizer;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IWindowedBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by elson on 14/11/17.
 */
public class ParseTweetBolt extends BaseRichBolt {

    // To output tuples from this bolt to the count bolt
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        // save the output collector for emitting tuples
        collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple)
    {
        // get the 1st column 'tweet' from tuple
        String tweet = tuple.getString(0);

        //Tokenization
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(tweet);

        //for each token/word, stem it
//        PorterStemmer stemmer = new PorterStemmer();
//        for (int i = 0; i < tokens.length; i++) {
//            tokens[i] = stemmer.stem(tokens[i]);
//        }

        ArrayList<String> tokenList = new ArrayList<>();
        tokenList.addAll(Arrays.asList(tokens));
        tokenList.add(tweet);
        collector.emit(new Values(tokenList));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        // tell storm the schema of the output tuple for this spout
        // tuple consists of a single column called 'tweet-word'
        declarer.declare(new Fields("tweet-words"));
    }
}
