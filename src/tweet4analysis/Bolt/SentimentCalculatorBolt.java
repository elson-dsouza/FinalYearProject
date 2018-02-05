package tweet4analysis.Bolt;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tweet4analysis.Utils.Constants;
import twitter4j.Status;
import twitter4j.URLEntity;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by elson on 5/2/18.
 */
public final class SentimentCalculatorBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentimentCalculatorBolt.class);
    private static final long serialVersionUID = -5094673458112825122L;
    private SortedMap<String,Integer> mAfinnSentimentMap = null;


    @Override
    public final void prepare(final Map map, final TopologyContext topologyContext,
                              final OutputCollector collector) {
        mAfinnSentimentMap = Maps.newTreeMap();
        //Bolt will read the AFINN Sentiment file and stores the key, value pairs to a Map.
        try {
            final URL url = Resources.getResource(Constants.AFINN_SENTIMENT_FILE_NAME);
            final String text = Resources.toString(url, Charsets.UTF_8);
            final Iterable<String> lineSplit = Splitter.on("\n").trimResults().omitEmptyStrings().split(text);
            List<String> tabSplit;
            for (final String str: lineSplit) {
                tabSplit = Lists.newArrayList(Splitter.on("\t").trimResults().omitEmptyStrings().split(str));
                mAfinnSentimentMap.put(tabSplit.get(0), Integer.parseInt(tabSplit.get(1)));
            }
        } catch (final IOException ioException) {
            LOGGER.error(ioException.getMessage(), ioException);
            ioException.printStackTrace();
            //Should not occur. If it occurs, we cant continue. So, exiting at this point itself.
            System.exit(1);
        }
    }

    @Override
    public final void declareOutputFields(final OutputFieldsDeclarer outputFieldsDeclarer) {
        //No output at the moment
    }

    @Override
    public final void execute(final Tuple input) {
        final Status status = (Status) input.getValueByField("tweet");
        final int sentimentCurrentTweet = getSentimentOfTweet(status);

        //TODO: Decide what is to be done with the sentiment
    }

    /**
     * Gets the sentiment of the current tweet.
     *
     * @param status -- Status Object.
     * @return sentiment of the current tweet.
     */
    private int getSentimentOfTweet(final Status status) {
        final String processedTweet = preprocessTweet(status);
        //Splitting the tweet on empty space to get words.
        final Iterable<String> words = Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .split(processedTweet);
        int sentimentOfCurrentTweet = 0;
        //Loop through all the words and find the sentiment of this tweet.
        for (final String word : words) {
            if(mAfinnSentimentMap.containsKey(word)){
                sentimentOfCurrentTweet += mAfinnSentimentMap.get(word);
            }
        }
        System.out.println("Tweet : Sentiment " + sentimentOfCurrentTweet + processedTweet);
        LOGGER.debug("Tweet : Sentiment {} ==> {}", processedTweet, sentimentOfCurrentTweet);
        return sentimentOfCurrentTweet;
    }

    /**
     * Preprocesses the current tweet.
     *
     * @param status -- Status Object.
     * @return Preprocessed tweet string.
     */
    private String preprocessTweet(final Status status) {
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
        //Remove all punctuation and new line chars in the tweet and return
        return truncatedTweet.toString().replaceAll("\\p{Punct}|\\n", " ").toLowerCase();
    }
}