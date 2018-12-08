/*
This is the FXML conroller for the Paper Manager
 */
package com.monsees.mailpiececalc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaperMgrFXMLController implements Initializable {

    //initialize the connection to the paper database
    private PaperDB paperDB = new PaperDB();

    @FXML
    private ListView lst_Papers, lst_Envelopes;

    @FXML
    private ComboBox cmb_PaperType;

    @FXML
    private TextField txt_PaperName, txt_PaperWeight, txt_PaperCaliper,
            txt_EnvName, txt_EnvWeight, txt_EnvCaliper;

    @FXML
    private Label lbl_PaperError, lbl_EnvError;

    private void initializePapers() {

        ArrayList allPapers = new ArrayList();
        allPapers = paperDB.getPapers("ALL", "ALL");
        ObservableList olAllPapers = FXCollections.observableArrayList(allPapers);
        lst_Papers.setItems(olAllPapers);

    }

    private void initializeEnvelopes() {
        ArrayList allEnvelopes = new ArrayList();
        allEnvelopes = paperDB.getEnvelopes();
        ObservableList olAllEnvelopes = FXCollections.observableArrayList(allEnvelopes);
        lst_Envelopes.setItems(olAllEnvelopes);
    }

    @FXML
    private void addPaper(ActionEvent event) {

        if (isPosDouble(txt_PaperCaliper) && (isPosDouble(txt_PaperWeight))) {
            double paperCaliper = Double.parseDouble(txt_PaperCaliper.getText());
            double paperWeight = Double.parseDouble(txt_PaperWeight.getText());
            String paperName = txt_PaperName.getText();
            String paperType = cmb_PaperType.getValue().toString();

            if (paperDB.paperExists(paperName)) {
                lbl_PaperError.setText("Paper name already exists.");
            } else {
                lbl_PaperError.setText("");
                paperDB.addPaper(paperName, paperType, paperWeight, paperCaliper);
            }
        }
        initializePapers();
    }

    @FXML
    private void removePaper(ActionEvent event) {

        String selectedPaper = lst_Papers.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectedPaper);
        paperDB.deletePaper(selectedPaper);
        initializePapers();
    }

    @FXML
    private void addEnvelope(ActionEvent event) {

        if (isPosDouble(txt_EnvCaliper) && (isPosDouble(txt_EnvWeight))) {
            double envCaliper = Double.parseDouble(txt_EnvCaliper.getText());
            double envWeight = Double.parseDouble(txt_EnvWeight.getText());
            String envName = txt_EnvName.getText();

            if (paperDB.envelopeExists(envName)) {
                lbl_EnvError.setText("Envelope name already exists.");
            } else {
                lbl_EnvError.setText("");
                paperDB.addEnvelope(envName, envWeight, envCaliper);
            }
        }
        initializeEnvelopes();
    }

    @FXML
    private void removeEnvelope(ActionEvent event) {

        String selectedEnvelope = lst_Envelopes.getSelectionModel().getSelectedItem().toString();
        System.out.println(selectedEnvelope);
        paperDB.deleteEnvelope(selectedEnvelope);
        initializeEnvelopes();
    }

    private boolean isPosDouble(TextField tf) {

        String value = tf.getText();

        Pattern p = Pattern.compile("\\d{0,7}([\\.]\\d{0,4})?");
        Matcher m = p.matcher(value);

        try {
            if (m.matches() && (Double.parseDouble(value) > 0)) {
                return (true);
            } else {
                return (false);
            }
        } catch (Exception e) {
            return (false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //if program is being run on a mac, use the macOS application menu bar
        //mnuMain.setUseSystemMenuBar(true);
        //Populate the list of papers for the papers tab pane
        initializePapers();

        //Populate the list of envelopes for the envelopes tab pane
        initializeEnvelopes();

        //Populate the list of paper types in the paper types combo box
        ArrayList paperTypes = new ArrayList();
        paperTypes = paperDB.getAllPaperTypes();
        ObservableList olPaperTypes = FXCollections.observableArrayList(paperTypes);
        cmb_PaperType.setItems(olPaperTypes);

    }
}
