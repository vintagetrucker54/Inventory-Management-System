package Model;
/** @author Jonathan Fletcher */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This the main class for the Inventory arrays. */
public class Inventory {

    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    /** Inventory method associates the parts/product arrays. */
    public Inventory() {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    /**
     The addPart method adds a new Part to the Inventory and checks for null values.
     @param newPart The Part to add to Inventory.
     */
    public void addPart(Part newPart){
        if(newPart != null){
            allParts.add(newPart);
        }
    }

    /**
     The addProduct method adds a new Product to the Inventory and checks for null values.
     @param newProduct The Product to add to Inventory.
     */
    public void addProduct(Product newProduct){
        if(newProduct != null){
            allProducts.add(newProduct);
        }
    }

/** lookupPart checks the array IDs.
 @param partId
 */
    public Part lookupPart(int partId){
        for (Part p : this.getAllParts()) {
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    /** lookupProduct checks the array IDs.
     @param productId
     */
    public Product lookupProduct(int productId){
        for (Product p : this.getAllProducts()) {
            if (p.getId() == productId) {
                return p;
            }
        }
        return null;
    }

    /** lookupPart checks the array for char values in the arrays name.
     @param partName
     */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for (Part p : this.getAllParts()){
            if (p.getName().toLowerCase().contains(partName.toLowerCase())) {
                filteredParts.add(p);
            }
        }
        return filteredParts;
    }


    /** lookupProduct checks the array for char values in the arrays name.
     @param productName
     */
    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product p : this.getAllProducts()){
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                filteredProducts.add(p);
            }
        }
        return filteredProducts;
    }

    /** updatePart removes the old value and populates the new value for the selected part.
     @param index Index of the selected part.
     @param selectedPart Selected part to update.
     */
    public void updatePart(int index, Part selectedPart){
        allParts.remove(index);
        allParts.add(index, selectedPart);
    }

    /** updateProduct removes the old value and populates the new value for the selected product.
     @param index Index of selected product.
     @param selectedProduct Selected product to update.
     */
    public void updateProduct(int index, Product selectedProduct){
        allProducts.remove(index);
        allProducts.add(index, selectedProduct);
    }

    /** deletePart removes a Part from Inventory.
     @param selectedPart Selected part to delete.
     @return Returns a boolean of the result.
     */
    public boolean deletePart(Part selectedPart){
        if(selectedPart != null){
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    /** deleteProduct removes a Product from Inventory.
     @param selectedProduct Selected product to delete.
     @return Returns a boolean of the result.
     */
    public boolean deleteProduct(Product selectedProduct){
        if(selectedProduct != null){
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }

    /** getAllParts returns all the Parts in Inventory.
     @return Returns all of the Parts in Inventory.
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** getAllProducts returns all the Products in Inventory.
     @return Returns all of the Products in Inventory.
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}