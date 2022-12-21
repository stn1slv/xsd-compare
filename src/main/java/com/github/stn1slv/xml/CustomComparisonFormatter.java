package com.github.stn1slv.xml;

import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonFormatter;
import org.xmlunit.diff.ComparisonType;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class CustomComparisonFormatter implements ComparisonFormatter {
    @Override
    public String getDescription(Comparison difference) {
        ComparisonType type = difference.getType();
        String description = type.getDescription();
        Comparison.Detail controlDetails = difference.getControlDetails();
        Comparison.Detail testDetails = difference.getTestDetails();

        StringBuilder sXmlBuilder = new StringBuilder();
        //Get original schemas
        DOMSource domSource = new DOMSource(controlDetails.getTarget().getOwnerDocument());
        DOMSource domTarget = new DOMSource(testDetails.getTarget().getOwnerDocument());
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();

            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            sXmlBuilder.append("Original:\n");
            sXmlBuilder.append(sw.toString());

            StringWriter swT = new StringWriter();
            StreamResult srT = new StreamResult(swT);
            transformer.transform(domTarget, srT);
            sXmlBuilder.append("\nTarget:\n");
            sXmlBuilder.append(swT.toString());
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        return sXmlBuilder.toString();
    }


    @Override
    public String getDetails(Comparison.Detail difference, ComparisonType type, boolean formatXml) {
        try {
            if (difference.getTarget() == null) {
                return "<NULL>";
            }
            return difference.getTarget().getOwnerDocument().toString();
        } catch (Exception ex) {
            return "<Exceptioon>" + ex.getMessage() + "</Exception>";
        }

    }
}
