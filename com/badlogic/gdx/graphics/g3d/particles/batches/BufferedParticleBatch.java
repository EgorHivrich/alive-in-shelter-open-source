/*    */ package com.badlogic.gdx.graphics.g3d.particles.batches;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ 
/*    */ public abstract class BufferedParticleBatch<T extends ParticleControllerRenderData>
/*    */   implements ParticleBatch<T> {
/*    */   protected Array<T> renderData;
/*    */   protected int bufferedParticlesCount;
/* 12 */   protected int currentCapacity = 0;
/*    */   protected ParticleSorter sorter;
/*    */   protected Camera camera;
/*    */   
/*    */   protected BufferedParticleBatch(Class<T> type) {
/* 17 */     this.sorter = (ParticleSorter)new ParticleSorter.Distance();
/* 18 */     this.renderData = new Array(false, 10, type);
/*    */   }
/*    */   
/*    */   public void begin() {
/* 22 */     this.renderData.clear();
/* 23 */     this.bufferedParticlesCount = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(T data) {
/* 28 */     if (((ParticleControllerRenderData)data).controller.particles.size > 0) {
/* 29 */       this.renderData.add(data);
/* 30 */       this.bufferedParticlesCount += ((ParticleControllerRenderData)data).controller.particles.size;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 36 */     if (this.bufferedParticlesCount > 0) {
/* 37 */       ensureCapacity(this.bufferedParticlesCount);
/* 38 */       flush(this.sorter.sort(this.renderData));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void ensureCapacity(int capacity) {
/* 44 */     if (this.currentCapacity >= capacity)
/* 45 */       return;  this.sorter.ensureCapacity(capacity);
/* 46 */     allocParticlesData(capacity);
/* 47 */     this.currentCapacity = capacity;
/*    */   }
/*    */   
/*    */   public void resetCapacity() {
/* 51 */     this.currentCapacity = this.bufferedParticlesCount = 0;
/*    */   }
/*    */   
/*    */   protected abstract void allocParticlesData(int paramInt);
/*    */   
/*    */   public void setCamera(Camera camera) {
/* 57 */     this.camera = camera;
/* 58 */     this.sorter.setCamera(camera);
/*    */   }
/*    */   
/*    */   public ParticleSorter getSorter() {
/* 62 */     return this.sorter;
/*    */   }
/*    */   
/*    */   public void setSorter(ParticleSorter sorter) {
/* 66 */     this.sorter = sorter;
/* 67 */     sorter.setCamera(this.camera);
/* 68 */     sorter.ensureCapacity(this.currentCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void flush(int[] paramArrayOfint);
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBufferedCount() {
/* 78 */     return this.bufferedParticlesCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\batches\BufferedParticleBatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */