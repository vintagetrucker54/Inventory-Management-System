package View_Controller;
/** @ author Jonathan Fletcher */
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.IllegalStateException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** main Class for the the Add Part window. */
public class AddPartController implements Initializable {

    private Inventory inventory;
    private Stage stage;
    private Parent scene;
    private int autoID;

    @FXML
    private TextField partSource;
    @FXML
    private Label partSourceLabel;
    @FXML
    private RadioButton inHouse;
    @FXML
    private ToggleGroup partType;
    @FXML
    private RadioButton outsourced;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField stock;
    @FXML
    private TextField price;
    @FXML
    private TextField min;
    @FXML
    private TextField machineId;
    @FXML
    private TextField max;
    @FXML
    private Button saveNewPart;
    @FXML
    private Button cancel;


    /**
     * Constructor that get test inventory in order to create new ID.
     *
     * @param inventory
     */
    public AddPartController(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * The initialize method populates a unique ID.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoID = -1;
        for (Part p : inventory.getAllParts()) {
            if (p.getId() > autoID) {
                autoID = p.getId();
            }
        }
        if (autoID > 0) {
            autoID++;
            id.setText(String.valueOf(autoID));
        }
    }

    /**
     * This method will set partSourceLabel txt when InHouse Radio Button is selected.
     *
     * @throws IOException
     */
    @FXML
    void selectInHouse() throws IOException {
        if (inHouse.isSelected()) {
            partSourceLabel.setText("Machine ID");
        }
    }

    /**
     * This method will set partSourceLabel txt when outsourced Radio Button is selected.
     *
     * @throws IOException
     */
    @FXML
    void selectOutsourced() throws IOException {
        if (outsourced.isSelected()) {
            partSourceLabel.setText("Company Name");
        }
    }

    /**
     * The savePart method gets values from text fields.
     * It then validates whether or not min < max, then checks if part is InHouse or Outsourced.
     * It also validates that correct values are in appropriate fields.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void savePart(MouseEvent event) throws IOException {
        try {
            int partID = autoID;
            String partName = name.getText().trim();
            double partPrice = Double.valueOf(price.getText().trim());
            int partStock = Integer.valueOf(stock.getText().trim());
            int partMin = Integer.valueOf(min.getText().trim());
            int partMax = Integer.valueOf(max.getText().trim());

                if (partMin <= partMax) {
                    if ((partMin <= partStock) && (partStock <= partMax)) {
                        if (inHouse.isSelected()) {
                            inHouse.setSelected(true);
                            outsourced.setSelected(false);
                            partSourceLabel.setText("Machine ID");
                            int partMachineID = Integer.parseInt(partSource.getText().trim());
                            InHouse newPart = new InHouse(partID, partName, partPrice, partStock, partMin, partMax, partMachineID);
                            inventory.addPart(newPart);
                        } else if (outsourced.isSelected()) {
                            inHouse.setSelected(false);
                            outsourced.setSelected(true);
                            partSourceLabel.setText("Company Name");
                            String partCompanyName = partSource.getText().trim();
                            Outsourced newPart = new Outsourced(partID, partName, partPrice, partStock, partMin, partMax, partCompanyName);
                            inventory.addPart(newPart);
                        }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Saved");
                        alert.setContentText("Part Added!");
                        alert.showAndWait();
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
                        MainScreenController controller = new MainScreenController(inventory);
                        fxmlLoader.setController(controller);
                        fxmlLoader.load();
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Parent scene = fxmlLoader.getRoot();
                        stage.setScene(new Scene(scene));
                        stage.show();
                    } else {
                        throw new IllegalStateException("Inventory must be within Min and Max.");
                    }

                } else if ((partMin > partStock) || (partStock > partMax) || (partMin > partMax) || (partMax < partMin)){
                    throw new IllegalStateException("Min must be less than Max");
                }

            } catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid inputs for one or more fields.");
            alert.showAndWait();
                } catch(IllegalStateException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();            }

    }
             /*   if (partMin > partMax) {
        throw new IllegalStateException("Min must be less than Max.");
    }
                if (cancel.isCancelButton()) {
        returnToMainScreen(event);
    }
                if ((partMin > partStock) || (partStock > partMax)) {
        throw new IllegalStateException("Inventory must be within Min and Max");
    }
*/





    /**
     * isInteger checks if a given string is an int.
     *
     * @param string The string to validate.
     * @return Returns a boolean of whether the string parameter is an Integer or not.
     */
    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Alt method for returning to main screen if needed.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void partCancel(MouseEvent event) throws IOException {
returnToMainScreen(event);
    }


    /**
     * Return to Main Screen. User able to cancel action.
     *
     * @param event
     * @throws IOException
     */
    private void returnToMainScreen(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit to main menu?");
        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent()) {
            // alert - no button has been pressed.
        } else if (result.get() == ButtonType.OK) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } else if (result.get() == ButtonType.CANCEL) {
            alert.close();

                }

            }
        }


