package com.github.stn1slv.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

public class App {
    private final static String XML_ELEMENT = "element";
    private final static String MIN_OCCURS_ATTR = "minOccurs";
    private final static String NAME_ATTR = "name";

    public static void main(String[] args) {
        String currentXSD = readFromFile("src/main/resources/purchaseOrder2.xsd");
        String newXSD = readFromFile("src/main/resources/purchaseOrder3.xsd");

        compareXMLUnit(currentXSD, newXSD);

    }

    private static void compareXMLUnit(String current, String newStr) {

        Diff d = DiffBuilder.compare(Input.fromString(current)).withTest(Input.fromString(newStr))
                .ignoreComments()
                .ignoreWhitespace()
                .normalizeWhitespace()
                .ignoreElementContentWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .build();

        System.out.println("DIFFERENT FILES: " + d.hasDifferences());

        getIncompatibleDifferences(d.getDifferences());

    }

    private static Set<Difference> getIncompatibleDifferences(Iterable<Difference> diffs) {
        System.out.println("DIFFERENCES:");
        Iterator<Difference> difItr = diffs.iterator();
        while (difItr.hasNext()) {
            Difference df = difItr.next();
            // Added/removed elements
            if (ComparisonType.CHILD_LOOKUP.equals(df.getComparison().getType())) {
                Comparison.Detail controlDetail = df.getComparison().getControlDetails();
                Comparison.Detail testDetail = df.getComparison().getTestDetails();
                if (controlDetail.getValue() != null && XML_ELEMENT.equalsIgnoreCase(controlDetail.getTarget().getLocalName())) {
                    System.out.println("Removed [" + controlDetail.getTarget().getAttributes().getNamedItem(NAME_ATTR).getNodeValue()
                            + "] element which is "
                            + (checkOptionalElement(controlDetail.getTarget()) ? "optional" : "mandatory"));
                }

                if (testDetail.getValue() != null && XML_ELEMENT.equalsIgnoreCase(testDetail.getTarget().getLocalName())) {
                    System.out.println("Added [" + testDetail.getTarget().getAttributes().getNamedItem(NAME_ATTR).getNodeValue()
                            + "] element which is "
                            + (checkOptionalElement(testDetail.getTarget()) ? "optional" : "mandatory"));

                }

            }
            // Added/removed attributes
            if (ComparisonType.ATTR_NAME_LOOKUP.equals(df.getComparison().getType())) {
                Comparison.Detail controlDetail = df.getComparison().getControlDetails();
                Comparison.Detail testDetail = df.getComparison().getTestDetails();
                if (controlDetail.getValue() != null) {
                    System.out.println("Removed [" + controlDetail.getValue()
                            + "] attribute for [" + controlDetail.getTarget().getAttributes().getNamedItem(NAME_ATTR).getNodeValue()
                            + "] element");
                }

                if (testDetail.getValue() != null) {
                    System.out.println("Added [" + testDetail.getValue()
                            + "] attribute for [" + testDetail.getTarget().getAttributes().getNamedItem(NAME_ATTR).getNodeValue()
                            + "] element");
                }
            }
            // Change value of the attribute
            if (ComparisonType.ATTR_VALUE.equals(df.getComparison().getType())) {
                Comparison.Detail controlDetail = df.getComparison().getControlDetails();
                Comparison.Detail testDetail = df.getComparison().getTestDetails();
                System.out.println("Changed value from [" + controlDetail.getValue() + "] to ["
                        + testDetail.getValue() + "] for ["
                        + controlDetail.getTarget().getLocalName() + "] attribute of ["
                        + getOwnerElementByChangedAttribute(controlDetail.getTarget()) + "] element"
                );

            }

        }
        return null;
    }

    private static boolean checkOptionalElement(Node node) {
        try {
            if (node.getAttributes().getNamedItem(MIN_OCCURS_ATTR).getNodeValue().equals("0"))
                return true;
        } catch (Exception e) {
            //do nothing
        }
        return false;
    }

    private static String getOwnerElementByChangedAttribute(Node node) {
        if (node instanceof org.w3c.dom.Attr) {
            Attr attr = (org.w3c.dom.Attr) node;
            return attr.getOwnerElement().getLocalName();
        }
        return "undefined";
    }


    private static String readFromFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

