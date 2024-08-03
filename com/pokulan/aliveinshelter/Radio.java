/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Radio
/*    */ {
/*    */   int kanal;
/*    */   AliveInShelter aliveInShelter;
/*  9 */   String[] komunikaty = new String[5]; Radio(AliveInShelter aliveInShelter) {
/* 10 */     for (int i = 0; i < 5; ) { this.komunikaty[i] = "...shshhshshh..."; i++; }
/* 11 */      this.aliveInShelter = aliveInShelter;
/*    */   }
/* 13 */   public int getKanal() { return this.kanal; }
/* 14 */   public String getKomunikat() { return this.komunikaty[this.kanal]; }
/* 15 */   public String getKomunikat(int n) { return this.komunikaty[n]; }
/* 16 */   public void setKanal(int m) { this.kanal = m; } public void setKomunikaty(int m, String n) {
/* 17 */     this.komunikaty[m] = n;
/*    */   } public void addKanal(int z) {
/* 19 */     this.kanal += z;
/* 20 */     if (this.kanal > 4) { this.kanal = 0; }
/* 21 */     else if (this.kanal < 0) { this.kanal = 4; }
/*    */   
/*    */   } public void setPomoc(String com, int k) {
/* 24 */     for (int i = 0; i < 5; ) { this.komunikaty[i] = "...shshhshshh..."; i++; }
/* 25 */      this.komunikaty[k] = com;
/*    */   }
/*    */   
/*    */   public boolean isKomunikat() {
/* 29 */     if (!this.komunikaty[this.kanal].equals("...shshhshshh...")) return true; 
/* 30 */     return false;
/*    */   }
/*    */   public void clean() {
/* 33 */     for (int i = 0; i < 5; ) { this.komunikaty[i] = "...shshhshshh..."; i++; }
/* 34 */      this.kanal = 0;
/*    */   }
/*    */   public void setPogoda(int pogoda) {
/* 37 */     if (this.komunikaty[0].equals("...shshhshshh...") == true)
/* 38 */     { if (pogoda == 0) { this.komunikaty[0] = Zdania.TEXT[346]; }
/* 39 */       else if (pogoda == 1) { this.komunikaty[0] = Zdania.TEXT[348]; }
/* 40 */       else if (pogoda == 2) { this.komunikaty[0] = Zdania.TEXT[347]; }
/* 41 */       else if (pogoda == 3) { this.komunikaty[0] = Zdania.TEXT[365]; }
/*    */        }
/* 43 */     else if (pogoda == 0) { this.komunikaty[1] = Zdania.TEXT[346]; }
/* 44 */     else if (pogoda == 1) { this.komunikaty[1] = Zdania.TEXT[348]; }
/* 45 */     else if (pogoda == 2) { this.komunikaty[1] = Zdania.TEXT[347]; }
/* 46 */     else if (pogoda == 3) { this.komunikaty[1] = Zdania.TEXT[365]; }
/*    */     
/* 48 */     boolean blabla1 = this.aliveInShelter.x.nextBoolean();
/* 49 */     boolean blabla2 = this.aliveInShelter.x.nextBoolean();
/* 50 */     boolean blabla3 = this.aliveInShelter.x.nextBoolean();
/*    */     
/* 52 */     if (blabla1) {
/* 53 */       for (int i = 0; i < 5; i++) {
/* 54 */         if (this.komunikaty[i].equals("...shshhshshh...") == true) {
/* 55 */           this.komunikaty[i] = Zdania.TEXT[349];
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     }
/* 60 */     if (blabla2) {
/* 61 */       for (int i = 0; i < 5; i++) {
/* 62 */         if (this.komunikaty[i].equals("...shshhshshh...") == true) {
/* 63 */           this.komunikaty[i] = Zdania.TEXT[350];
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     }
/* 68 */     if (blabla3)
/* 69 */       for (int i = 0; i < 5; i++) {
/* 70 */         if (this.komunikaty[i].equals("...shshhshshh...") == true) {
/* 71 */           this.komunikaty[i] = Zdania.TEXT[360];
/*    */           break;
/*    */         } 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Radio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */