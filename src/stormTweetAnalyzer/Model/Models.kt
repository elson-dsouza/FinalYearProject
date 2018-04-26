package stormTweetAnalyzer.Model

import twitter4j.Status
import java.util.*

/**
 * Created by elson on 12/2/18.
 */
class TweetPotential(val status: Status, val sentiment: Float)

class TopicPotential(val time: Date, val topicPotential: Double)
class TweetDBModel(val tweet: String, val tweetPotential: Float)
class TopicDBModel(val time: Long, val topicPotential: Double, val a: Double, val b: Double)