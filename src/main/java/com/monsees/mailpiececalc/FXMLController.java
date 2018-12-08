package com.monsees.mailpiececalc;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author justinmonsees
 */
public class FXMLController implements Initializable {

    @FXML
    private MenuBar mnuMain;

    @FXML
    private Label label;
    @FXML
    private ComboBox compType;

    //SELF MAILER VARIABLES
    @FXML
    private ComboBox cmb_SM_Paper;
    @FXML
    private TextField txt_SM_Height, txt_SM_Width, txt_SM_NumPanels;

    //INSERT VARIABLES
    @FXML
    private ComboBox cmb_INS_paper;
    @FXML
    private TextField txt_INS_Height, txt_INS_Width, txt_INS_NumPanels, txt_INS_NumPages;

    //BOOKLET VARIABLES
    @FXML
    private ComboBox cmb_BK_CvrPaper, cmb_BK_TxtPaper;
    @FXML
    private TextField txt_BK_Height, txt_BK_Width, txt_BK_NumPages;

    //ENVELOPE VARIABLES
    @FXML
    private ComboBox cmb_ENV_paper;

    //COMPONENT ANCHOR PANE VARIABLES
    @FXML
    private ListView lst_components;

    //COMPONENT TYPE GRIDPANE VARIABLES TO CONTROL VISIBILITY OF PANEL
    @FXML
    private AnchorPane ancPane_SelfMailer, ancPane_Insert, ancPane_Booklet, ancPane_Envelope;

    //RESULTS LABEL VARIABLES
    @FXML
    private Label lblWeightPerPiece, lblThicknessPerPiece, lblThicknessPer10Pieces, lblWeightPer10Pieces;

    //Global variable to store all of the components to be displayed in the listview box
    private ArrayList<MailComponent> MailCompArray;

    @FXML
    private void openPaperManager(ActionEvent event) {

        try {
            //Load the fxml class for the paper manager window
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("/fxml/DatabaseScene.fxml"));

            //Create a new scene for the paper manager window
            Scene paperMgrScene = new Scene(fxmlLoader.load(), 665, 550);

            //load the CSS file
            paperMgrScene.getStylesheets().add(this.getClass().getResource("/styles/Styles.css").toExternalForm());
            Stage paperMgrStage = new Stage();

            //Set the title and make sure that this window will appear over the main application window
            paperMgrStage.setTitle("Paper Manager");
            paperMgrStage.setScene(paperMgrScene);
            paperMgrStage.initModality(Modality.APPLICATION_MODAL);

            // register listener for window close event
            // When the paper manager window is closed, we need to intialize the papers
            // so that the main application's paper choice dropdowns will be updated with
            // any changes made to the database
            paperMgrStage.setOnCloseRequest(closeEvent -> {
                initializePapers();
                System.out.println("paper manager closed.");
            });

            //Load the actual window so it's visible to the user
            paperMgrStage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML file for Paper Manager");
            System.out.println(e);
        }

    }

    @FXML
    private void closeProgram(ActionEvent event) {
        System.exit(0);
    }

    //ACTION EVENT WHEN COMPONENT TYPE IS CHANGED
    // Change the visibility of the component grid based on component type
    @FXML
    private void compTypeChanged(ActionEvent event) {

        String selectedCompType = compType.getValue().toString();
        System.out.println("You clicked me!Current value is: " + selectedCompType);

        if (null != selectedCompType) {
            switch (selectedCompType) {
                case "Self Mailer":
                    ancPane_SelfMailer.setVisible(true);
                    ancPane_Booklet.setVisible(false);
                    ancPane_Insert.setVisible(false);
                    ancPane_Envelope.setVisible(false);
                    break;
                case "Insert":
                    ancPane_SelfMailer.setVisible(false);
                    ancPane_Booklet.setVisible(false);
                    ancPane_Insert.setVisible(true);
                    ancPane_Envelope.setVisible(false);
                    break;
                case "Booklet":
                    ancPane_SelfMailer.setVisible(false);
                    ancPane_Booklet.setVisible(true);
                    ancPane_Insert.setVisible(false);
                    ancPane_Envelope.setVisible(false);
                    break;
                case "Envelope":
                    ancPane_SelfMailer.setVisible(false);
                    ancPane_Booklet.setVisible(false);
                    ancPane_Insert.setVisible(false);
                    ancPane_Envelope.setVisible(true);
                    break;
                default:
                    break;
            }
        }

    }

    //This function is run when the user clicks the add button to add
    //a component to be calculated
    @FXML
    private void addComponent(ActionEvent event) {
        System.out.println("Component added");

        String selectedCompType = compType.getValue().toString().toLowerCase();

        if (selectedCompType.equals("self mailer")) {
            //validate that all the inputs are valid
            if (isPosDouble(txt_SM_Width) && isPosDouble(txt_SM_Height) && isPosInt(txt_SM_NumPanels)) {

                //if they are valid, create a new SelfMailer Object to store the data and add it to the array list of MailComp objects
                String selectedPaper = cmb_SM_Paper.getValue().toString();
                double smWidth = Double.parseDouble(txt_SM_Width.getText());
                double smHeight = Double.parseDouble(txt_SM_Height.getText());
                int smNumPanels = Integer.parseInt(txt_SM_NumPanels.getText());

                SelfMailer sm = new SelfMailer(selectedPaper, smWidth, smHeight, smNumPanels);
                MailCompArray.add(sm);

            }
        } else if (selectedCompType.equals("insert")) {

            //validate that all the inputs are valid
            if (isPosDouble(txt_INS_Width) && isPosDouble(txt_INS_Height) && isPosInt(txt_INS_NumPanels) && isPosInt(txt_INS_NumPages)) {

                //if they are all valid, create a new Insert object to store the data and add it to the array list of MailComp objects
                String selectedPaper = cmb_INS_paper.getValue().toString();
                double insWidth = Double.parseDouble(txt_INS_Width.getText());
                double insHeight = Double.parseDouble(txt_INS_Height.getText());
                int insNumPanels = Integer.parseInt(txt_INS_NumPanels.getText());
                int insNumPages = Integer.parseInt(txt_INS_NumPages.getText());

                Insert ins = new Insert(selectedPaper, insWidth, insHeight, insNumPanels, insNumPages);
                MailCompArray.add(ins);

            }
        } else if (selectedCompType.equals("booklet")) {

            //validate that all the inputs are valid
            if (isPosDouble(txt_BK_Height) && isPosDouble(txt_BK_Height) && isPosInt(txt_BK_NumPages)) {

                //if they are all valid, create a new Booklet object to store the data and add it to the array list of MailComp objects
                String selectedCVRPaper = cmb_BK_CvrPaper.getValue().toString();
                String selectedTXTPaper = cmb_BK_TxtPaper.getValue().toString();
                double bkWidth = Double.parseDouble(txt_BK_Width.getText());
                double bkHeight = Double.parseDouble(txt_BK_Height.getText());
                int bkNumPages = Integer.parseInt(txt_BK_NumPages.getText());

                Booklet bk = new Booklet(selectedCVRPaper, selectedTXTPaper, bkWidth, bkHeight, bkNumPages);
                MailCompArray.add(bk);
            }
        } else if (selectedCompType.equals("envelope")) {
            String envelopeName = cmb_ENV_paper.getValue().toString();

            Envelope env = new Envelope(envelopeName);
            MailCompArray.add(env);
        }
        //refresh the listview box to display all of the components in the list
        refreshListView();
    }

    //This function is run when the user clicks the remove button to remove
    //a component to be calculated
    @FXML
    private void removeComponent(ActionEvent event) {

        //only delete items if there are items to delete
        if (MailCompArray.size() > 0) {

            //get the index of the selected it in the components list
            int selectedIndex = lst_components.getSelectionModel().getSelectedIndex();

            //remove the item from the components array
            MailCompArray.remove(selectedIndex);

            //refresh the list
            refreshListView();
        }
    }

    //This function runs when the user clicks on the calculate button
    //It takes all the weights and thickness for each mail component in the list
    //and adds them up and then displays the totals to the user
    @FXML
    private void calculate(ActionEvent event) {

        //initialize the totals for weight and thickness
        double totalWeight = 0;
        double totalThickness = 0;

        //iterate through the MailCompArray and add the thickness and weight of each mail component to the running total
        //this will call the getThickness() and getWeight() function for each MailComponent
        //Mail Components may be different but all extend the same abstract class so they have those functions
        for (MailComponent mc : MailCompArray) {
            totalWeight += mc.getWeight();
            totalThickness += mc.getThickness();
        }

        DecimalFormat df = new DecimalFormat("###.###");

        //Set the labels for the weight and thickness of one mail piece
        lblWeightPerPiece.setText(String.valueOf(df.format(totalWeight)));
        lblThicknessPerPiece.setText(String.valueOf(df.format(totalThickness)));

        //Calculate and set the labels for the weight and thickness of 10 pieces
        double weightPer10Pieces = totalWeight * 10.0;
        double thicknessPer10Pieces = totalThickness * 10.0;
        lblWeightPer10Pieces.setText(String.valueOf(df.format(weightPer10Pieces)));
        lblThicknessPer10Pieces.setText(String.valueOf(df.format(thicknessPer10Pieces)));
    }

    //Anytime the lstComponent objects which stores a list of the mail components is modified
    // the list box in GUI must be updated to reflect those changes
    private void refreshListView() {

        //convert the MailCompArray to an observable list that can be displayed in the listview box
        ObservableList<String> lstComponents = FXCollections.observableArrayList();

        for (MailComponent mc : MailCompArray) {
            lstComponents.add(mc.toString());
        }

        lst_components.setItems(lstComponents);

    }

    final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    final PseudoClass validClass = PseudoClass.getPseudoClass("valid");

    //This class is used to validate the input values being entered from the user
    // and is used to validate that a decimal number was entered
    private class TextFieldValidateDecimal implements ChangeListener<Boolean> {

        private final TextField textField;

        TextFieldValidateDecimal(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

            String textFieldValue = this.textField.getText();

            Pattern p = Pattern.compile("\\d{0,7}([\\.]\\d{0,4})?");
            Matcher m = p.matcher(textFieldValue);

            if (newPropertyValue) {
                //System.out.println("Textfield on focus");
                this.textField.setText("");
                this.textField.pseudoClassStateChanged(errorClass, false);
                this.textField.pseudoClassStateChanged(validClass, false);
            } else {
                //System.out.println("Textfield out focus");

                if (!isPosDouble(this.textField)) {
                    this.textField.setText("Invalid");
                    //this.textField.pseudoClassStateChanged(errorClass, true);
                } else {
                    this.textField.pseudoClassStateChanged(validClass, true);
                }
            }
        }
    }

       //This class is used to validate the input values being entered from the user
    // and is used to validate that a integer number was entered
    private class TextFieldValidateInteger implements ChangeListener<Boolean> {

        private final TextField textField;

        TextFieldValidateInteger(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

            String textFieldValue = this.textField.getText();

            Pattern p = Pattern.compile("\\d*");
            Matcher m = p.matcher(textFieldValue);

            if (newPropertyValue) {
                //System.out.println("Textfield on focus");
                this.textField.setText("");
                this.textField.pseudoClassStateChanged(errorClass, false);
                this.textField.pseudoClassStateChanged(validClass, false);
            } else {
                //System.out.println("Textfield out focus");

                if (!m.matches()) {
                    this.textField.setText("Invalid");
                    this.textField.pseudoClassStateChanged(errorClass, true);
                } else {
                    this.textField.pseudoClassStateChanged(validClass, true);
                }
            }
        }
    }

    private boolean isPosDouble(TextField tf) {

        String value = tf.getText();

        Pattern p = Pattern.compile("\\d{0,7}([\\.]\\d{0,4})?");
        Matcher m = p.matcher(value);

        try {
            if (m.matches() && (Double.parseDouble(value) > 0)) {
                return (true);
            } else {
                tf.pseudoClassStateChanged(errorClass, true);
                return (false);
            }
        } catch (Exception e) {
            tf.pseudoClassStateChanged(errorClass, true);
            return (false);
        }
    }

    private boolean isPosInt(TextField tf) {

        String value = tf.getText();

        Pattern p = Pattern.compile("\\d{0,7}([\\.]\\d{0,4})?");
        Matcher m = p.matcher(value);

        try {
            if (m.matches() && (Integer.parseInt(value) > 0)) {
                return (true);
            } else {
                tf.pseudoClassStateChanged(errorClass, true);
                return (false);
            }
        } catch (Exception e) {
            tf.pseudoClassStateChanged(errorClass, true);
            return (false);
        }
    }

    private void initializePapers() {

        //initialize the connection to the paper database
        PaperDB paperDB = new PaperDB();

        ArrayList allPapers = new ArrayList();
        allPapers = paperDB.getPapers("ALL", "ALL");
        ObservableList olAllPapers = FXCollections.observableArrayList(allPapers);
        cmb_SM_Paper.setItems(olAllPapers);
        cmb_SM_Paper.getSelectionModel().selectFirst();
        cmb_INS_paper.setItems(olAllPapers);
        cmb_INS_paper.getSelectionModel().selectFirst();

        //***** WHEN INITIALIZING MAYBE SET UP A FILTER SYSTEM FOR PAPERS
        //IT IS POSSIBLE THAT USER SELECTS A TEXT WEIGHT PAPER FOR A COVER AND A LIGHTER PAPER FOR THE INSIDE
        //ADDITIONALLY SELF COVER BOOKS MIGHT USE COVER STOCK ONLY
        //FOR NOW JUST SELECT ALL STOCKS
        ArrayList coverPapers, textPapers = new ArrayList();
        coverPapers = paperDB.getPapers("ALL", "ALL");
        textPapers = paperDB.getPapers("ALL", "ALL");
        ObservableList olCoverPapers = FXCollections.observableArrayList(coverPapers);
        ObservableList olTextPapers = FXCollections.observableArrayList(textPapers);
        cmb_BK_CvrPaper.setItems(olCoverPapers);
        cmb_BK_CvrPaper.getSelectionModel().selectFirst();
        cmb_BK_TxtPaper.setItems(olTextPapers);
        cmb_BK_TxtPaper.getSelectionModel().selectFirst();

        ArrayList envelopes = new ArrayList();
        envelopes = paperDB.getEnvelopes();
        ObservableList olEnvelopes = FXCollections.observableArrayList(envelopes);
        cmb_ENV_paper.setItems(olEnvelopes);
        cmb_ENV_paper.getSelectionModel().selectFirst();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //if program is being run on a mac, use the macOS application menu bar
        //mnuMain.setUseSystemMenuBar(true);
        //mnuMain.setVisible(false);
        //Initialize the ArrayList that will hold the Mailing Components
        MailCompArray = new ArrayList<MailComponent>();

        //Populate the list of papers for the Self Mailer component & Insert component
        initializePapers();

        txt_SM_Width.focusedProperty().addListener(new TextFieldValidateDecimal(txt_SM_Width));
        txt_SM_Height.focusedProperty().addListener(new TextFieldValidateDecimal(txt_SM_Height));
        txt_SM_NumPanels.focusedProperty().addListener(new TextFieldValidateInteger(txt_SM_NumPanels));

        //Initialize the visible of the component panels so that the Self Mailer panel is the default panel
        // to be displayed when the program is first launched
        ancPane_SelfMailer.setVisible(true);
        ancPane_Booklet.setVisible(false);
        ancPane_Insert.setVisible(false);
        ancPane_Envelope.setVisible(false);

    }
}
