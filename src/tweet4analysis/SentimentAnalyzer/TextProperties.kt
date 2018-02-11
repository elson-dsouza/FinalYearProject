package tweet4analysis.SentimentAnalyzer

import tweet4analysis.Utils.Utils
import java.io.IOException
import java.util.*


/**
 * Created by elson on 11/2/18.
 *
 * The TextProperties class implements the pre-processing steps of the input string for sentiment analysis.
 * It utilizes the Lucene analyzer to perform processing on the input string.
 */
class TextProperties
/**
 * Parameterized constructor accepting the input string that will be processed.
 * @param inputText the input string
 * @throws IOException if there is an issue with the lucene analyzers
 */
@Throws(IOException::class)
constructor(private val inputText: String) {

    /**
     * List of tokens and emoticons extracted from the [TextProperties.inputText].
     */
    var wordsAndEmoticons: List<String>? = null
        private set

    /**
     * List of tokens extracted from the [TextProperties.inputText].
     * Emoticons are removed here.
     */
    var wordsOnly: List<String>? = null
        private set

    //Flags that specifies if the current string has yelling words.
    var isCapDiff: Boolean = false
        private set

    /**
     * Return true if the input has yelling words i.e. all caps in the tokens, but all the token should not be
     * in upper case.
     * e.g. [GET, THE, HELL, OUT] returns false
     * [GET, the, HELL, OUT] returns true
     * [get, the, hell, out] returns false
     *
     * @return boolean value
     */
    private val isAllCapDifferential: Boolean
        get() {
            var countAllCaps = 0
            for (token in wordsAndEmoticons!!) {
                if (Utils.isUpper(token)) {
                    countAllCaps++
                }
            }
            val capDifferential = wordsAndEmoticons!!.size - countAllCaps
            return 0 < capDifferential && capDifferential < wordsAndEmoticons!!.size
        }

    init {
        setWordsAndEmoticons()
        isCapDiff = isAllCapDifferential
    }

    /**
     * This method tokenizes the input string, preserving the punctuation marks using
     * @throws IOException if something goes wrong in the Lucene analyzer.
     * @see InputAnalyzer.tokenize
     */
    @Throws(IOException::class)
    private fun setWordsAndEmoticons() {
        setWordsOnly()

        val wordsAndEmoticonsList: MutableList<String> = InputAnalyzer().defaultSplit(inputText)
                .toMutableList()

        for (currentWord in wordsOnly!!) {
            for (currentPunctuation in Utils.PUNCTUATION_LIST) {
                val wordPunctuation = currentWord + currentPunctuation
                var wordPunctCount = Collections.frequency(wordsAndEmoticonsList, wordPunctuation)
                while (wordPunctCount > 0) {
                    val index = wordsAndEmoticonsList.indexOf(wordPunctuation)
                    wordsAndEmoticonsList.remove(wordPunctuation)
                    wordsAndEmoticonsList.add(index, currentWord)
                    wordPunctCount = Collections.frequency(wordsAndEmoticonsList, wordPunctuation)
                }

                val punctuationWord = currentPunctuation + currentWord
                var punctuationWordCount = Collections.frequency(wordsAndEmoticonsList, punctuationWord)
                while (punctuationWordCount > 0) {
                    val index = wordsAndEmoticonsList.indexOf(punctuationWord)
                    wordsAndEmoticonsList.remove(punctuationWord)
                    wordsAndEmoticonsList.add(index, currentWord)
                    punctuationWordCount = Collections.frequency(wordsAndEmoticonsList, punctuationWord)
                }
            }
        }
        this.wordsAndEmoticons = wordsAndEmoticonsList
    }

    /**
     * This method tokenize's the input string, removing the special characters as well.
     * @throws IOException iff there is an error which using Lucene analyzers.
     * @see InputAnalyzer.removePunctuation
     */
    @Throws(IOException::class)
    private fun setWordsOnly() {
        this.wordsOnly = InputAnalyzer().removePunctuation(inputText)
    }
}