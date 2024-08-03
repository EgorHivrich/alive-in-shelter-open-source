/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IabInterface
/*    */ {
/*    */   public static final String SKU_REMOVE_ADS = "removeads";
/*    */   public static final String SKU_REMOVE_ADS2 = "removeads2";
/*    */   public static final String SKU_REMOVE_ADS3 = "removeads3";
/*    */   public static final String SKU_PREMIUM = "extension";
/* 12 */   public static final String[] SKU_COINS = new String[] { "addcoins", "addcoins2", "addcoins3", "addcoins4", "addcoins5", "coins5" };
/*    */   public static final int RC_REQUEST = 10001;
/*    */   
/*    */   void removeAds(int paramInt);
/*    */   
/*    */   void processPurchases();
/*    */   
/*    */   boolean removed();
/*    */   
/*    */   boolean isPremium();
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\IabInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */