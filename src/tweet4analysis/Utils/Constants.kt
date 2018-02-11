package tweet4analysis.Utils

/**
 * Created by elson on 11/2/18.
 */

object Constants {

    //Name of the Topology. Used while launching the LocalCluster
    @JvmField
    val TOPOLOGY_NAME = "SentimentAnalysis"

    //Properties file which has all the configurable parameters required for execution of this Topology.
    @JvmField
    val CONFIG_PROPERTIES_FILE = "config.properties"

    /**
     * In order to create the spout, you need to get twitter credentials
     * If you need to use Twitter Firehose / Tweet stream for your idea,
     * create a set of credentials by following the instructions at
     * https://dev.twitter.com/discussions/631
     */
    @JvmField
    val OAUTH_ACCESS_TOKEN = "707193271085195264-fq4yVwn1zvJPxKBlxjOUAaiVkXFp5ew"
    @JvmField
    val OAUTH_ACCESS_TOKEN_SECRET = "VsZnHrtCQVWr3UZHxB6eWrn4u4H1Ptl7NM0BEIc1M0G5J"
    @JvmField
    val OAUTH_CONSUMER_KEY = "SHo4lHNWxFvRFMs4XRoFFHXqi"
    @JvmField
    val OAUTH_CONSUMER_SECRET = "R3ZX67z2nnPkiPv7TQ45Ajxx6foN2LmnuMhfCfBR8v0IY3fclY"

    //Sentiment scores of few words are present in this file.
    //For more info on this, please check:
    // http://www2.imm.dtu.dk/pubdb/views/publication_details.php?id=6010
    @JvmField
    val AFINN_SENTIMENT_FILE_NAME = "AFINN/AFINN-111.txt"

    /**
     * Max allowed question marks in a string.
     * Beyond this value the affect of the Question marks will be considered the same.
     */
    @JvmField
    val MAX_QUESTION_MARKS = 3

    //Window size for preceding trigram.
    @JvmField
    val PRECEDING_TRIGRAM_WINDOW = 3

    //Window size for preceding bigram.
    @JvmField
    val PRECEDING_BIGRAM_WINDOW = 2

    //Window size for preceding unigram.
    @JvmField
    val PRECEDING_UNIGRAM_WINDOW = 1

    //Maximum number for exclamation marks that could be processed.
    @JvmField
    val MAX_EXCLAMATION_MARKS = 4

    /**
     * This is the window size within which processing will be done.
     * This means that we would be dealing only with unigrams, bigrams and
     * trigrams.
     */
    @JvmField
    val MAX_GRAM_WINDOW_SIZE = 3

    //This alpha approximates the max expected value for a sentiment score.
    @JvmField
    val DEFAULT_ALPHA = 15.0f

    //This regex checks if a string has only alphabets and no special characters or numbers.
    @JvmField
    val NON_NUMERIC_STRING_REGEX = ".*[a-zA-Z]+.*"

    //This string defines the prefix for a string that has a URL.
    @JvmField
    val URL_PREFIX = "http://"

    val VADER_FILE: String = "VADER/lexicon.txt"
}