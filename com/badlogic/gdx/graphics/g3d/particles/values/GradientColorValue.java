/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Json;
/*    */ import com.badlogic.gdx.utils.JsonValue;
/*    */ 
/*    */ public class GradientColorValue
/*    */   extends ParticleValue
/*    */ {
/*  9 */   private static float[] temp = new float[3];
/*    */   
/* 11 */   private float[] colors = new float[] { 1.0F, 1.0F, 1.0F };
/* 12 */   public float[] timeline = new float[] { 0.0F };
/*    */   
/*    */   public float[] getTimeline() {
/* 15 */     return this.timeline;
/*    */   }
/*    */   
/*    */   public void setTimeline(float[] timeline) {
/* 19 */     this.timeline = timeline;
/*    */   }
/*    */   
/*    */   public float[] getColors() {
/* 23 */     return this.colors;
/*    */   }
/*    */   
/*    */   public void setColors(float[] colors) {
/* 27 */     this.colors = colors;
/*    */   }
/*    */   
/*    */   public float[] getColor(float percent) {
/* 31 */     getColor(percent, temp, 0);
/* 32 */     return temp;
/*    */   }
/*    */   
/*    */   public void getColor(float percent, float[] out, int index) {
/* 36 */     int startIndex = 0, endIndex = -1;
/* 37 */     float[] timeline = this.timeline;
/* 38 */     int n = timeline.length;
/* 39 */     for (int i = 1; i < n; i++) {
/* 40 */       float t = timeline[i];
/* 41 */       if (t > percent) {
/* 42 */         endIndex = i;
/*    */         break;
/*    */       } 
/* 45 */       startIndex = i;
/*    */     } 
/* 47 */     float startTime = timeline[startIndex];
/* 48 */     startIndex *= 3;
/* 49 */     float r1 = this.colors[startIndex];
/* 50 */     float g1 = this.colors[startIndex + 1];
/* 51 */     float b1 = this.colors[startIndex + 2];
/* 52 */     if (endIndex == -1) {
/* 53 */       out[index] = r1;
/* 54 */       out[index + 1] = g1;
/* 55 */       out[index + 2] = b1;
/*    */       return;
/*    */     } 
/* 58 */     float factor = (percent - startTime) / (timeline[endIndex] - startTime);
/* 59 */     endIndex *= 3;
/* 60 */     out[index] = r1 + (this.colors[endIndex] - r1) * factor;
/* 61 */     out[index + 1] = g1 + (this.colors[endIndex + 1] - g1) * factor;
/* 62 */     out[index + 2] = b1 + (this.colors[endIndex + 2] - b1) * factor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(Json json) {
/* 67 */     super.write(json);
/* 68 */     json.writeValue("colors", this.colors);
/* 69 */     json.writeValue("timeline", this.timeline);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(Json json, JsonValue jsonData) {
/* 74 */     super.read(json, jsonData);
/* 75 */     this.colors = (float[])json.readValue("colors", float[].class, jsonData);
/* 76 */     this.timeline = (float[])json.readValue("timeline", float[].class, jsonData);
/*    */   }
/*    */   
/*    */   public void load(GradientColorValue value) {
/* 80 */     load(value);
/* 81 */     this.colors = new float[value.colors.length];
/* 82 */     System.arraycopy(value.colors, 0, this.colors, 0, this.colors.length);
/* 83 */     this.timeline = new float[value.timeline.length];
/* 84 */     System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\values\GradientColorValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */