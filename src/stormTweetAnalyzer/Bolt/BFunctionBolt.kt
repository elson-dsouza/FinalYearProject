package stormTweetAnalyzer.Bolt

import org.apache.storm.task.OutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Tuple
import org.apache.storm.tuple.Values
import stormTweetAnalyzer.Model.TopicPotential
import utils.Constants
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


/**
 * Created by elson on 5/4/18.
 */
class BFunctionBolt : BaseRichBolt() {

    // To output tuples from this bolt to the count bolt
    private lateinit var collector: OutputCollector
    private var maxPotential: Double = 0.0
    private var minPotential: Double = 0.0

    //TODO: Determine how we can have multiple values of this
    private val m = 0.1

    override fun prepare(map: MutableMap<Any?, Any?>, topologyContext: TopologyContext,
                         outputCollector: OutputCollector) {
        collector = outputCollector
    }

    override fun execute(input: Tuple) {
        val topicPotential = input.getValueByField(Constants.EMITTED_TUPLE_NAMES.TOPIC_POTENTIAL)
                as TopicPotential
        maxPotential = max(maxPotential, topicPotential.topicPotential)
        minPotential = min(minPotential, topicPotential.topicPotential)

        val A = maxPotential
        val B = (minPotential - A) / Constants.T.pow(m)

        collector.emit(Values(Date(topicPotential.time), Constants.KAFKA_TOPIC, topicPotential.topicPotential, A, B))
    }

    override fun declareOutputFields(declarer: OutputFieldsDeclarer) {
        declarer.declare(Fields("time", "topicName", "topicPotential", "A", "B"))
    }

}