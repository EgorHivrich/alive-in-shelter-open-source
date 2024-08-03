/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SpawnShapeValue
/*    */   extends ParticleValue
/*    */   implements ResourceData.Configurable, Json.Serializable
/*    */ {
/* 16 */   public RangedNumericValue xOffsetValue = new RangedNumericValue();
/* 17 */   public RangedNumericValue yOffsetValue = new RangedNumericValue();
/* 18 */   public RangedNumericValue zOffsetValue = new RangedNumericValue();
/*    */ 
/*    */   
/*    */   public SpawnShapeValue(SpawnShapeValue spawnShapeValue) {
/* 22 */     this();
/*    */   }
/*    */   public SpawnShapeValue() {}
/*    */   public abstract void spawnAux(Vector3 paramVector3, float paramFloat);
/*    */   
/*    */   public final Vector3 spawn(Vector3 vector, float percent) {
/* 28 */     spawnAux(vector, percent);
/* 29 */     if (this.xOffsetValue.active) vector.x += this.xOffsetValue.newLowValue(); 
/* 30 */     if (this.yOffsetValue.active) vector.y += this.yOffsetValue.newLowValue(); 
/* 31 */     if (this.zOffsetValue.active) vector.z += this.zOffsetValue.newLowValue(); 
/* 32 */     return vector;
/*    */   }
/*    */   
/*    */   public void init() {}
/*    */   
/*    */   public void start() {}
/*    */   
/*    */   public void load(ParticleValue value) {
/* 40 */     super.load(value);
/* 41 */     SpawnShapeValue shape = (SpawnShapeValue)value;
/* 42 */     this.xOffsetValue.load(shape.xOffsetValue);
/* 43 */     this.yOffsetValue.load(shape.yOffsetValue);
/* 44 */     this.zOffsetValue.load(shape.zOffsetValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract SpawnShapeValue copy();
/*    */   
/*    */   public void write(Json json) {
/* 51 */     super.write(json);
/* 52 */     json.writeValue("xOffsetValue", this.xOffsetValue);
/* 53 */     json.writeValue("yOffsetValue", this.yOffsetValue);
/* 54 */     json.writeValue("zOffsetValue", this.zOffsetValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 59 */     super.read(json, jsonData);
/* 60 */     this.xOffsetValue = (RangedNumericValue)json.readValue("xOffsetValue", RangedNumericValue.class, jsonData);
/* 61 */     this.yOffsetValue = (RangedNumericValue)json.readValue("yOffsetValue", RangedNumericValue.class, jsonData);
/* 62 */     this.zOffsetValue = (RangedNumericValue)json.readValue("zOffsetValue", RangedNumericValue.class, jsonData);
/*    */   }
/*    */   
/*    */   public void save(AssetManager manager, ResourceData data) {}
/*    */   
/*    */   public void load(AssetManager manager, ResourceData data) {}
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\SpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */