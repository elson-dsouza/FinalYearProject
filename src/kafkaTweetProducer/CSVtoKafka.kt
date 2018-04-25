package kafkaTweetProducer

import com.google.gson.Gson
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder
import utils.Constants
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by elson on 25/4/18.
 */
@Deprecated("Temporary")
object CSVtoKafka {

    @JvmStatic
    fun main(args: Array<String>) {
        val queue = LinkedBlockingQueue<Status>(1000)

        val cb = ConfigurationBuilder()
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.OAUTH_CONSUMER_KEY)
                .setOAuthConsumerSecret(Constants.OAUTH_CONSUMER_SECRET)
                .setOAuthAccessToken(Constants.OAUTH_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Constants.OAUTH_ACCESS_TOKEN_SECRET)

        val twitterStream = TwitterStreamFactory(cb.build()).instance

        val tweetListener = object : StatusListener {
            override fun onException(e: Exception) {
                e.printStackTrace()
            }

            override fun onStatus(status: Status) {
                queue.offer(status)
            }

            override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {

            }

            override fun onTrackLimitationNotice(i: Int) {

            }

            override fun onScrubGeo(l: Long, l1: Long) {

            }

            override fun onStallWarning(stallWarning: StallWarning) {

            }
        }

        twitterStream.addListener(tweetListener)

        val tweetFilterQuery = FilterQuery()
        tweetFilterQuery.track(*Constants.TOPIC)
        tweetFilterQuery.language(*Constants.LANGUAGES)
        twitterStream.filter(tweetFilterQuery)

        //Add Kafka producer config settings
        val props = Properties()
        props["metadata.broker.list"] = "localhost:9092"
        props["bootstrap.servers"] = "localhost:9092"
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

        val producer = KafkaProducer<String, String>(props)

        //Handle shutdown
        Runtime.getRuntime().addShutdownHook(Thread {
            producer.close()
            twitterStream.shutdown()
        })

        val gson = Gson()

        while (true) {
            val tweet = queue.poll()
            if (tweet == null) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    producer.close()
                    twitterStream.shutdown()
                    e.printStackTrace()
                }

            } else {
                producer.send(ProducerRecord(Constants.KAFKA_TOPIC, gson.toJson(tweet)))
            }
        }
    }
}