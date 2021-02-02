package Model;
/** @author Jonathan Fletcher */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This the main class for the Product. */
public class Product {

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Product Constructor.
     @param id The id of the product.
     @param name The name of the product.
     @param price The price of the product.
     @param stock The amount of products in stock.
     @param min The minimum amount of products.
     @param max The maximum amount of products.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
    @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
    @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
    @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
    @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /** addAssociatedPart adds a specific Part to the associated parts list of a product.
     Checks for null value.
    @param part The Part to add.
     */
    public void addAssociatedPart(Part part){
        if(part != null){
            associatedParts.add(part);
        }
    }

    /** deleteAssociatedPart deletes a specified Part from the list of associated parts of a product.
     Else if null value, does not delete.
     @param selectedAssociatedPart The selected Part to delete.
     @return Returns a boolean of the result.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(selectedAssociatedPart != null){
            return associatedParts.remove(selectedAssociatedPart);
        } else {
            return false;
        }
    }

    /** getAllAssociatedParts returns all Parts that were added to the associated parts list of a product.
     @return Returns an ObservableList of type Part.
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}