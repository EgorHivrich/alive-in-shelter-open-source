/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Cubemap;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class FrameBufferCubemap
/*     */   extends GLFrameBuffer<Cubemap>
/*     */ {
/*     */   private int currentSide;
/*     */   
/*     */   public FrameBufferCubemap(Pixmap.Format format, int width, int height, boolean hasDepth) {
/*  75 */     this(format, width, height, hasDepth, false);
/*     */   }
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
/*     */   public FrameBufferCubemap(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/*  88 */     super(format, width, height, hasDepth, hasStencil);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Cubemap createColorTexture() {
/*  93 */     int glFormat = Pixmap.Format.toGlFormat(this.format);
/*  94 */     int glType = Pixmap.Format.toGlType(this.format);
/*  95 */     GLOnlyTextureData data = new GLOnlyTextureData(this.width, this.height, 0, glFormat, glFormat, glType);
/*  96 */     Cubemap result = new Cubemap(data, data, data, data, data, data);
/*  97 */     result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/*  98 */     result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
/*  99 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void disposeColorTexture(Cubemap colorTexture) {
/* 104 */     colorTexture.dispose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {
/* 111 */     this.currentSide = -1;
/* 112 */     super.bind();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nextSide() {
/* 118 */     if (this.currentSide > 5)
/* 119 */       throw new GdxRuntimeException("No remaining sides."); 
/* 120 */     if (this.currentSide == 5) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     this.currentSide++;
/* 125 */     bindSide(getSide());
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindSide(Cubemap.CubemapSide side) {
/* 132 */     Gdx.gl20.glFramebufferTexture2D(36160, 36064, side.glEnum, this.colorTexture
/* 133 */         .getTextureObjectHandle(), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Cubemap.CubemapSide getSide() {
/* 138 */     return (this.currentSide < 0) ? null : Cubemap.CubemapSide.values()[this.currentSide];
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\glutils\FrameBufferCubemap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */