package tweet4analysis.Bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tweet4analysis.SentimentAnalyzer.SentimentAnalyzer;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.io.IOException;
import java.util.Map;

/**
 * Created by elson on 5/2/18.
 */
public final class SentimentCalculatorBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentimentCalculatorBolt.class);

    @Override
    public final void prepare(final Map map, final TopologyContext topologyContext,
                              final OutputCollector collector) {
    }

    @Override
    public final void declareOutputFields(final OutputFieldsDeclarer outputFieldsDeclarer) {
        //No output at the moment
    }

    @Override
    public final void execute(final Tuple input) {
        final Status status = (Status) input.getValueByField("tweet");
        final String tweetProcessed = preProcessTweet(status);
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        sentimentAnalyzer.setInputString(tweetProcessed);
        try {
            sentimentAnalyzer.setInputStringProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sentimentAnalyzer.analyze();
        String sentimentOfCurrentTweet = sentimentAnalyzer.getPolarity().toString();
        System.out.println("Tweet : Sentiment " + sentimentOfCurrentTweet + tweetProcessed);
        LOGGER.debug("Tweet : Sentiment {} ==> {}", tweetProcessed, sentimentOfCurrentTweet);

        //TODO: Decide what is to be done with the sentiment
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