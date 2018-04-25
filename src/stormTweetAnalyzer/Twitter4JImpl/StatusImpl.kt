package stormTweetAnalyzer.Twitter4JImpl

import twitter4j.*
import java.util.*

/**
 * Created by elson on 21/3/18.
 */
class StatusImpl : Status {

    private var createdAt: Date? = null
    private var text: String? = null
    private var favoriteCount: Int = 0
    private var retweetCount: Int = 0
    private var user: UserImpl? = null

    override fun getUserMentionEntities(): Array<UserMentionEntity>? {
        return null
    }

    override fun getContributors(): LongArray? {
        return null
    }

    override fun isFavorited(): Boolean {
        return favoriteCount > 0
    }

    override fun getInReplyToScreenName(): String? {
        return null
    }

    override fun getGeoLocation(): GeoLocation? {
        return null
    }

    override fun getSource(): String? {
        return null
    }

    override fun getInReplyToStatusId(): Long {
        return -1
    }

    override fun getId(): Long {
        return -1
    }

    override fun getWithheldInCountries(): Array<String>? {
        return null
    }

    override fun getCurrentUserRetweetId(): Long {
        return -1
    }

    override fun getSymbolEntities(): Array<SymbolEntity>? {
        return null
    }

    override fun getText(): String? {
        return text
    }

    override fun getAccessLevel(): Int {
        return -1
    }

    override fun getInReplyToUserId(): Long {
        return -1
    }

    override fun getMediaEntities(): Array<MediaEntity>? {
        return null
    }

    override fun getPlace(): Place? {
        return null
    }

    override fun isRetweetedByMe(): Boolean {
        return false
    }

    override fun getUser(): User? {
        return user
    }

    override fun getURLEntities(): Array<URLEntity>? {
        return null
    }

    override fun isRetweeted(): Boolean {
        return retweetCount > 0
    }

    override fun getLang(): String {
        return "en"
    }

    override fun getQuotedStatus(): Status {
        return this
    }

    override fun getRateLimitStatus(): RateLimitStatus? {
        return null
    }

    override fun compareTo(other: Status?): Int {
        return -1
    }

    override fun getQuotedStatusId(): Long {
        return -1
    }

    override fun isRetweet(): Boolean {
        return retweetCount > 0
    }

    override fun getRetweetedStatus(): Status {
        return this
    }

    override fun getFavoriteCount(): Int {
        return favoriteCount
    }

    override fun isPossiblySensitive(): Boolean {
        return false
    }

    override fun getScopes(): Scopes? {
        return null
    }

    override fun isTruncated(): Boolean {
        return false
    }

    override fun getCreatedAt(): Date? {
        return createdAt
    }

    override fun getExtendedMediaEntities(): Array<ExtendedMediaEntity>? {
        return null
    }

    override fun getHashtagEntities(): Array<HashtagEntity>? {
        return null
    }

    override fun getRetweetCount(): Int {
        return retweetCount
    }
}