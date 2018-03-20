package stormTweetAnalyzer.Bolt

import org.apache.storm.task.OutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Tuple
import org.apache.storm.tuple.Values
import stormTweetAnalyzer.Model.TopicPotential
import stormTweetAnalyzer.Model.TweetPotential
import utils.Constants
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

/**
 * Created by elson on 8/3/18.
 */
class TopicPotentialBolt : BaseRichBolt(){


    // To output tuples from this bolt to the count bolt
    private lateinit var collector: OutputCollector
    private var topicPotentialScore: BigDecimal = BigDecimal.ZERO

    override fun prepare(map: MutableMap<Any?, Any?>, topologyContext: TopologyContext,
                         outputCollector: OutputCollector) {
        collector = outputCollector
    }

    override fun execute(input: Tuple) {
        val tweetPotential = input.getValueByField(Constants.EMITTED_TUPLE_NAMES.TWEET_POTENTIAL)
                as TweetPotential
        val retweetCount = tweetPotential.status.retweetCount
        val sentiment = tweetPotential.sentiment
        val time = Date().time
        topicPotentialScore += MathContext(((retweetCount + 1) * sentiment).toInt())

        val topicPotential = TopicPotential(time, topicPotentialScore)
        System.out.println("Topic potential at $time : $topicPotentialScore")
        collector.emit(Values(topicPotential))
    }

    override fun declareOutputFields(declarer: OutputFieldsDeclarer) {
        declarer.declare(Fields(Constants.EMITTED_TUPLE_NAMES.TOPIC_POTENTIAL))
    }

}