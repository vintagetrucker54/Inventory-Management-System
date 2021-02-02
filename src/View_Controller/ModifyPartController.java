package View_Controller;
/** @ author Jonathan Fletcher */
import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/** main Class for the Modify Part window. */
public class ModifyPartController implements Initializable {

    private Inventory inventory;
    private Part part;
    private Stage stage;
    private Parent scene;
    private int autoID;
    private int selectedIndex;

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
    private TextField partSource;
    @FXML
    private TextField max;
    @FXML
    private Label partSourceLabel;
    @FXML
    private Button savePart;
    @FXML
    private Button cancel;

    /**
     Constructor for test inventory and selected items.
     @param inventory
     */
    public ModifyPartController(Inventory inventory, int index) {
        this.inventory = inventory;
        this.selectedIndex = index;
    }

    /**
     The initialize method populates the selected Part values.
     It also validates and sets the appropriate radio button.
     @param url
     @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part part = inventory.getAllParts().get(selectedIndex);
        id.setText(String.valueOf(part.getId()).trim());
        name.setText(part.getName().trim());
        price.setText(String.valueOf(part.getPrice()).trim());
        stock.setText(String.valueOf(part.getStock()).trim());
        min.setText(String.valueOf(part.getMin()).trim());
        max.setText(String.valueOf(part.getMax()).trim());

        if(part instanceof InHouse){
            inHouse.setSelected(true);
            outsourced.setSelected(false);
            partSource.setText(String.valueOf(((InHouse) part).getMachineId()).trim());
            partSourceLabel.setText("In-House");
        } else if(part instanceof Outsourced){
            outsourced.setSelected(true);
            inHouse.setSelected(false);
            partSource.setText(((Outsourced) part).getCompanyName().trim());
            partSourceLabel.setText("Outsourced");
        }
    }


    /**
     This method will set partSourceLabel txt when InHouse Radio Button is selected.
     @throws IOException
     */
    @FXML
    void selectInHouse () throws IOException {
        if(inHouse.isSelected()){
            partSourceLabel.setText("Machine ID");
        }
    }

    /**
     This method will set partSourceLabel txt when outsourced Radio Button is selected.
     @throws IOException
     */
    @FXML
    void selectOutsourced () throws IOException {
        if(outsourced.isSelected()){
            partSourceLabel.setText("Company Name");
        }
    }


    /**
     The savePart method gets values from text fields.
     It then validates whether or not min < max, then checks if part is InHouse or Outsourced.
     It also validates that correct values are in appropriate fields.
     @param event
     @throws IOException
     */
    @FXML
    void savePart(MouseEvent event) throws IOException {
        try {
        int partID = Integer.valueOf(id.getText().trim());
        String partName = name.getText();
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
                        inventory.updatePart(selectedIndex, newPart);
                    } else if (outsourced.isSelected()) {
                        inHouse.setSelected(false);
                        outsourced.setSelected(true);
                        partSourceLabel.setText("Company Name");
                        String partCompanyName = partSource.getText().trim();
                        Outsourced newPart = new Outsourced(partID, partName, partPrice, partStock, partMin, partMax, partCompanyName);
                        inventory.updatePart(selectedIndex, newPart);
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Saved");
                    alert.setContentText("Part Updated!");
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


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid inputs for one or more fields.");
            alert.showAndWait();

        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    /**
     isInteger checks if a given string is an int.
     @param string The string to validate.
     @return Returns a boolean of whether the string parameter is an Integer or not.
     */
    private boolean isInteger(String string){
        try {
            Integer.parseInt(string);
            return true;
        }
        catch( NumberFormatException e) {
            return false;
        }
    }

    /**
     Alt method for returning to main screen if needed.
     @param event
     @throws IOException
     */
    @FXML
    void partCancel (MouseEvent event) throws IOException {
        returnToMainScreen(event);
    }


    /**
     Return to Main Screen. User able to cancel action.
     @param event
     @throws IOException
     */
    private void returnToMainScreen(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit to main menu?");
        Optional<ButtonType> result = alert.showAndWait();

        if (!result.isPresent()) {
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
            Part part = inventory.getAllParts().get(selectedIndex);
            id.setText(String.valueOf(part.getId()));
            name.setText(part.getName());
            price.setText(String.valueOf(part.getPrice()));
            stock.setText(String.valueOf(part.getStock()));
            min.setText(String.valueOf(part.getMin()));
            max.setText(String.valueOf(part.getMax()));
            if(part instanceof InHouse){
                inHouse.setSelected(true);
                partSource.setText(String.valueOf(((InHouse) part).getMachineId()));
            } else if(part instanceof Outsourced){
                outsourced.setSelected(true);
                partSource.setText(((Outsourced) part).getCompanyName());
            }
        }
    }

}