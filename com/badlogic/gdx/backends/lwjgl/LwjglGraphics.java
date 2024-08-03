/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.graphics.Cursor;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GL30;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.glutils.GLVersion;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Toolkit;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.ContextAttribs;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.DisplayMode;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.PixelFormat;
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
/*     */ public class LwjglGraphics
/*     */   implements Graphics
/*     */ {
/*     */   static Array<String> extensions;
/*     */   static GLVersion glVersion;
/*     */   GL20 gl20;
/*     */   GL30 gl30;
/*  54 */   long frameId = -1L;
/*  55 */   float deltaTime = 0.0F;
/*  56 */   long frameStart = 0L;
/*  57 */   int frames = 0;
/*     */   int fps;
/*  59 */   long lastTime = System.nanoTime();
/*     */   Canvas canvas;
/*     */   boolean vsync = false;
/*     */   boolean resize = false;
/*     */   LwjglApplicationConfiguration config;
/*  64 */   Graphics.BufferFormat bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 8, 0, false);
/*     */   volatile boolean isContinuous = true;
/*     */   volatile boolean requestRendering = false;
/*     */   boolean softwareMode;
/*     */   boolean usingGL30;
/*     */   
/*     */   LwjglGraphics(LwjglApplicationConfiguration config) {
/*  71 */     this.config = config;
/*     */   }
/*     */   
/*     */   LwjglGraphics(Canvas canvas) {
/*  75 */     this.config = new LwjglApplicationConfiguration();
/*  76 */     this.config.width = canvas.getWidth();
/*  77 */     this.config.height = canvas.getHeight();
/*  78 */     this.canvas = canvas;
/*     */   }
/*     */   
/*     */   LwjglGraphics(Canvas canvas, LwjglApplicationConfiguration config) {
/*  82 */     this.config = config;
/*  83 */     this.canvas = canvas;
/*     */   }
/*     */   
/*     */   public GL20 getGL20() {
/*  87 */     return this.gl20;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  91 */     if (this.canvas != null) {
/*  92 */       return Math.max(1, this.canvas.getHeight());
/*     */     }
/*  94 */     return (int)(Display.getHeight() * Display.getPixelScaleFactor());
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  98 */     if (this.canvas != null) {
/*  99 */       return Math.max(1, this.canvas.getWidth());
/*     */     }
/* 101 */     return (int)(Display.getWidth() * Display.getPixelScaleFactor());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferWidth() {
/* 106 */     return getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBackBufferHeight() {
/* 111 */     return getHeight();
/*     */   }
/*     */   
/*     */   public boolean isGL20Available() {
/* 115 */     return (this.gl20 != null);
/*     */   }
/*     */   
/*     */   public long getFrameId() {
/* 119 */     return this.frameId;
/*     */   }
/*     */   
/*     */   public float getDeltaTime() {
/* 123 */     return this.deltaTime;
/*     */   }
/*     */   
/*     */   public float getRawDeltaTime() {
/* 127 */     return this.deltaTime;
/*     */   }
/*     */   
/*     */   public Graphics.GraphicsType getType() {
/* 131 */     return Graphics.GraphicsType.LWJGL;
/*     */   }
/*     */   
/*     */   public GLVersion getGLVersion() {
/* 135 */     return glVersion;
/*     */   }
/*     */   
/*     */   public int getFramesPerSecond() {
/* 139 */     return this.fps;
/*     */   }
/*     */   
/*     */   void updateTime() {
/* 143 */     long time = System.nanoTime();
/* 144 */     this.deltaTime = (float)(time - this.lastTime) / 1.0E9F;
/* 145 */     this.lastTime = time;
/*     */     
/* 147 */     if (time - this.frameStart >= 1000000000L) {
/* 148 */       this.fps = this.frames;
/* 149 */       this.frames = 0;
/* 150 */       this.frameStart = time;
/*     */     } 
/* 152 */     this.frames++;
/*     */   }
/*     */   
/*     */   void setupDisplay() throws LWJGLException {
/* 156 */     if (this.config.useHDPI) {
/* 157 */       System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "true");
/*     */     }
/*     */     
/* 160 */     if (this.canvas != null) {
/* 161 */       Display.setParent(this.canvas);
/*     */     } else {
/* 163 */       boolean displayCreated = false;
/*     */       
/* 165 */       if (!this.config.fullscreen) {
/* 166 */         displayCreated = setWindowedMode(this.config.width, this.config.height);
/*     */       } else {
/* 168 */         Graphics.DisplayMode bestMode = null;
/* 169 */         for (Graphics.DisplayMode mode : getDisplayModes()) {
/* 170 */           if (mode.width == this.config.width && mode.height == this.config.height && (
/* 171 */             bestMode == null || bestMode.refreshRate < (getDisplayMode()).refreshRate)) {
/* 172 */             bestMode = mode;
/*     */           }
/*     */         } 
/*     */         
/* 176 */         if (bestMode == null) {
/* 177 */           bestMode = getDisplayMode();
/*     */         }
/* 179 */         displayCreated = setFullscreenMode(bestMode);
/*     */       } 
/* 181 */       if (!displayCreated) {
/* 182 */         if (this.config.setDisplayModeCallback != null) {
/* 183 */           this.config = this.config.setDisplayModeCallback.onFailure(this.config);
/* 184 */           if (this.config != null) {
/* 185 */             displayCreated = setWindowedMode(this.config.width, this.config.height);
/*     */           }
/*     */         } 
/* 188 */         if (!displayCreated) {
/* 189 */           throw new GdxRuntimeException("Couldn't set display mode " + this.config.width + "x" + this.config.height + ", fullscreen: " + this.config.fullscreen);
/*     */         }
/*     */       } 
/*     */       
/* 193 */       if (this.config.iconPaths.size > 0) {
/* 194 */         ByteBuffer[] icons = new ByteBuffer[this.config.iconPaths.size];
/* 195 */         for (int i = 0, n = this.config.iconPaths.size; i < n; i++) {
/* 196 */           Pixmap pixmap = new Pixmap(Gdx.files.getFileHandle((String)this.config.iconPaths.get(i), (Files.FileType)this.config.iconFileTypes.get(i)));
/* 197 */           if (pixmap.getFormat() != Pixmap.Format.RGBA8888) {
/* 198 */             Pixmap rgba = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
/* 199 */             rgba.drawPixmap(pixmap, 0, 0);
/* 200 */             pixmap = rgba;
/*     */           } 
/* 202 */           icons[i] = ByteBuffer.allocateDirect(pixmap.getPixels().limit());
/* 203 */           icons[i].put(pixmap.getPixels()).flip();
/* 204 */           pixmap.dispose();
/*     */         } 
/* 206 */         Display.setIcon(icons);
/*     */       } 
/*     */     } 
/* 209 */     Display.setTitle(this.config.title);
/* 210 */     Display.setResizable(this.config.resizable);
/* 211 */     Display.setInitialBackground(this.config.initialBackgroundColor.r, this.config.initialBackgroundColor.g, this.config.initialBackgroundColor.b);
/*     */ 
/*     */     
/* 214 */     Display.setLocation(this.config.x, this.config.y);
/* 215 */     createDisplayPixelFormat(this.config.useGL30, this.config.gles30ContextMajorVersion, this.config.gles30ContextMinorVersion);
/* 216 */     initiateGL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void initiateGL() {
/* 223 */     extractVersion();
/* 224 */     extractExtensions();
/* 225 */     initiateGLInstances();
/*     */   }
/*     */   
/*     */   private static void extractVersion() {
/* 229 */     String versionString = GL11.glGetString(7938);
/* 230 */     String vendorString = GL11.glGetString(7936);
/* 231 */     String rendererString = GL11.glGetString(7937);
/* 232 */     glVersion = new GLVersion(Application.ApplicationType.Desktop, versionString, vendorString, rendererString);
/*     */   }
/*     */   
/*     */   private static void extractExtensions() {
/* 236 */     extensions = new Array();
/* 237 */     if (glVersion.isVersionEqualToOrHigher(3, 2)) {
/* 238 */       int numExtensions = GL11.glGetInteger(33309);
/* 239 */       for (int i = 0; i < numExtensions; i++)
/* 240 */         extensions.add(GL30.glGetStringi(7939, i)); 
/*     */     } else {
/* 242 */       extensions.addAll((Object[])GL11.glGetString(7939).split(" "));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean fullCompatibleWithGLES3() {
/* 249 */     return glVersion.isVersionEqualToOrHigher(4, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean fullCompatibleWithGLES2() {
/* 256 */     return (glVersion.isVersionEqualToOrHigher(4, 1) || extensions.contains("GL_ARB_ES2_compatibility", false));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean supportsFBO() {
/* 261 */     return (glVersion.isVersionEqualToOrHigher(3, 0) || extensions.contains("GL_EXT_framebuffer_object", false) || extensions
/* 262 */       .contains("GL_ARB_framebuffer_object", false));
/*     */   }
/*     */   
/*     */   private void createDisplayPixelFormat(boolean useGL30, int gles30ContextMajor, int gles30ContextMinor) {
/*     */     try {
/* 267 */       if (useGL30) {
/*     */         
/* 269 */         ContextAttribs context = (new ContextAttribs(gles30ContextMajor, gles30ContextMinor)).withForwardCompatible(false).withProfileCore(true);
/*     */         try {
/* 271 */           Display.create(new PixelFormat(this.config.r + this.config.g + this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples), context);
/*     */         }
/* 273 */         catch (Exception e) {
/* 274 */           System.out.println("LwjglGraphics: OpenGL " + gles30ContextMajor + "." + gles30ContextMinor + "+ core profile (GLES 3.0) not supported.");
/*     */           
/* 276 */           createDisplayPixelFormat(false, gles30ContextMajor, gles30ContextMinor);
/*     */           return;
/*     */         } 
/* 279 */         System.out.println("LwjglGraphics: created OpenGL " + gles30ContextMajor + "." + gles30ContextMinor + "+ core profile (GLES 3.0) context. This is experimental!");
/*     */         
/* 281 */         this.usingGL30 = true;
/*     */       } else {
/*     */         
/* 284 */         Display.create(new PixelFormat(this.config.r + this.config.g + this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples));
/* 285 */         this.usingGL30 = false;
/*     */       } 
/* 287 */       this.bufferFormat = new Graphics.BufferFormat(this.config.r, this.config.g, this.config.b, this.config.a, this.config.depth, this.config.stencil, this.config.samples, false);
/*     */     }
/* 289 */     catch (Exception ex) {
/* 290 */       Display.destroy();
/*     */       try {
/* 292 */         Thread.sleep(200L);
/* 293 */       } catch (InterruptedException interruptedException) {}
/*     */       
/*     */       try {
/* 296 */         Display.create(new PixelFormat(0, 16, 8));
/* 297 */         if ((getDisplayMode()).bitsPerPixel == 16) {
/* 298 */           this.bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 16, 8, 0, false);
/*     */         }
/* 300 */         if ((getDisplayMode()).bitsPerPixel == 24) {
/* 301 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 0, 16, 8, 0, false);
/*     */         }
/* 303 */         if ((getDisplayMode()).bitsPerPixel == 32) {
/* 304 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 16, 8, 0, false);
/*     */         }
/* 306 */       } catch (Exception ex2) {
/* 307 */         Display.destroy();
/*     */         try {
/* 309 */           Thread.sleep(200L);
/* 310 */         } catch (InterruptedException interruptedException) {}
/*     */         
/*     */         try {
/* 313 */           Display.create(new PixelFormat());
/* 314 */         } catch (Exception ex3) {
/* 315 */           if (!this.softwareMode && this.config.allowSoftwareMode) {
/* 316 */             this.softwareMode = true;
/* 317 */             System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
/* 318 */             createDisplayPixelFormat(useGL30, gles30ContextMajor, gles30ContextMinor);
/*     */             return;
/*     */           } 
/* 321 */           throw new GdxRuntimeException("OpenGL is not supported by the video driver: " + glVersion.getDebugVersionString(), ex3);
/*     */         } 
/* 323 */         if ((getDisplayMode()).bitsPerPixel == 16) {
/* 324 */           this.bufferFormat = new Graphics.BufferFormat(5, 6, 5, 0, 8, 0, 0, false);
/*     */         }
/* 326 */         if ((getDisplayMode()).bitsPerPixel == 24) {
/* 327 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 0, 8, 0, 0, false);
/*     */         }
/* 329 */         if ((getDisplayMode()).bitsPerPixel == 32) {
/* 330 */           this.bufferFormat = new Graphics.BufferFormat(8, 8, 8, 8, 8, 0, 0, false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initiateGLInstances() {
/* 337 */     if (this.usingGL30) {
/* 338 */       this.gl30 = new LwjglGL30();
/* 339 */       this.gl20 = (GL20)this.gl30;
/*     */     } else {
/* 341 */       this.gl20 = new LwjglGL20();
/*     */     } 
/*     */     
/* 344 */     if (!glVersion.isVersionEqualToOrHigher(2, 0)) {
/* 345 */       throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: " + 
/* 346 */           GL11.glGetString(7938) + "\n" + glVersion.getDebugVersionString());
/*     */     }
/* 348 */     if (!supportsFBO()) {
/* 349 */       throw new GdxRuntimeException("OpenGL 2.0 or higher with the FBO extension is required. OpenGL version: " + 
/* 350 */           GL11.glGetString(7938) + ", FBO extension: false\n" + glVersion.getDebugVersionString());
/*     */     }
/*     */     
/* 353 */     Gdx.gl = this.gl20;
/* 354 */     Gdx.gl20 = this.gl20;
/* 355 */     Gdx.gl30 = this.gl30;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiX() {
/* 360 */     return Toolkit.getDefaultToolkit().getScreenResolution();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpiY() {
/* 365 */     return Toolkit.getDefaultToolkit().getScreenResolution();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcX() {
/* 370 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPpcY() {
/* 375 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 2.54F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDensity() {
/* 380 */     if (this.config.overrideDensity != -1) return this.config.overrideDensity / 160.0F; 
/* 381 */     return Toolkit.getDefaultToolkit().getScreenResolution() / 160.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDisplayModeChange() {
/* 386 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getPrimaryMonitor() {
/* 391 */     return new LwjglMonitor(0, 0, "Primary Monitor");
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor getMonitor() {
/* 396 */     return getPrimaryMonitor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.Monitor[] getMonitors() {
/* 401 */     return new Graphics.Monitor[] { getPrimaryMonitor() };
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes(Graphics.Monitor monitor) {
/* 406 */     return getDisplayModes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode(Graphics.Monitor monitor) {
/* 411 */     return getDisplayMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setFullscreenMode(Graphics.DisplayMode displayMode) {
/* 416 */     DisplayMode mode = ((LwjglDisplayMode)displayMode).mode;
/*     */     try {
/* 418 */       if (!mode.isFullscreenCapable()) {
/* 419 */         Display.setDisplayMode(mode);
/*     */       } else {
/* 421 */         Display.setDisplayModeAndFullscreen(mode);
/*     */       } 
/* 423 */       float scaleFactor = Display.getPixelScaleFactor();
/* 424 */       this.config.width = (int)(mode.getWidth() * scaleFactor);
/* 425 */       this.config.height = (int)(mode.getHeight() * scaleFactor);
/* 426 */       if (Gdx.gl != null) Gdx.gl.glViewport(0, 0, this.config.width, this.config.height); 
/* 427 */       this.resize = true;
/* 428 */       return true;
/* 429 */     } catch (LWJGLException e) {
/* 430 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setWindowedMode(int width, int height) {
/* 437 */     if (getWidth() == width && getHeight() == height && !Display.isFullscreen()) {
/* 438 */       return true;
/*     */     }
/*     */     
/*     */     try {
/* 442 */       DisplayMode targetDisplayMode = null;
/* 443 */       boolean fullscreen = false;
/*     */       
/* 445 */       if (fullscreen) {
/* 446 */         DisplayMode[] modes = Display.getAvailableDisplayModes();
/* 447 */         int freq = 0;
/*     */         
/* 449 */         for (int i = 0; i < modes.length; i++) {
/* 450 */           DisplayMode current = modes[i];
/*     */           
/* 452 */           if (current.getWidth() == width && current.getHeight() == height) {
/* 453 */             if ((targetDisplayMode == null || current.getFrequency() >= freq) && (
/* 454 */               targetDisplayMode == null || current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
/* 455 */               targetDisplayMode = current;
/* 456 */               freq = targetDisplayMode.getFrequency();
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 463 */             if (current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel() && current
/* 464 */               .getFrequency() == Display.getDesktopDisplayMode().getFrequency()) {
/* 465 */               targetDisplayMode = current;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 471 */         targetDisplayMode = new DisplayMode(width, height);
/*     */       } 
/*     */       
/* 474 */       if (targetDisplayMode == null) {
/* 475 */         return false;
/*     */       }
/*     */       
/* 478 */       boolean resizable = (!fullscreen && this.config.resizable);
/*     */       
/* 480 */       Display.setDisplayMode(targetDisplayMode);
/* 481 */       Display.setFullscreen(fullscreen);
/*     */       
/* 483 */       if (resizable == Display.isResizable()) {
/* 484 */         Display.setResizable(!resizable);
/*     */       }
/* 486 */       Display.setResizable(resizable);
/*     */       
/* 488 */       float scaleFactor = Display.getPixelScaleFactor();
/* 489 */       this.config.width = (int)(targetDisplayMode.getWidth() * scaleFactor);
/* 490 */       this.config.height = (int)(targetDisplayMode.getHeight() * scaleFactor);
/* 491 */       if (Gdx.gl != null) Gdx.gl.glViewport(0, 0, this.config.width, this.config.height); 
/* 492 */       this.resize = true;
/* 493 */       return true;
/* 494 */     } catch (LWJGLException e) {
/* 495 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode[] getDisplayModes() {
/*     */     try {
/* 502 */       DisplayMode[] availableDisplayModes = Display.getAvailableDisplayModes();
/* 503 */       Graphics.DisplayMode[] modes = new Graphics.DisplayMode[availableDisplayModes.length];
/*     */       
/* 505 */       int idx = 0;
/* 506 */       for (DisplayMode mode : availableDisplayModes) {
/* 507 */         if (mode.isFullscreenCapable()) {
/* 508 */           modes[idx++] = new LwjglDisplayMode(mode.getWidth(), mode.getHeight(), mode.getFrequency(), mode
/* 509 */               .getBitsPerPixel(), mode);
/*     */         }
/*     */       } 
/*     */       
/* 513 */       return modes;
/* 514 */     } catch (LWJGLException e) {
/* 515 */       throw new GdxRuntimeException("Couldn't fetch available display modes", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.DisplayMode getDisplayMode() {
/* 521 */     DisplayMode mode = Display.getDesktopDisplayMode();
/* 522 */     return new LwjglDisplayMode(mode.getWidth(), mode.getHeight(), mode.getFrequency(), mode.getBitsPerPixel(), mode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {
/* 527 */     Display.setTitle(title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUndecorated(boolean undecorated) {
/* 536 */     System.setProperty("org.lwjgl.opengl.Window.undecorated", undecorated ? "true" : "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResizable(boolean resizable) {
/* 545 */     this.config.resizable = resizable;
/* 546 */     Display.setResizable(resizable);
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics.BufferFormat getBufferFormat() {
/* 551 */     return this.bufferFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVSync(boolean vsync) {
/* 556 */     this.vsync = vsync;
/* 557 */     Display.setVSyncEnabled(vsync);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsExtension(String extension) {
/* 562 */     return extensions.contains(extension, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContinuousRendering(boolean isContinuous) {
/* 567 */     this.isContinuous = isContinuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isContinuousRendering() {
/* 572 */     return this.isContinuous;
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestRendering() {
/* 577 */     synchronized (this) {
/* 578 */       this.requestRendering = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRender() {
/* 583 */     synchronized (this) {
/* 584 */       boolean rq = this.requestRendering;
/* 585 */       this.requestRendering = false;
/* 586 */       return (rq || this.isContinuous || Display.isDirty());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullscreen() {
/* 592 */     return Display.isFullscreen();
/*     */   }
/*     */   
/*     */   public boolean isSoftwareMode() {
/* 596 */     return this.softwareMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGL30Available() {
/* 601 */     return (this.gl30 != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public GL30 getGL30() {
/* 606 */     return this.gl30;
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
/*     */   
/*     */   public Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot) {
/* 620 */     return new LwjglCursor(pixmap, xHotspot, yHotspot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 625 */     if (this.canvas != null && SharedLibraryLoader.isMac) {
/*     */       return;
/*     */     }
/*     */     try {
/* 629 */       Mouse.setNativeCursor(((LwjglCursor)cursor).lwjglCursor);
/* 630 */     } catch (LWJGLException e) {
/* 631 */       throw new GdxRuntimeException("Could not set cursor image.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSystemCursor(Cursor.SystemCursor systemCursor) {
/* 637 */     if (this.canvas != null && SharedLibraryLoader.isMac) {
/*     */       return;
/*     */     }
/*     */     try {
/* 641 */       Mouse.setNativeCursor(null);
/* 642 */     } catch (LWJGLException e) {
/* 643 */       throw new GdxRuntimeException("Couldn't set system cursor");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface SetDisplayModeCallback {
/*     */     LwjglApplicationConfiguration onFailure(LwjglApplicationConfiguration param1LwjglApplicationConfiguration); }
/*     */   
/*     */   private class LwjglDisplayMode extends Graphics.DisplayMode { public LwjglDisplayMode(int width, int height, int refreshRate, int bitsPerPixel, DisplayMode mode) {
/* 651 */       super(width, height, refreshRate, bitsPerPixel);
/* 652 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     DisplayMode mode; }
/*     */   
/*     */   private class LwjglMonitor extends Graphics.Monitor { protected LwjglMonitor(int virtualX, int virtualY, String name) {
/* 658 */       super(virtualX, virtualY, name);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\LwjglGraphics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */