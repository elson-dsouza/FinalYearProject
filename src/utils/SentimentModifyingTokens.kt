package utils

/**
 * This enumeration contains the list of tokens that invert the sentiment of the current token
 *
 * Created by elson on 11/2/18.
 */
enum class SentimentModifyingTokens(val value: String) {
    NEVER("never"),
    SO("so"),
    THIS("this"),
    AT("at"),
    LEAST("least"),
    KIND("kind"),
    OF("of"),
    VERY("very"),
    BUT("but"),
    EXCLAMATION_MARK("!"),
    QUESTION_MARK("?"),
    CONTRACTION("n't")
}