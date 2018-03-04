package stormTweetAnalyzer.Utils

/**
 * Created by elson on 11/2/18.
 */

enum class Valence(val value: Float) {
    //This denotes the default valence for token that boost.
    DEFAULT_BOOSTING(0.293f),

    //This denotes the default valence for token that damp.
    DEFAULT_DAMPING(-0.293f),

    //Boosting factor for strings having a '?'.
    ALL_CAPS_FACTOR(0.733f),

    //If a negative word is encountered, its valence is reduced by this factor.
    NEGATIVE_WORD_DAMPING_FACTOR(-0.74f),

    //Boosting factor for strings having a '!'.
    EXCLAMATION_BOOSTING(0.292f),

    //Boosting factor for strings having a '?'.
    QUESTION_MARK_BOOSTING(0.96f),

    //Boosting factor for strings having 3 or more '?'s.
    QUESTION_MARK_MAX_COUNT_BOOSTING(0.18f),

    //If the preceding trigram has a "never" type phrase, increase the negative valence by 25%.
    PRECEDING_TRIGRAM_HAVING_NEVER_DAMPING_FACTOR(1.25f),

    //If the preceding bigram has a "never" type phrase, increase the negative valence by 50%.
    PRECEDING_BIGRAM_HAVING_NEVER_DAMPING_FACTOR(1.5f),

    //At distance of 1 from current token, reduce current gram's valence by 5%.
    ONE_WORD_DISTANCE_DAMPING_FACTOR(0.95f),

    //At distance of 2 from current token, reduce current gram's valence by 10%.
    TWO_WORD_DISTANCE_DAMPING_FACTOR(0.9f),

    //If the conjunction is after the current token then reduce valence by 50%.
    PRE_CONJUNCTION_ADJUSTMENT_FACTOR(0.5f),

    //If the conjunction is before the current token then increase valence by 50%.
    POST_CONJUNCTION_ADJUSTMENT_FACTOR(1.5f)
}