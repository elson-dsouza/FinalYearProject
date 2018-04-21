package stormTweetAnalyzer.Topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.cassandra.bolt.CassandraWriterBolt;
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
import stormTweetAnalyzer.Bolt.BFunctionBolt;
import stormTweetAnalyzer.Bolt.ParseTweetBolt;
import stormTweetAnalyzer.Bolt.TopicPotentialBolt;
import stormTweetAnalyzer.Bolt.TweetPotentialBolt;
import utils.Constants;

import static org.apache.storm.cassandra.DynamicStatementBuilder.*;

public class MainTopology {

    public static void main(String[] args) {

        ZkHosts zookeeperHosts = new ZkHosts(Constants.ZOOKEEPER_HOST);
        SpoutConfig kafkaConfig = new SpoutConfig(zookeeperHosts, Constants.KAFKA_TOPIC
                , "", Constants.BOLT_OR_SPOUT_NAMES.TWEET_KAFKA_SPOUT);

        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme() {
            @Override
            public Fields getOutputFields() {
                return new Fields(Constants.EMITTED_TUPLE_NAMES.TWEET_JSON);
            }
        });

        // Define the topology
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(Constants.BOLT_OR_SPOUT_NAMES.TWEET_KAFKA_SPOUT, new KafkaSpout(kafkaConfig),
                8);
        builder.setBolt(Constants.BOLT_OR_SPOUT_NAMES.TWEET_PARSER_BOLT, new ParseTweetBolt(),
                8).shuffleGrouping(Constants.BOLT_OR_SPOUT_NAMES.TWEET_KAFKA_SPOUT);
        builder.setBolt(Constants.BOLT_OR_SPOUT_NAMES.TWEET_POTENTIAL_BOLT, new TweetPotentialBolt(),
                8).shuffleGrouping(Constants.BOLT_OR_SPOUT_NAMES.TWEET_PARSER_BOLT);
        builder.setBolt(Constants.BOLT_OR_SPOUT_NAMES.TOPIC_POTENTIAL_BOLT, new TopicPotentialBolt(),
                1).globalGrouping(Constants.BOLT_OR_SPOUT_NAMES.TWEET_POTENTIAL_BOLT);
        builder.setBolt(Constants.BOLT_OR_SPOUT_NAMES.B_FUNCTION_BOLT, new BFunctionBolt(),
                1).globalGrouping(Constants.BOLT_OR_SPOUT_NAMES.TOPIC_POTENTIAL_BOLT);
        builder.setBolt(Constants.BOLT_OR_SPOUT_NAMES.REPORTER_BOLT, new CassandraWriterBolt(
                        async(simpleQuery("INSERT INTO TopicSeries(time, topicName, topicPotential, A, B) " +
                                "VALUES (?, ?, ?, ?, ?);").with(all()))), 1)
                .globalGrouping(Constants.BOLT_OR_SPOUT_NAMES.B_FUNCTION_BOLT);


        Config conf = new Config();
        conf.put(Constants.CASSANDRA_HOST, "localhost:9042");
        conf.put(Constants.CASSANDRA_KEYSPACE,"tutorialspoint");
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
            // set the number of threads to run
            conf.setMaxTaskParallelism(3);

            // create the local cluster instance
            LocalCluster cluster = new LocalCluster();

            // submit the topology to the local cluster
            cluster.submitTopology(Constants.TOPOLOGY_NAME, conf, builder.createTopology());

            // let the topology run for 1 hour
            Utils.sleep(3600000);

            // Shutdown the topology
            cluster.killTopology(Constants.TOPOLOGY_NAME);
            cluster.shutdown();
        }
    }
}
