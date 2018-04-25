package stormTweetAnalyzer.Bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTweetAnalyzer.Model.TweetPotential;
import stormTweetAnalyzer.SentimentAnalyzer.SentimentAnalyzer;
import twitter4j.Status;
import utils.Constants;

import java.util.Map;

/**
 * Created by elson on 5/2/18.
 */
public class TweetPotentialBolt extends BaseRichBolt {
    // To output tuples from this bolt to the count bolt
    private OutputCollector collector;

    @Override
    public final void prepare(final Map map, final TopologyContext topologyContext,
                              final OutputCollector collector) {
        // save the output collector for emitting tuples
        this.collector = collector;
    }

    @Override
    public final void declareOutputFields(final OutputFieldsDeclarer outputFieldsDeclarer) {
        // tell storm the schema of the output tuple for this spout
        // tuple consists of a single column called 'tweet-word'
        outputFieldsDeclarer.declare(new Fields(Constants.EMITTED_TUPLE_NAMES.TWEET_POTENTIAL));
    }

    @Override
    public final void execute(final Tuple input) {
        final Status status = (Status) input.getValueByField(Constants.EMITTED_TUPLE_NAMES.RAW_TWEET);
        final String tweetText = status.getText();
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();

        float sentimentOfCurrentTweet = sentimentAnalyzer.analyze(tweetText);
        TweetPotential tweetPotential = new TweetPotential(status, sentimentOfCurrentTweet);
        System.out.println("\n\n" +
                "Tweet: " + tweetText + "\nTweet potential: " + sentimentOfCurrentTweet);
        collector.emit(new Values(tweetPotential));
    }
}