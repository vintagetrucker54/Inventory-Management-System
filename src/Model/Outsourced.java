package Model;

/** This the main class for the Outsourced Part.
 * @author Jonathan Fletcher
 */
public class Outsourced extends Part {

    private String companyName;

    /**
     Outsourced Part constructor.
     @param id
     @param name
     @param price
     @param stock
     @param min
     @param max
     @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** getCompanyName returns the company name of the Outsourced part.
     @return Returns the name of the company.
     */
    public String getCompanyName() {
        return companyName;
    }

    /** setCompanyName sets the company name of the Outsourced part.
     @param companyName The name of the company.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}