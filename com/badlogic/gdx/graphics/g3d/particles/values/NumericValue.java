/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ public class NumericValue
/*    */   extends ParticleValue
/*    */ {
/*    */   private float value;
/*    */   
/*    */   public float getValue() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 16 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void load(NumericValue value) {
/* 20 */     load(value);
/* 21 */     this.value = value.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 26 */     super.write(json);
/* 27 */     json.writeValue("value", Float.valueOf(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 32 */     super.read(json, jsonData);
/* 33 */     this.value = ((Float)json.readValue("value", float.class, jsonData)).floatValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\NumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */