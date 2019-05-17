package nl.tudelft.tobuilder.languages;


import nl.tudelft.tobuilder.Pair;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import java.util.List;
import java.util.Map;

public abstract class Language<L extends Lexer, P extends Parser> implements ANTLRErrorListener {

    private L lexer;
    private P parser;

    protected abstract void setUp(String text);

    protected abstract L createLexer(String text);

    protected abstract P createParser(L lexer);

    public abstract boolean validate(String text);

    public abstract Map<String, String> identifyMatcher(String text, List<String> targets);

    public L getLexer() {
        return lexer;
    }

    public void setLexer(L lexer) {
        this.lexer = lexer;
    }

    public P getParser() {
        return parser;
    }

    public void setParser(P parser) {
        this.parser = parser;
    }
}
