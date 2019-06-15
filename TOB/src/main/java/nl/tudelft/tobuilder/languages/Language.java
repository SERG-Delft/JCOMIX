package nl.tudelft.tobuilder.languages;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import java.util.List;
import java.util.Map;

/**
 * This abstract class is the describes a language class.
 * It implements the ANTLRErrorListener to be able to validate texts.
 *
 * @param <L> the class type of the Lexer object
 * @param <P> the class type of the Parser object
 *
 * @author Dimitri Stallenberg
 */
public abstract class Language<L extends Lexer, P extends Parser> implements ANTLRErrorListener {

    private L lexer;
    private P parser;

    /**
     * This method will setup the language Lexer and Parser objects.
     *
     * @param text the text to create the Lexer and Parser for
     */
    protected void languageSetUp(String text) {
        setLexer(createLexer(text));
        setParser(createParser(getLexer()));
    }

    /**
     * This method creates the ANTLR Lexer object.
     *
     * @param text the text to create the Lexer for
     * @return the newly created Lexer object
     */
    protected abstract L createLexer(String text);

    /**
     * This method creates the ANTLR Parser object.
     *
     * @param lexer the lexer to create the Parser for
     * @return the newly created Parser object
     */
    protected abstract P createParser(L lexer);

    /**
     * This method validates the given text for a specific language using ANTLR.
     *
     * @param text the text to validate
     * @return whether the language is valid or not
     */
    public abstract boolean validate(String text);

    /**
     * This method tries to identify matcher strings that contain the given targets.
     * These matcher strings can then be used to find and replace certain parts in the given text.
     *
     * @param text the text to find the matcher strings in
     * @param targets the targets to find matcher strings for
     * @return a mapping between the target strings and the matcher strings
     */
    public abstract Map<String, String> identifyMatcher(String text, List<String> targets);

    /**
     * This method gets the Lexer object.
     *
     * @return the Lexer object
     */
    public L getLexer() {
        return lexer;
    }

    /**
     * This method sets the Lexer object.
     *
     * @param lexer the new Lexer
     */
    public void setLexer(L lexer) {
        this.lexer = lexer;
    }

    /**
     * This method gets the Parser object.
     *
     * @return the Parser object
     */
    public P getParser() {
        return parser;
    }

    /**
     * This method sets the Parser object.
     *
     * @param parser the new Parser
     */
    public void setParser(P parser) {
        this.parser = parser;
    }
}
