package com.github.stn1slv.xml;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        String currentXSD = readFromFile("src/main/resources/purchaseOrder2.xsd");
        String newXSD = readFromFile("src/main/resources/purchaseOrder.xsd");

        compareXMLUnit(currentXSD, newXSD);

    }

    private static void compareXMLUnit(String current, String newStr) {

        Diff d = DiffBuilder.compare(Input.fromString(current)).withTest(Input.fromString(newStr))
                .ignoreComments()
                .ignoreWhitespace()
                .normalizeWhitespace()
                .ignoreElementContentWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes))
                .build();

        System.out.println("DIFFERENT FILES: " + d.hasDifferences());

        getIncompatibleDifferences(d.getDifferences());

    }

    private static Set<Difference> getIncompatibleDifferences(Iterable<Difference> diffs) {
        System.out.println("DIFFERENCES:");
        Iterator<Difference> difItr = diffs.iterator();
        while (difItr.hasNext()) {
            Difference df = difItr.next();
            if (df.getComparison().getType().equals(ComparisonType.CHILD_LOOKUP)) {
                System.out.println("ELEMENT: " + df.getComparison().toString());
            }
            if (df.getComparison().getType().equals(ComparisonType.ATTR_NAME_LOOKUP)) {
                System.out.println("ARRTIBUTE: " + df.getComparison().toString());
            }

        }
        return null;
    }

    private static String readFromFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

