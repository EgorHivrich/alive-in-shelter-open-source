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
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.opengl.Display;
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
/*     */ public class LwjglCanvas
/*     */   implements Application
/*     */ {
/*  50 */   static boolean isWindows = System.getProperty("os.name").contains("Windows");
/*     */   
/*     */   LwjglGraphics graphics;
/*     */   OpenALAudio audio;
/*     */   LwjglFiles files;
/*     */   LwjglInput input;
/*     */   LwjglNet net;
/*     */   ApplicationListener listener;
/*     */   Canvas canvas;
/*  59 */   final Array<Runnable> runnables = new Array();
/*  60 */   final Array<Runnable> executedRunnables = new Array();
/*  61 */   final Array<LifecycleListener> lifecycleListeners = new Array();
/*     */   boolean running = true;
/*  63 */   int logLevel = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   Cursor cursor;
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, Preferences> preferences;
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  76 */     LwjglNativesLoader.load();
/*     */     
/*  78 */     this.canvas = new Canvas() {
/*  79 */         private final Dimension minSize = new Dimension(1, 1);
/*     */         
/*     */         public final void addNotify() {
/*  82 */           super.addNotify();
/*  83 */           if (SharedLibraryLoader.isMac) {
/*  84 */             EventQueue.invokeLater(new Runnable() {
/*     */                   public void run() {
/*  86 */                     LwjglCanvas.this.create();
/*     */                   }
/*     */                 });
/*     */           } else {
/*  90 */             LwjglCanvas.this.create();
/*     */           } 
/*     */         }
/*     */         public final void removeNotify() {
/*  94 */           LwjglCanvas.this.stop();
/*  95 */           super.removeNotify();
/*     */         }
/*     */         
/*     */         public Dimension getMinimumSize() {
/*  99 */           return this.minSize;
/*     */         }
/*     */       };
/* 102 */     this.canvas.setSize(1, 1);
/* 103 */     this.canvas.setIgnoreRepaint(true);
/*     */     
/* 105 */     this.graphics = new LwjglGraphics(this.canvas, config) {
/*     */         public void setTitle(String title) {
/* 107 */           super.setTitle(title);
/* 108 */           LwjglCanvas.this.setTitle(title);
/*     */         }
/*     */         
/*     */         public boolean setWindowedMode(int width, int height, boolean fullscreen) {
/* 112 */           if (!setWindowedMode(width, height)) return false; 
/* 113 */           if (!fullscreen) LwjglCanvas.this.setDisplayMode(width, height); 
/* 114 */           return true;
/*     */         }
/*     */         
/*     */         public boolean setFullscreenMode(Graphics.DisplayMode displayMode) {
/* 118 */           if (!super.setFullscreenMode(displayMode)) return false; 
/* 119 */           LwjglCanvas.this.setDisplayMode(displayMode.width, displayMode.height);
/* 120 */           return true;
/*     */         }
/*     */       };
/* 123 */     this.graphics.setVSync(config.vSyncEnabled);
/* 124 */     if (!LwjglApplicationConfiguration.disableAudio) this.audio = new OpenALAudio(); 
/* 125 */     this.files = new LwjglFiles();
/* 126 */     this.input = new LwjglInput();
/* 127 */     this.net = new LwjglNet();
/* 128 */     this.listener = listener;
/*     */     
/* 130 */     Gdx.app = this;
/* 131 */     Gdx.graphics = this.graphics;
/* 132 */     Gdx.audio = (Audio)this.audio;
/* 133 */     Gdx.files = this.files;
/* 134 */     Gdx.input = this.input;
/* 135 */     Gdx.net = this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDisplayMode(int width, int height) {}
/*     */ 
/*     */   
/*     */   protected void setTitle(String title) {}
/*     */ 
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 146 */     return this.listener;
/*     */   }
/*     */   
/*     */   public Canvas getCanvas() {
/* 150 */     return this.canvas;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 155 */     return (Audio)this.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 160 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics getGraphics() {
/* 165 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 170 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 175 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 180 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 185 */     return 0;
/*     */   }
/*     */   
/*     */   void create() {
/*     */     try {
/* 190 */       this.graphics.setupDisplay();
/*     */       
/* 192 */       this.listener.create();
/* 193 */       this.listener.resize(Math.max(1, this.graphics.getWidth()), Math.max(1, this.graphics.getHeight()));
/*     */       
/* 195 */       start();
/* 196 */     } catch (Exception ex) {
/* 197 */       stopped();
/* 198 */       exception(ex);
/*     */       
/*     */       return;
/*     */     } 
/* 202 */     EventQueue.invokeLater(new Runnable() {
/* 203 */           int lastWidth = Math.max(1, LwjglCanvas.this.graphics.getWidth());
/* 204 */           int lastHeight = Math.max(1, LwjglCanvas.this.graphics.getHeight());
/*     */           
/*     */           public void run() {
/* 207 */             if (!LwjglCanvas.this.running || Display.isCloseRequested()) {
/* 208 */               LwjglCanvas.this.running = false;
/* 209 */               LwjglCanvas.this.stopped();
/*     */               return;
/*     */             } 
/*     */             try {
/* 213 */               Display.processMessages();
/* 214 */               if (LwjglCanvas.this.cursor != null || !LwjglCanvas.isWindows) LwjglCanvas.this.canvas.setCursor(LwjglCanvas.this.cursor);
/*     */               
/* 216 */               boolean shouldRender = false;
/*     */               
/* 218 */               int width = Math.max(1, LwjglCanvas.this.graphics.getWidth());
/* 219 */               int height = Math.max(1, LwjglCanvas.this.graphics.getHeight());
/* 220 */               if (this.lastWidth != width || this.lastHeight != height) {
/* 221 */                 this.lastWidth = width;
/* 222 */                 this.lastHeight = height;
/* 223 */                 Gdx.gl.glViewport(0, 0, this.lastWidth, this.lastHeight);
/* 224 */                 LwjglCanvas.this.resize(width, height);
/* 225 */                 LwjglCanvas.this.listener.resize(width, height);
/* 226 */                 shouldRender = true;
/*     */               } 
/*     */               
/* 229 */               if (LwjglCanvas.this.executeRunnables()) shouldRender = true;
/*     */ 
/*     */               
/* 232 */               if (!LwjglCanvas.this.running)
/*     */                 return; 
/* 234 */               LwjglCanvas.this.input.update();
/* 235 */               shouldRender |= LwjglCanvas.this.graphics.shouldRender();
/* 236 */               LwjglCanvas.this.input.processEvents();
/* 237 */               if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.update();
/*     */               
/* 239 */               if (shouldRender) {
/* 240 */                 LwjglCanvas.this.graphics.updateTime();
/* 241 */                 LwjglCanvas.this.graphics.frameId++;
/* 242 */                 LwjglCanvas.this.listener.render();
/* 243 */                 Display.update(false);
/*     */               } 
/*     */               
/* 246 */               Display.sync(LwjglCanvas.this.getFrameRate());
/* 247 */             } catch (Throwable ex) {
/* 248 */               LwjglCanvas.this.exception(ex);
/*     */             } 
/* 250 */             EventQueue.invokeLater(this);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 256 */     synchronized (this.runnables) {
/* 257 */       for (int i = this.runnables.size - 1; i >= 0; i--) {
/* 258 */         this.executedRunnables.addAll((Object[])new Runnable[] { (Runnable)this.runnables.get(i) });
/* 259 */       }  this.runnables.clear();
/*     */     } 
/* 261 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 263 */       ((Runnable)this.executedRunnables.pop()).run();
/* 264 */       if (this.executedRunnables.size <= 0)
/* 265 */         return true; 
/*     */     } 
/*     */   }
/*     */   protected int getFrameRate() {
/* 269 */     int frameRate = Display.isActive() ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 270 */     if (frameRate == -1) frameRate = 10; 
/* 271 */     if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 272 */     if (frameRate == 0) frameRate = 30; 
/* 273 */     return frameRate;
/*     */   }
/*     */   
/*     */   protected void exception(Throwable ex) {
/* 277 */     ex.printStackTrace();
/* 278 */     stop();
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
/* 294 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 296 */             if (!LwjglCanvas.this.running)
/* 297 */               return;  LwjglCanvas.this.running = false;
/*     */             try {
/* 299 */               Display.destroy();
/* 300 */               if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.dispose(); 
/* 301 */             } catch (Throwable throwable) {}
/*     */             
/* 303 */             Array<LifecycleListener> listeners = LwjglCanvas.this.lifecycleListeners;
/* 304 */             synchronized (listeners) {
/* 305 */               for (LifecycleListener listener : listeners) {
/* 306 */                 listener.pause();
/* 307 */                 listener.dispose();
/*     */               } 
/*     */             } 
/* 310 */             LwjglCanvas.this.listener.pause();
/* 311 */             LwjglCanvas.this.listener.dispose();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 318 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 323 */     return getJavaHeap();
/*     */   }
/*     */   
/* 326 */   public LwjglCanvas(ApplicationListener listener) { this.preferences = new HashMap<String, Preferences>(); LwjglApplicationConfiguration config = new LwjglApplicationConfiguration(); initialize(listener, config); } public LwjglCanvas(ApplicationListener listener, LwjglApplicationConfiguration config) { this.preferences = new HashMap<String, Preferences>();
/*     */     initialize(listener, config); }
/*     */   
/*     */   public Preferences getPreferences(String name) {
/* 330 */     if (this.preferences.containsKey(name)) {
/* 331 */       return this.preferences.get(name);
/*     */     }
/* 333 */     Preferences prefs = new LwjglPreferences(name, ".prefs/");
/* 334 */     this.preferences.put(name, prefs);
/* 335 */     return prefs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 341 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 346 */     synchronized (this.runnables) {
/* 347 */       this.runnables.add(runnable);
/* 348 */       Gdx.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 354 */     if (this.logLevel >= 3) {
/* 355 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 361 */     if (this.logLevel >= 3) {
/* 362 */       System.out.println(tag + ": " + message);
/* 363 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void log(String tag, String message) {
/* 368 */     if (this.logLevel >= 2) {
/* 369 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 375 */     if (this.logLevel >= 2) {
/* 376 */       System.out.println(tag + ": " + message);
/* 377 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message) {
/* 383 */     if (this.logLevel >= 1) {
/* 384 */       System.err.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 390 */     if (this.logLevel >= 1) {
/* 391 */       System.err.println(tag + ": " + message);
/* 392 */       exception.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 398 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 403 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 408 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 411 */             LwjglCanvas.this.listener.pause();
/* 412 */             LwjglCanvas.this.listener.dispose();
/* 413 */             if (LwjglCanvas.this.audio != null) LwjglCanvas.this.audio.dispose(); 
/* 414 */             System.exit(-1);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCursor(Cursor cursor) {
/* 421 */     this.cursor = cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 426 */     synchronized (this.lifecycleListeners) {
/* 427 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 433 */     synchronized (this.lifecycleListeners) {
/* 434 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\LwjglCanvas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */