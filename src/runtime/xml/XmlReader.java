package runtime.xml;

import data.models.ProjectFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;

public class XmlReader {

    private final String dirPath;
    private final File targetXml;

    public XmlReader(String dirPath) {
        this.dirPath = dirPath;
        this.targetXml = new File(dirPath + "\\.flare\\flare.xml");
    }

    public ArrayList<ProjectFile> getProjectFilesFromXml() throws Exception {

        // Create a DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML file
        Document document = builder.parse(targetXml);

        document.normalizeDocument();

        ArrayList<ProjectFile> projectFiles = new ArrayList<>();

        // Access elements by tag name
        NodeList nodeList = document.getElementsByTagName("ProjectFile");
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            NodeList children = node.getChildNodes();

            ProjectFile pf = new ProjectFile();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("FileName")) {
                    pf.fileName = child.getTextContent();
                }
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("FileType")) {
                    pf.fileType = child.getTextContent();
                }
                if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("Hash")) {
                    pf.hash = child.getTextContent();
                }
            }

            projectFiles.add(pf);

        }

        return projectFiles;
    }

}
