package tweet4analysis.Spout;

import org.apache.storm.Config;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import tweet4analysis.Utils.Constants;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by elson on 14/11/17.
 */

public class TweetSpout extends BaseRichSpout {

    // To output tuples from spout to the next stage bolt
    private SpoutOutputCollector mCollector;

    // Twitter4j - twitter stream to get tweets
    private TwitterStream mTwitterStream;

    // Shared mQueue for getting buffering tweets received
    private LinkedBlockingQueue<Status> mQueue = null;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        // create the buffer to block tweets
        mQueue = new LinkedBlockingQueue<>(1000);

        // save the output mCollector for emitting tuples
        mCollector = spoutOutputCollector;

        // build the config with credentials for twitter 4j
        ConfigurationBuilder config = new ConfigurationBuilder()
                        .setOAuthConsumerKey(Constants.OAUTH_CONSUMER_KEY)
                        .setOAuthConsumerSecret(Constants.OAUTH_CONSUMER_SECRET)
                        .setOAuthAccessToken(Constants.OAUTH_ACCESS_TOKEN)
                        .setOAuthAccessTokenSecret(Constants.OAUTH_ACCESS_TOKEN_SECRET);

        // create the twitter stream factory with the config
        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(config.build());

        // get an instance of twitter stream
        mTwitterStream = twitterStreamFactory.getInstance();

        // provide the handler for twitter stream
        mTwitterStream.addListener(new TweetListener());

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(Constants.ALL_TOPICS());
        tweetFilterQuery.language(Constants.LANGUAGES);
        mTwitterStream.filter(tweetFilterQuery);
    }

    @Override
    public void nextTuple() {
        // try to pick a tweet from the buffer
        Status ret = mQueue.poll();

        // if no tweet is available, wait for 50 ms and return
        if (ret==null) {
            Utils.sleep(500);
            return;
        }

        // now emit the tweet to next stage bolt
        mCollector.emit(new Values(ret));
    }

    @Override
    public void close() {
        // shutdown the stream - when we are going to exit
        mTwitterStream.shutdown();
    }

    /**
     * Component specific configuration
     */
    @Override
    public Map<String, Object> getComponentConfiguration() {
        // create the component config
        Config ret = new Config();

        // set the parallelism for this spout to be 1
        ret.setMaxTaskParallelism(1);

        return ret;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        // tell storm the schema of the output tuple for this spout
        // tuple consists of a single column called 'tweet'
        outputFieldsDeclarer.declare(new Fields(Constants.EMITTED_TUPLES.RAW_TWEET));
    }

    // Class for listening on the tweet stream - for twitter4j
    private class TweetListener implements StatusListener {

        // Implement the callback function when a tweet arrives
        @Override
        public void onStatus(Status status) {
            // add the tweet into the mQueue buffer
            mQueue.offer(status);
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice sdn) {
        }

        @Override
        public void onTrackLimitationNotice(int i) {
        }

        @Override
        public void onScrubGeo(long l, long l1) {
        }

        @Override
        public void onStallWarning(StallWarning warning) {
        }

        @Override
        public void onException(Exception e) {
            e.printStackTrace();
        }
    }
}
