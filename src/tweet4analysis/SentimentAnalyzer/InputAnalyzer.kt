package tweet4analysis.SentimentAnalyzer

import org.apache.lucene.analysis.Tokenizer
import org.apache.lucene.analysis.core.WhitespaceTokenizer
import org.apache.lucene.analysis.miscellaneous.LengthFilter
import org.apache.lucene.analysis.standard.StandardTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import java.io.IOException
import java.io.StringReader
import java.util.*


/**
 * Created by elson on 11/2/18.
 *
 * This class defines a Lucene analyzer that is applied on the input tweet
 */

class InputAnalyzer {

    /**
     * This function applies a Lucene analyzer that splits a string into a tokens.
     * Here we are using two types of Lucene [Tokenizer]s:
     * 1. [WhitespaceTokenizer] which tokenizes from the white spaces
     * 2. [StandardTokenizer] which tokenizes from white space as well as removed any punctuations
     *
     * @param inputString The input string to be pre-processed with Lucene tokenizer
     * @param removePunctuation Flag which specifies if punctuations have to be removed from the string.
     * @return tokens
     * @throws IOException if Lucene's analyzer encounters any error
     */
    @Throws(IOException::class)
    private fun tokenize(inputString: String, removePunctuation: Boolean): List<String> {
        val reader = StringReader(inputString)
        val currentTokenizer: Tokenizer = if (removePunctuation) StandardTokenizer()
        else WhitespaceTokenizer()

        currentTokenizer.setReader(reader)
        val tokenStream = LengthFilter(currentTokenizer, 2, Integer.MAX_VALUE)
        val charTermAttribute = tokenStream.addAttribute(CharTermAttribute::class.java)
        tokenStream.reset()

        val tokenizedString = ArrayList<String>()
        while (tokenStream.incrementToken()) {
            tokenizedString.add(charTermAttribute.toString())
        }

        tokenStream.end()
        tokenStream.close()

        return tokenizedString
    }

    @Throws(IOException::class)
    fun defaultSplit(inputString: String): List<String> {
        return tokenize(inputString, false)
    }

    @Throws(IOException::class)
    fun removePunctuation(inputString: String): List<String> {
        return tokenize(inputString, true)
    }
}