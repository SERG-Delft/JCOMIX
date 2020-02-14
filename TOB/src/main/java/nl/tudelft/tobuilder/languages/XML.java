package nl.tudelft.tobuilder.languages;

import nl.tudelft.generated.XMLLexer;
import nl.tudelft.generated.XMLParser;
import nl.tudelft.generated.XMLParserBaseListener;
import nl.tudelft.generated.XMLParserListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;

/**
 * This class implements the XMLLanguage class.
 *
 * @author Dimitri Stallenberg
 */
public class XML extends Language<XMLLexer, XMLParser> {

    @Override
    protected XMLLexer createLexer(String text) {
        return new XMLLexer(CharStreams.fromString(text));
    }

    @Override
    protected XMLParser createParser(XMLLexer lexer) {
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new XMLParser(tokenStream);
    }

    @Override
    public boolean validate(String text) {
        languageSetUp(text);

        getParser().addErrorListener(this);

        ParseTree parseTree = getParser().content();
        ParseTreeWalker walker = new ParseTreeWalker();
        XMLParserListener listener = new XMLParserBaseListener();

        walker.walk(listener, parseTree);

        ParseTreeWalker.DEFAULT.walk(listener, parseTree);

        return false;
    }

    @Override
    public Map<String, String> identifyMatcher(String fileText, List<String> targets) {
        Map<String, String> pairs = new HashMap<>();

        languageSetUp(fileText);

        Queue<XMLParser.ContentContext> queue = new LinkedList<>();
        queue.add(getParser().content());
        while (!queue.isEmpty()) {
            XMLParser.ContentContext tree = queue.remove();

            if (tree == null || tree.element() == null) {
                continue;
            }

            for (XMLParser.ElementContext child: tree.element()) {
//                System.out.println(child.getText());

                if (child.content() == null) {
                    continue;
                }

                String text = child.content().getText();

                if (targets.contains(text)) {
                    pairs.put(text, child.getText());
                }

                if (targets.size() == pairs.size()) {
                    return pairs;
                }

                queue.add(child.content());
            }
        }
        throw new IllegalArgumentException("Cannot find all targets it file!");

        // TODO
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        // TODO

        throw new RuntimeException();
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {
        // TODO

        throw new RuntimeException();
    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {
        // TODO

        throw new RuntimeException();
    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {
        // TODO

        throw new RuntimeException();
    }
}
