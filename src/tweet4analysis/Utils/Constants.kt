package tweet4analysis.Utils

/**
 * Created by elson on 11/2/18.
 */

object Constants {

    //Name of the Topology. Used while launching the LocalCluster
    const val TOPOLOGY_NAME = "SentimentAnalysis"

    //Properties file which has all the configurable parameters required for execution of this Topology.
    const val CONFIG_PROPERTIES_FILE = "config.properties"

    /**
     * In order to create the spout, you need to get twitter credentials
     * If you need to use Twitter Firehose / Tweet stream for your idea,
     * create a set of credentials by following the instructions at
     * https://dev.twitter.com/discussions/631
     */
    const val OAUTH_ACCESS_TOKEN = "707193271085195264-fq4yVwn1zvJPxKBlxjOUAaiVkXFp5ew"
    const val OAUTH_ACCESS_TOKEN_SECRET = "VsZnHrtCQVWr3UZHxB6eWrn4u4H1Ptl7NM0BEIc1M0G5J"
    const val OAUTH_CONSUMER_KEY = "SHo4lHNWxFvRFMs4XRoFFHXqi"
    const val OAUTH_CONSUMER_SECRET = "R3ZX67z2nnPkiPv7TQ45Ajxx6foN2LmnuMhfCfBR8v0IY3fclY"

    //Sentiment scores of few words are present in this file.
    //For more info on this, please check:
    // http://www2.imm.dtu.dk/pubdb/views/publication_details.php?id=6010
    const val AFINN_SENTIMENT_FILE_NAME = "AFINN/AFINN-111.txt"

    /**
     * Max allowed question marks in a string.
     * Beyond this value the affect of the Question marks will be considered the same.
     */
    const val MAX_QUESTION_MARKS = 3

    //Window size for preceding trigram.
    const val PRECEDING_TRIGRAM_WINDOW = 3

    //Window size for preceding bigram.
    const val PRECEDING_BIGRAM_WINDOW = 2

    //Window size for preceding unigram.
    const val PRECEDING_UNIGRAM_WINDOW = 1

    //Maximum number for exclamation marks that could be processed.
    const val MAX_EXCLAMATION_MARKS = 4

    /**
     * This is the window size within which processing will be done.
     * This means that we would be dealing only with unigrams, bigrams and
     * trigrams.
     */
    const val MAX_GRAM_WINDOW_SIZE = 3

    //This alpha approximates the max expected value for a sentiment score.
    const val DEFAULT_ALPHA = 15.0f

    //This regex checks if a string has only alphabets and no special characters or numbers.
    const val NON_NUMERIC_STRING_REGEX = ".*[a-zA-Z]+.*"

    //This string defines the prefix for a string that has a URL.
    const val URL_PREFIX = "http://"

    //This string defines the path to the VADER lexicon file need for Sentiment Analysis
    const val VADER_FILE: String = "VADER/lexicon.txt"

    //This object holds the list of Emitted Tuple names
    object EMITTED_TUPLES {
        const val RAW_TWEET = "raw-tweet"
        const val TWEET_WITH_SENTIMENT = "tweet-with-sentiment"
    }

    //This object holds the spout and bolt names
    object BOLT_NAMES {
        const val TWEET_SPOUT = "tweet-spout"
        const val TWEET_SENTIMENT_BOLT = "tweet-sentiment-bolt"
    }

    //This is the list of all languages whose tweets we should obtain
    @JvmField
    val LANGUAGES = arrayOf("en")

    //This is the list of all positive topics with respect to the event
    @JvmField
    val POSITIVE_TOPICS = arrayOf("modi", "bjp")

    //This is the list of all negative topics with respect to the event
    @JvmField
    val NEGATIVE_TOPICS = arrayOf("congress", "jds")

    //This is the list of all topics with respect to the event
    @JvmStatic
    fun ALL_TOPICS(): Array<String> {
        return POSITIVE_TOPICS + NEGATIVE_TOPICS
    }
}