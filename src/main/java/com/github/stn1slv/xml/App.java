package com.github.stn1slv.xml;

import com.github.stn1slv.xml.old.MyDomComparator;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        String currentXSD = getCurrentXSD();
        String newXSD = newXSD3();

        //System.out.println("Current schema: " + currentXSD);
        //System.out.println("A new schema: " + newXSD);

//        compareCustom(currentXSD, newXSD);

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

        System.out.println("Diff: \n" + d.hasDifferences());
        System.out.println("Description: \n" + d.toString());
    }

    private static void compareCustom(String current, String newStr) throws Exception {
        Document currentDoc = convertStringToDocument(current);
        currentDoc.normalizeDocument();
        Document aNewDoc = convertStringToDocument(newStr);
        aNewDoc.normalizeDocument();

        List diff = new ArrayList<String>();
        MyDomComparator mdc = new MyDomComparator();
        mdc.diff(currentDoc, aNewDoc, diff);

        System.out.println("Diffs: " + diff.size());
        System.out.println(Arrays.toString(diff.toArray()));
    }

    private static String readFromFile(String fileName){
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getCurrentXSD() {
        return "<xs:schema elementFormDefault=\"qualified\"\n" +
                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"purchaseOrder\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element type=\"nameType\" name=\"name\"/>\n" +
                "                <xs:element type=\"xs:string\" name=\"sku\"/>\n" +
                "                <xs:element type=\"xs:decimal\" name=\"price\"/>\n" +
                "                <xs:element type=\"xs:date\" name=\"orderDate\"/>\n" +
                "                <xs:element name=\"shipTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "                <xs:element name=\"billTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "    <xs:simpleType name=\"nameType\">\n" +
                "        <xs:restriction base=\"xs:string\">\n" +
                "            <xs:maxLength value=\"512\"/>\n" +
                "        </xs:restriction>\n" +
                "    </xs:simpleType>\n" +
                "</xs:schema>";
    }

    private static String newXSD() {
        return "<xs:schema elementFormDefault=\"qualified\"\n" +
                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"purchaseOrder\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element type=\"nameType\" name=\"name\"/>\n" +
                "                <xs:element type=\"xs:string\" name=\"sku\"/>\n" +
                "                <xs:element type=\"xs:decimal\" name=\"price\"/>\n" +
                "                <xs:element type=\"xs:date\" name=\"orderDate\"/>\n" +
                "                <!-- new element -->\n" +
                "                <xs:element type=\"xs:date\" name=\"plannedDate\"/>\n" +
                "                <xs:element name=\"shipTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "                <xs:element name=\"billTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "    <xs:simpleType name=\"nameType\">\n" +
                "        <xs:restriction base=\"xs:string\">\n" +
                "            <xs:maxLength value=\"512\"/>\n" +
                "        </xs:restriction>\n" +
                "    </xs:simpleType>\n" +
                "</xs:schema>";
    }


    private static String newXSD2() {
        return "<xs:schema elementFormDefault=\"qualified\"\n" +
                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"purchaseOrder\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element type=\"nameType\" name=\"name\"/>\n" +
                "                <xs:element type=\"xs:string\" name=\"sku\"/>\n" +
                "                <xs:element type=\"xs:decimal\" name=\"price\"/>\n" +
                "                <xs:element type=\"xs:date\" name2=\"orderDate\"/>\n" +
                "                <!-- new element -->\n" +
                "                <xs:element type=\"xs:date\" name=\"plannedDate\"/>\n" +
                "                <xs:element name=\"shipTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "                <xs:element name=\"billTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "    <xs:simpleType name=\"nameType\">\n" +
                "        <xs:restriction base=\"xs:string\">\n" +
                "            <xs:maxLength value=\"512\"/>\n" +
                "        </xs:restriction>\n" +
                "    </xs:simpleType>\n" +
                "</xs:schema>";
    }

    private static String newXSD3() {
        return "<xs:schema elementFormDefault=\"qualified\"\n" +
                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"purchaseOrder\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element type=\"nameType\" name=\"name\"/>\n" +
                "                <xs:element type=\"xs:string\" name=\"sku\"/>\n" +
                "                <xs:element type=\"xs:date\" name=\"orderDate\"/>\n" +
                "                <xs:element name=\"shipTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "                <xs:element name=\"billTo\">\n" +
                "                    <xs:complexType>\n" +
                "                        <xs:sequence>\n" +
                "                            <xs:element type=\"xs:string\" name=\"name\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"address\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"city\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"state\"/>\n" +
                "                            <xs:element type=\"xs:string\" name=\"zip\"/>\n" +
                "                        </xs:sequence>\n" +
                "                    </xs:complexType>\n" +
                "                </xs:element>\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "    <xs:simpleType name=\"nameType\">\n" +
                "        <xs:restriction base=\"xs:string\">\n" +
                "            <xs:maxLength value=\"512\"/>\n" +
                "        </xs:restriction>\n" +
                "    </xs:simpleType>\n" +
                "</xs:schema>";
    }
}

