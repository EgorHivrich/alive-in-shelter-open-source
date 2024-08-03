/*    */ package com.pokulan.aliveinshelter.Classes;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.pokulan.aliveinshelter.Zdania;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mod
/*    */ {
/*    */   int cenaFree;
/*    */   int cenaDLC;
/*    */   int name;
/*    */   int desc;
/*    */   Texture texture;
/*    */   boolean kupiony;
/*    */   
/*    */   public Mod(int cenaDLC, int cenaFree, Texture texture, int name, int desc) {
/* 18 */     this.kupiony = false;
/* 19 */     this.cenaFree = cenaFree;
/* 20 */     this.cenaDLC = cenaDLC;
/* 21 */     this.texture = texture;
/* 22 */     this.name = name;
/* 23 */     this.desc = desc;
/*    */   }
/* 25 */   public Texture getTexture() { return this.texture; }
/* 26 */   public int getCenaFree() { return this.cenaFree; }
/* 27 */   public int getCenaDLC() { return this.cenaDLC; }
/* 28 */   public boolean getKupiony() { return this.kupiony; }
/* 29 */   public void setKupiony(boolean kupiony) { this.kupiony = kupiony; }
/* 30 */   public String getName() { return Zdania.TEXT[this.name]; } public String getDesc() {
/* 31 */     return Zdania.TEXT[this.desc];
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Classes\Mod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */