package stormTweetAnalyzer.Twitter4JImpl

import twitter4j.RateLimitStatus
import twitter4j.Status
import twitter4j.URLEntity
import twitter4j.User
import java.util.*

class UserImpl : User {

    private var id: Long = 0
    private var name: String? = null
    private var screenName: String? = null
    private var location: String? = null
    private var description: String? = null
    private var isContributorsEnabled: Boolean = false
    private var profileImageUrl: String? = null
    private var profileImageUrlHttps: String? = null
    private var isDefaultProfileImage: Boolean = false
    private var url: String? = null
    private var isProtected: Boolean = false
    private var followersCount: Int = 0
    private var status: StatusImpl? = null
    private var profileBackgroundColor: String? = null
    private var profileTextColor: String? = null
    private var profileLinkColor: String? = null
    private var profileSidebarFillColor: String? = null
    private var profileSidebarBorderColor: String? = null
    private var profileUseBackgroundImage: Boolean = false
    private var isDefaultProfile: Boolean = false
    private var showAllInlineMedia: Boolean = false
    private var friendsCount: Int = 0
    private var createdAt: Date? = null
    private var favouritesCount: Int = 0
    private var utcOffset: Int = 0
    private var timeZone: String? = null
    private var profileBackgroundImageUrl: String? = null
    private var profileBackgroundImageUrlHttps: String? = null
    private var profileBannerImageUrl: String? = null
    private var profileBackgroundTiled: Boolean = false
    private var lang: String? = null
    private var statusesCount: Int = 0
    private var isGeoEnabled: Boolean = false
    private var isVerified: Boolean = false
    private var translator: Boolean = false
    private var listedCount: Int = 0
    private var isFollowRequestSent: Boolean = false
    private var withheldInCountries: Array<String>? = null

    override fun compareTo(other: User): Int {
        return (this.id - other.id).toInt()
    }

    override fun getId(): Long {
        return this.id
    }

    override fun getName(): String? {
        return this.name
    }

    override fun getScreenName(): String? {
        return this.screenName
    }

    override fun getLocation(): String? {
        return this.location
    }

    override fun getDescription(): String? {
        return this.description
    }

    override fun isContributorsEnabled(): Boolean {
        return this.isContributorsEnabled
    }

    override fun getProfileImageURL(): String? {
        return this.profileImageUrl
    }

    override fun getBiggerProfileImageURL(): String? {
        return this.toResizedURL(this.profileImageUrl, "_bigger")
    }

    override fun getMiniProfileImageURL(): String? {
        return this.toResizedURL(this.profileImageUrl, "_mini")
    }

    override fun getOriginalProfileImageURL(): String? {
        return this.toResizedURL(this.profileImageUrl, "")
    }

    private fun toResizedURL(originalURL: String?, sizeSuffix: String): String? {
        if (null != originalURL) {
            val index = originalURL.lastIndexOf("_")
            val suffixIndex = originalURL.lastIndexOf(".")
            val slashIndex = originalURL.lastIndexOf("/")
            var url = originalURL.substring(0, index) + sizeSuffix
            if (suffixIndex > slashIndex) {
                url = url + originalURL.substring(suffixIndex)
            }

            return url
        } else {
            return null
        }
    }

    override fun getProfileImageURLHttps(): String? {
        return this.profileImageUrlHttps
    }

    override fun getBiggerProfileImageURLHttps(): String? {
        return this.toResizedURL(this.profileImageUrlHttps, "_bigger")
    }

    override fun getMiniProfileImageURLHttps(): String? {
        return this.toResizedURL(this.profileImageUrlHttps, "_mini")
    }

    override fun getOriginalProfileImageURLHttps(): String? {
        return this.toResizedURL(this.profileImageUrlHttps, "")
    }

    override fun isDefaultProfileImage(): Boolean {
        return this.isDefaultProfileImage
    }

    override fun getURL(): String? {
        return this.url
    }

    override fun isProtected(): Boolean {
        return this.isProtected
    }

    override fun getFollowersCount(): Int {
        return this.followersCount
    }

    override fun getProfileBackgroundColor(): String? {
        return this.profileBackgroundColor
    }

    override fun getProfileTextColor(): String? {
        return this.profileTextColor
    }

    override fun getProfileLinkColor(): String? {
        return this.profileLinkColor
    }

    override fun getProfileSidebarFillColor(): String? {
        return this.profileSidebarFillColor
    }

    override fun getProfileSidebarBorderColor(): String? {
        return this.profileSidebarBorderColor
    }

    override fun isProfileUseBackgroundImage(): Boolean {
        return this.profileUseBackgroundImage
    }

    override fun isDefaultProfile(): Boolean {
        return this.isDefaultProfile
    }

    override fun isShowAllInlineMedia(): Boolean {
        return this.showAllInlineMedia
    }

    override fun getFriendsCount(): Int {
        return this.friendsCount
    }

    override fun getStatus(): Status? {
        return this.status
    }

    override fun getCreatedAt(): Date? {
        return this.createdAt
    }

    override fun getFavouritesCount(): Int {
        return this.favouritesCount
    }

    override fun getUtcOffset(): Int {
        return this.utcOffset
    }

    override fun getTimeZone(): String? {
        return this.timeZone
    }

    override fun getProfileBackgroundImageURL(): String? {
        return this.profileBackgroundImageUrl
    }

    override fun getProfileBackgroundImageUrlHttps(): String? {
        return this.profileBackgroundImageUrlHttps
    }

    override fun getProfileBannerURL(): String? {
        return if (this.profileBannerImageUrl != null) this.profileBannerImageUrl!! + "/web"
        else null
    }

    override fun getProfileBannerRetinaURL(): String? {
        return if (this.profileBannerImageUrl != null)
            this.profileBannerImageUrl!! + "/web_retina"
        else null
    }

    override fun getProfileBannerIPadURL(): String? {
        return if (this.profileBannerImageUrl != null)
            this.profileBannerImageUrl!! + "/ipad"
        else null
    }

    override fun getProfileBannerIPadRetinaURL(): String? {
        return if (this.profileBannerImageUrl != null)
            this.profileBannerImageUrl!! + "/ipad_retina"
        else null
    }

    override fun getProfileBannerMobileURL(): String? {
        return if (this.profileBannerImageUrl != null)
            this.profileBannerImageUrl!! + "/mobile"
        else null
    }

    override fun getProfileBannerMobileRetinaURL(): String? {
        return if (this.profileBannerImageUrl != null) this.profileBannerImageUrl!! + "/mobile_retina" else null
    }

    override fun isProfileBackgroundTiled(): Boolean {
        return this.profileBackgroundTiled
    }

    override fun getLang(): String? {
        return this.lang
    }

    override fun getStatusesCount(): Int {
        return this.statusesCount
    }

    override fun isGeoEnabled(): Boolean {
        return this.isGeoEnabled
    }

    override fun isVerified(): Boolean {
        return this.isVerified
    }

    override fun isTranslator(): Boolean {
        return this.translator
    }

    override fun getListedCount(): Int {
        return this.listedCount
    }

    override fun isFollowRequestSent(): Boolean {
        return this.isFollowRequestSent
    }

    override fun getDescriptionURLEntities(): Array<URLEntity>? {
        return this.descriptionURLEntities
    }

    override fun getWithheldInCountries(): Array<String>? {
        return this.withheldInCountries
    }

    override fun hashCode(): Int {
        return this.id.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other) false
        else if (this === other) true
        else other is User && other.id == this.id
    }

    override fun toString(): String {
        return "UserImpl{id=" + this.id + ", name='" + this.name + '\''.toString() +
                ", screenName='" + this.screenName + '\''.toString() + ", location='" +
                this.location + '\''.toString() + ", description='" + this.description +
                '\''.toString() + ", isContributorsEnabled=" + this.isContributorsEnabled +
                ", profileImageUrl='" + this.profileImageUrl + '\''.toString() +
                ", profileImageUrlHttps='" + this.profileImageUrlHttps + '\''.toString() +
                ", isDefaultProfileImage=" + this.isDefaultProfileImage + ", url='" +
                this.url + '\''.toString() + ", isProtected=" + this.isProtected +
                ", followersCount=" + this.followersCount + ", status=" + this.status +
                ", profileBackgroundColor='" + this.profileBackgroundColor +
                '\''.toString() + ", profileTextColor='" + this.profileTextColor +
                '\''.toString() + ", profileLinkColor='" + this.profileLinkColor +
                '\''.toString() + ", profileSidebarFillColor='" +
                this.profileSidebarFillColor + '\''.toString() +
                ", profileSidebarBorderColor='" + this.profileSidebarBorderColor +
                '\''.toString() + ", profileUseBackgroundImage=" +
                this.profileUseBackgroundImage + ", isDefaultProfile=" +
                this.isDefaultProfile + ", showAllInlineMedia=" +
                this.showAllInlineMedia + ", friendsCount=" + this.friendsCount +
                ", createdAt=" + this.createdAt + ", favouritesCount=" +
                this.favouritesCount + ", utcOffset=" + this.utcOffset +
                ", timeZone='" + this.timeZone + '\''.toString() +
                ", profileBackgroundImageUrl='" +
                this.profileBackgroundImageUrl + '\''.toString() +
                ", profileBackgroundImageUrlHttps='" + this.profileBackgroundImageUrlHttps +
                '\''.toString() + ", profileBackgroundTiled=" +
                this.profileBackgroundTiled + ", lang='" + this.lang +
                '\''.toString() + ", statusesCount=" + this.statusesCount +
                ", isGeoEnabled=" + this.isGeoEnabled + ", isVerified=" +
                this.isVerified + ", translator=" + this.translator + ", listedCount=" +
                this.listedCount + ", isFollowRequestSent=" + this.isFollowRequestSent +
                ", withheldInCountries=" + Arrays.toString(this.withheldInCountries) +
                '}'.toString()
    }

    override fun getRateLimitStatus(): RateLimitStatus? {
        return null
    }

    override fun getAccessLevel(): Int {
        return -1
    }

    override fun getURLEntity(): URLEntity? {
        return null
    }
}