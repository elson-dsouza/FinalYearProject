package tweet4analysis;

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

        // provide the delimiters for splitting the tweet
        String delims = "[ .,?!]+";

        // now split the tweet into tokens
        String[] tokens = tweet.split(delims);

        // for each token/word, emit it
        for (String token: tokens) {
            collector.emit(new Values(token));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        // tell storm the schema of the output tuple for this spout
        // tuple consists of a single column called 'tweet-word'
        declarer.declare(new Fields("tweet-word"));
    }
}
