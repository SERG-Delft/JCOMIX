package nl.tudelft.tobuilder;

import nl.tudelft.tobuilder.lang.*;
import nl.tudelft.tobuilder.languages.Language;
import nl.tudelft.tobuilder.languages.XMLLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Builder {

    private Language language;

    public Builder(String language) {
        this.language = getLanguageObject(language);
    }

    public Builder(Language language) {
        this.language = language;
    }

    private Language getLanguageObject(String language) {
        switch (language) {
            case "xml":
                return new XMLLanguage();
            default:
                // TODO
                throw new IllegalArgumentException("Language not implemented!");
        }
    }

    public Map<String, String> build(String expectedOutput, List<Pair<String, Integer>> targets) {
        language.validate(expectedOutput);

        List<String> targetsSingle = new ArrayList<>();

        for (int i = 0; i < targets.size(); i++) {
            targetsSingle.add(targets.get(i).getFirst());
        }

        Map<String, String> targetMatchStrings = language.identifyMatcher(expectedOutput, targetsSingle);

        Modifier modifier = new Modifier();

        List<LangNode> injections = new ArrayList<>();

        // Add injections
        injections.add(new ReplaceNode("close", "</test>"));
        injections.add(new PointNode("meta", "<"));
        injections.add(new PointNode("meta", "\'"));
        injections.add(new PointNode("meta", "\""));
        injections.add(new PointNode("meta", ">"));
//
        injections.add(new ReplaceNode("insert", "0 or~/**/1"));
        injections.add(new ReplaceNode("insert", "1 or/**/\"a\"=\"a\" or"));
        injections.add(new ReplaceNode("insert", "')/**/or true#"));
        injections.add(new ReplaceNode("insert", "0/**/||'a'='a'/**/"));
        injections.add(new ReplaceNode("insert", "'/**/;select sleep(5)--"));
        injections.add(new ReplaceNode("insert", "0 union/**/(select 0)#"));
        injections.add(new ReplaceNode("insert", "')/**/union/**/(select 0)#"));

        // TODO doesnt work yet
//        injections.add(new PairReplaceNode("replace", "0111</lu:IssuerBankCode> <!--", "--> <lu:RequestId>0 or~/**/1</lu:RequestId> <lu:CardNumber>123456789123456"));
//        injections.add(new PairReplaceNode("replace", "0111</lu:IssuerBankCode> <!--", "<lu:CardNumber>  --><lu:RequestId>1 or/**/\"a\"=\"a\" or</lu:RequestId>"));

        injections.add(new ReplicateNode("replicate", targetMatchStrings,"0 or~/**/1"));
        injections.add(new ReplicateNode("replicate", targetMatchStrings,"1 or/**/\"a\"=\"a\" or"));
        injections.add(new ReplicateNode("replicate", targetMatchStrings,"')/**/or true#"));
        injections.add(new ReplicateNode("replicate", targetMatchStrings,"0/**/||'a'='a'/**/"));


        Injections injectionsObject = new Injections(injections);

        Map<String, String> generatedTOs = new HashMap<>();

        int count = 0;

        while (injectionsObject.hasNext()) {
            count++;
            List<List<Pair<String, String>>> permutations = injectionsObject.getNext(targets);

            for (int i = 0; i < permutations.size(); i++) {
                List<Pair<String, Pair<String, String>>> targetAndReplacements = new ArrayList<>();

                List<Pair<String, String>> replacements = permutations.get(i);

                for (int j = 0; j < replacements.size(); j++) {
                    Pair<String, String> replacement = replacements.get(j);

                    if (replacement.getFirst().equals(replacement.getSecond())) {
                        continue;
                    }

                    if(!targetMatchStrings.containsKey(replacement.getFirst())) {
                        // TODO
                        throw new IllegalStateException("Error");
                    }

                    String matcher = targetMatchStrings.get(replacement.getFirst());

                    targetAndReplacements.add(new Pair<>(replacement.getFirst(), new Pair<>(matcher, replacement.getSecond())));
                }

                String modifiedString = modifier.modify(expectedOutput, targetAndReplacements);

                try {
                    language.validate(modifiedString);
                } catch (RuntimeException e) {
                    continue;
                }

                String fileName = injectionsObject.lastName() + (i + 1) + "-" + count + ".xml";

                generatedTOs.put(fileName, modifiedString);
            }

        }
        return generatedTOs;
    }
}
