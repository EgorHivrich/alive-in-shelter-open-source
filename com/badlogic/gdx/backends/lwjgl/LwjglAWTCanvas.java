/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Audio;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.Graphics;
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.LifecycleListener;
/*     */ import com.badlogic.gdx.Net;
/*     */ import com.badlogic.gdx.Preferences;
/*     */ import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Clipboard;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.PaintEvent;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.opengl.AWTGLCanvas;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.Drawable;
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
/*     */ 
/*     */ public class LwjglAWTCanvas
/*     */   implements Application
/*     */ {
/*     */   static int instanceCount;
/*     */   LwjglGraphics graphics;
/*     */   OpenALAudio audio;
/*     */   LwjglFiles files;
/*     */   LwjglAWTInput input;
/*     */   LwjglNet net;
/*     */   final ApplicationListener listener;
/*     */   AWTGLCanvas canvas;
/*  69 */   final Array<Runnable> runnables = new Array();
/*  70 */   final Array<Runnable> executedRunnables = new Array();
/*  71 */   final Array<LifecycleListener> lifecycleListeners = new Array();
/*     */   boolean running = true;
/*     */   int lastWidth;
/*     */   int lastHeight;
/*  75 */   int logLevel = 2;
/*  76 */   final String logTag = "LwjglAWTCanvas"; Cursor cursor;
/*     */   Map<String, Preferences> preferences;
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener) {
/*  80 */     this(listener, null, null);
/*     */   }
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener, LwjglAWTCanvas sharedContextCanvas) {
/*  84 */     this(listener, null, sharedContextCanvas);
/*     */   }
/*     */   
/*     */   public LwjglAWTCanvas(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  88 */     this(listener, config, null);
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
/*     */   protected void setDisplayMode(int width, int height) {}
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
/*     */   protected void setTitle(String title) {}
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
/*     */   public ApplicationListener getApplicationListener() {
/* 180 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Canvas getCanvas() {
/* 184 */     return (Canvas)this.canvas;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 189 */     return Gdx.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 194 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics getGraphics() {
/* 199 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 204 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 209 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 214 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 219 */     return 0;
/*     */   }
/*     */   
/*     */   void setGlobals() {
/* 223 */     Gdx.app = this;
/* 224 */     if (this.audio != null) Gdx.audio = (Audio)this.audio; 
/* 225 */     if (this.files != null) Gdx.files = this.files; 
/* 226 */     if (this.net != null) Gdx.net = this.net; 
/* 227 */     Gdx.graphics = this.graphics;
/* 228 */     Gdx.input = this.input;
/*     */   }
/*     */   
/*     */   void create() {
/*     */     try {
/* 233 */       setGlobals();
/* 234 */       this.graphics.initiateGL();
/* 235 */       this.canvas.setVSyncEnabled(this.graphics.config.vSyncEnabled);
/* 236 */       this.listener.create();
/* 237 */       this.lastWidth = Math.max(1, this.graphics.getWidth());
/* 238 */       this.lastHeight = Math.max(1, this.graphics.getHeight());
/* 239 */       this.listener.resize(this.lastWidth, this.lastHeight);
/* 240 */       start();
/* 241 */     } catch (Throwable ex) {
/* 242 */       stopped();
/* 243 */       exception(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   void render(boolean shouldRender) throws LWJGLException {
/* 248 */     if (!this.running)
/*     */       return; 
/* 250 */     setGlobals();
/* 251 */     this.canvas.setCursor(this.cursor);
/*     */     
/* 253 */     int width = Math.max(1, this.graphics.getWidth());
/* 254 */     int height = Math.max(1, this.graphics.getHeight());
/* 255 */     if (this.lastWidth != width || this.lastHeight != height) {
/* 256 */       this.lastWidth = width;
/* 257 */       this.lastHeight = height;
/* 258 */       Gdx.gl.glViewport(0, 0, this.lastWidth, this.lastHeight);
/* 259 */       resize(width, height);
/* 260 */       this.listener.resize(width, height);
/* 261 */       shouldRender = true;
/*     */     } 
/*     */     
/* 264 */     if (executeRunnables()) shouldRender = true;
/*     */ 
/*     */     
/* 267 */     if (!this.running)
/*     */       return; 
/* 269 */     shouldRender |= this.graphics.shouldRender();
/* 270 */     this.input.processEvents();
/* 271 */     if (this.audio != null) this.audio.update();
/*     */     
/* 273 */     if (shouldRender) {
/* 274 */       this.graphics.updateTime();
/* 275 */       this.graphics.frameId++;
/* 276 */       this.listener.render();
/* 277 */       this.canvas.swapBuffers();
/*     */     } 
/*     */     
/* 280 */     Display.sync(getFrameRate() * instanceCount);
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 284 */     synchronized (this.runnables) {
/* 285 */       for (int i = this.runnables.size - 1; i >= 0; i--) {
/* 286 */         this.executedRunnables.addAll((Object[])new Runnable[] { (Runnable)this.runnables.get(i) });
/* 287 */       }  this.runnables.clear();
/*     */     } 
/* 289 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 291 */       ((Runnable)this.executedRunnables.pop()).run();
/* 292 */       if (this.executedRunnables.size <= 0)
/* 293 */         return true; 
/*     */     } 
/*     */   }
/*     */   protected int getFrameRate() {
/* 297 */     int frameRate = isActive() ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 298 */     if (frameRate == -1) frameRate = 10; 
/* 299 */     if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 300 */     if (frameRate == 0) frameRate = 30; 
/* 301 */     return frameRate;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 306 */     Component root = SwingUtilities.getRoot((Component)this.canvas);
/* 307 */     return (root instanceof Frame) ? ((Frame)root).isActive() : true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resize(int width, int height) {}
/*     */ 
/*     */   
/*     */   protected void stopped() {}
/*     */ 
/*     */   
/*     */   public void stop() {
/* 323 */     if (!this.running)
/* 324 */       return;  this.running = false;
/* 325 */     setGlobals();
/* 326 */     Array<LifecycleListener> listeners = this.lifecycleListeners;
/*     */ 
/*     */     
/* 329 */     if (this.canvas.isDisplayable()) {
/* 330 */       makeCurrent();
/*     */     } else {
/* 332 */       error("LwjglAWTCanvas", "OpenGL context destroyed before application listener has had a chance to dispose of textures.");
/*     */     } 
/*     */     
/* 335 */     synchronized (listeners) {
/* 336 */       for (LifecycleListener listener : listeners) {
/* 337 */         listener.pause();
/* 338 */         listener.dispose();
/*     */       } 
/*     */     } 
/* 341 */     this.listener.pause();
/* 342 */     this.listener.dispose();
/*     */     
/* 344 */     Gdx.app = null;
/*     */     
/* 346 */     Gdx.graphics = null;
/*     */     
/* 348 */     if (this.audio != null) {
/* 349 */       this.audio.dispose();
/* 350 */       Gdx.audio = null;
/*     */     } 
/*     */     
/* 353 */     if (this.files != null) Gdx.files = null;
/*     */     
/* 355 */     if (this.net != null) Gdx.net = null;
/*     */     
/* 357 */     instanceCount--;
/*     */     
/* 359 */     stopped();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 364 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 369 */     return getJavaHeap();
/*     */   }
/*     */   
/* 372 */   public LwjglAWTCanvas(ApplicationListener listener, LwjglApplicationConfiguration config, LwjglAWTCanvas sharedContextCanvas) { this.preferences = new HashMap<String, Preferences>(); this.listener = listener; if (config == null) config = new LwjglApplicationConfiguration();  LwjglNativesLoader.load(); instanceCount++; AWTGLCanvas sharedDrawable = (sharedContextCanvas != null) ? sharedContextCanvas.canvas : null; try { this.canvas = new AWTGLCanvas(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), new PixelFormat(), (Drawable)sharedDrawable) {
/*     */           private final Dimension minSize = new Dimension(0, 0); private final LwjglAWTCanvas.NonSystemPaint nonSystemPaint = new LwjglAWTCanvas.NonSystemPaint(this); public Dimension getMinimumSize() { return this.minSize; } public void initGL() { LwjglAWTCanvas.this.create(); } public void paintGL() { try { boolean systemPaint = !(EventQueue.getCurrentEvent() instanceof LwjglAWTCanvas.NonSystemPaint); LwjglAWTCanvas.this.render(systemPaint); Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(this.nonSystemPaint); } catch (Throwable ex) { LwjglAWTCanvas.this.exception(ex); }  }
/*     */         }; } catch (Throwable ex) { exception(ex); return; }  this.canvas.setBackground(new Color(config.initialBackgroundColor.r, config.initialBackgroundColor.g, config.initialBackgroundColor.b, config.initialBackgroundColor.a)); this.graphics = new LwjglGraphics((Canvas)this.canvas, config) {
/*     */         public void setTitle(String title) { super.setTitle(title); LwjglAWTCanvas.this.setTitle(title); } public boolean setWindowedMode(int width, int height) { if (!super.setWindowedMode(width, height)) return false;  LwjglAWTCanvas.this.setDisplayMode(width, height); return true; } public boolean setFullscreenMode(Graphics.DisplayMode displayMode) { if (!super.setFullscreenMode(displayMode)) return false;  LwjglAWTCanvas.this.setDisplayMode(displayMode.width, displayMode.height); return true; } public boolean shouldRender() { synchronized (this) { boolean rq = this.requestRendering; this.requestRendering = false; return (rq || this.isContinuous); }  }
/* 376 */       }; if (!LwjglApplicationConfiguration.disableAudio && Gdx.audio == null) this.audio = new OpenALAudio();  if (Gdx.files == null) this.files = new LwjglFiles();  if (Gdx.net == null) this.net = new LwjglNet();  this.input = new LwjglAWTInput(this); setGlobals(); } public Preferences getPreferences(String name) { if (this.preferences.containsKey(name)) {
/* 377 */       return this.preferences.get(name);
/*     */     }
/* 379 */     Preferences prefs = new LwjglPreferences(name, ".prefs/");
/* 380 */     this.preferences.put(name, prefs);
/* 381 */     return prefs; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 387 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 392 */     synchronized (this.runnables) {
/* 393 */       this.runnables.add(runnable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 399 */     if (this.logLevel >= 3) {
/* 400 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 406 */     if (this.logLevel >= 3) {
/* 407 */       System.out.println(tag + ": " + message);
/* 408 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String tag, String message) {
/* 414 */     if (this.logLevel >= 2) {
/* 415 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 421 */     if (this.logLevel >= 2) {
/* 422 */       System.out.println(tag + ": " + message);
/* 423 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message) {
/* 429 */     if (this.logLevel >= 1) {
/* 430 */       System.err.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 436 */     if (this.logLevel >= 1) {
/* 437 */       System.err.println(tag + ": " + message);
/* 438 */       exception.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 444 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 449 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 454 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 457 */             LwjglAWTCanvas.this.stop();
/* 458 */             System.exit(-1);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void makeCurrent() {
/*     */     try {
/* 467 */       this.canvas.makeCurrent();
/* 468 */       setGlobals();
/* 469 */     } catch (Throwable ex) {
/* 470 */       exception(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCurrent() {
/*     */     try {
/* 477 */       return this.canvas.isCurrent();
/* 478 */     } catch (Throwable ex) {
/* 479 */       exception(ex);
/* 480 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 486 */     this.cursor = cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 491 */     synchronized (this.lifecycleListeners) {
/* 492 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 498 */     synchronized (this.lifecycleListeners) {
/* 499 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void exception(Throwable ex) {
/* 504 */     ex.printStackTrace();
/* 505 */     stop();
/*     */   }
/*     */   
/*     */   public static class NonSystemPaint extends PaintEvent {
/*     */     public NonSystemPaint(AWTGLCanvas canvas) {
/* 510 */       super((Component)canvas, 801, new Rectangle(0, 0, 99999, 99999));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\LwjglAWTCanvas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */