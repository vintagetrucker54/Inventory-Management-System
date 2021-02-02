package Model;

/** This the main class for the InHouse part.
 * @author Jonathan Fletcher
 */

public class InHouse extends Part{

    private int machineId;

    /**
     * The InHouse Part constructor.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** The getMachineID method gets the machine ID of the InHouse part.
     @return Returns the machine ID of the InHouse part.
     */
    public int getMachineId() {
        return machineId;
    }

    /** The setMachineId method sets the machine ID of the InHouse part.
     @param machineId The machine ID of the InHouse part.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}