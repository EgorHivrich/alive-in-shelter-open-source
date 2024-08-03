/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ 
/*     */ 
/*     */ public abstract class ParticleSorter
/*     */ {
/*     */   protected Camera camera;
/*  12 */   static final Vector3 TMP_V1 = new Vector3();
/*     */   
/*     */   public abstract <T extends ParticleControllerRenderData> int[] sort(Array<T> paramArray);
/*     */   
/*  16 */   public static class None extends ParticleSorter { int currentCapacity = 0;
/*     */     
/*     */     int[] indices;
/*     */     
/*     */     public void ensureCapacity(int capacity) {
/*  21 */       if (this.currentCapacity < capacity) {
/*  22 */         this.indices = new int[capacity];
/*  23 */         for (int i = 0; i < capacity; i++)
/*  24 */           this.indices[i] = i; 
/*  25 */         this.currentCapacity = capacity;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends ParticleControllerRenderData> int[] sort(Array<T> renderData) {
/*  31 */       return this.indices;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Distance extends ParticleSorter {
/*     */     private float[] distances;
/*     */     private int[] particleIndices;
/*     */     private int[] particleOffsets;
/*  39 */     private int currentSize = 0;
/*     */ 
/*     */     
/*     */     public void ensureCapacity(int capacity) {
/*  43 */       if (this.currentSize < capacity) {
/*  44 */         this.distances = new float[capacity];
/*  45 */         this.particleIndices = new int[capacity];
/*  46 */         this.particleOffsets = new int[capacity];
/*  47 */         this.currentSize = capacity;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends ParticleControllerRenderData> int[] sort(Array<T> renderData) {
/*  53 */       float[] val = this.camera.view.val;
/*  54 */       float cx = val[2], cy = val[6], cz = val[10];
/*  55 */       int count = 0, i = 0;
/*  56 */       for (ParticleControllerRenderData data : renderData) {
/*  57 */         int k; int c; for (k = 0, c = i + data.controller.particles.size; i < c; i++, k += data.positionChannel.strideSize) {
/*  58 */           this.distances[i] = cx * data.positionChannel.data[k + 0] + cy * data.positionChannel.data[k + 1] + cz * data.positionChannel.data[k + 2];
/*  59 */           this.particleIndices[i] = i;
/*     */         } 
/*  61 */         count += data.controller.particles.size;
/*     */       } 
/*     */       
/*  64 */       qsort(0, count - 1);
/*     */       
/*  66 */       for (i = 0; i < count; i++) {
/*  67 */         this.particleOffsets[this.particleIndices[i]] = i;
/*     */       }
/*  69 */       return this.particleOffsets;
/*     */     }
/*     */ 
/*     */     
/*     */     public void qsort(int si, int ei) {
/*  74 */       if (si < ei) {
/*     */ 
/*     */ 
/*     */         
/*  78 */         if (ei - si <= 8) {
/*  79 */           for (int k = si; k <= ei; k++) {
/*  80 */             for (int m = k; m > si && this.distances[m - 1] > this.distances[m]; m--) {
/*  81 */               float tmp = this.distances[m];
/*  82 */               this.distances[m] = this.distances[m - 1];
/*  83 */               this.distances[m - 1] = tmp;
/*     */ 
/*     */               
/*  86 */               int tmpIndex = this.particleIndices[m];
/*  87 */               this.particleIndices[m] = this.particleIndices[m - 1];
/*  88 */               this.particleIndices[m - 1] = tmpIndex;
/*     */             } 
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*  94 */         float pivot = this.distances[si];
/*  95 */         int i = si + 1;
/*  96 */         int particlesPivotIndex = this.particleIndices[si];
/*     */ 
/*     */         
/*  99 */         for (int j = si + 1; j <= ei; j++) {
/* 100 */           if (pivot > this.distances[j]) {
/* 101 */             if (j > i) {
/*     */               
/* 103 */               float tmp = this.distances[j];
/* 104 */               this.distances[j] = this.distances[i];
/* 105 */               this.distances[i] = tmp;
/*     */ 
/*     */               
/* 108 */               int tmpIndex = this.particleIndices[j];
/* 109 */               this.particleIndices[j] = this.particleIndices[i];
/* 110 */               this.particleIndices[i] = tmpIndex;
/*     */             } 
/* 112 */             i++;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 117 */         this.distances[si] = this.distances[i - 1];
/* 118 */         this.distances[i - 1] = pivot;
/* 119 */         this.particleIndices[si] = this.particleIndices[i - 1];
/* 120 */         this.particleIndices[i - 1] = particlesPivotIndex;
/*     */ 
/*     */         
/* 123 */         qsort(si, i - 2);
/* 124 */         qsort(i, ei);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCamera(Camera camera) {
/* 136 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public void ensureCapacity(int capacity) {}
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleSorter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */