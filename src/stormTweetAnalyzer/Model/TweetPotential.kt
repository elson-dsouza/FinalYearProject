package stormTweetAnalyzer.Model

import twitter4j.Status

/**
 * Created by elson on 12/2/18.
 */
class TweetPotential(val status: Status, val preProcessedTweet: String, val sentiment: Float)