/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ public class Potion
/*    */ {
/*    */   Texture tekstura;
/*    */   int ilosc;
/*    */   String nazwa;
/*    */   String opis;
/*    */   boolean combat;
/*    */   int dmg;
/*    */   int off;
/*    */   boolean used;
/*    */   
/*    */   Potion(Texture _tekstura, String _nazwa, String _opis) {
/* 18 */     this.tekstura = _tekstura;
/* 19 */     this.nazwa = _nazwa;
/* 20 */     this.opis = _opis;
/* 21 */     this.ilosc = 0;
/* 22 */     this.combat = false;
/* 23 */     this.dmg = 0;
/* 24 */     this.off = 0;
/* 25 */     this.used = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Potion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */