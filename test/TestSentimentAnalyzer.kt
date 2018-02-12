import org.junit.Assert
import org.junit.Before
import org.junit.Test
import tweet4analysis.SentimentAnalyzer.SentimentAnalyzer

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
        Assert.assertTrue(sentimentAnalyzer.analyze("This is :)") > 0)
        Assert.assertTrue(sentimentAnalyzer.analyze("This is horrific") < 0)
        Assert.assertTrue(sentimentAnalyzer.analyze("Indian team on fire!! boys playing well") > 0)
    }
}