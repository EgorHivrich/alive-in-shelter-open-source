/*     */ package com.pokulan.aliveinshelter;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InnySchron
/*     */ {
/*     */   AliveInShelter aliveinshelter;
/*     */   Texture tlo;
/*     */   Texture tlo2;
/*     */   Texture pracujo;
/*     */   int ziemniakiPoziom;
/*     */   int coRobia;
/*     */   boolean posadzone;
/*     */   boolean danieWody;
/*     */   boolean danieWiazania;
/*     */   boolean podlane;
/*     */   boolean odbity;
/*     */   Enemy ochrona;
/*     */   boolean sadzonko;
/*     */   boolean podlewanko;
/*     */   int stanWezly;
/*     */   int typ;
/*     */   int ileDrewna;
/*     */   
/*     */   InnySchron(Texture tlo, int typ, Enemy ochrona, AliveInShelter aliveinshelter) {
/*  34 */     this.aliveinshelter = aliveinshelter;
/*  35 */     this.tlo = tlo;
/*  36 */     this.ochrona = ochrona;
/*  37 */     this.typ = typ;
/*  38 */     this.ziemniakiPoziom = 0;
/*  39 */     this.posadzone = this.danieWody = this.danieWiazania = false;
/*  40 */     this.podlane = false;
/*  41 */     this.odbity = false;
/*  42 */     this.podlewanko = false;
/*  43 */     this.sadzonko = false;
/*  44 */     this.uratunek = 0;
/*  45 */     this.wezwano = false;
/*  46 */     this.stanWezly = 20;
/*  47 */     this.speedDrewno = 8;
/*  48 */     this.speedZelazo = 8;
/*  49 */     this.speedMiedz = 12;
/*  50 */     this.speedZloto = 18;
/*  51 */     this.speedUran = 34;
/*  52 */     this.ileZlota = this.procentZloto = 0;
/*  53 */     this.ileMiedzi = this.procentMiedz = 0;
/*  54 */     this.ileZelaza = this.procentZelazo = 0;
/*  55 */     this.ileDrewna = this.procentDrewno = 0;
/*  56 */     this.ileUranu = this.procentUran = 0;
/*  57 */     this.energia = 0;
/*  58 */     this.coRobia = 0;
/*  59 */     if (typ == 0) {
/*  60 */       this.tlo2 = aliveinshelter.typSchron1;
/*  61 */       this.pracujo = aliveinshelter.zakladnicy_kopalnia;
/*  62 */     } else if (typ == 1) {
/*  63 */       this.tlo2 = aliveinshelter.typSchron2;
/*  64 */       this.pracujo = aliveinshelter.zakladnicy_drewutnia;
/*  65 */     } else if (typ == 2) {
/*  66 */       this.tlo2 = aliveinshelter.typSchron3;
/*  67 */       this.pracujo = aliveinshelter.blank;
/*     */     } 
/*     */   }
/*     */   int procentDrewno; int speedDrewno; int ileZelaza; int procentZelazo; int speedZelazo; int ileMiedzi; int procentMiedz; int speedMiedz; int ileZlota; int procentZloto; int speedZloto; int ileUranu; int procentUran; int speedUran; int energia; int uratunek; boolean wezwano;
/*     */   void zmiana() {
/*  72 */     if (this.aliveinshelter.kierunek == 1 || this.aliveinshelter.kierunek == 2) {
/*  73 */       this.aliveinshelter.view = !this.aliveinshelter.view;
/*  74 */       this.aliveinshelter.kierunek = 0;
/*  75 */       this.aliveinshelter.fadeAlpha = 1.0F;
/*  76 */       this.aliveinshelter.subPanel = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   void showMenu() {
/*  81 */     if (this.typ == 0) {
/*  82 */       this.aliveinshelter.batch.draw(this.aliveinshelter.zakladnikMenu, 0.0F, 0.0F, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);
/*     */       
/*  84 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barYellow, 39.0F * this.aliveinshelter.ResX, 74.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (5 * this.energia), this.aliveinshelter.ResY * 4.0F);
/*  85 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barGreen, 36.0F * this.aliveinshelter.ResX, 67.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (81 / this.speedZelazo * this.procentZelazo), this.aliveinshelter.ResY * 4.0F);
/*  86 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barGreen, 36.0F * this.aliveinshelter.ResX, 60.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (81 / this.speedMiedz * this.procentMiedz), this.aliveinshelter.ResY * 4.0F);
/*  87 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barGreen, 36.0F * this.aliveinshelter.ResX, 53.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (81 / this.speedZloto * this.procentZloto), this.aliveinshelter.ResY * 4.0F);
/*  88 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barGreen, 36.0F * this.aliveinshelter.ResX, 46.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (81 / this.speedUran * this.procentUran), this.aliveinshelter.ResY * 4.0F);
/*  89 */       this.aliveinshelter.font.getData().setScale(this.aliveinshelter.tinyFont * this.aliveinshelter.ResX, this.aliveinshelter.tinyFont * this.aliveinshelter.ResY);
/*  90 */       this.aliveinshelter.font.draw((Batch)this.aliveinshelter.batch, Zdania.TEXT[123], 36.0F * this.aliveinshelter.ResX, 70.0F * this.aliveinshelter.ResY, 81.0F * this.aliveinshelter.ResX, 1, false);
/*  91 */       this.aliveinshelter.font.draw((Batch)this.aliveinshelter.batch, Zdania.TEXT[135], 36.0F * this.aliveinshelter.ResX, 63.0F * this.aliveinshelter.ResY, 81.0F * this.aliveinshelter.ResX, 1, false);
/*  92 */       this.aliveinshelter.font.draw((Batch)this.aliveinshelter.batch, Zdania.TEXT[153], 36.0F * this.aliveinshelter.ResX, 56.0F * this.aliveinshelter.ResY, 81.0F * this.aliveinshelter.ResX, 1, false);
/*  93 */       this.aliveinshelter.font.draw((Batch)this.aliveinshelter.batch, Zdania.TEXT[320], 36.0F * this.aliveinshelter.ResX, 49.0F * this.aliveinshelter.ResY, 81.0F * this.aliveinshelter.ResX, 1, false);
/*  94 */       this.aliveinshelter.font.getData().setScale(this.aliveinshelter.normalFont * this.aliveinshelter.ResX, this.aliveinshelter.normalFont * this.aliveinshelter.ResY);
/*  95 */       if (this.coRobia == 1 && !this.danieWody)
/*  96 */       { if (this.aliveinshelter.isTap(100, 69, 127, 47)) {
/*  97 */           this.aliveinshelter.tapX = 0;
/*  98 */           this.danieWiazania = !this.danieWiazania;
/*     */         } 
/* 100 */         if (this.danieWiazania) this.aliveinshelter.batch.draw(this.aliveinshelter.menelDanie2, 0.0F, -5.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);  }
/* 101 */       else { this.aliveinshelter.batch.draw(this.aliveinshelter.menelBrak2, 0.0F, -5.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY); }
/*     */       
/* 103 */       if (!this.danieWiazania && this.aliveinshelter.shelter.iloscWody > 0.2D)
/*     */       
/* 105 */       { if (this.aliveinshelter.isTap(39, 70, 80, 46) && !this.danieWody) {
/* 106 */           this.aliveinshelter.tapX = 0;
/* 107 */           this.danieWody = true;
/* 108 */           this.aliveinshelter.shelter.iloscWody -= 0.25D;
/*     */         } 
/* 110 */         if (this.aliveinshelter.isTap(39, 70, 80, 46) && this.danieWody == true) {
/* 111 */           this.aliveinshelter.tapX = 0;
/* 112 */           this.danieWody = false;
/* 113 */           this.aliveinshelter.shelter.iloscWody += 0.25D;
/*     */         } 
/*     */         
/* 116 */         if (this.danieWody) this.aliveinshelter.batch.draw(this.aliveinshelter.menelDanie1, 0.0F, -6.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);  }
/* 117 */       else { this.aliveinshelter.batch.draw(this.aliveinshelter.menelBrak1, 0.0F, -6.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY); }
/*     */     
/* 119 */     } else if (this.typ == 1) {
/* 120 */       this.aliveinshelter.batch.draw(this.aliveinshelter.zakladnikMenu2, 0.0F, 0.0F, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);
/*     */       
/* 122 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barYellow, 39.0F * this.aliveinshelter.ResX, 64.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (5 * this.energia), this.aliveinshelter.ResY * 4.0F);
/* 123 */       this.aliveinshelter.batch.draw(this.aliveinshelter.barGreen, 36.0F * this.aliveinshelter.ResX, 53.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * (81 / this.speedDrewno * this.procentDrewno), this.aliveinshelter.ResY * 4.0F);
/*     */       
/* 125 */       if (this.coRobia == 1 && !this.danieWody)
/* 126 */       { if (this.aliveinshelter.isTap(100, 69, 127, 47)) {
/* 127 */           this.aliveinshelter.tapX = 0;
/* 128 */           this.danieWiazania = !this.danieWiazania;
/*     */         } 
/* 130 */         if (this.danieWiazania) this.aliveinshelter.batch.draw(this.aliveinshelter.menelDanie2, 0.0F, -2.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);  }
/* 131 */       else { this.aliveinshelter.batch.draw(this.aliveinshelter.menelBrak2, 0.0F, -2.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY); }
/*     */       
/* 133 */       if (!this.danieWiazania && this.aliveinshelter.shelter.iloscWody > 0.2D)
/*     */       
/* 135 */       { if (this.aliveinshelter.isTap(39, 70, 80, 46) && !this.danieWody) {
/* 136 */           this.aliveinshelter.tapX = 0;
/* 137 */           this.danieWody = true;
/* 138 */           this.aliveinshelter.shelter.iloscWody -= 0.25D;
/*     */         } 
/* 140 */         if (this.aliveinshelter.isTap(39, 70, 80, 46) && this.danieWody == true) {
/* 141 */           this.aliveinshelter.tapX = 0;
/* 142 */           this.danieWody = false;
/* 143 */           this.aliveinshelter.shelter.iloscWody += 0.25D;
/*     */         } 
/*     */         
/* 146 */         if (this.danieWody) this.aliveinshelter.batch.draw(this.aliveinshelter.menelDanie1, 0.0F, -2.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);  }
/* 147 */       else { this.aliveinshelter.batch.draw(this.aliveinshelter.menelBrak1, 0.0F, -2.0F * this.aliveinshelter.ResY, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY); }
/*     */     
/* 149 */     } else if (this.typ == 2) {
/* 150 */       if (!this.wezwano) { this.aliveinshelter.batch.draw(this.aliveinshelter.radiostacjaMenu, 0.0F, 0.0F, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY); }
/*     */       else
/* 152 */       { this.aliveinshelter.batch.draw(this.aliveinshelter.odpowiedz, 0.0F, 0.0F, this.aliveinshelter.ResX * this.aliveinshelter.gameScreenX, this.aliveinshelter.ResY * this.aliveinshelter.gameScreenY);
/*     */         
/* 154 */         this.aliveinshelter.font.setColor(Color.WHITE);
/* 155 */         this.aliveinshelter.font.draw((Batch)this.aliveinshelter.batch, Zdania.TEXT[198] + this.uratunek, 34.0F * this.aliveinshelter.ResX, 55.0F * this.aliveinshelter.ResY);
/* 156 */         this.aliveinshelter.font.setColor(Color.BLACK); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   void kartofelki() {
/* 162 */     if (this.ziemniakiPoziom > 0 && this.ziemniakiPoziom < 10) this.ziemniakiPoziom++; 
/* 163 */     if (this.sadzonko == true) {
/* 164 */       this.sadzonko = false;
/* 165 */       this.ziemniakiPoziom++;
/*     */     } 
/* 167 */     if (this.podlewanko) {
/* 168 */       this.ziemniakiPoziom++;
/* 169 */       this.podlewanko = false;
/*     */     } 
/* 171 */     if (this.ziemniakiPoziom > 10) this.ziemniakiPoziom = 10;
/*     */     
/* 173 */     if (this.danieWody) {
/* 174 */       this.coRobia = 1;
/* 175 */       this.energia = 16;
/* 176 */       this.danieWody = false;
/*     */     } 
/* 178 */     if (this.danieWiazania) {
/* 179 */       this.coRobia = 0;
/* 180 */       this.stanWezly = 20;
/* 181 */       this.danieWiazania = false;
/* 182 */       this.energia = 0;
/*     */     } 
/*     */     
/* 185 */     if (this.coRobia == 0) {
/* 186 */       if (this.odbity == true) this.stanWezly--; 
/* 187 */       if (this.stanWezly == 0) {
/* 188 */         this.stanWezly = 20;
/* 189 */         this.odbity = false;
/* 190 */         this.ochrona.health = this.ochrona.maxHealth / 2;
/*     */       } 
/* 192 */     } else if (this.coRobia == 1) {
/* 193 */       if (this.typ == 1) {
/* 194 */         this.procentDrewno++;
/* 195 */         if (this.procentDrewno == this.speedDrewno) {
/* 196 */           this.procentDrewno = 0;
/* 197 */           this.ileDrewna++;
/*     */         } 
/* 199 */       } else if (this.typ == 0) {
/* 200 */         this.procentZelazo++;
/* 201 */         this.procentMiedz++;
/* 202 */         this.procentZloto++;
/* 203 */         this.procentUran++;
/* 204 */         if (this.procentZelazo == this.speedZelazo) {
/* 205 */           this.procentZelazo = 0;
/* 206 */           this.ileZelaza++;
/*     */         } 
/* 208 */         if (this.procentMiedz == this.speedMiedz) {
/* 209 */           this.procentMiedz = 0;
/* 210 */           this.ileMiedzi++;
/*     */         } 
/* 212 */         if (this.procentZloto == this.speedZloto) {
/* 213 */           this.procentZloto = 0;
/* 214 */           this.ileZlota++;
/*     */         } 
/* 216 */         if (this.procentUran == this.speedUran) {
/* 217 */           this.procentUran = 0;
/* 218 */           this.ileUranu++;
/*     */         } 
/*     */       } 
/* 221 */       this.energia--;
/* 222 */       if (this.energia == 0) {
/* 223 */         this.stanWezly = 20;
/* 224 */         this.odbity = false;
/* 225 */         this.ochrona.health = this.ochrona.maxHealth / 2;
/*     */       } 
/*     */     } 
/*     */     
/* 229 */     if (this.aliveinshelter.shelter.dzien == this.uratunek && this.wezwano) {
/* 230 */       this.aliveinshelter.panelPostac = 5;
/* 231 */       this.aliveinshelter.schronBG = this.aliveinshelter.schronBGHELP;
/* 232 */       this.aliveinshelter.addCoins += 25;
/* 233 */       if (!this.aliveinshelter.PC) AliveInShelter.playServices.unlockAchievement(49); 
/* 234 */       if (this.aliveinshelter.shelter.dzien > this.aliveinshelter.najdluzej) {
/* 235 */         if (!this.aliveinshelter.easyMode) this.aliveinshelter.najdluzej = this.aliveinshelter.shelter.dzien; 
/* 236 */         if (!this.aliveinshelter.easyMode) this.aliveinshelter.prefs.putInteger("najdluzej", this.aliveinshelter.najdluzej); 
/* 237 */         this.aliveinshelter.prefs.flush();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   void reLoad() {
/* 242 */     this.ziemniakiPoziom = 0;
/* 243 */     this.posadzone = this.danieWody = this.danieWiazania = false;
/* 244 */     this.podlane = false;
/* 245 */     this.odbity = false;
/* 246 */     this.podlewanko = false;
/* 247 */     this.sadzonko = false;
/* 248 */     this.stanWezly = 20;
/* 249 */     this.ochrona.reFill();
/* 250 */     this.ileZlota = this.procentZloto = 0;
/* 251 */     this.ileMiedzi = this.procentMiedz = 0;
/* 252 */     this.ileZelaza = this.procentZelazo = 0;
/* 253 */     this.ileDrewna = this.procentDrewno = 0;
/* 254 */     this.ileUranu = this.procentUran = 0;
/* 255 */     this.energia = 0;
/* 256 */     this.coRobia = 0;
/* 257 */     this.uratunek = 0;
/* 258 */     this.wezwano = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\InnySchron.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */