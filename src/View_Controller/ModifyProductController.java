package View_Controller;
/** @ author Jonathan Fletcher */
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/** main Class for the Modify Product window. */
public class ModifyProductController implements Initializable {

    private Inventory inventory;
    private Stage stage;
    private Parent scene;
    private int selectedIndex;

    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> availableParts;
    @FXML
    private TableView<Part> associatedParts;
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
    private TextField max;
    @FXML
    private Button saveNewPart;
    @FXML
    private Button cancel;
    @FXML
    private AnchorPane partPane;
    @FXML
    private TextField partSearchField;
    @FXML
    private Button partAddButton;
    @FXML
    private AnchorPane partPane1;
    @FXML
    private Button partAddButton1;

    /**
     Constructor for test inventory and selected items.
     @param inventory
     */
    public ModifyProductController(Inventory inventory, int index) {
        this.inventory = inventory;
        this.selectedIndex = index;
    }

    /**
     The initialize method populates the selected Product values and generates unique IDs.
     Then populates the available Part table and the associated Part table.
     @param url
     @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Product product = inventory.getAllProducts().get(selectedIndex);

        // Populate product data
        id.setText(String.valueOf(product.getId()));
        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        stock.setText(String.valueOf(product.getStock()));
        min.setText(String.valueOf(product.getMin()));
        max.setText(String.valueOf(product.getMax()));

        // Get available parts
        partList.setAll(inventory.getAllParts());
        availableParts.setItems(partList);
        availableParts.refresh();

        // Get associated parts
        selectedParts.setAll(product.getAllAssociatedParts());
        associatedParts.setItems(selectedParts);
        associatedParts.refresh();
    }

    /**
     This method adds a selected Part to the associated Parts list.
     User may add multiples of the same item if needed, throws error if nothing selected.
     @param event
     */
    @FXML
    void addPart(MouseEvent event) throws IOException {
        Part selectedPart = availableParts.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            selectedParts.add(availableParts.getSelectionModel().getSelectedItem());
            associatedParts.setItems(selectedParts);
            associatedParts.refresh();
        }
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No part selected!");
            alert.show();
        }
    }



    /**
     This event removes the selected Part from list of associated Parts.
     @param event
     */
    @FXML
    void removePart(MouseEvent event) {
        Part selected = associatedParts.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No part selected!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove the selected Part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedParts.remove(selected);
                associatedParts.refresh();
            }
        }
    }

    /**
     This will validates input fields and save the new Product.
     @param event
     @throws IOException
     <p><b>Part B: FUTURE ENHANCEMENT  - The saveProduct method did not initially allowed for all
     associated parts to be removed and saved. Therefore, it could not be removed from the Main product list.
     I decided to allow for all associated part removal in case the user had need to remove a specific product.
     </b></p>
     */
    @FXML
    void saveProduct(MouseEvent event) throws IOException {
        try {
            int productID = Integer.valueOf(id.getText());
            String productName = name.getText();
            double productPrice = Double.valueOf(price.getText());
            int productStock = Integer.valueOf(stock.getText());
            int productMin = Integer.valueOf(min.getText());
            int productMax = Integer.valueOf(max.getText());

            if (productMin <= productMax) {
                if ((productMin <= productStock) && (productStock <= productMax)) {

                    Product product = new Product(productID, productName, productPrice, productStock, productMin, productMax);
                    for (Part p : selectedParts) {
                        product.addAssociatedPart(p);
                    }
                    inventory.updateProduct(selectedIndex, product);
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

            } else if ((productMin > productStock) || (productStock > productMax) || (productMin > productMax) || (productMax < productMin)){
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
     This method gets text from search box by name or by ID and returns an ObservableList of type Part.
     @param event
     */
    @FXML
    void searchPart(KeyEvent event) {
        String partToSearch = partSearchField.getText();
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        // If empty, show all Parts
        if (partToSearch.isEmpty()) {
            availableParts.setItems(partList);
        }
        // If string is int, lookup by partId and partName
        else if (isInteger(partToSearch)) {
            filteredParts.addAll(inventory.lookupPart(partToSearch));

            // Skip adding the Part if it already exists from the previous add.
            if (!filteredParts.contains(inventory.lookupPart(Integer.parseInt(partToSearch)))) {
                filteredParts.add(inventory.lookupPart(Integer.parseInt(partToSearch)));
            }
            availableParts.setItems(filteredParts);
        }
        // Else, lookup all Parts containing the search string
        else {
            filteredParts.addAll(inventory.lookupPart(partToSearch));
            availableParts.setItems(filteredParts);
        }

        availableParts.refresh();
    }

    /**
     isInteger method checks if a given string is an int.
     @param string The string to validate.
     @return Returns boolean of whether the string parameter is an Integer or not.
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
     Alt method for returning to main screen if needed.
     @param event
     @throws IOException
     */
    @FXML
    void productCancel(MouseEvent event) throws IOException {
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

        } else if (result.get() == ButtonType.CANCEL){
            Product product = inventory.getAllProducts().get(selectedIndex);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
}