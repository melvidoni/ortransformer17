package transformations.ort;


import javafx.concurrent.Task;

import java.io.File;
import java.util.Random;


/**
 * Class that guides the transformations to bge performed, called
 * by the corresponding listener. Works as an independent task.
 * @author Melina Vidoni, INGAR CONICET-UTN.
 */
public class TranslationTask extends Task {



    @Override
    protected Boolean call() throws Exception {
        // Get the transformation status
        TransformationStatus tStatus = TransformationStatus.getInstance(false);


        // STEP 1
        // Get the temporal directory according to the operative system
        String tempPath = System.getProperty("java.io.tmpdir") + File.separator + "ORTransformer";
        String tempName = fileName();
        File tempDir = new File(tempPath);
        tempDir.mkdirs();
        tStatus.setProgressProperty(10);


        // STEP 2
        // Translate from UML to XML





/*
        // Translate from UML to XML
        UMLtoXML guiaXML = new UMLtoXML(path, number, uml);
        guiaXML.transformToXML();
        vp.setProgressValue(25);

        // Transform towards the metamodel
        MetamodelMapping guiaMetamodelo = new MetamodelMapping(path, number);
        guiaMetamodelo.obtainXML();
        vp.setProgressValue(40);

        // First mapping: types
        TypeMapping guiaTipos = new TypeMapping(path, number);
        guiaTipos.obtainXML();
        vp.setProgressValue(55);

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
