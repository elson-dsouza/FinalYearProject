package stormTweetAnalyzer.Bolt;

import Utils.Constants;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTweetAnalyzer.Model.Tweet;
import stormTweetAnalyzer.SentimentAnalyzer.SentimentAnalyzer;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.util.Map;

/**
 * Created by elson on 5/2/18.
 */
public final class SentimentCalculatorBolt extends BaseRichBolt {
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
        outputFieldsDeclarer.declare(new Fields(Constants.EMITTED_TUPLES.TWEET_WITH_SENTIMENT));
    }

    @Override
    public final void execute(final Tuple input) {
        final Status status = (Status) input.getValueByField(Constants.EMITTED_TUPLES.RAW_TWEET);
        final String tweetProcessed = preProcessTweet(status);
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();

        float sentimentOfCurrentTweet = sentimentAnalyzer.analyze(tweetProcessed);
        System.out.println("Tweet : Sentiment " + sentimentOfCurrentTweet + tweetProcessed);
        Tweet tweetWithSentiment = new Tweet(status, tweetProcessed, sentimentOfCurrentTweet);
        collector.emit(new Values(tweetWithSentiment));
    }

    /**
     * Preprocesses the current tweet.
     *
     * @param status Status Object.
     * @return Preprocessed tweet string.
     */
    private String preProcessTweet(final Status status) {
        //Knocking off the URLs from the tweet since they don't need to parsed
        final String tweet = status.getText();
        final URLEntity[] urlEntities = status.getURLEntities();
        int startOfURL;
        int endOfURL;
        StringBuilder truncatedTweet = new StringBuilder();
        for(final URLEntity urlEntity: urlEntities){
            startOfURL = urlEntity.getStart();
            endOfURL = urlEntity.getEnd();
            truncatedTweet.append(tweet.substring(0, startOfURL)).append(tweet.substring(endOfURL));
        }
        return truncatedTweet.toString();
    }
}