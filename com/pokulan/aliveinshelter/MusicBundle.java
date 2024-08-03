/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.audio.Music;
/*    */ 
/*    */ public class MusicBundle {
/*    */   Music[] sounds;
/*    */   int curr;
/*    */   
/*    */   MusicBundle(Music[] s) {
/* 10 */     this.sounds = new Music[s.length];
/* 11 */     this.sounds = s;
/* 12 */     this.curr = 0;
/*    */   }
/*    */   
/*    */   public void play() {
/* 16 */     this.curr++;
/* 17 */     if (this.curr > this.sounds.length - 1) this.curr = 0; 
/* 18 */     this.sounds[0].stop();
/* 19 */     this.sounds[1].stop();
/* 20 */     this.sounds[this.curr].play();
/*    */   }
/*    */   
/*    */   public void stop() {
/* 24 */     for (int i = 0; i < this.sounds.length; i++) {
/* 25 */       this.sounds[i].stop();
/*    */     }
/*    */   }
/*    */   
/*    */   public void pause() {
/* 30 */     this.sounds[this.curr].pause();
/*    */   }
/*    */   
/*    */   public void dispose() {
/* 34 */     for (int i = 0; i < this.sounds.length; i++) {
/* 35 */       this.sounds[i].dispose();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setLooping(boolean bol) {
/* 40 */     for (int i = 0; i < this.sounds.length; i++)
/* 41 */       this.sounds[i].setLooping(bol); 
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\MusicBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */