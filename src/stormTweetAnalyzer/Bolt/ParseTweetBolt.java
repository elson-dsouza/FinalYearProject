package stormTweetAnalyzer.Bolt;

import Utils.Constants;
import com.google.gson.Gson;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import twitter4j.Status;

import java.util.Map;

/**
 * Created by elson on 14/11/17.
 */
public class ParseTweetBolt extends BaseRichBolt {

    // To output tuples from this bolt to the count bolt
    private OutputCollector collector;
    private Gson gson;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        // save the output collector for emitting tuples
        collector = outputCollector;
        gson = new Gson();
    }

    @Override
    public void execute(final Tuple input)
    {
        final String tweetJson = (String) input.getValueByField(Constants.EMITTED_TUPLES.TWEET_JSON);
        Status status = gson.fromJson(tweetJson, Status.class);
        collector.emit(new Values(status));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        // tell storm the schema of the output tuple for this spout
        declarer.declare(new Fields(Constants.EMITTED_TUPLES.RAW_TWEET));
    }
}
