/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ public class WikiRecord {
/*    */   String name;
/*    */   
/*    */   public WikiRecord(String name, String button, String desc) {
/*  7 */     this.name = name;
/*  8 */     this.desc = desc;
/*  9 */     this.button = button;
/*    */   }
/*    */   String desc; String button;
/*    */   public String getDesc() {
/* 13 */     return this.desc;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 17 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getButton() {
/* 21 */     return this.button;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\WikiRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */