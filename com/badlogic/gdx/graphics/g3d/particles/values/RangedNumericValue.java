/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ public class RangedNumericValue
/*    */   extends ParticleValue {
/*    */   private float lowMin;
/*    */   private float lowMax;
/*    */   
/*    */   public float newLowValue() {
/* 13 */     return this.lowMin + (this.lowMax - this.lowMin) * MathUtils.random();
/*    */   }
/*    */   
/*    */   public void setLow(float value) {
/* 17 */     this.lowMin = value;
/* 18 */     this.lowMax = value;
/*    */   }
/*    */   
/*    */   public void setLow(float min, float max) {
/* 22 */     this.lowMin = min;
/* 23 */     this.lowMax = max;
/*    */   }
/*    */   
/*    */   public float getLowMin() {
/* 27 */     return this.lowMin;
/*    */   }
/*    */   
/*    */   public void setLowMin(float lowMin) {
/* 31 */     this.lowMin = lowMin;
/*    */   }
/*    */   
/*    */   public float getLowMax() {
/* 35 */     return this.lowMax;
/*    */   }
/*    */   
/*    */   public void setLowMax(float lowMax) {
/* 39 */     this.lowMax = lowMax;
/*    */   }
/*    */   
/*    */   public void load(RangedNumericValue value) {
/* 43 */     load(value);
/* 44 */     this.lowMax = value.lowMax;
/* 45 */     this.lowMin = value.lowMin;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 50 */     super.write(json);
/* 51 */     json.writeValue("lowMin", Float.valueOf(this.lowMin));
/* 52 */     json.writeValue("lowMax", Float.valueOf(this.lowMax));
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 57 */     super.read(json, jsonData);
/* 58 */     this.lowMin = ((Float)json.readValue("lowMin", float.class, jsonData)).floatValue();
/* 59 */     this.lowMax = ((Float)json.readValue("lowMax", float.class, jsonData)).floatValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\RangedNumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */