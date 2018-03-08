package stormTweetAnalyzer.Bolt

import org.apache.storm.task.OutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichBolt
import org.apache.storm.tuple.Fields
import org.apache.storm.tuple.Tuple
import utils.Constants

/**
 * Created by elson on 8/3/18.
 */
class TopicPotentialBolt : BaseRichBolt(){


    // To output tuples from this bolt to the count bolt
    private var collector: OutputCollector? = null

    override fun prepare(map: MutableMap<Any?, Any?>, topologyContext: TopologyContext,
                         outputCollector: OutputCollector) {
        collector = outputCollector
    }

    override fun execute(tuple: Tuple) {
        TODO("Topic potential")
    }

    override fun declareOutputFields(declarer: OutputFieldsDeclarer) {
        declarer.declare(Fields(Constants.EMITTED_TUPLE_NAMES.TOPIC_POTENTIAL))
    }

}