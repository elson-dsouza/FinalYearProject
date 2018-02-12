package tweet4analysis.Model

import twitter4j.Status

/**
 * Created by elson on 12/2/18.
 */
class Tweet(val status: Status, val preProcessedTweet: String, val sentiment: Float)