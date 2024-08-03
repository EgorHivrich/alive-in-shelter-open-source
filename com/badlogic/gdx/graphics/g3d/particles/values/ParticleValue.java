/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleValue
/*    */   implements Json.Serializable
/*    */ {
/*    */   public boolean active;
/*    */   
/*    */   public ParticleValue() {}
/*    */   
/*    */   public ParticleValue(ParticleValue value) {
/* 16 */     this.active = value.active;
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 20 */     return this.active;
/*    */   }
/*    */   
/*    */   public void setActive(boolean active) {
/* 24 */     this.active = active;
/*    */   }
/*    */   
/*    */   public void load(ParticleValue value) {
/* 28 */     this.active = value.active;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 33 */     json.writeValue("active", Boolean.valueOf(this.active));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 38 */     this.active = ((Boolean)json.readValue("active", Boolean.class, jsonData)).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\ParticleValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */