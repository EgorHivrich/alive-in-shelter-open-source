/*     */ package com.pokulan.aliveinshelter;
/*     */ 
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WikiBook
/*     */ {
/*  16 */   public Vector<WikiRecord> items = new Vector<>();
/*  17 */   public Vector<WikiRecord> characters = new Vector<>();
/*  18 */   public Vector<WikiRecord> locations = new Vector<>();
/*  19 */   public Vector<WikiRecord> endings = new Vector<>();
/*  20 */   int cat = 0;
/*  21 */   int picked = 0;
/*     */   boolean fresh = false;
/*  23 */   String date = "---";
/*  24 */   int pos_scroll = 0;
/*     */   
/*     */   public void addItem(WikiRecord a) {
/*  27 */     this.items.addElement(a);
/*  28 */   } public void addCharacter(WikiRecord a) { this.characters.addElement(a); }
/*  29 */   public void addLocation(WikiRecord a) { this.locations.addElement(a); } public void addEnding(WikiRecord a) {
/*  30 */     this.endings.addElement(a);
/*     */   }
/*     */   public WikiRecord getPickedUp() {
/*  33 */     if (this.cat == 0)
/*  34 */       return this.items.elementAt(this.picked + this.pos_scroll); 
/*  35 */     if (this.cat == 1)
/*  36 */       return this.characters.elementAt(this.picked + this.pos_scroll); 
/*  37 */     if (this.cat == 2) {
/*  38 */       return this.locations.elementAt(this.picked + this.pos_scroll);
/*     */     }
/*  40 */     return this.endings.elementAt(this.picked + this.pos_scroll);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector<WikiRecord> getList() {
/*  45 */     if (this.cat == 0)
/*  46 */       return this.items; 
/*  47 */     if (this.cat == 1)
/*  48 */       return this.characters; 
/*  49 */     if (this.cat == 2) {
/*  50 */       return this.locations;
/*     */     }
/*  52 */     return this.endings;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPos_scroll() {
/*  57 */     return this.pos_scroll;
/*     */   }
/*     */   public void setPos_scroll(int i) {
/*  60 */     this.pos_scroll = i;
/*     */   }
/*     */   public void setFresh(boolean fresh) {
/*  63 */     this.fresh = fresh;
/*     */   }
/*     */   
/*     */   public void setDate(String date) {
/*  67 */     this.date = date;
/*     */   }
/*     */   
/*     */   public void setCat(int cat) {
/*  71 */     this.cat = cat;
/*     */   }
/*     */   
/*     */   public void setPicked(int p) {
/*  75 */     this.picked = p;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPicked() {
/*  80 */     return this.picked;
/*     */   }
/*     */   
/*     */   public int getCat() {
/*  84 */     return this.cat;
/*     */   }
/*     */   
/*     */   public boolean isFresh() {
/*  88 */     return this.fresh;
/*     */   }
/*     */   
/*     */   public String getDate() {
/*  92 */     return this.date;
/*     */   }
/*     */   
/*     */   public void search(String str) {
/*  96 */     if (this.cat == 0) {
/*  97 */       for (int i = 0; i < this.items.size(); i++) {
/*  98 */         if (((WikiRecord)this.items.elementAt(i)).getName().toLowerCase().contains(str)) {
/*  99 */           this.picked = 0;
/* 100 */           this.pos_scroll = i;
/* 101 */           if (this.pos_scroll > this.items.size() - 5) {
/* 102 */             this.picked = this.pos_scroll - this.items.size() - 5 + 1;
/* 103 */             this.pos_scroll = this.items.size() - 6;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 109 */     } else if (this.cat == 1) {
/* 110 */       for (int i = 0; i < this.characters.size(); i++) {
/* 111 */         if (((WikiRecord)this.characters.elementAt(i)).getName().toLowerCase().contains(str)) {
/* 112 */           this.picked = 0;
/* 113 */           this.pos_scroll = i;
/* 114 */           if (this.pos_scroll > this.characters.size() - 5) {
/* 115 */             this.picked = this.pos_scroll - this.characters.size() - 5 + 1;
/* 116 */             this.pos_scroll = this.characters.size() - 6;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 122 */     } else if (this.cat == 2) {
/* 123 */       for (int i = 0; i < this.locations.size(); i++) {
/* 124 */         if (((WikiRecord)this.locations.elementAt(i)).getName().toLowerCase().contains(str)) {
/* 125 */           this.picked = 0;
/* 126 */           this.pos_scroll = i;
/* 127 */           if (this.pos_scroll > this.locations.size() - 5) {
/* 128 */             this.picked = this.pos_scroll - this.locations.size() - 5 + 1;
/* 129 */             this.pos_scroll = this.locations.size() - 6;
/*     */           } 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 135 */     } else if (this.cat == 3) {
/* 136 */       for (int i = 0; i < this.endings.size(); i++) {
/* 137 */         if (((WikiRecord)this.endings.elementAt(i)).getName().toLowerCase().contains(str)) {
/* 138 */           this.picked = 0;
/* 139 */           this.pos_scroll = i;
/* 140 */           if (this.pos_scroll > this.endings.size() - 5) {
/* 141 */             this.picked = this.pos_scroll - this.endings.size() - 5 + 1;
/* 142 */             this.pos_scroll = this.endings.size() - 6;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\WikiBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */