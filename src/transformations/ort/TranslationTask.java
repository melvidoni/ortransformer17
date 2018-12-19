package transformations.ort;


import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Random;





/**
 * Class that guides the transformations to bge performed, called
 * by the corresponding listener. Works as an independent task.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class TranslationTask extends Task {



    /**
     * Empty constructor of the class.
     */
    public TranslationTask() {}




    /**
     * Main method that executes the translation task
     * @return true if it was completed successfully.
     * @throws Exception For diverse errors when transforming.
     */
    @Override
    protected Boolean call() throws Exception {
        // Get the transformation status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);

        // STEP 0
        // Copy files to the temp directory
        String userPath = System.getProperty("user.dir") + File.separator + "files" + File.separator;
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + "ORTransformer";
        String tempFilesPath = tempPath + File.separator + "files";
        FileUtils.copyDirectory( new File(userPath), new File(tempFilesPath));
        tempFilesPath += File.separator;


        // STEP 1
        // Get the temporal directory according to the operative system
        String tempName = fileName();
        File tempDir = new File(tempPath);
        tempDir.mkdirs();
        updateProgress(10, 100);
        updateMessage("10% Completed. Temp files loaded successfully.");


        // STEP 2
        // Translate from UML to XML
        File preFile = new File(tempPath + File.separator + tempName + "-pre.xml");
        UMLtoXML.transformToXML(preFile, true, null);
        updateProgress(25, 100);
        updateMessage("25% Completed. XML translation completed successfully.");


        // STEP 3
        // Transform towards the metamodel
        File modFile = new File(tempPath + File.separator + tempName + "-mod.xml");
        LinkedList<String[]> paramsList1 = new LinkedList<>();
        String[] params1 = {"UMLMetamodel", tempFilesPath + "UMLMetamodel.xsd"};
        paramsList1.add(params1);
        transform(tempFilesPath + "UMLToModel.xslt", paramsList1, preFile, modFile);
        updateProgress(40, 100);
        updateMessage("40% Completed. Metamodel transformation achieved.");


        // STEP 4
        // First mapping, using types
        File tpoFile = new File(tempPath + File.separator + tempName+ "-tpo.xml");
        transform(tempFilesPath + "FirstMapping.xslt",
                getParamsList(modFile.getAbsolutePath().substring(1), 8, "Param_",
                        "SQL2003Metamodel_data_1",
                        tempFilesPath + "SQL2003Metamodel_data.xsd"),
                modFile, tpoFile);
        updateProgress(55, 100);
        updateMessage("55% Completed. Types transformation achieved.");


        // STEP 5
        // Second mapping, using tables
        File tblFile = new File(tempPath + File.separator + tempName + "-tbl.xml");
        transform(tempFilesPath + "SecondMapping" + tStatus.getImplementation().getExtension() + ".xslt",
                        getParamsList(tpoFile.getAbsolutePath().substring(1), 2,
                        "SQL2003Metamodel_data",
                        "SQL2003Metamodel_schema_1",
                        tempFilesPath + "SQL2003Metamodel_schema.xsd"),
                tpoFile, tblFile);
        updateProgress(70, 100);
        updateMessage("70% Completed. Tables successfully mapped.");


        // STEP 6
        // Translate towards SQL
        XMLtoORSQL sqlGuide = new XMLtoORSQL(tempPath, tempName);
        switch (tStatus.getDatabase()) {
            case OBJECT_RELATIONAL: // Translate types
                tStatus.setTypesScript(sqlGuide.translateORTypes());
                updateProgress(85, 100);
                updateMessage("85% Completed. Translation towards SQL in progress.");

                // Translate tables
                tStatus.setTablesScript(sqlGuide.translateORTables(tStatus.getImplementation()));
                tStatus.setTransformed(true);
                updateProgress(100, 100);
                updateMessage("100% Completed. Preparing results visualization.");

                break;

            case RELATIONAL_MSQL: // TODO: FUTURE WORK RELATIONAL_SQL MODEL
                break;
        }


        // Return a value from the transformation
        return true;
    }







    /**
     * Method that performs an XSL Transformation, using the information received
     * as parameters when calling.
     * @param xslt The XSLT file to lead the transformation.
     * @param params Any possible parameters for the file.
     * @param startFile The origin XML file.
     * @param endFile The target XML file.
     * @throws TransformerException An exception for transformation issues.
     */
    private void transform(String xslt, LinkedList<String[]> params, File startFile, File endFile)
            throws TransformerException {
        // Prepare the factory and source
        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xsltSource = new StreamSource(new File(xslt));
        xsltSource.setSystemId(xslt);

        // Prepare the transformation
        Transformer transformer = factory.newTransformer(xsltSource);
        for(String[] p: params) {
            transformer.setParameter(p[0], p[1]);
        }

        // Obtain the input file
        StreamSource text = new StreamSource(startFile);
        text.setSystemId(startFile.getAbsolutePath());
        transformer.transform(text, new StreamResult(endFile));
    }





    /**
     * Method that prepares the parameters for those requiring a list.
     * @param fileName The name of the file that is going to be used.
     * @param max The maximum name of iterations.
     * @param paramName The names of the params on the loop, without number.
     * @param lastPName The name of the last param.
     * @param lastPPath The path of the last param.
     * @return The parameters as a linked list of arrays of string.
     */
    private LinkedList<String[]> getParamsList(String fileName, int max, String paramName,
                                               String lastPName, String lastPPath) {
        // Prepare the uri
        String uri = fileName.replace(File.separator, "/");

        // Create a list
        LinkedList<String[]> parameters = new LinkedList<>();

        // Add basic parameters
        for(int i=1; i<=max; i++) {
            // Create the element
            String[] param = {paramName + i, "file:/" + uri};
            parameters.add(param);
        }

        // Create the last one
        String[] lastParam = {lastPName,
                (new File(lastPPath)).getAbsolutePath().replace(File.separator, "/")
        };
        parameters.addLast(lastParam);

        // Return the list
        return parameters;
    }






    /**
     * Method that generates a random name of seven digitis to
     * be used as name of the *.mod generated file.
     * @return String with the random name.
     */
    private String fileName() {
        Random r = new Random();
        return String.valueOf(r.nextInt(10000000) + 1000000);
    }


}
