package tweet4analysis.Utils;

/**
 * Created by elson on 5/2/18.
 */
public class Constants {

    //Name of the Topology. Used while launching the LocalCluster
    public static final String TOPOLOGY_NAME = "SentimentAnalysis";

    //Properties file which has all the configurable parameters required for execution of this Topology.
    public static final String CONFIG_PROPERTIES_FILE = "config.properties";

    /**
     * In order to create the spout, you need to get twitter credentials
     * If you need to use Twitter Firehose / Tweet stream for your idea,
     * create a set of credentials by following the instructions at
     * https://dev.twitter.com/discussions/631
     **/
    public static final String OAUTH_ACCESS_TOKEN = "707193271085195264-fq4yVwn1zvJPxKBlxjOUAaiVkXFp5ew";
    public static final String OAUTH_ACCESS_TOKEN_SECRET = "VsZnHrtCQVWr3UZHxB6eWrn4u4H1Ptl7NM0BEIc1M0G5J";
    public static final String OAUTH_CONSUMER_KEY = "SHo4lHNWxFvRFMs4XRoFFHXqi";
    public static final String OAUTH_CONSUMER_SECRET = "R3ZX67z2nnPkiPv7TQ45Ajxx6foN2LmnuMhfCfBR8v0IY3fclY";

    //Sentiment scores of few words are present in this file.
    //For more info on this, please check: http://www2.imm.dtu.dk/pubdb/views/publication_details.php?id=6010
    public static final String AFINN_SENTIMENT_FILE_NAME = "AFINN/AFINN-111.txt";
}
