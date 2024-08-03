/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleEffect
/*     */   implements Disposable, ResourceData.Configurable
/*     */ {
/*     */   private Array<ParticleController> controllers;
/*     */   private BoundingBox bounds;
/*     */   
/*     */   public ParticleEffect() {
/*  36 */     this.controllers = new Array(true, 3, ParticleController.class);
/*     */   }
/*     */   
/*     */   public ParticleEffect(ParticleEffect effect) {
/*  40 */     this.controllers = new Array(true, effect.controllers.size);
/*  41 */     for (int i = 0, n = effect.controllers.size; i < n; i++)
/*  42 */       this.controllers.add(((ParticleController)effect.controllers.get(i)).copy()); 
/*     */   }
/*     */   
/*     */   public ParticleEffect(ParticleController... emitters) {
/*  46 */     this.controllers = new Array((Object[])emitters);
/*     */   }
/*     */   
/*     */   public void init() {
/*  50 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  51 */       ((ParticleController)this.controllers.get(i)).init(); 
/*     */   }
/*     */   
/*     */   public void start() {
/*  55 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  56 */       ((ParticleController)this.controllers.get(i)).start(); 
/*     */   }
/*     */   public void end() {
/*  59 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  60 */       ((ParticleController)this.controllers.get(i)).end(); 
/*     */   }
/*     */   public void reset() {
/*  63 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  64 */       ((ParticleController)this.controllers.get(i)).reset(); 
/*     */   }
/*     */   
/*     */   public void update() {
/*  68 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  69 */       ((ParticleController)this.controllers.get(i)).update(); 
/*     */   }
/*     */   
/*     */   public void draw() {
/*  73 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  74 */       ((ParticleController)this.controllers.get(i)).draw(); 
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/*  78 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  79 */       if (!((ParticleController)this.controllers.get(i)).isComplete()) {
/*  80 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransform(Matrix4 transform) {
/*  89 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  90 */       ((ParticleController)this.controllers.get(i)).setTransform(transform);
/*     */     }
/*     */   }
/*     */   
/*     */   public void rotate(Quaternion rotation) {
/*  95 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  96 */       ((ParticleController)this.controllers.get(i)).rotate(rotation);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Vector3 axis, float angle) {
/* 103 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 104 */       ((ParticleController)this.controllers.get(i)).rotate(axis, angle);
/*     */     }
/*     */   }
/*     */   
/*     */   public void translate(Vector3 translation) {
/* 109 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 110 */       ((ParticleController)this.controllers.get(i)).translate(translation);
/*     */     }
/*     */   }
/*     */   
/*     */   public void scale(float scaleX, float scaleY, float scaleZ) {
/* 115 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 116 */       ((ParticleController)this.controllers.get(i)).scale(scaleX, scaleY, scaleZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public void scale(Vector3 scale) {
/* 121 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 122 */       ((ParticleController)this.controllers.get(i)).scale(scale.x, scale.y, scale.z);
/*     */     }
/*     */   }
/*     */   
/*     */   public Array<ParticleController> getControllers() {
/* 127 */     return this.controllers;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleController findController(String name) {
/* 132 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 133 */       ParticleController emitter = (ParticleController)this.controllers.get(i);
/* 134 */       if (emitter.name.equals(name)) return emitter; 
/*     */     } 
/* 136 */     return null;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 140 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 141 */       ((ParticleController)this.controllers.get(i)).dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 147 */     if (this.bounds == null) this.bounds = new BoundingBox();
/*     */     
/* 149 */     BoundingBox bounds = this.bounds;
/* 150 */     bounds.inf();
/* 151 */     for (ParticleController emitter : this.controllers)
/* 152 */       bounds.ext(emitter.getBoundingBox()); 
/* 153 */     return bounds;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBatch(Array<ParticleBatch<?>> batches) {
/* 159 */     for (ParticleController controller : this.controllers) {
/* 160 */       for (ParticleBatch<?> batch : batches) {
/* 161 */         if (controller.renderer.setBatch(batch))
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ParticleEffect copy() {
/* 168 */     return new ParticleEffect(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager assetManager, ResourceData data) {
/* 173 */     for (ParticleController controller : this.controllers) {
/* 174 */       controller.save(assetManager, data);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager assetManager, ResourceData data) {
/* 180 */     int i = 0;
/* 181 */     for (ParticleController controller : this.controllers)
/* 182 */       controller.load(assetManager, data); 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\particles\ParticleEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */