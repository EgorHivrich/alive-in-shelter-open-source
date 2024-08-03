/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Animation
/*     */ {
/*     */   final TextureRegion[] keyFrames;
/*     */   private float frameDuration;
/*     */   private float animationDuration;
/*     */   private int lastFrameNumber;
/*     */   private float lastStateTime;
/*     */   
/*     */   public enum PlayMode
/*     */   {
/*  32 */     NORMAL,
/*  33 */     REVERSED,
/*  34 */     LOOP,
/*  35 */     LOOP_REVERSED,
/*  36 */     LOOP_PINGPONG,
/*  37 */     LOOP_RANDOM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private PlayMode playMode = PlayMode.NORMAL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
/*  53 */     this.frameDuration = frameDuration;
/*  54 */     this.animationDuration = keyFrames.size * frameDuration;
/*  55 */     this.keyFrames = new TextureRegion[keyFrames.size];
/*  56 */     for (int i = 0, n = keyFrames.size; i < n; i++) {
/*  57 */       this.keyFrames[i] = (TextureRegion)keyFrames.get(i);
/*     */     }
/*     */     
/*  60 */     this.playMode = PlayMode.NORMAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
/*  70 */     this.frameDuration = frameDuration;
/*  71 */     this.animationDuration = keyFrames.size * frameDuration;
/*  72 */     this.keyFrames = new TextureRegion[keyFrames.size];
/*  73 */     for (int i = 0, n = keyFrames.size; i < n; i++) {
/*  74 */       this.keyFrames[i] = (TextureRegion)keyFrames.get(i);
/*     */     }
/*     */     
/*  77 */     this.playMode = playMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, TextureRegion... keyFrames) {
/*  85 */     this.frameDuration = frameDuration;
/*  86 */     this.animationDuration = keyFrames.length * frameDuration;
/*  87 */     this.keyFrames = keyFrames;
/*  88 */     this.playMode = PlayMode.NORMAL;
/*     */   }
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
/*     */   public TextureRegion getKeyFrame(float stateTime, boolean looping) {
/* 101 */     PlayMode oldPlayMode = this.playMode;
/* 102 */     if (looping && (this.playMode == PlayMode.NORMAL || this.playMode == PlayMode.REVERSED)) {
/* 103 */       if (this.playMode == PlayMode.NORMAL)
/* 104 */       { this.playMode = PlayMode.LOOP; }
/*     */       else
/* 106 */       { this.playMode = PlayMode.LOOP_REVERSED; } 
/* 107 */     } else if (!looping && this.playMode != PlayMode.NORMAL && this.playMode != PlayMode.REVERSED) {
/* 108 */       if (this.playMode == PlayMode.LOOP_REVERSED) {
/* 109 */         this.playMode = PlayMode.REVERSED;
/*     */       } else {
/* 111 */         this.playMode = PlayMode.LOOP;
/*     */       } 
/*     */     } 
/* 114 */     TextureRegion frame = getKeyFrame(stateTime);
/* 115 */     this.playMode = oldPlayMode;
/* 116 */     return frame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getKeyFrame(float stateTime) {
/* 126 */     int frameNumber = getKeyFrameIndex(stateTime);
/* 127 */     return this.keyFrames[frameNumber];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKeyFrameIndex(float stateTime) {
/*     */     int lastFrameNumber;
/* 134 */     if (this.keyFrames.length == 1) return 0;
/*     */     
/* 136 */     int frameNumber = (int)(stateTime / this.frameDuration);
/* 137 */     switch (this.playMode) {
/*     */       case NORMAL:
/* 139 */         frameNumber = Math.min(this.keyFrames.length - 1, frameNumber);
/*     */         break;
/*     */       case LOOP:
/* 142 */         frameNumber %= this.keyFrames.length;
/*     */         break;
/*     */       case LOOP_PINGPONG:
/* 145 */         frameNumber %= this.keyFrames.length * 2 - 2;
/* 146 */         if (frameNumber >= this.keyFrames.length) frameNumber = this.keyFrames.length - 2 - frameNumber - this.keyFrames.length; 
/*     */         break;
/*     */       case LOOP_RANDOM:
/* 149 */         lastFrameNumber = (int)(this.lastStateTime / this.frameDuration);
/* 150 */         if (lastFrameNumber != frameNumber) {
/* 151 */           frameNumber = MathUtils.random(this.keyFrames.length - 1); break;
/*     */         } 
/* 153 */         frameNumber = this.lastFrameNumber;
/*     */         break;
/*     */       
/*     */       case REVERSED:
/* 157 */         frameNumber = Math.max(this.keyFrames.length - frameNumber - 1, 0);
/*     */         break;
/*     */       case LOOP_REVERSED:
/* 160 */         frameNumber %= this.keyFrames.length;
/* 161 */         frameNumber = this.keyFrames.length - frameNumber - 1;
/*     */         break;
/*     */     } 
/*     */     
/* 165 */     this.lastFrameNumber = frameNumber;
/* 166 */     this.lastStateTime = stateTime;
/*     */     
/* 168 */     return frameNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion[] getKeyFrames() {
/* 174 */     return this.keyFrames;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayMode getPlayMode() {
/* 179 */     return this.playMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlayMode(PlayMode playMode) {
/* 186 */     this.playMode = playMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnimationFinished(float stateTime) {
/* 193 */     int frameNumber = (int)(stateTime / this.frameDuration);
/* 194 */     return (this.keyFrames.length - 1 < frameNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrameDuration(float frameDuration) {
/* 200 */     this.frameDuration = frameDuration;
/* 201 */     this.animationDuration = this.keyFrames.length * frameDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFrameDuration() {
/* 206 */     return this.frameDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAnimationDuration() {
/* 211 */     return this.animationDuration;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\Animation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */