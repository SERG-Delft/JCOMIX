package nl.tudelft.tobuilder.lang;

import nl.tudelft.tobuilder.Pair;

import java.util.List;

public interface LangNode {

    List<List<Pair<String, String>>> inject(List<Pair<String, Integer>> strings);

    String getName();
}
