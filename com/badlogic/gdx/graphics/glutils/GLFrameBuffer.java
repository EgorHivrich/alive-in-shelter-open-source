/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class GLFrameBuffer<T extends GLTexture>
/*     */   implements Disposable
/*     */ {
/*  52 */   private static final Map<Application, Array<GLFrameBuffer>> buffers = new HashMap<Application, Array<GLFrameBuffer>>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int GL_DEPTH24_STENCIL8_OES = 35056;
/*     */ 
/*     */ 
/*     */   
/*     */   protected T colorTexture;
/*     */ 
/*     */   
/*     */   private static int defaultFramebufferHandle;
/*     */ 
/*     */   
/*     */   private static boolean defaultFramebufferHandleInitialized = false;
/*     */ 
/*     */   
/*     */   private int framebufferHandle;
/*     */ 
/*     */   
/*     */   private int depthbufferHandle;
/*     */ 
/*     */   
/*     */   private int stencilbufferHandle;
/*     */ 
/*     */   
/*     */   private int depthStencilPackedBufferHandle;
/*     */ 
/*     */   
/*     */   protected final int width;
/*     */ 
/*     */   
/*     */   protected final int height;
/*     */ 
/*     */   
/*     */   protected final boolean hasDepth;
/*     */ 
/*     */   
/*     */   protected final boolean hasStencil;
/*     */ 
/*     */   
/*     */   private boolean hasDepthStencilPackedBuffer;
/*     */ 
/*     */   
/*     */   protected final Pixmap.Format format;
/*     */ 
/*     */ 
/*     */   
/*     */   public GLFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
/* 101 */     this(format, width, height, hasDepth, false);
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
/*     */   public GLFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/* 113 */     this.width = width;
/* 114 */     this.height = height;
/* 115 */     this.format = format;
/* 116 */     this.hasDepth = hasDepth;
/* 117 */     this.hasStencil = hasStencil;
/* 118 */     build();
/*     */     
/* 120 */     addManagedFrameBuffer(Gdx.app, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T createColorTexture();
/*     */ 
/*     */   
/*     */   protected abstract void disposeColorTexture(T paramT);
/*     */   
/*     */   private void build() {
/* 130 */     GL20 gl = Gdx.gl20;
/*     */ 
/*     */     
/* 133 */     if (!defaultFramebufferHandleInitialized) {
/* 134 */       defaultFramebufferHandleInitialized = true;
/* 135 */       if (Gdx.app.getType() == Application.ApplicationType.iOS) {
/* 136 */         IntBuffer intbuf = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
/* 137 */         gl.glGetIntegerv(36006, intbuf);
/* 138 */         defaultFramebufferHandle = intbuf.get(0);
/*     */       } else {
/* 140 */         defaultFramebufferHandle = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     this.colorTexture = createColorTexture();
/*     */     
/* 146 */     this.framebufferHandle = gl.glGenFramebuffer();
/*     */     
/* 148 */     if (this.hasDepth) {
/* 149 */       this.depthbufferHandle = gl.glGenRenderbuffer();
/*     */     }
/*     */     
/* 152 */     if (this.hasStencil) {
/* 153 */       this.stencilbufferHandle = gl.glGenRenderbuffer();
/*     */     }
/*     */     
/* 156 */     gl.glBindTexture(3553, this.colorTexture.getTextureObjectHandle());
/*     */     
/* 158 */     if (this.hasDepth) {
/* 159 */       gl.glBindRenderbuffer(36161, this.depthbufferHandle);
/* 160 */       gl.glRenderbufferStorage(36161, 33189, this.colorTexture.getWidth(), this.colorTexture
/* 161 */           .getHeight());
/*     */     } 
/*     */     
/* 164 */     if (this.hasStencil) {
/* 165 */       gl.glBindRenderbuffer(36161, this.stencilbufferHandle);
/* 166 */       gl.glRenderbufferStorage(36161, 36168, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/*     */     } 
/*     */     
/* 169 */     gl.glBindFramebuffer(36160, this.framebufferHandle);
/* 170 */     gl.glFramebufferTexture2D(36160, 36064, 3553, this.colorTexture
/* 171 */         .getTextureObjectHandle(), 0);
/* 172 */     if (this.hasDepth) {
/* 173 */       gl.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthbufferHandle);
/*     */     }
/*     */     
/* 176 */     if (this.hasStencil) {
/* 177 */       gl.glFramebufferRenderbuffer(36160, 36128, 36161, this.stencilbufferHandle);
/*     */     }
/*     */     
/* 180 */     gl.glBindRenderbuffer(36161, 0);
/* 181 */     gl.glBindTexture(3553, 0);
/*     */     
/* 183 */     int result = gl.glCheckFramebufferStatus(36160);
/*     */     
/* 185 */     if (result == 36061 && this.hasDepth && this.hasStencil && (Gdx.graphics
/* 186 */       .supportsExtension("GL_OES_packed_depth_stencil") || Gdx.graphics
/* 187 */       .supportsExtension("GL_EXT_packed_depth_stencil"))) {
/* 188 */       if (this.hasDepth) {
/* 189 */         gl.glDeleteRenderbuffer(this.depthbufferHandle);
/* 190 */         this.depthbufferHandle = 0;
/*     */       } 
/* 192 */       if (this.hasStencil) {
/* 193 */         gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/* 194 */         this.stencilbufferHandle = 0;
/*     */       } 
/*     */       
/* 197 */       this.depthStencilPackedBufferHandle = gl.glGenRenderbuffer();
/* 198 */       this.hasDepthStencilPackedBuffer = true;
/* 199 */       gl.glBindRenderbuffer(36161, this.depthStencilPackedBufferHandle);
/* 200 */       gl.glRenderbufferStorage(36161, 35056, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/* 201 */       gl.glBindRenderbuffer(36161, 0);
/*     */       
/* 203 */       gl.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthStencilPackedBufferHandle);
/* 204 */       gl.glFramebufferRenderbuffer(36160, 36128, 36161, this.depthStencilPackedBufferHandle);
/* 205 */       result = gl.glCheckFramebufferStatus(36160);
/*     */     } 
/*     */     
/* 208 */     gl.glBindFramebuffer(36160, defaultFramebufferHandle);
/*     */     
/* 210 */     if (result != 36053) {
/* 211 */       disposeColorTexture(this.colorTexture);
/*     */       
/* 213 */       if (this.hasDepthStencilPackedBuffer) {
/* 214 */         gl.glDeleteBuffer(this.depthStencilPackedBufferHandle);
/*     */       } else {
/* 216 */         if (this.hasDepth) gl.glDeleteRenderbuffer(this.depthbufferHandle); 
/* 217 */         if (this.hasStencil) gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/*     */       
/*     */       } 
/* 220 */       gl.glDeleteFramebuffer(this.framebufferHandle);
/*     */       
/* 222 */       if (result == 36054)
/* 223 */         throw new IllegalStateException("frame buffer couldn't be constructed: incomplete attachment"); 
/* 224 */       if (result == 36057)
/* 225 */         throw new IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions"); 
/* 226 */       if (result == 36055)
/* 227 */         throw new IllegalStateException("frame buffer couldn't be constructed: missing attachment"); 
/* 228 */       if (result == 36061)
/* 229 */         throw new IllegalStateException("frame buffer couldn't be constructed: unsupported combination of formats"); 
/* 230 */       throw new IllegalStateException("frame buffer couldn't be constructed: unknown error " + result);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 237 */     GL20 gl = Gdx.gl20;
/*     */     
/* 239 */     disposeColorTexture(this.colorTexture);
/*     */     
/* 241 */     if (this.hasDepthStencilPackedBuffer) {
/* 242 */       gl.glDeleteRenderbuffer(this.depthStencilPackedBufferHandle);
/*     */     } else {
/* 244 */       if (this.hasDepth) gl.glDeleteRenderbuffer(this.depthbufferHandle); 
/* 245 */       if (this.hasStencil) gl.glDeleteRenderbuffer(this.stencilbufferHandle);
/*     */     
/*     */     } 
/* 248 */     gl.glDeleteFramebuffer(this.framebufferHandle);
/*     */     
/* 250 */     if (buffers.get(Gdx.app) != null) ((Array)buffers.get(Gdx.app)).removeValue(this, true);
/*     */   
/*     */   }
/*     */   
/*     */   public void bind() {
/* 255 */     Gdx.gl20.glBindFramebuffer(36160, this.framebufferHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void unbind() {
/* 260 */     Gdx.gl20.glBindFramebuffer(36160, defaultFramebufferHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 265 */     bind();
/* 266 */     setFrameBufferViewport();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setFrameBufferViewport() {
/* 271 */     Gdx.gl20.glViewport(0, 0, this.colorTexture.getWidth(), this.colorTexture.getHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 276 */     end(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end(int x, int y, int width, int height) {
/* 286 */     unbind();
/* 287 */     Gdx.gl20.glViewport(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getColorBufferTexture() {
/* 292 */     return this.colorTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFramebufferHandle() {
/* 297 */     return this.framebufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepthBufferHandle() {
/* 302 */     return this.depthbufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStencilBufferHandle() {
/* 307 */     return this.stencilbufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDepthStencilPackedBuffer() {
/* 312 */     return this.depthStencilPackedBufferHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 317 */     return this.colorTexture.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 322 */     return this.colorTexture.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 327 */     return this.colorTexture.getDepth();
/*     */   }
/*     */   
/*     */   private static void addManagedFrameBuffer(Application app, GLFrameBuffer frameBuffer) {
/* 331 */     Array<GLFrameBuffer> managedResources = buffers.get(app);
/* 332 */     if (managedResources == null) managedResources = new Array(); 
/* 333 */     managedResources.add(frameBuffer);
/* 334 */     buffers.put(app, managedResources);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invalidateAllFrameBuffers(Application app) {
/* 340 */     if (Gdx.gl20 == null)
/*     */       return; 
/* 342 */     Array<GLFrameBuffer> bufferArray = buffers.get(app);
/* 343 */     if (bufferArray == null)
/* 344 */       return;  for (int i = 0; i < bufferArray.size; i++) {
/* 345 */       ((GLFrameBuffer)bufferArray.get(i)).build();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clearAllFrameBuffers(Application app) {
/* 350 */     buffers.remove(app);
/*     */   }
/*     */   
/*     */   public static StringBuilder getManagedStatus(StringBuilder builder) {
/* 354 */     builder.append("Managed buffers/app: { ");
/* 355 */     for (Application app : buffers.keySet()) {
/* 356 */       builder.append(((Array)buffers.get(app)).size);
/* 357 */       builder.append(" ");
/*     */     } 
/* 359 */     builder.append("}");
/* 360 */     return builder;
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 364 */     return getManagedStatus(new StringBuilder()).toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\glutils\GLFrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */