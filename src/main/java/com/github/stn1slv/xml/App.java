package com.github.stn1slv.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        String currentXSD = readFromFile("src/main/resources/purchaseOrder.xsd");
        String newXSD = readFromFile("src/main/resources/purchaseOrder2.xsd");

        //System.out.println("Current schema: " + currentXSD);
        //System.out.println("A new schema: " + newXSD);

        compareXMLUnit(currentXSD, newXSD);

    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void compareXMLUnit(String current, String newStr) {
        Diff d = DiffBuilder.compare(Input.fromString(current)).withTest(Input.fromString(newStr))
                .checkForSimilar()
                .checkForIdentical()
                .ignoreComments()
                .ignoreWhitespace()
                .normalizeWhitespace()
                .ignoreElementContentWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .withComparisonFormatter(new CustomComparisonFormatter())
                .build();

        System.out.println("DIFFERENT FILES: " + d.hasDifferences());
        System.out.println("DESCRIPTION: \n" + d.toString());
    }

    private static String readFromFile(String fileName){
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

