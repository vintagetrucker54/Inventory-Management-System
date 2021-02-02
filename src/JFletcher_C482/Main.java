package JFletcher_C482;
/** @author Jonathan Fletcher */

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** main class for the MAIN application.
 * JavaDocs location: 482 INV MGMT\Javadocs
 * */
public class Main extends Application {

    /**
     Starts the primary stage for the application.
     @param primaryStage The default JavaFX stage.
     @throws Exception Throws exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Inventory inventory = new Inventory();
        testData(inventory);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inventory);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("C482 - Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     This is the main method for the program.
     @param args Arguments from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /** Test inventory data for easier evaluation.
     @param inventory
     */
    private void testData(Inventory inventory){
        Part test1 = new InHouse(1, "B part", 0.99, 50, 1, 100, 101);
        Part test2 = new InHouse(2, "C part", 1.99, 50, 1, 100, 102);
        Part test3 = new Outsourced(3, "D part", 9.99, 100, 1, 100, "Alpha Company");
        Part test4 = new Outsourced(4, "E part", 99.99, 1, 1, 100, "Beta Company");
        inventory.addPart(test1);
        inventory.addPart(test2);
        inventory.addPart(test3);
        inventory.addPart(test4);

        Product prod1 = new Product(1, "F product", 9.99,10,1,100);
        Product prod2 = new Product(2, "G product", 15.99,10,1,100);
        prod1.addAssociatedPart(test1);
        prod1.addAssociatedPart(test2);
        prod2.addAssociatedPart(test1);
        prod2.addAssociatedPart(test2);
        inventory.addProduct(prod1);
        inventory.addProduct(prod2);

    }
}