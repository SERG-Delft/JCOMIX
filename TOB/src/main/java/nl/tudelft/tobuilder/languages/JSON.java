package nl.tudelft.tobuilder.languages;

import nl.tudelft.generated.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;

public class JSON extends Language<JSONLexer, JSONParser> {
    @Override
    protected JSONLexer createLexer(String text) {
        return new JSONLexer(CharStreams.fromString(text));
    }

    @Override
    protected JSONParser createParser(JSONLexer lexer) {
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new JSONParser(tokenStream);
    }

    @Override
    public boolean validate(String text) {
        languageSetUp(text);

        getParser().addErrorListener(this);

        ParseTree parseTree = getParser().json();
        ParseTreeWalker walker = new ParseTreeWalker();
        JSONBaseListener listener = new JSONBaseListener();

        walker.walk(listener, parseTree);

        ParseTreeWalker.DEFAULT.walk(listener, parseTree);

        return false;
    }

    @Override
    public Map<String, String> identifyMatcher(String fileText, List<String> targets) {

        Map<String, String> pairs = new HashMap<>();

        // JSON does not have special tags around its values like in XML so it is quiet easy to simply replace stuff
        for (String target : targets) {
            pairs.put(target, target);
        }

        return pairs;
//        languageSetUp(fileText);
//        Queue<JSONParser.ValueContext> queue = new LinkedList<>();
//        queue.add(getParser().json().value());
//        while (!queue.isEmpty()) {
//            JSONParser.ValueContext tree = queue.remove();
//
//            if (tree == null || tree.value() == null) {
//                continue;
//            }
//
//            if (tree.array() != null) {
//                for (JSONParser.ValueContext item: tree.array().value()) {
//
//                    // TODO check for pairs
//
//                    queue.add(item);
//                }
//            } else if (tree.obj() != null) {
//                for (JSONParser.PairContext pair: tree.obj().pair()) {
//                    queue.add(pair.value());
//                }
//            } else if (tree.STRING() != null) {
//                if (targets.contains(tree.STRING().getText())) {
//                    pairs.put(text, child.getText());
//
//                }
//            } else if (tree.NUMBER() != null) {
//                if (targets.contains(tree.NUMBER().getText())) {
//                    pairs.put(text, child.getText());
//                }
//            }
//
//
//            for (XMLParser.ElementContext child: tree.value()) {
////                System.out.println(child.getText());
//
//                if (child.content() == null) {
//                    continue;
//                }
//
//                String text = child.content().getText();
//
//                if (targets.contains(text)) {
//                    pairs.put(text, child.getText());
//                }
//
//                if (targets.size() == pairs.size()) {
//                    return pairs;
//                }
//
//                queue.add(child.content());
//            }
//        }
//        throw new IllegalArgumentException("Cannot find all targets it file!");
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {

    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
}
