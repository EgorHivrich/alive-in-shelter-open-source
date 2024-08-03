/*    */ package com.pokulan.aliveinshelter;
/*    */ 
/*    */ import com.badlogic.gdx.audio.Sound;
/*    */ 
/*    */ public class SoundBundle {
/*    */   Sound[] sounds;
/*    */   int curr;
/*    */   
/*    */   SoundBundle(Sound[] s) {
/* 10 */     this.sounds = new Sound[s.length];
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
/*    */   public void dispose() {
/* 30 */     for (int i = 0; i < this.sounds.length; i++)
/* 31 */       this.sounds[i].dispose(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\pokulan\aliveinshelter\SoundBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */