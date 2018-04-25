package stormTweetAnalyzer.Twitter4JImpl

import twitter4j.*
import java.util.*

/**
 * Created by elson on 21/3/18.
 */
class StatusImpl(private var createdAt: Date? = null,
                 private var text: String? = null,
                 private var favoriteCount: Int = 0,
                 private var retweetCount: Int = 0,
                 private var user: UserImpl? = null,
                 private var id: Long = 0,
                 private var source: String? = null,
                 private var isTruncated: Boolean = false,
                 private var inReplyToStatusId: Long = 0,
                 private var inReplyToUserId: Long = 0,
                 private var isFavorited: Boolean = false,
                 private var isRetweeted: Boolean = false,
                 private var inReplyToScreenName: String? = null,
                 private var geoLocation: GeoLocation? = null,
                 private var place: PlaceImpl? = null,
                 private var isPossiblySensitive: Boolean = false,
                 private var lang: String? = null,
                 private var contributorsIDs: LongArray? = null,
                 private var retweetedStatus: StatusImpl? = null,
                 private var currentUserRetweetId: Long = -1L,
                 private var withheldInCountries: Array<String>? = null,
                 private var quotedStatus: StatusImpl? = null,
                 private var quotedStatusId: Long = -1L) : Status {

    override fun compareTo(other: Status): Int {
        return 0
    }

    override fun getCreatedAt(): Date? {
        return this.createdAt
    }

    override fun getId(): Long {
        return this.id
    }

    override fun getText(): String? {
        return this.text
    }

    override fun getSource(): String? {
        return this.source
    }

    override fun isTruncated(): Boolean {
        return this.isTruncated
    }

    override fun getInReplyToStatusId(): Long {
        return this.inReplyToStatusId
    }

    override fun getInReplyToUserId(): Long {
        return this.inReplyToUserId
    }

    override fun getInReplyToScreenName(): String? {
        return this.inReplyToScreenName
    }

    override fun getGeoLocation(): GeoLocation? {
        return this.geoLocation
    }

    override fun getPlace(): Place? {
        return this.place
    }

    override fun getContributors(): LongArray? {
        return this.contributorsIDs
    }

    override fun isFavorited(): Boolean {
        return this.isFavorited
    }

    override fun isRetweeted(): Boolean {
        return this.isRetweeted
    }

    override fun getFavoriteCount(): Int {
        return this.favoriteCount
    }

    override fun getUser(): User? {
        return this.user
    }

    override fun isRetweet(): Boolean {
        return this.retweetedStatus != null
    }

    override fun getRetweetedStatus(): Status? {
        return this.retweetedStatus
    }

    override fun getRetweetCount(): Int {
        return this.retweetCount.toInt()
    }

    override fun isRetweetedByMe(): Boolean {
        return this.currentUserRetweetId != -1L
    }

    override fun getCurrentUserRetweetId(): Long {
        return this.currentUserRetweetId
    }

    override fun isPossiblySensitive(): Boolean {
        return this.isPossiblySensitive
    }

    override fun getURLEntities(): Array<URLEntity>? {
        return null
    }

    override fun getWithheldInCountries(): Array<String>? {
        return this.withheldInCountries
    }

    override fun getQuotedStatusId(): Long {
        return this.quotedStatusId
    }

    override fun getQuotedStatus(): Status? {
        return this.quotedStatus
    }

    override fun getAccessLevel(): Int {
        return -1
    }

    override fun getLang(): String? {
        return this.lang
    }

    override fun getRateLimitStatus(): RateLimitStatus? {
        return null
    }

    override fun hashCode(): Int {
        return this.id.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other) false
        else if (this === other) true
        else other is Status && other.id == this.id
    }

    override fun toString(): String {
        return "StatusImpl{createdAt=" + this.createdAt +
                ", id=" + this.id + ", text='" + this.text + '\''.toString() +
                ", source='" + this.source + '\''.toString() + ", isTruncated=" +
                this.isTruncated + ", inReplyToStatusId=" + this.inReplyToStatusId +
                ", inReplyToUserId=" + this.inReplyToUserId + ", isFavorited=" +
                this.isFavorited + ", isRetweeted=" + this.isRetweeted + ", favoriteCount=" +
                this.favoriteCount + ", inReplyToScreenName='" + this.inReplyToScreenName +
                '\''.toString() + ", geoLocation=" + this.geoLocation + ", place=" +
                this.place + ", retweetCount=" + this.retweetCount +
                ", isPossiblySensitive=" + this.isPossiblySensitive + ", lang='" +
                this.lang + '\''.toString() + ", contributorsIDs=" +
                Arrays.toString(this.contributorsIDs) + ", retweetedStatus=" +
                this.retweetedStatus + ", userMentionEntities=" +
                Arrays.toString(this.userMentionEntities) + ", urlEntities=" +
                Arrays.toString(this.urlEntities) + ", hashtagEntities=" +
                Arrays.toString(this.hashtagEntities) + ", mediaEntities=" +
                Arrays.toString(this.mediaEntities) + ", symbolEntities=" +
                Arrays.toString(this.symbolEntities) + ", currentUserRetweetId=" +
                this.currentUserRetweetId + ", user=" + this.user + ", withHeldInCountries=" +
                Arrays.toString(this.withheldInCountries) + ", quotedStatusId=" +
                this.quotedStatusId + ", quotedStatus=" + this.quotedStatus + '}'.toString()
    }

    override fun getUserMentionEntities(): Array<UserMentionEntity>? {
        return null
    }

    override fun getSymbolEntities(): Array<SymbolEntity>? {
        return null
    }

    override fun getMediaEntities(): Array<MediaEntity>? {
        return null
    }

    override fun getScopes(): Scopes? {
        return null
    }

    override fun getExtendedMediaEntities(): Array<ExtendedMediaEntity>? {
        return null
    }

    override fun getHashtagEntities(): Array<HashtagEntity>? {
        return null
    }
}