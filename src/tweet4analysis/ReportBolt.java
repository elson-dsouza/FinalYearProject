package tweet4analysis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.IWindowedBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by elson on 14/11/17.
 */
public class ReportBolt extends BaseRichBolt {

    // place holder to keep the connection to redis
    private transient RedisConnection<String,String> redis;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        // instantiate a redis connection
        RedisClient client = new RedisClient("localhost",6379);

        // initiate the actual connection
        redis = client.connect();
    }

    @Override
    public void execute(Tuple tuple)
    {
        // access the word
        String word = tuple.getStringByField("tweet-word");

        // publish the word count to redis using word as the key
        redis.publish("WordCountTopology", word );
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        // nothing to add - since it is the final bolt
    }
}
