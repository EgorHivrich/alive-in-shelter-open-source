/*    */ package com.badlogic.gdx.graphics.g3d.particles.emitters;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Emitter
/*    */   extends ParticleControllerComponent
/*    */   implements Json.Serializable
/*    */ {
/*    */   public int minParticleCount;
/* 14 */   public int maxParticleCount = 4;
/*    */ 
/*    */   
/*    */   public float percent;
/*    */ 
/*    */   
/*    */   public Emitter(Emitter regularEmitter) {
/* 21 */     set(regularEmitter);
/*    */   }
/*    */ 
/*    */   
/*    */   public Emitter() {}
/*    */   
/*    */   public void init() {
/* 28 */     this.controller.particles.size = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 33 */     this.controller.particles.size = 0;
/*    */   }
/*    */   
/*    */   public boolean isComplete() {
/* 37 */     return (this.percent >= 1.0F);
/*    */   }
/*    */   
/*    */   public int getMinParticleCount() {
/* 41 */     return this.minParticleCount;
/*    */   }
/*    */   
/*    */   public void setMinParticleCount(int minParticleCount) {
/* 45 */     this.minParticleCount = minParticleCount;
/*    */   }
/*    */   
/*    */   public int getMaxParticleCount() {
/* 49 */     return this.maxParticleCount;
/*    */   }
/*    */   
/*    */   public void setMaxParticleCount(int maxParticleCount) {
/* 53 */     this.maxParticleCount = maxParticleCount;
/*    */   }
/*    */   
/*    */   public void setParticleCount(int aMin, int aMax) {
/* 57 */     setMinParticleCount(aMin);
/* 58 */     setMaxParticleCount(aMax);
/*    */   }
/*    */   
/*    */   public void set(Emitter emitter) {
/* 62 */     this.minParticleCount = emitter.minParticleCount;
/* 63 */     this.maxParticleCount = emitter.maxParticleCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 68 */     json.writeValue("minParticleCount", Integer.valueOf(this.minParticleCount));
/* 69 */     json.writeValue("maxParticleCount", Integer.valueOf(this.maxParticleCount));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 74 */     this.minParticleCount = ((Integer)json.readValue("minParticleCount", int.class, jsonData)).intValue();
/* 75 */     this.maxParticleCount = ((Integer)json.readValue("maxParticleCount", int.class, jsonData)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\emitters\Emitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */