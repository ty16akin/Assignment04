/*****************************************************************
 * File:  Inventory.java
 * Course materials CST 8277
 * @author Mike Norman
 *
 */
package com.algonquincollege.cst8277.regionalInventory.dao;

public interface Inventory {

    /**
     * @return the primary key from database
     */
    public int getId();
    public void setId(int id);

    /**
     * @return the name of the retailer
     */
    public String getRetailerName();
    /**
     * @param new name of the retailer
     */
    public void setRetailerName(String retailerName);

    /**
     * @return the name of retailer
     */
    public String getRegion();
    /**
     * @param retailer new name of retailer
     */
    public void setRegion(String firstName);

    /**
     * @return the inventory level
     */
    public int getInventoryLevel();
    /**
     * @param new inventory level
     */
    public void setInventoryLevel(int level);

}