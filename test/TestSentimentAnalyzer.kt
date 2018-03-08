import org.junit.Before
import org.junit.Test
import stormTweetAnalyzer.SentimentAnalyzer.SentimentAnalyzer

/**
 * Created by elson on 12/2/18.
 */
class TestSentimentAnalyzer {
    lateinit var sentimentAnalyzer: SentimentAnalyzer

    @Before
    fun setup() {
        sentimentAnalyzer = SentimentAnalyzer()
    }

    @Test
    fun test() {
        //TODO: Write some unit tests
        System.out.println(sentimentAnalyzer.analyze("This is :)"))
        System.out.println(sentimentAnalyzer.analyze("This is horrific"))

        System.out.println(sentimentAnalyzer.analyze(
                "RT @siddaramaiah: I am shocked by the vandalism displayed by BJP " +
                        "workers in desecrating #PeriyarStatue . Why is the @BJP4India afraid of " +
                        "th…"))
    }
}