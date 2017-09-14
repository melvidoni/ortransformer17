package transformations.ort;


import javafx.concurrent.Task;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.nio.file.Path;
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





    @Override
    protected Boolean call() throws Exception {
        // Get the transformation status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);


        // STEP 1
        // Get the temporal directory according to the operative system
        String tempPath = System.getProperty("java.io.tmpdir") + "ORTransformer";
        String tempName = fileName();
        File tempDir = new File(tempPath);
        tempDir.mkdirs();
        updateProgress(10,100);
        updateMessage("10% Completed. Temp files loaded successfully.");



        // STEP 2
        // Translate from UML to XML
        File preFile = new File(tempPath + File.separator + tempName + ".xml.pre");
        UMLtoXML.transformToXML(preFile,true, null);
        updateProgress(25, 100);
        updateMessage("25% Completed. XML translation compleated successfully.");



        // STEP 3
        // Transform towards the metamodel
        File modFile = new File(tempPath + File.separator + tempName + ".xml.mod");

        LinkedList<String[]> paramsList1 = new LinkedList<>();
        String[] params1 = {"UMLMetamodel", "UMLMetamodel.xsd"};
        paramsList1.add(params1);

        transform("files/UMLToModel.xslt", paramsList1, preFile, modFile);
        updateProgress(40, 100);
        updateMessage("40% Completed. Metamodel transformation achieved.");



        // STEP 4
        // First mapping, using types
        File tpoFile = new File(tempPath + File.separator + tempName + ".xml.tpo");
        transform("files/FirstMapping.xslt", getParamsFirstStep(modFile.getAbsolutePath()), modFile, tpoFile);
        updateProgress(55, 100);
        updateMessage("55% Completed. Types transformation achieved.");






/*



        // Second mapping: tables
        TableMapping guiaTablas = new TableMapping(path, number, Implementation.getImplementacion(impl));
        guiaTablas.obtainXMLOR();
        vp.setProgressValue(70);

        // Translate to SQL
        XMLtoSQL sqlGuide = new XMLtoSQL(path, number);
        String types = sqlGuide.translateORTypes();
        String tables = sqlGuide.translateORTables(Implementation.getImplementacion(impl));
        vp.setProgressValue(85);

        // Remove the files
        this.removeOR(path, number);
        vp.setProgressValue(100);
        vp.dispose();

        // Call the results window
        ResultsWindow vr = new ResultsWindow(ortMain, types, tables, true);
        vr.setLocationRelativeTo(null);
        ortMain.setEnabled(false);
        vr.setVisible(true);
*/




        return true;
    }







    private void transform(String xslt, LinkedList<String[]> params, File startFile, File endFile)
            throws TransformerException {

        // Prepare the factory and source
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xsltSource = new StreamSource(new File(xslt));

        // Prepare the transformation
        Transformer transformer = factory.newTransformer(xsltSource);
        for(String[] p: params) {
            transformer.setParameter(p[0], p[1]);
        }

        // Obtain the input file
        Source text = new StreamSource(startFile);
        transformer.transform(text, new StreamResult(endFile));
    }








    private LinkedList<String[]> getParamsFirstStep(String modName) {
        // Prepare the uri
        String uri = modName.replace(File.separator, "/");

        // Create a list
        LinkedList<String[]> parameters = new LinkedList<>();

        // Add basic parameters
        for(int i=1; i<=8; i++) {
            // Create the element
            String[] param = {"Param_" + i, "file:///" + uri};
            parameters.add(param);
        }

        // Create the last one
        String[] lastParam = {"SQL2003Metamodel_data_1",
                (new File("files/SQL2003Metamodel_data.xsd"))
                        .getAbsolutePath().replace(File.separator, "/")
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
