/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ 
/*     */ public class ScaledNumericValue
/*     */   extends RangedNumericValue
/*     */ {
/*  11 */   private float[] scaling = new float[] { 1.0F };
/*  12 */   public float[] timeline = new float[] { 0.0F };
/*     */   
/*     */   private float highMin;
/*     */   
/*     */   public float newHighValue() {
/*  17 */     return this.highMin + (this.highMax - this.highMin) * MathUtils.random();
/*     */   }
/*     */   private float highMax; private boolean relative = false;
/*     */   public void setHigh(float value) {
/*  21 */     this.highMin = value;
/*  22 */     this.highMax = value;
/*     */   }
/*     */   
/*     */   public void setHigh(float min, float max) {
/*  26 */     this.highMin = min;
/*  27 */     this.highMax = max;
/*     */   }
/*     */   
/*     */   public float getHighMin() {
/*  31 */     return this.highMin;
/*     */   }
/*     */   
/*     */   public void setHighMin(float highMin) {
/*  35 */     this.highMin = highMin;
/*     */   }
/*     */   
/*     */   public float getHighMax() {
/*  39 */     return this.highMax;
/*     */   }
/*     */   
/*     */   public void setHighMax(float highMax) {
/*  43 */     this.highMax = highMax;
/*     */   }
/*     */   
/*     */   public float[] getScaling() {
/*  47 */     return this.scaling;
/*     */   }
/*     */   
/*     */   public void setScaling(float[] values) {
/*  51 */     this.scaling = values;
/*     */   }
/*     */   
/*     */   public float[] getTimeline() {
/*  55 */     return this.timeline;
/*     */   }
/*     */   
/*     */   public void setTimeline(float[] timeline) {
/*  59 */     this.timeline = timeline;
/*     */   }
/*     */   
/*     */   public boolean isRelative() {
/*  63 */     return this.relative;
/*     */   }
/*     */   
/*     */   public void setRelative(boolean relative) {
/*  67 */     this.relative = relative;
/*     */   }
/*     */   
/*     */   public float getScale(float percent) {
/*  71 */     int endIndex = -1;
/*  72 */     int n = this.timeline.length;
/*     */ 
/*     */     
/*  75 */     for (int i = 1; i < n; i++) {
/*  76 */       float t = this.timeline[i];
/*  77 */       if (t > percent) {
/*  78 */         endIndex = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*  82 */     if (endIndex == -1) return this.scaling[n - 1]; 
/*  83 */     int startIndex = endIndex - 1;
/*  84 */     float startValue = this.scaling[startIndex];
/*  85 */     float startTime = this.timeline[startIndex];
/*  86 */     return startValue + (this.scaling[endIndex] - startValue) * (percent - startTime) / (this.timeline[endIndex] - startTime);
/*     */   }
/*     */   
/*     */   public void load(ScaledNumericValue value) {
/*  90 */     load(value);
/*  91 */     this.highMax = value.highMax;
/*  92 */     this.highMin = value.highMin;
/*  93 */     this.scaling = new float[value.scaling.length];
/*  94 */     System.arraycopy(value.scaling, 0, this.scaling, 0, this.scaling.length);
/*  95 */     this.timeline = new float[value.timeline.length];
/*  96 */     System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/*  97 */     this.relative = value.relative;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 102 */     super.write(json);
/* 103 */     json.writeValue("highMin", Float.valueOf(this.highMin));
/* 104 */     json.writeValue("highMax", Float.valueOf(this.highMax));
/* 105 */     json.writeValue("relative", Boolean.valueOf(this.relative));
/* 106 */     json.writeValue("scaling", this.scaling);
/* 107 */     json.writeValue("timeline", this.timeline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 112 */     super.read(json, jsonData);
/* 113 */     this.highMin = ((Float)json.readValue("highMin", float.class, jsonData)).floatValue();
/* 114 */     this.highMax = ((Float)json.readValue("highMax", float.class, jsonData)).floatValue();
/* 115 */     this.relative = ((Boolean)json.readValue("relative", boolean.class, jsonData)).booleanValue();
/* 116 */     this.scaling = (float[])json.readValue("scaling", float[].class, jsonData);
/* 117 */     this.timeline = (float[])json.readValue("timeline", float[].class, jsonData);
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\ScaledNumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */