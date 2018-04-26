package kafkaTweetProducer;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import utils.Constants;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class the kafka producer which streams in the tweets from the twitter intermediate and
 * stores in in a Kafka broker intermediate before being streamed into the storm topology
 * <p>
 * Created by elson on 4/3/18.
 */
public class TweetProducer {

    public static void main(String args[]) {
        LinkedBlockingQueue<Status> queue = new LinkedBlockingQueue<>(1000);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(Constants.OAUTH_CONSUMER_SECRET)
                .setOAuthAccessToken(Constants.OAUTH_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Constants.OAUTH_ACCESS_TOKEN_SECRET);

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener tweetListener = new StatusListener() {
            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onStatus(Status status) {
                queue.offer(status);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }
        };

        twitterStream.addListener(tweetListener);

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.track(Constants.TOPIC);
        tweetFilterQuery.language(Constants.LANGUAGES);
        twitterStream.filter(tweetFilterQuery);

        //Add Kafka producer config settings
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        //Handle shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            producer.close();
            twitterStream.shutdown();
        }));

        Gson gson = new Gson();

        //noinspection InfiniteLoopStatement
        while (true) {
            Status tweet = queue.poll();
            if (tweet == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    producer.close();
                    twitterStream.shutdown();
                    e.printStackTrace();
                }
            } else {
                producer.send(new ProducerRecord<>(Constants.KAFKA_TOPIC, gson.toJson(tweet)));
            }
        }
    }
}
