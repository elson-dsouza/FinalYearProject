package stormTweetAnalyzer.Twitter4JImpl

import twitter4j.RateLimitStatus
import twitter4j.Status
import twitter4j.URLEntity
import twitter4j.User
import java.util.*

class UserImpl : User {

    private var name: String? = null

    override fun compareTo(other: User): Int {
        return (this.id - other.id).toInt()
    }

    override fun getId(): Long {
        return -1
    }

    override fun getName(): String? {
        return this.name
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun getLocation(): String? {
        return null
    }

    override fun getDescription(): String? {
        return null
    }

    override fun isContributorsEnabled(): Boolean {
        return false
    }

    override fun getProfileImageURL(): String? {
        return null
    }

    override fun getBiggerProfileImageURL(): String? {
        return null
    }

    override fun getMiniProfileImageURL(): String? {
        return null
    }

    override fun getOriginalProfileImageURL(): String? {
        return null
    }

    override fun getProfileImageURLHttps(): String? {
        return null
    }

    override fun getBiggerProfileImageURLHttps(): String? {
        return null
    }

    override fun getMiniProfileImageURLHttps(): String? {
        return null
    }

    override fun getOriginalProfileImageURLHttps(): String? {
        return null
    }

    override fun isDefaultProfileImage(): Boolean {
        return false
    }

    override fun getURL(): String? {
        return null
    }

    override fun isProtected(): Boolean {
        return false
    }

    override fun getFollowersCount(): Int {
        return -1
    }

    override fun getProfileBackgroundColor(): String? {
        return null
    }

    override fun getProfileTextColor(): String? {
        return null
    }

    override fun getProfileLinkColor(): String? {
        return null
    }

    override fun getProfileSidebarFillColor(): String? {
        return null
    }

    override fun getProfileSidebarBorderColor(): String? {
        return null
    }

    override fun isProfileUseBackgroundImage(): Boolean {
        return false
    }

    override fun isDefaultProfile(): Boolean {
        return false
    }

    override fun isShowAllInlineMedia(): Boolean {
        return false
    }

    override fun getFriendsCount(): Int {
        return -1
    }

    override fun getStatus(): Status? {
        return null
    }

    override fun getCreatedAt(): Date? {
        return null
    }

    override fun getFavouritesCount(): Int {
        return -1
    }

    override fun getUtcOffset(): Int {
        return 0
    }

    override fun getTimeZone(): String? {
        return null
    }

    override fun getProfileBackgroundImageURL(): String? {
        return null
    }

    override fun getProfileBackgroundImageUrlHttps(): String? {
        return null
    }

    override fun getProfileBannerURL(): String? {
        return null
    }

    override fun getProfileBannerRetinaURL(): String? {
        return null
    }

    override fun getProfileBannerIPadURL(): String? {
        return null
    }

    override fun getProfileBannerIPadRetinaURL(): String? {
        return null
    }

    override fun getProfileBannerMobileURL(): String? {
        return null
    }

    override fun getProfileBannerMobileRetinaURL(): String? {
        return null
    }

    override fun isProfileBackgroundTiled(): Boolean {
        return false
    }

    override fun getLang(): String? {
        return "en"
    }

    override fun getStatusesCount(): Int {
        return -1
    }

    override fun isGeoEnabled(): Boolean {
        return false
    }

    override fun isVerified(): Boolean {
        return false
    }

    override fun isTranslator(): Boolean {
        return false
    }

    override fun getListedCount(): Int {
        return -1
    }

    override fun isFollowRequestSent(): Boolean {
        return false
    }

    override fun getDescriptionURLEntities(): Array<URLEntity>? {
        return null
    }

    override fun getWithheldInCountries(): Array<String>? {
        return null
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