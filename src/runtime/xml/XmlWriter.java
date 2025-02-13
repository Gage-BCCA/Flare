package runtime.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.models.Project;
import data.models.ProjectFile;
import data.models.Entry;


import java.util.ArrayList;

public class XmlWriter {

    public void CreateNew(Project project,
                          ArrayList<ProjectFile> projectFiles,
                          String outputPath
                          ) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Create root element
        Element root = document.createElement("Project");
        document.appendChild(root);

        // Add first child containing Project Title
        Element projectTitleElement = document.createElement("Title");
        projectTitleElement.appendChild(document.createTextNode(project.title));
        root.appendChild(projectTitleElement);

        // Add Language to root
        Element projectLanguageElement = document.createElement("Language");
        projectLanguageElement.appendChild(document.createTextNode(project.language));
        root.appendChild(projectLanguageElement);


        Element filesElement = document.createElement("Files");
        for (ProjectFile file : projectFiles) {
            // Create parent
            Element individualFileElement = document.createElement("ProjectFile");

            // Create Name Child
            Element projectFileName = document.createElement("FileName");
            projectFileName.appendChild(document.createTextNode(file.fileName));

            // Create Type Child
            Element projectFileType = document.createElement("FileType");
            projectFileType.appendChild(document.createTextNode(file.fileType));

            // Create Hash Child
            Element projectFileHash = document.createElement("Hash");
            projectFileHash.appendChild(document.createTextNode(file.hash));

            // Append all children to parent created at the top
            // of the loop
            individualFileElement.appendChild(projectFileName);
            individualFileElement.appendChild(projectFileType);
            individualFileElement.appendChild(projectFileHash);

            // Append to filesElement
            filesElement.appendChild(individualFileElement);
        }

        // Append newly built Files element to root
        root.appendChild(filesElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        // Specify your local file path
        StreamResult result = new StreamResult(outputPath + "\\flare.xml");
        transformer.transform(source, result);

    }

}
