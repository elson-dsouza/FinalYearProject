package stormTweetAnalyzer.Bolt;

import org.apache.storm.cassandra.bolt.CassandraWriterBolt;
import org.apache.storm.cassandra.query.CqlMapper;
import org.apache.storm.cassandra.query.impl.SimpleCQLStatementMapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by elson on 14/11/17.
 */
@Deprecated
public class ReportBolt extends CassandraWriterBolt {

    private OutputCollector collector;

    public ReportBolt() {
        super(new SimpleCQLStatementMapper(
                "INSERT INTO topic_series VALUES (*,",
                new CqlMapper.DefaultCqlMapper()));
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
    {
        collector = outputCollector;
    }

    @Override
    protected void process(Tuple input) {
        // = (String) input.getValueByField(Constants.EMITTED_TUPLE_NAMES.TWEET_JSON);
    }
}
