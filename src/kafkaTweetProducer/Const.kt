package kafkaTweetProducer

/**
 * Created by elson on 3/3/18.
 */
object Const {
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
    const val KAFKA_TOPIC = "kafka.twitter.raw.topic"

    @JvmField
    val LANGUAGES = arrayOf("en")
    @JvmField
    val TOPIC = arrayOf("modi", "bjp")
}