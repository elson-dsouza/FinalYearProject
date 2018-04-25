package stormTweetAnalyzer.Bolt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTweetAnalyzer.Twitter4JImpl.StatusImpl;
import utils.Constants;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by elson on 14/11/17.
 */
public class ParseTweetBolt extends BaseRichBolt {

    // To output tuples from this bolt to the count bolt
    private OutputCollector collector;
    private Gson gson;
    private Type gsonType;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        // save the output collector for emitting tuples
        collector = outputCollector;
        gson = new Gson();
        gsonType = new TypeToken<StatusImpl>() {
        }.getType();
    }

    @Override
    public void execute(final Tuple input) {
        final String tweetJson = (String) input.getValueByField(Constants.EMITTED_TUPLE_NAMES.TWEET_JSON);
        StatusImpl status = gson.fromJson(tweetJson, gsonType);
        System.out.println("Original tweet: " + status);
        collector.emit(new Values(status));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // tell storm the schema of the output tuple for this spout
        declarer.declare(new Fields(Constants.EMITTED_TUPLE_NAMES.RAW_TWEET));
    }
}
