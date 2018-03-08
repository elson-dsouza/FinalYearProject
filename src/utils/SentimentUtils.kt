package utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * This object contains the constants that are the used by the sentiment analyzer.
 * The constants are same as the ones used in the official implementation which can be found at
 * http://www.nltk.org/_modules/nltk/sentiment/vader.html
 *
 * Created by elson on 11/2/18.
 */
object SentimentUtils {
    // Class loader to get the path to the resources folder.
    private val CLASS_LOADER = SentimentUtils::class.java.classLoader

    // List of possible punctuation marks.
    val PUNCTUATION_LIST = arrayOf(".", "!", "?", ",", ";", ":", "-", "'", "\"", "!!",
            "!!!", "??", "???", "?!?", "!?!", "?!?!", "!?!?")

    // Hash set of all negating words.
    val NEGATIVE_WORDS = HashSet(Arrays.asList("aint", "arent", "cannot", "cant", "couldnt",
            "darent", "didnt", "doesnt", "ain't", "aren't", "can't", "couldn't", "daren't",
            "didn't", "doesn't", "dont", "hadnt", "hasnt", "havent", "isnt", "mightnt", "mustnt",
            "neither", "don't", "hadn't", "hasn't", "haven't", "isn't", "mightn't", "mustn't",
            "neednt", "needn't", "never", "none", "nope", "nor", "not", "nothing", "nowhere",
            "oughtnt", "shant", "shouldnt", "uhuh", "wasnt", "werent", "oughtn't", "shan't",
            "shouldn't", "uh-uh", "wasn't", "weren't", "without", "wont", "wouldnt", "won't",
            "wouldn't", "rarely", "seldom", "despite"))

    // This dictionary holds a token and its corresponding boosting/dampening
    // factor for sentiment scoring.
    val BoosterDictionary = HashMap<String, Float>()

    // Idioms with their respective valencies.
    val SentimentLadenIdioms = HashMap<String, Float>()

    // Tokens with their respective valencies.
    val WordValenceDictionary: Map<String, Float> = readLexiconFile()

    //Constructor invoked
    init {
        BoosterDictionary["decidedly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["uber"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["barely"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["particularly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["enormously"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["less"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["absolutely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["kinda"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["flipping"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["awfully"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["purely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["majorly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["substantially"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["partly"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["remarkably"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["really"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["sort of"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["little"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["fricking"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["sorta"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["amazingly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["kind of"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["just enough"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["fucking"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["occasionally"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["somewhat"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["kindof"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["friggin"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["incredibly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["totally"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["marginally"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["more"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["considerably"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["fabulously"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["hardly"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["very"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["sortof"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["kind-of"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["scarcely"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["thoroughly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["quite"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["most"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["completely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["frigging"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["intensely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["utterly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["highly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["extremely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["unbelievably"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["almost"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["especially"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["fully"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["frickin"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["tremendously"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["exceptionally"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["flippin"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["hella"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["so"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["greatly"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["hugely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["deeply"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["unusually"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["entirely"] = Valence.DEFAULT_BOOSTING.value
        BoosterDictionary["slightly"] = Valence.DEFAULT_DAMPING.value
        BoosterDictionary["effing"] = Valence.DEFAULT_BOOSTING.value

        SentimentLadenIdioms["cut the mustard"] = 2f
        SentimentLadenIdioms["bad ass"] = 1.5f
        SentimentLadenIdioms["kiss of death"] = -1.5f
        SentimentLadenIdioms["yeah right"] = -2f
        SentimentLadenIdioms["the bomb"] = 3f
        SentimentLadenIdioms["hand to mouth"] = -2f
        SentimentLadenIdioms["the shit"] = 3f
    }

    /**
     * This function returns false if the input token:
     * 1. is a URL
     * 2. is a number as string
     * 3. has one character in lower case
     *
     * @param token input token
     * @return true if none of the above conditions occur
     */
    fun isUpper(token: String): Boolean {
        Constants.URL_PREFIXES.forEach {
            if(token.toLowerCase().startsWith(it)) return false
        }
        if (!token.matches(Constants.NON_NUMERIC_STRING_REGEX.toRegex())) {
            return false
        }
        return (0 until token.length).none { Character.isLowerCase(token[it]) }
    }

    /**
     * This function reads in a file that stores lexicon and their corresponding valence intensity.
     * Each pair of lexicon and its valence is then stored as key-value pairs in a HashMap.
     *
     * @return map of lexicons with their corresponding valence
     */
    private fun readLexiconFile(): Map<String, Float> {
        val lexFile = CLASS_LOADER.getResourceAsStream(Constants.VADER_FILE_PATH)
        val lexDictionary = HashMap<String, Float>()
        if (lexFile != null) {
            try {
                BufferedReader(InputStreamReader(lexFile)).use { br ->
                    var line: String? = br.readLine()
                    while (line != null) {
                        val lexFileData = line.split("\\t".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        val currentText = lexFileData[0]
                        val currentTextValence = java.lang.Float.parseFloat(lexFileData[1])
                        lexDictionary[currentText] = currentTextValence
                        line = br.readLine()
                    }
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }
        return lexDictionary
    }
}