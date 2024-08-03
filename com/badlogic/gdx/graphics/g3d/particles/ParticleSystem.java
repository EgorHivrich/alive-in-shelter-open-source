/*    */ package com.badlogic.gdx.graphics.g3d.particles;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.RenderableProvider;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.Pool;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ParticleSystem
/*    */   implements RenderableProvider
/*    */ {
/*    */   private static ParticleSystem instance;
/*    */   private Array<ParticleBatch<?>> batches;
/*    */   private Array<ParticleEffect> effects;
/*    */   
/*    */   public static ParticleSystem get() {
/* 19 */     if (instance == null)
/* 20 */       instance = new ParticleSystem(); 
/* 21 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParticleSystem() {
/* 28 */     this.batches = new Array();
/* 29 */     this.effects = new Array();
/*    */   }
/*    */   
/*    */   public void add(ParticleBatch<?> batch) {
/* 33 */     this.batches.add(batch);
/*    */   }
/*    */   
/*    */   public void add(ParticleEffect effect) {
/* 37 */     this.effects.add(effect);
/*    */   }
/*    */   
/*    */   public void remove(ParticleEffect effect) {
/* 41 */     this.effects.removeValue(effect, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAll() {
/* 46 */     this.effects.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 51 */     for (ParticleEffect effect : this.effects) {
/* 52 */       effect.update();
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateAndDraw() {
/* 57 */     for (ParticleEffect effect : this.effects) {
/* 58 */       effect.update();
/* 59 */       effect.draw();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 65 */     for (ParticleBatch<?> batch : this.batches) {
/* 66 */       batch.begin();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw() {
/* 72 */     for (ParticleEffect effect : this.effects) {
/* 73 */       effect.draw();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 79 */     for (ParticleBatch<?> batch : this.batches) {
/* 80 */       batch.end();
/*    */     }
/*    */   }
/*    */   
/*    */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 85 */     for (ParticleBatch<?> batch : this.batches)
/* 86 */       batch.getRenderables(renderables, pool); 
/*    */   }
/*    */   
/*    */   public Array<ParticleBatch<?>> getBatches() {
/* 90 */     return this.batches;
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */