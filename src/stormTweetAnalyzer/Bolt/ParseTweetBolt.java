package stormTweetAnalyzer.Bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import utils.Constants;

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
    public void execute(final Tuple input)
    {
        final String tweetJson = (String) input.getValueByField(Constants.EMITTED_TUPLE_NAMES.TWEET_JSON);
        Status status = null;
        try {
            status = TwitterObjectFactory.createStatus(tweetJson);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        collector.emit(new Values(status));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        // tell storm the schema of the output tuple for this spout
        declarer.declare(new Fields(Constants.EMITTED_TUPLE_NAMES.RAW_TWEET));
    }
}
