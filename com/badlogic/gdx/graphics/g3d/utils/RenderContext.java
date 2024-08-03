/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RenderContext
/*     */ {
/*     */   public final TextureBinder textureBinder;
/*     */   private boolean blending;
/*     */   private int blendSFactor;
/*     */   private int blendDFactor;
/*     */   private int depthFunc;
/*     */   private float depthRangeNear;
/*     */   private float depthRangeFar;
/*     */   private boolean depthMask;
/*     */   private int cullFace;
/*     */   
/*     */   public RenderContext(TextureBinder textures) {
/*  39 */     this.textureBinder = textures;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void begin() {
/*  45 */     Gdx.gl.glDisable(2929);
/*  46 */     this.depthFunc = 0;
/*  47 */     Gdx.gl.glDepthMask(true);
/*  48 */     this.depthMask = true;
/*  49 */     Gdx.gl.glDisable(3042);
/*  50 */     this.blending = false;
/*  51 */     Gdx.gl.glDisable(2884);
/*  52 */     this.cullFace = this.blendSFactor = this.blendDFactor = 0;
/*  53 */     this.textureBinder.begin();
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  58 */     if (this.depthFunc != 0) Gdx.gl.glDisable(2929); 
/*  59 */     if (!this.depthMask) Gdx.gl.glDepthMask(true); 
/*  60 */     if (this.blending) Gdx.gl.glDisable(3042); 
/*  61 */     if (this.cullFace > 0) Gdx.gl.glDisable(2884); 
/*  62 */     this.textureBinder.end();
/*     */   }
/*     */   
/*     */   public void setDepthMask(boolean depthMask) {
/*  66 */     if (this.depthMask != depthMask) Gdx.gl.glDepthMask(this.depthMask = depthMask); 
/*     */   }
/*     */   
/*     */   public void setDepthTest(int depthFunction) {
/*  70 */     setDepthTest(depthFunction, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void setDepthTest(int depthFunction, float depthRangeNear, float depthRangeFar) {
/*  74 */     boolean wasEnabled = (this.depthFunc != 0);
/*  75 */     boolean enabled = (depthFunction != 0);
/*  76 */     if (this.depthFunc != depthFunction) {
/*  77 */       this.depthFunc = depthFunction;
/*  78 */       if (enabled) {
/*  79 */         Gdx.gl.glEnable(2929);
/*  80 */         Gdx.gl.glDepthFunc(depthFunction);
/*     */       } else {
/*  82 */         Gdx.gl.glDisable(2929);
/*     */       } 
/*  84 */     }  if (enabled) {
/*  85 */       if (!wasEnabled || this.depthFunc != depthFunction) Gdx.gl.glDepthFunc(this.depthFunc = depthFunction); 
/*  86 */       if (!wasEnabled || this.depthRangeNear != depthRangeNear || this.depthRangeFar != depthRangeFar)
/*  87 */         Gdx.gl.glDepthRangef(this.depthRangeNear = depthRangeNear, this.depthRangeFar = depthRangeFar); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBlending(boolean enabled, int sFactor, int dFactor) {
/*  92 */     if (enabled != this.blending) {
/*  93 */       this.blending = enabled;
/*  94 */       if (enabled) {
/*  95 */         Gdx.gl.glEnable(3042);
/*     */       } else {
/*  97 */         Gdx.gl.glDisable(3042);
/*     */       } 
/*  99 */     }  if (enabled && (this.blendSFactor != sFactor || this.blendDFactor != dFactor)) {
/* 100 */       Gdx.gl.glBlendFunc(sFactor, dFactor);
/* 101 */       this.blendSFactor = sFactor;
/* 102 */       this.blendDFactor = dFactor;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCullFace(int face) {
/* 107 */     if (face != this.cullFace) {
/* 108 */       this.cullFace = face;
/* 109 */       if (face == 1028 || face == 1029 || face == 1032) {
/* 110 */         Gdx.gl.glEnable(2884);
/* 111 */         Gdx.gl.glCullFace(face);
/*     */       } else {
/* 113 */         Gdx.gl.glDisable(2884);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3\\utils\RenderContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */