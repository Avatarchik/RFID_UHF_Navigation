package de.unierlangen.like.rfid;

/**
 * Class describes generic RFID tag. Objects supposed to be created by reader or
 * its simulation.
 * 
 * @author Kate Lyavinskova
 * 
 */
public class GenericTag extends Object {

    private String epc;
    private int rssi;
    /** Describes whether tag was or was not read(in range) */
    private boolean isRead;

    /**
     * Debug constructor used to create tags with certain RSSI value and
     * (random) EPC
     * 
     * @param epc
     * @param rssi
     * @param isRead
     */
    public GenericTag(String epc, int rssi, boolean isRead) {
        super();
        this.rssi = rssi;
        this.isRead = isRead;
        this.epc = epc;
        // epc = (int)(Math.random()*100);
    }

    // Getters and setters
    public String getEpc() {
        return epc;
    }

    public int getRssi() {
        return rssi;
    }

    public boolean isRead() {
        return isRead;
    }
}
