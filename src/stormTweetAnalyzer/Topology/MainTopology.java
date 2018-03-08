package stormTweetAnalyzer.Topology;

import Utils.Constants;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;
import stormTweetAnalyzer.Bolt.ParseTweetBolt;
import stormTweetAnalyzer.Bolt.SentimentCalculatorBolt;

public class MainTopology {

    public static void main(String[] args) {

        ZkHosts zookeeperHosts = new ZkHosts(Constants.ZOOKEEPER_HOST);
        SpoutConfig kafkaConfig = new SpoutConfig(zookeeperHosts, Constants.KAFKA_TOPIC
                , "", Constants.BOLT_NAMES.TWEET_KAFKA_SPOUT);

        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme() {
            @Override
            public Fields getOutputFields() {
                return new Fields(Constants.EMITTED_TUPLES.TWEET_JSON);
            }
        });

        // create the topology
        TopologyBuilder builder = new TopologyBuilder();
        // now create the tweet spout with the credentials and
        // attach the tweet spout to the topology - parallelism of 1
        builder.setSpout(Constants.BOLT_NAMES.TWEET_KAFKA_SPOUT, new KafkaSpout(kafkaConfig),
                8);
        builder.setBolt(Constants.BOLT_NAMES.TWEET_PARSER_BOLT, new ParseTweetBolt(),
                8).shuffleGrouping(Constants.BOLT_NAMES.TWEET_KAFKA_SPOUT);
        builder.setBolt(Constants.BOLT_NAMES.TWEET_SENTIMENT_BOLT, new SentimentCalculatorBolt(),
                8).shuffleGrouping(Constants.BOLT_NAMES.TWEET_PARSER_BOLT);

        // create the default config object
        Config conf = new Config();
        // set the config in debugging mode
        conf.setDebug(true);

        if (args != null && args.length > 0) {

            // run it in a live cluster
            // set the number of workers for running all spout and bolt tasks

            conf.setNumWorkers(3);

            // create the topology and submit with config
            try {
                StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
            } catch (AlreadyAliveException | InvalidTopologyException | AuthorizationException e) {
                e.printStackTrace();
            }
        }
        else {
            // run it in a simulated local cluster

            // set the number of threads to run - similar to setting number of workers in live cluster
            conf.setMaxTaskParallelism(3);

            // create the local cluster instance
            LocalCluster cluster = new LocalCluster();

            // submit the topology to the local cluster
            cluster.submitTopology(Constants.TOPOLOGY_NAME, conf, builder.createTopology());

            // let the topology run for 1 hour. note topologies never terminate!
            Utils.sleep(3600000);

            // now kill the topology
            cluster.killTopology(Constants.TOPOLOGY_NAME);

            // we are done, so shutdown the local cluster
            cluster.shutdown();
        }
    }
}
