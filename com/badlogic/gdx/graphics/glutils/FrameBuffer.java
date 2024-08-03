/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.GLTexture;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FrameBuffer
/*    */   extends GLFrameBuffer<Texture>
/*    */ {
/*    */   public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
/* 45 */     this(format, width, height, hasDepth, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/* 57 */     super(format, width, height, hasDepth, hasStencil);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Texture createColorTexture() {
/* 62 */     int glFormat = Pixmap.Format.toGlFormat(this.format);
/* 63 */     int glType = Pixmap.Format.toGlType(this.format);
/* 64 */     GLOnlyTextureData data = new GLOnlyTextureData(this.width, this.height, 0, glFormat, glFormat, glType);
/* 65 */     Texture result = new Texture(data);
/* 66 */     result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/* 67 */     result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
/* 68 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void disposeColorTexture(Texture colorTexture) {
/* 73 */     colorTexture.dispose();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void unbind() {
/* 78 */     GLFrameBuffer.unbind();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\glutils\FrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */