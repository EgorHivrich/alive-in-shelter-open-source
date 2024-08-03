/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.Animation;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Enemy
/*    */ {
/*    */   int health;
/*    */   int maxHealth;
/*    */   int timerek;
/*    */   int dmg_standart;
/*    */   int dmg_big;
/*    */   int copy_dmg;
/*    */   int copy_dmgBig;
/*    */   String monsterName;
/*    */   Animation statyczna;
/*    */   int chance;
/*    */   boolean outside;
/*    */   Texture guardKey;
/*    */   boolean guardKilled;
/*    */   
/*    */   Enemy(int health, Animation statyczna, int dmg_standart, int dmg_big, String monsterName, int chance, boolean outside, Texture guardKey) {
/* 25 */     this.health = health;
/* 26 */     this.statyczna = statyczna;
/* 27 */     this.dmg_standart = dmg_standart;
/* 28 */     this.dmg_big = dmg_big;
/* 29 */     this.maxHealth = health;
/* 30 */     this.monsterName = monsterName;
/* 31 */     this.chance = chance;
/* 32 */     this.outside = outside;
/* 33 */     this.guardKey = guardKey;
/* 34 */     this.copy_dmg = dmg_standart;
/* 35 */     this.copy_dmgBig = dmg_big;
/* 36 */     this.timerek = 0;
/* 37 */     statyczna.setPlayMode(Animation.PlayMode.LOOP);
/* 38 */     this.guardKilled = false;
/*    */   }
/*    */   
/*    */   void reFill() {
/* 42 */     this.health = this.maxHealth;
/* 43 */     this.dmg_standart = this.copy_dmg;
/* 44 */     this.dmg_big = this.copy_dmgBig;
/* 45 */     this.timerek = 0;
/*    */   }
/*    */   
/*    */   void difficult(int level) {
/* 49 */     this.maxHealth += this.maxHealth / 8 * level;
/* 50 */     this.dmg_standart += this.dmg_standart / 10 * level;
/* 51 */     this.dmg_big += this.dmg_big / 10 * level;
/* 52 */     this.health = this.maxHealth;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\Enemy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */