package alliness.core.convertor;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class DocumentConverter {

    public static String toString(Document doc) {
        DOMSource          domSource   = new DOMSource(doc);
        StringWriter       writer      = new StringWriter();
        StreamResult       result      = new StreamResult(writer);
        TransformerFactory tf          = TransformerFactory.newInstance();
        Transformer        transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(domSource, result);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private static DocumentBuilder getBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    public static Document toDocument(String data) throws ParserConfigurationException, IOException, SAXException {
        return getBuilder().parse(new ByteArrayInputStream(data.getBytes("UTF-8")));
    }

    public static Document toDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        return getBuilder().parse(file);
    }

}
