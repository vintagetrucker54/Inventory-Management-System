package View_Controller;
/** @ author Jonathan Fletcher */
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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


/** main Class for the Add Product window. */
public class AddProductController implements Initializable {

    private Inventory inventory;
    private Stage stage;
    private Parent scene;
    private int autoID;

    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> accessParts;
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
     Constructor which passes in the test inventory data.
     @param inventory
     */
    public AddProductController(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     This initializes & populates the Part table and generates an ID.
     @param url
     @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partList.setAll(inventory.getAllParts());
        accessParts.setItems(partList);
        accessParts.refresh();

        autoID = -1;
        for(Product p : inventory.getAllProducts()){
            if(p.getId() > autoID) {
                autoID = p.getId();
            }
        }
        if(autoID > 0){
            autoID++;
            id.setText(String.valueOf(autoID));
        }
    }

    /**
     searchPart allows user to search by ID or Name and displays matches.
     Also alerts user if no part was entered into the txt field.
     @param event
     */
    @FXML
    void searchPart(KeyEvent event) {
        String partToSearch = partSearchField.getText().trim();
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        // If empty, show all Parts
        if(partToSearch.isEmpty()) {
            accessParts.setItems(partList);
        }
        // If string is int, lookup by partId and partName
        else if(isInteger(partToSearch)){
            filteredParts.addAll(inventory.lookupPart(partToSearch));

            // Skip adding the Part if it already exists from the previous add.
            if(!filteredParts.contains(inventory.lookupPart(Integer.parseInt(partToSearch)))){
                filteredParts.add(inventory.lookupPart(Integer.parseInt(partToSearch)));
            }
            accessParts.setItems(filteredParts);
        }
        // Else, lookup all Parts containing the search string
        else{
            filteredParts.addAll(inventory.lookupPart(partToSearch));
            accessParts.setItems(filteredParts);
        }

        accessParts.refresh();
    }

    /**
     This method adds a selected Part to the associated Parts list.
     User may add multiples of the same item if needed, throws error if nothing selected.
     @param event
     */
    @FXML
    void addPart(MouseEvent event) throws IOException {
        Part selectedPart = accessParts.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            selectedParts.add(accessParts.getSelectionModel().getSelectedItem());
            associatedParts.setItems(selectedParts);
            associatedParts.refresh();
        }
        if (selectedPart == null ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No part selected!");
                alert.show();

        }
    }

    /**
     This will validates input fields and save the new Product.
     @param event
     @throws IOException
     */
    @FXML
    void saveProduct(MouseEvent event) throws IOException {
        try {
            int productID = autoID;
            String productName = name.getText().trim();
            double productPrice = Double.valueOf(price.getText().trim());
            int productStock = Integer.valueOf(stock.getText().trim());
            int productMin = Integer.valueOf(min.getText().trim());
            int productMax = Integer.valueOf(max.getText().trim());

                    if (productMin <= productMax) {
                        if ((productMin <= productStock) && (productStock <= productMax)) {

                            Product product = new Product(productID, productName, productPrice, productStock, productMin, productMax);
                            for (Part p : selectedParts) {
                                product.addAssociatedPart(p);
                            }
                            inventory.addProduct(product);
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

            if (associatedParts == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("No Part selected!");
                            alert.showAndWait();
                        }


            }catch (NumberFormatException e) {
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
     This event removes the selected Part from list of associated Parts.
     @param event
     */
    @FXML
    void removePart(MouseEvent event) {
        Part selected = associatedParts.getSelectionModel().getSelectedItem();

        if(selected == null){
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
     isInteger method checks if a given string is an int.
     @param string The string to validate.
     @return Returns boolean of whether the string parameter is an Integer or not.
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

        } else if (result.get() == ButtonType.CANCEL) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
}