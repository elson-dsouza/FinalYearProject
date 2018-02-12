package tweet4analysis.SentimentAnalyzer

import org.apache.storm.shade.org.apache.commons.lang.StringUtils
import tweet4analysis.Utils.Constants
import tweet4analysis.Utils.SentimentModifyingTokens
import tweet4analysis.Utils.Utils
import tweet4analysis.Utils.Valence
import java.util.*


/**
 * Created by elson on 11/2/18.
 */
class SentimentAnalyzer {

    private var inputString: String? = null
    private var inputStringProperties: TextProperties? = null

    /**
     * This section performs the following evaluation:
     *
     * If the term at currentItemPosition is followed by "kind of" or the it is present in
     * [Utils.BoosterDictionary], add the currentValence to sentiment array and break
     * to the next loop.
     *
     * If currentValence was 0.0, then current word's valence will also be 0.0.
     *
     * If current item in lowercase is in [Utils.WordValenceDictionary]...
     *
     * If current item is all in uppercase and the input string has yelling words,
     * accordingly adjust currentValence.
     */
    private val tokenWiseSentiment: List<Float>
        get() {
            var sentiments: MutableList<Float> = ArrayList()
            val wordsAndEmoticons = inputStringProperties?.wordsAndEmoticons

            for (currentItem in wordsAndEmoticons!!) {
                var currentValence = 0.0f
                val currentItemPosition = wordsAndEmoticons.indexOf(currentItem)
                val currentItemLower = currentItem.toLowerCase()

                if ((currentItemPosition < wordsAndEmoticons.size - 1
                                && currentItemLower == SentimentModifyingTokens.KIND.value
                                && wordsAndEmoticons[currentItemPosition + 1].toLowerCase()
                                == SentimentModifyingTokens.OF.value)
                        || Utils.BoosterDictionary.containsKey(currentItemLower)) {
                    sentiments.add(currentValence)
                    continue
                }

                if (Utils.WordValenceDictionary.containsKey(currentItemLower)) {
                    currentValence = Utils.WordValenceDictionary[currentItemLower]!!

                    if (Utils.isUpper(currentItem) && inputStringProperties!!.isCapDiff) {
                        if (currentValence > 0.0) {
                            currentValence += Valence.ALL_CAPS_FACTOR.value
                        } else {
                            currentValence -= Valence.ALL_CAPS_FACTOR.value
                        }
                    }

                    var distance = 0
                    while (distance < Constants.MAX_GRAM_WINDOW_SIZE) {
                        var closeTokenIndex = currentItemPosition - (distance + 1)
                        if (closeTokenIndex < 0) {
                            closeTokenIndex = inputStringProperties!!.wordsAndEmoticons!!.size -
                                    Math.abs(closeTokenIndex)
                        }

                        if (currentItemPosition > distance && !Utils.WordValenceDictionary
                                        .containsKey(wordsAndEmoticons[closeTokenIndex]
                                                .toLowerCase())) {

                            var gramBasedValence = valenceModifier(wordsAndEmoticons[closeTokenIndex],
                                    currentValence)
                            if (gramBasedValence != 0.0f) {
                                if (distance == 1) {
                                    gramBasedValence *= Valence.ONE_WORD_DISTANCE_DAMPING_FACTOR.value
                                } else if (distance == 2) {
                                    gramBasedValence *= Valence.TWO_WORD_DISTANCE_DAMPING_FACTOR.value
                                }
                            }
                            currentValence += gramBasedValence

                            currentValence = checkForNever(currentValence, distance,
                                    currentItemPosition, closeTokenIndex)
                            if (distance == 2) {
                                currentValence = checkForIdioms(currentValence, currentItemPosition)
                            }
                        }
                        distance++
                    }
                    currentValence = adjustIfHasAtLeast(currentItemPosition, wordsAndEmoticons,
                            currentValence)
                }

                sentiments.add(currentValence)
            }
            sentiments = checkConjunctionBut(wordsAndEmoticons, sentiments)
            return sentiments
        }

    /**
     * This is the main function.
     * This triggers all the sentiment scoring on the [SentimentAnalyzer.inputString].
     */
    fun analyze(inputString: String): Float {
        inputStringProperties = TextProperties(inputString)
        val tokenWiseSentiments = tokenWiseSentiment
        return getPolarityScores(tokenWiseSentiments)
    }

    /**
     * Adjust valence if a token is in [Utils.BoosterDictionary] or is a yelling word (all caps).
     * @param precedingToken token
     * @param currentValence valence to be adjusted
     * @return adjusted valence
     */
    private fun valenceModifier(precedingToken: String, currentValence: Float): Float {
        var scalar = 0.0f
        val precedingWordLower = precedingToken.toLowerCase()
        if (Utils.BoosterDictionary.containsKey(precedingWordLower)) {
            scalar = Utils.BoosterDictionary[precedingWordLower]!!
            if (currentValence < 0.0f) {
                scalar = -scalar
            }
            if (Utils.isUpper(precedingToken) && inputStringProperties!!.isCapDiff) {
                if (currentValence > 0.0f) {
                    scalar += Valence.ALL_CAPS_FACTOR.value
                } else {
                    scalar -= Valence.ALL_CAPS_FACTOR.value
                }
            }
        }
        return scalar
    }

    /**
     * This method checks for phrases having
     * - "never so current_word"
     * - "never this current_word"
     * - "never so this" etc.
     * @param currentValence valence before
     * @param distance gram window size
     * @param currentItemPosition position of the current token
     * @param closeTokenIndex token at the distance position from current item
     * @return adjusted valence
     */
    private fun checkForNever(currentValence: Float, distance: Int, currentItemPosition: Int,
                              closeTokenIndex: Int): Float {
        val wordsAndEmoticons = inputStringProperties!!.wordsAndEmoticons
        var tempValence = currentValence

        if (distance == 0) {
            if (isNegative(wordsAndEmoticons!![closeTokenIndex])) {
                tempValence *= Valence.NEGATIVE_WORD_DAMPING_FACTOR.value
            }
        } else if (distance == 1) {
            val wordAtDistanceTwoLeft = wordsAndEmoticons!![currentItemPosition -
                    Constants.PRECEDING_BIGRAM_WINDOW]
            val wordAtDistanceOneLeft = wordsAndEmoticons[currentItemPosition -
                    Constants.PRECEDING_UNIGRAM_WINDOW]
            if (wordAtDistanceTwoLeft == SentimentModifyingTokens.NEVER.value
                    && (wordAtDistanceOneLeft == SentimentModifyingTokens.SO.value
                            || wordAtDistanceOneLeft == SentimentModifyingTokens.NEVER.value)) {
                tempValence *= Valence.PRECEDING_BIGRAM_HAVING_NEVER_DAMPING_FACTOR.value
            } else if (isNegative(wordsAndEmoticons[closeTokenIndex])) {
                tempValence *= Valence.NEGATIVE_WORD_DAMPING_FACTOR.value
            }
        } else if (distance == 2) {
            val wordAtDistanceThreeLeft = wordsAndEmoticons!![currentItemPosition -
                    Constants.PRECEDING_TRIGRAM_WINDOW]
            val wordAtDistanceTwoLeft = wordsAndEmoticons[currentItemPosition -
                    Constants.PRECEDING_BIGRAM_WINDOW]
            val wordAtDistanceOneLeft = wordsAndEmoticons[currentItemPosition -
                    Constants.PRECEDING_UNIGRAM_WINDOW]
            if (wordAtDistanceThreeLeft == SentimentModifyingTokens.NEVER.value
                    && (wordAtDistanceTwoLeft == SentimentModifyingTokens.SO.value
                            || wordAtDistanceTwoLeft == SentimentModifyingTokens.THIS.value)
                    || wordAtDistanceOneLeft == SentimentModifyingTokens.SO.value
                    || wordAtDistanceOneLeft == SentimentModifyingTokens.THIS.value) {
                tempValence *= Valence.PRECEDING_TRIGRAM_HAVING_NEVER_DAMPING_FACTOR.value
            } else if (isNegative(wordsAndEmoticons[closeTokenIndex])) {
                tempValence *= Valence.NEGATIVE_WORD_DAMPING_FACTOR.value
            }
        }

        return tempValence
    }

    /**
     * Search if the any bi-gram/tri-grams around the currentItemPosition contains any idioms defined
     * in [Utils.SentimentLadenIdioms]. Adjust the current valence if there are any idioms found.
     * @param currentValence      valence
     * @param currentItemPosition current tokens position
     * @return adjusted valence
     */
    private fun checkForIdioms(currentValence: Float, currentItemPosition: Int): Float {
        val wordsAndEmoticons = inputStringProperties!!.wordsAndEmoticons
        val currentWord = wordsAndEmoticons!![currentItemPosition]
        val oneWordLeftToCurrentWord = wordsAndEmoticons[currentItemPosition -
                Constants.PRECEDING_UNIGRAM_WINDOW]
        val twoWordsLeftToCurrentWord = wordsAndEmoticons[currentItemPosition -
                Constants.PRECEDING_BIGRAM_WINDOW]
        val threeWordsLeftToCurrentWord = wordsAndEmoticons[currentItemPosition -
                Constants.PRECEDING_TRIGRAM_WINDOW]

        val bigramFormat = "%s %s"
        val trigramFormat = "%s %s %s"

        val leftBiGramFromCurrent = String.format(
                bigramFormat,
                oneWordLeftToCurrentWord,
                currentWord
        )
        val leftTriGramFromCurrent = String.format(
                trigramFormat,
                twoWordsLeftToCurrentWord,
                oneWordLeftToCurrentWord,
                currentWord
        )
        val leftBiGramFromOnePrevious = String.format(
                bigramFormat,
                twoWordsLeftToCurrentWord,
                oneWordLeftToCurrentWord
        )
        val leftTriGramFromOnePrevious = String.format(
                trigramFormat,
                threeWordsLeftToCurrentWord,
                twoWordsLeftToCurrentWord,
                oneWordLeftToCurrentWord
        )
        val leftBiGramFromTwoPrevious = String.format(
                bigramFormat,
                threeWordsLeftToCurrentWord,
                twoWordsLeftToCurrentWord
        )

        val leftGramSequences = ArrayList<String>()
        leftGramSequences.add(leftBiGramFromCurrent)
        leftGramSequences.add(leftTriGramFromCurrent)
        leftGramSequences.add(leftBiGramFromOnePrevious)
        leftGramSequences.add(leftTriGramFromOnePrevious)
        leftGramSequences.add(leftBiGramFromTwoPrevious)

        var tempValence = currentValence

        for (leftGramSequence in leftGramSequences) {
            if (Utils.SentimentLadenIdioms.containsKey(leftGramSequence)) {
                tempValence = Utils.SentimentLadenIdioms[leftGramSequence]!!
                break
            }
        }

        if (wordsAndEmoticons.size - 1 > currentItemPosition) {
            val rightBiGramFromCurrent = String.format(
                    bigramFormat,
                    wordsAndEmoticons[currentItemPosition],
                    wordsAndEmoticons[currentItemPosition + 1]
            )

            if (Utils.SentimentLadenIdioms.containsKey(rightBiGramFromCurrent)) {
                tempValence = Utils.SentimentLadenIdioms[rightBiGramFromCurrent]!!
            }
        }

        if (wordsAndEmoticons.size - 1 > currentItemPosition + 1) {
            val rightTriGramFromCurrent = String.format(
                    trigramFormat,
                    wordsAndEmoticons[currentItemPosition],
                    wordsAndEmoticons[currentItemPosition + 1],
                    wordsAndEmoticons[currentItemPosition + 2]
            )
            if (Utils.SentimentLadenIdioms.containsKey(rightTriGramFromCurrent)) {
                tempValence = Utils.SentimentLadenIdioms[rightTriGramFromCurrent]!!
            }
        }

        if (Utils.BoosterDictionary.containsKey(leftBiGramFromTwoPrevious)
                || Utils.BoosterDictionary.containsKey(leftBiGramFromOnePrevious)) {
            tempValence += Valence.DEFAULT_DAMPING.value
        }

        return tempValence
    }

    /**
     * This methods calculates the positive, negative and neutral sentiment from the sentiment values of the input
     * string.
     * @param tokenWiseSentimentState valence of the each token in input string
     * @return the positive, negative and neutral sentiment as an List
     */
    private fun siftSentimentScores(tokenWiseSentimentState: List<Float>): List<Float> {
        var positiveSentimentScore = 0.0f
        var negativeSentimentScore = 0.0f
        var neutralSentimentCount = 0
        for (valence in tokenWiseSentimentState) {
            when {
                valence > 0.0f -> positiveSentimentScore += valence + 1.0f
                valence < 0.0f -> negativeSentimentScore += valence - 1.0f
                else -> neutralSentimentCount += 1
            }
        }
        return ArrayList(
                Arrays.asList(
                        positiveSentimentScore,
                        negativeSentimentScore,
                        neutralSentimentCount.toFloat()
                )
        )
    }

    /**
     * Convert the lower level token wise valence to a higher level polarity scores.
     * @param tokenWiseSentimentState the tokenwise scores of the input string
     * @return the positive, negative, neutral and compound polarity scores as a map
     */
    private fun getPolarityScores(tokenWiseSentimentState: List<Float>): Float {
        var result = 0.0f
        if (!tokenWiseSentimentState.isEmpty()) {

            //Compute the total valence.
            var totalValence = tokenWiseSentimentState.sum()

            //Adjust the total valence score on the basis of the punctuations in the input string.
            val punctuationAmplifier = boostByPunctuation()

            if (totalValence > 0.0f) {
                totalValence += punctuationAmplifier
            } else if (totalValence < 0.0f) {
                totalValence -= punctuationAmplifier
            }

            val siftedScores = siftSentimentScores(tokenWiseSentimentState)
            var positiveSentimentScore = siftedScores[0]
            var negativeSentimentScore = siftedScores[1]
            val neutralSentimentCount = Math.round(siftedScores[2])

            if (positiveSentimentScore > Math.abs(negativeSentimentScore)) {
                positiveSentimentScore += punctuationAmplifier
            } else if (positiveSentimentScore < Math.abs(negativeSentimentScore)) {
                negativeSentimentScore -= punctuationAmplifier
            }

            val normalizationFactor = (positiveSentimentScore + Math.abs(negativeSentimentScore)
                    + neutralSentimentCount.toFloat())

            result += positiveSentimentScore / normalizationFactor
            result += negativeSentimentScore / normalizationFactor
        }
        return result
    }

    /**
     * This function jointly performs the boosting if input string contains
     * '!'s and/or '?'s and then returns the sum of the boosted scores from
     * [SentimentAnalyzer.boostByExclamation] and [SentimentAnalyzer.boostByQuestionMark].
     *
     * @return joint boosted score
     */
    private fun boostByPunctuation(): Float {
        return boostByExclamation() + boostByQuestionMark()
    }

    /**
     * Valence boosting when '!' is found in the input string.
     *
     * @return boosting score
     */
    private fun boostByExclamation(): Float {
        val exclamationCount = StringUtils.countMatches(inputString,
                SentimentModifyingTokens.EXCLAMATION_MARK.value)

        return Math.min(exclamationCount, Constants.MAX_EXCLAMATION_MARKS) *
                Valence.EXCLAMATION_BOOSTING.value
    }

    /**
     * Valence boosting when '?' is found in the input string.
     *
     * @return boosting score
     */
    private fun boostByQuestionMark(): Float {
        val questionMarkCount = StringUtils.countMatches(inputString,
                SentimentModifyingTokens.QUESTION_MARK.value)

        var questionMarkAmplifier = 0.0f
        if (questionMarkCount > 1) {
            questionMarkAmplifier =
                    if (questionMarkCount <= Constants.MAX_QUESTION_MARKS)
                        questionMarkCount * Valence.QUESTION_MARK_MAX_COUNT_BOOSTING.value
                    else Valence.QUESTION_MARK_BOOSTING.value
        }
        return questionMarkAmplifier
    }

    /**
     * This methods manages the effect of contrastive conjunctions like "but" on the valence of a token.
     * @param inputTokens list of token and/or emoticons in the input string
     * @param tokenWiseSentimentState current token wise sentiment scores
     * @return adjusted token wise sentiment scores
     */
    private fun checkConjunctionBut(inputTokens: List<String>,
                                    tokenWiseSentimentState: MutableList<Float>): MutableList<Float> {
        var indexOfConjunction = inputTokens.indexOf(SentimentModifyingTokens.BUT.value)
        if (indexOfConjunction < 0) {
            indexOfConjunction = inputTokens.indexOf(SentimentModifyingTokens.BUT.value.toUpperCase())
        }
        if (indexOfConjunction >= 0) {
            for (valenceIndex in tokenWiseSentimentState.indices) {
                var currentValence = tokenWiseSentimentState[valenceIndex]
                if (valenceIndex < indexOfConjunction) {
                    currentValence *= Valence.PRE_CONJUNCTION_ADJUSTMENT_FACTOR.value
                } else if (valenceIndex > indexOfConjunction) {
                    currentValence *= Valence.POST_CONJUNCTION_ADJUSTMENT_FACTOR.value
                }
                tokenWiseSentimentState[valenceIndex] = currentValence
            }
        }
        return tokenWiseSentimentState
    }

    /**
     * Check for the cases where you have phrases having "least" in the words preceding the token at
     * currentItemPosition and accordingly adjust the valence.
     * @param currentItemPosition position of the token in wordsAndEmoticons around which we will search for "least"
     * type phrases
     * @param wordsAndEmoticons   list of token and/or emoticons in the input string
     * @param currentValence      valence of the token at currentItemPosition
     * @return adjusted currentValence
     */
    private fun adjustIfHasAtLeast(currentItemPosition: Int, wordsAndEmoticons: List<String>,
                                   currentValence: Float): Float {
        var valence = currentValence
        if (currentItemPosition > 1
                && !Utils.WordValenceDictionary.containsKey(wordsAndEmoticons[currentItemPosition - 1]
                        .toLowerCase())
                && wordsAndEmoticons[currentItemPosition - 1].toLowerCase() ==
                SentimentModifyingTokens.LEAST.value) {

            if (!(wordsAndEmoticons[currentItemPosition - 2].toLowerCase() ==
                            SentimentModifyingTokens.AT.value
                            || wordsAndEmoticons[currentItemPosition - 2].toLowerCase() ==
                            SentimentModifyingTokens.VERY.value)) {
                valence *= Valence.NEGATIVE_WORD_DAMPING_FACTOR.value
            }
        } else if (currentItemPosition > 0 && !Utils.WordValenceDictionary
                        .containsKey(wordsAndEmoticons[currentItemPosition - 1].toLowerCase())
                && wordsAndEmoticons[currentItemPosition - 1] == SentimentModifyingTokens.LEAST.value) {
            valence *= Valence.NEGATIVE_WORD_DAMPING_FACTOR.value
        }
        return valence
    }

    /**
     * Check if token has "n't" in the end.
     * @param token current token
     * @return true if token has "n't" in the end
     */
    private fun hasContraction(token: String): Boolean {
        return token.endsWith(SentimentModifyingTokens.CONTRACTION.value)
    }

    /**
     * Check if token belongs to a pre-defined list of negative words. e.g. [Utils.NEGATIVE_WORDS].
     * @param token current token
     * @param newNegWords set of negative words
     * @return true if token belongs to newNegWords
     */
    private fun hasNegativeWord(token: String, newNegWords: Set<String>): Boolean {
        return newNegWords.contains(token)
    }

    /**
     * Check if token belongs to a pre-defined list of negative words. e.g. [Utils.NEGATIVE_WORDS]
     * and also checks if the token has "n't" in the end.
     * @param token current token
     * @param checkContractions flag to check "n't" in end of token
     * @return true iff token is in newNegWords or if checkContractions is true,
     * token should have "n't" in its end
     */
    private fun isNegative(token: String, checkContractions: Boolean = true): Boolean {
        val result = hasNegativeWord(token, Utils.NEGATIVE_WORDS)
        return if (!checkContractions) {
            result
        } else result || hasContraction(token)
    }
}