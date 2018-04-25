package stormTweetAnalyzer.Twitter4JImpl

import twitter4j.GeoLocation
import twitter4j.Place
import twitter4j.RateLimitStatus
import java.util.*

class PlaceImpl(private var name: String? = null,
                private var streetAddress: String? = null,
                private var countryCode: String? = null,
                private var id: String? = null,
                private var country: String? = null,
                private var placeType: String? = null,
                private var url: String? = null,
                private var fullName: String? = null,
                private var boundingBoxType: String? = null,
                private var boundingBoxCoordinates: Array<Array<GeoLocation>>? = null,
                private var geometryType: String? = null,
                private var geometryCoordinates: Array<Array<GeoLocation>>? = null,
                private var containedWithIn: Array<PlaceImpl>? = null) : Place {

    override operator fun compareTo(other: Place): Int {
        return this.id!!.compareTo(other.id)
    }

    override fun getName(): String? {
        return this.name
    }

    override fun getStreetAddress(): String? {
        return this.streetAddress
    }

    override fun getCountryCode(): String? {
        return this.countryCode
    }

    override fun getId(): String? {
        return this.id
    }

    override fun getCountry(): String? {
        return this.country
    }

    override fun getPlaceType(): String? {
        return this.placeType
    }

    override fun getURL(): String? {
        return this.url
    }

    override fun getFullName(): String? {
        return this.fullName
    }

    override fun getBoundingBoxType(): String? {
        return this.boundingBoxType
    }

    override fun getBoundingBoxCoordinates(): Array<Array<GeoLocation>>? {
        return this.boundingBoxCoordinates
    }

    override fun getGeometryType(): String? {
        return this.geometryType
    }

    override fun getGeometryCoordinates(): Array<Array<GeoLocation>>? {
        return this.geometryCoordinates
    }

    override fun getContainedWithIn(): Array<Place>? {
        return this.containedWithIn as Array<Place>
    }

    override fun equals(other: Any?): Boolean {
        return if (null == other) false
        else if (this === other) true
        else other is Place && other.id == this.id
    }

    override fun hashCode(): Int {
        return this.id!!.hashCode()
    }

    override fun toString(): String {
        return "PlaceJSONImpl{name='" + this.name + '\''.toString() + ", streetAddress='" +
                this.streetAddress + '\''.toString() + ", countryCode='" + this.countryCode +
                '\''.toString() + ", id='" + this.id + '\''.toString() + ", country='" +
                this.country + '\''.toString() + ", placeType='" + this.placeType +
                '\''.toString() + ", url='" + this.url + '\''.toString() + ", fullName='" +
                this.fullName + '\''.toString() + ", boundingBoxType='" +
                this.boundingBoxType + '\''.toString() + ", boundingBoxCoordinates=" +
                (if (this.boundingBoxCoordinates == null) null
                else Arrays.asList(*this.boundingBoxCoordinates!!)) + ", geometryType='" +
                this.geometryType + '\''.toString() + ", geometryCoordinates=" +
                (if (this.geometryCoordinates == null) null
                else Arrays.asList(*this.geometryCoordinates!!)) + ", containedWithIn=" +
                (if (this.containedWithIn == null) null
                else Arrays.asList(*this.containedWithIn!!)) + '}'.toString()
    }

    override fun getRateLimitStatus(): RateLimitStatus? {
        return null
    }

    override fun getAccessLevel(): Int {
        return -1
    }
}

