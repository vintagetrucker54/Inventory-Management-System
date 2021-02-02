package View_Controller;
/**
 *
 * @ author Jonathan Fletcher
 *
 */
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the main class for the initial screen. */
public class MainScreenController implements Initializable {

    Inventory inventory;
    Stage stage;
    Parent scene;

    private ObservableList<Part> partList = FXCollections.observableArrayList();
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private HBox mainHBox;
    @FXML
    private AnchorPane partPane;
    @FXML
    private TextField partSearchField;
    @FXML
    private Button partSearchButton;
    @FXML
    private Button partAddButton;
    @FXML
    private Button partModifyButton;
    @FXML
    private Button partDeleteButton;
    @FXML
    private AnchorPane productPane;
    @FXML
    private TextField productSearchField;
    @FXML
    private Button productSearchButton;
    @FXML
    private Button productAddButton;
    @FXML
    private Button productModifyButton;
    @FXML
    private Button productDeleteButton;
    @FXML
    private Button exitAppButton;

    /**
     Constructor
     @param inventory The inventory object.
     */
    public MainScreenController(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     Generates data for tables using the two methods.
     @param url
     @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePartsTable();
        generateProductsTable();
    }

    /**
     This method uses inventory values to generate parts table.
     */
    private void generatePartsTable(){
        partList.setAll(inventory.getAllParts());
        partTableView.setItems(partList);
        partTableView.refresh();
    }

    /**
     This method uses inventory values to generate products table.
     */
    private void generateProductsTable(){
        productList.setAll(inventory.getAllProducts());
        productTableView.setItems(productList);
        productTableView.refresh();
    }



    /**
     searchParts allows user to search by ID or Name and displays matches.
     Also alerts user if no part was entered into the txt field.
     @param event
     */
    @FXML
    void searchPart(KeyEvent event){
        String partToSearch = partSearchField.getText().trim();
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();

        // If empty, show all the Parts
        if(partToSearch.isEmpty()) {
            partTableView.setItems(partList);

        }

        // If string is int, search by partId and partName
        else if(isInteger(partToSearch)){
            filteredParts.addAll(inventory.lookupPart(partToSearch));

            // Skip adding the Part if it already exists from the previous add
            if(!filteredParts.contains(inventory.lookupPart(Integer.parseInt(partToSearch)))){
                filteredParts.add(inventory.lookupPart(Integer.parseInt(partToSearch)));
            }
            partTableView.setItems(filteredParts);
        }

        // Else lookup all Parts containing string
        else{
            filteredParts.addAll(inventory.lookupPart(partToSearch));
            partTableView.setItems(filteredParts);
        }

        //Alert if empty text if searched
        if(partSearchField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Search Error");
            alert.setContentText("Must enter part ID or name");
            alert.show();
        }
        partTableView.refresh();
    }

    /**
     searchProducts allows user to search by ID or Name and displays matches.
     Also alerts user if no product was entered into the txt field.
     @param event
     */
    @FXML
    void searchProduct(KeyEvent event){
        String productToSearch = productSearchField.getText().trim();
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

        // If empty, show all the Products
        if(productToSearch.isEmpty()) {
            productTableView.setItems(productList);
        }

        // If string is Integer, search by productId and productName
        else if(isInteger(productToSearch)){
            filteredProducts.addAll(inventory.lookupProduct(productToSearch));

            // Skip adding the Product if it already exists from the previous add
            if(!filteredProducts.contains(inventory.lookupProduct(Integer.parseInt(productToSearch)))){
                filteredProducts.add(inventory.lookupProduct(Integer.parseInt(productToSearch)));
            }
            productTableView.setItems(filteredProducts);
        }

        // Else lookup all Products containing the search string
        else{
            filteredProducts.addAll(inventory.lookupProduct(productToSearch));
            productTableView.setItems(filteredProducts);
        }

        //Alert if empty text if searched
        if(productSearchField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Search Error");
            alert.setContentText("Must enter product ID or name");
            alert.show();

            productTableView.refresh();
        }
    }

    /**
     Passes inventory to AddPart page, and confirms user action.
     @param event
     @throws IOException
     */
    @FXML
    void addPart(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to add a part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
        } else if (result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/AddPart.fxml"));
            AddPartController controller = new AddPartController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } else if (result.get() == ButtonType.CANCEL) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /**
     Passes inventory to AddProduct page, and confirms user action.
     @param event
     @throws IOException
     <p><b> Part B: LOGICAL ERROR - Initially, after following along in the tutorials/webinars, there was only a single
     alert used for pathing to other screens. However, I wanted to give the user the ability to cancel if needed.
     Thus, I added the alert button functionalities for "ok" & "cancel". </b></p>
     */
    @FXML
    void addProduct(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to add a product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
        } else if (result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } else if (result.get() == ButtonType.CANCEL) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController(inventory);
            fxmlLoader.setController(controller);
            fxmlLoader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /**
     deletePart removes selected Part from Inventory.
     Also validates whether or not a part was selected.
     @param event
     */
    @FXML
    void deletePart(MouseEvent event) {
        Part selected = partTableView.getSelectionModel().getSelectedItem();

        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No part selected!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected Part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                inventory.deletePart(selected);
                partList.remove(selected);
                partTableView.refresh();
            }
        }
    }

    /**
     deleteProduct removes selected Product from Inventory.
     Also validates whether a product was selected or if the product has associated parts.
     @param event
     */
    @FXML
    void deleteProduct(MouseEvent event) {
        Product selected = productTableView.getSelectionModel().getSelectedItem();

        if(selected == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No product selected!");
            alert.showAndWait();
        } else {
            if (productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error deleting Product");
                alert.setContentText("Cannot delete, Product has associated parts!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected Product?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    inventory.deleteProduct(selected);
                    productList.remove(selected);
                    productTableView.refresh();
                }
            }
        }
    }


    /**
     Passes inventory and selected Part index to the ModifyPart page.
     Also validates whether a part was selected or not and verifies that the user wishes to modify.
     @param event
     @throws IOException
     */
    @FXML
    void modifyPart(MouseEvent event) throws IOException {
        int selected = partTableView.getSelectionModel().getSelectedIndex();

        if(selected < 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No part selected!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Modify selected Part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
            } else if (result.get() == ButtonType.OK) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/View_Controller/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inventory, selected);
                fxmlLoader.setController(controller);
                fxmlLoader.load();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = fxmlLoader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            } else if (result.get() == ButtonType.CANCEL) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
                MainScreenController controller = new MainScreenController(inventory);
                fxmlLoader.setController(controller);
                fxmlLoader.load();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = fxmlLoader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }


    /**
     Passes inventory and selected Product index to the ModifyProduct page.
     Also validates whether no product was selected and verifies that the user wishes to modify.
     @param event
     @throws IOException
     */
    @FXML
    void modifyProduct(MouseEvent event) throws IOException {
        int selected = productTableView.getSelectionModel().getSelectedIndex();

        if (selected < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No product selected!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Modify selected Product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent()) {
            } else if (result.get() == ButtonType.OK) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inventory, selected);
                fxmlLoader.setController(controller);
                fxmlLoader.load();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = fxmlLoader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            } else if (result.get() == ButtonType.CANCEL) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/View_Controller/MainScreen.fxml"));
                MainScreenController controller = new MainScreenController(inventory);
                fxmlLoader.setController(controller);
                fxmlLoader.load();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = fxmlLoader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

            }
        }
    }


    /**
     * Check if string is an int.
     * @param string The string to validate.
     * @return Returns a boolean value of whether the string parameter is an Integer or not.
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
     * Exit program
     * @param event
     */
    @FXML
    void exitApplication(MouseEvent event) {
        Platform.exit();
    }
}