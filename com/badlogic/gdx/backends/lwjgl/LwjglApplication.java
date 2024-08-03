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
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ import java.awt.Canvas;
/*     */ import java.io.File;
/*     */ import org.lwjgl.LWJGLException;
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
/*     */ public class LwjglApplication
/*     */   implements Application
/*     */ {
/*     */   protected final LwjglGraphics graphics;
/*     */   protected OpenALAudio audio;
/*     */   protected final LwjglFiles files;
/*     */   protected final LwjglInput input;
/*     */   protected final LwjglNet net;
/*     */   protected final ApplicationListener listener;
/*     */   protected Thread mainLoopThread;
/*     */   protected boolean running = true;
/*  51 */   protected final Array<Runnable> runnables = new Array();
/*  52 */   protected final Array<Runnable> executedRunnables = new Array();
/*  53 */   protected final SnapshotArray<LifecycleListener> lifecycleListeners = new SnapshotArray(LifecycleListener.class);
/*  54 */   protected int logLevel = 2; protected String preferencesdir;
/*     */   protected Files.FileType preferencesFileType;
/*     */   ObjectMap<String, Preferences> preferences;
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, String title, int width, int height) {
/*  59 */     this(listener, createConfig(title, width, height));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener) {
/*  63 */     this(listener, null, 640, 480);
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  67 */     this(listener, config, new LwjglGraphics(config));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, Canvas canvas) {
/*  71 */     this(listener, new LwjglApplicationConfiguration(), new LwjglGraphics(canvas));
/*     */   }
/*     */   
/*     */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config, Canvas canvas) {
/*  75 */     this(listener, config, new LwjglGraphics(canvas, config));
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static LwjglApplicationConfiguration createConfig(String title, int width, int height) {
/* 110 */     LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
/* 111 */     config.title = title;
/* 112 */     config.width = width;
/* 113 */     config.height = height;
/* 114 */     config.vSyncEnabled = true;
/* 115 */     return config;
/*     */   }
/*     */   
/*     */   private void initialize() {
/* 119 */     this.mainLoopThread = new Thread("LWJGL Application")
/*     */       {
/*     */         public void run() {
/* 122 */           LwjglApplication.this.graphics.setVSync(LwjglApplication.this.graphics.config.vSyncEnabled);
/*     */           try {
/* 124 */             LwjglApplication.this.mainLoop();
/* 125 */           } catch (Throwable t) {
/* 126 */             if (LwjglApplication.this.audio != null) LwjglApplication.this.audio.dispose(); 
/* 127 */             Gdx.input.setCursorCatched(false);
/* 128 */             if (t instanceof RuntimeException) {
/* 129 */               throw (RuntimeException)t;
/*     */             }
/* 131 */             throw new GdxRuntimeException(t);
/*     */           } 
/*     */         }
/*     */       };
/* 135 */     this.mainLoopThread.start();
/*     */   }
/*     */   
/*     */   void mainLoop() {
/* 139 */     SnapshotArray<LifecycleListener> lifecycleListeners = this.lifecycleListeners;
/*     */     
/*     */     try {
/* 142 */       this.graphics.setupDisplay();
/* 143 */     } catch (LWJGLException e) {
/* 144 */       throw new GdxRuntimeException(e);
/*     */     } 
/*     */     
/* 147 */     this.listener.create();
/* 148 */     this.graphics.resize = true;
/*     */     
/* 150 */     int lastWidth = this.graphics.getWidth();
/* 151 */     int lastHeight = this.graphics.getHeight();
/*     */     
/* 153 */     this.graphics.lastTime = System.nanoTime();
/* 154 */     boolean wasActive = true;
/* 155 */     while (this.running) {
/* 156 */       Display.processMessages();
/* 157 */       if (Display.isCloseRequested()) exit();
/*     */       
/* 159 */       boolean isActive = Display.isActive();
/* 160 */       if (wasActive && !isActive) {
/* 161 */         wasActive = false;
/* 162 */         synchronized (lifecycleListeners) {
/* 163 */           LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 164 */           for (int i = 0, n = lifecycleListeners.size; i < n; i++)
/* 165 */             listeners[i].pause(); 
/* 166 */           lifecycleListeners.end();
/*     */         } 
/* 168 */         this.listener.pause();
/*     */       } 
/* 170 */       if (!wasActive && isActive) {
/* 171 */         wasActive = true;
/* 172 */         synchronized (lifecycleListeners) {
/* 173 */           LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 174 */           for (int i = 0, n = lifecycleListeners.size; i < n; i++)
/* 175 */             listeners[i].resume(); 
/* 176 */           lifecycleListeners.end();
/*     */         } 
/* 178 */         this.listener.resume();
/*     */       } 
/*     */       
/* 181 */       boolean shouldRender = false;
/*     */       
/* 183 */       if (this.graphics.canvas != null) {
/* 184 */         int width = this.graphics.canvas.getWidth();
/* 185 */         int height = this.graphics.canvas.getHeight();
/* 186 */         if (lastWidth != width || lastHeight != height) {
/* 187 */           lastWidth = width;
/* 188 */           lastHeight = height;
/* 189 */           Gdx.gl.glViewport(0, 0, lastWidth, lastHeight);
/* 190 */           this.listener.resize(lastWidth, lastHeight);
/* 191 */           shouldRender = true;
/*     */         } 
/*     */       } else {
/* 194 */         this.graphics.config.x = Display.getX();
/* 195 */         this.graphics.config.y = Display.getY();
/* 196 */         if (this.graphics.resize || Display.wasResized() || 
/* 197 */           (int)(Display.getWidth() * Display.getPixelScaleFactor()) != this.graphics.config.width || 
/* 198 */           (int)(Display.getHeight() * Display.getPixelScaleFactor()) != this.graphics.config.height) {
/* 199 */           this.graphics.resize = false;
/* 200 */           this.graphics.config.width = (int)(Display.getWidth() * Display.getPixelScaleFactor());
/* 201 */           this.graphics.config.height = (int)(Display.getHeight() * Display.getPixelScaleFactor());
/* 202 */           Gdx.gl.glViewport(0, 0, this.graphics.config.width, this.graphics.config.height);
/* 203 */           if (this.listener != null) this.listener.resize(this.graphics.config.width, this.graphics.config.height); 
/* 204 */           this.graphics.requestRendering();
/*     */         } 
/*     */       } 
/*     */       
/* 208 */       if (executeRunnables()) shouldRender = true;
/*     */ 
/*     */       
/* 211 */       if (!this.running)
/*     */         break; 
/* 213 */       this.input.update();
/* 214 */       shouldRender |= this.graphics.shouldRender();
/* 215 */       this.input.processEvents();
/* 216 */       if (this.audio != null) this.audio.update();
/*     */       
/* 218 */       if (!isActive && this.graphics.config.backgroundFPS == -1) shouldRender = false; 
/* 219 */       int frameRate = isActive ? this.graphics.config.foregroundFPS : this.graphics.config.backgroundFPS;
/* 220 */       if (shouldRender) {
/* 221 */         this.graphics.updateTime();
/* 222 */         this.graphics.frameId++;
/* 223 */         this.listener.render();
/* 224 */         Display.update(false);
/*     */       } else {
/*     */         
/* 227 */         if (frameRate == -1) frameRate = 10; 
/* 228 */         if (frameRate == 0) frameRate = this.graphics.config.backgroundFPS; 
/* 229 */         if (frameRate == 0) frameRate = 30; 
/*     */       } 
/* 231 */       if (frameRate > 0) Display.sync(frameRate);
/*     */     
/*     */     } 
/* 234 */     synchronized (lifecycleListeners) {
/* 235 */       LifecycleListener[] listeners = (LifecycleListener[])lifecycleListeners.begin();
/* 236 */       for (int i = 0, n = lifecycleListeners.size; i < n; i++) {
/* 237 */         listeners[i].pause();
/* 238 */         listeners[i].dispose();
/*     */       } 
/* 240 */       lifecycleListeners.end();
/*     */     } 
/* 242 */     this.listener.pause();
/* 243 */     this.listener.dispose();
/* 244 */     Display.destroy();
/* 245 */     if (this.audio != null) this.audio.dispose(); 
/* 246 */     if (this.graphics.config.forceExit) System.exit(-1); 
/*     */   }
/*     */   
/*     */   public boolean executeRunnables() {
/* 250 */     synchronized (this.runnables) {
/* 251 */       for (int i = this.runnables.size - 1; i >= 0; i--)
/* 252 */         this.executedRunnables.add(this.runnables.get(i)); 
/* 253 */       this.runnables.clear();
/*     */     } 
/* 255 */     if (this.executedRunnables.size == 0) return false; 
/*     */     while (true) {
/* 257 */       ((Runnable)this.executedRunnables.pop()).run();
/* 258 */       if (this.executedRunnables.size <= 0)
/* 259 */         return true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ApplicationListener getApplicationListener() {
/* 264 */     return this.listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public Audio getAudio() {
/* 269 */     return (Audio)this.audio;
/*     */   }
/*     */ 
/*     */   
/*     */   public Files getFiles() {
/* 274 */     return this.files;
/*     */   }
/*     */ 
/*     */   
/*     */   public LwjglGraphics getGraphics() {
/* 279 */     return this.graphics;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input getInput() {
/* 284 */     return this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   public Net getNet() {
/* 289 */     return this.net;
/*     */   }
/*     */ 
/*     */   
/*     */   public Application.ApplicationType getType() {
/* 294 */     return Application.ApplicationType.Desktop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 299 */     return 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 303 */     this.running = false;
/*     */     try {
/* 305 */       this.mainLoopThread.join();
/* 306 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getJavaHeap() {
/* 312 */     return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getNativeHeap() {
/* 317 */     return getJavaHeap();
/*     */   }
/*     */   
/* 320 */   public LwjglApplication(ApplicationListener listener, LwjglApplicationConfiguration config, LwjglGraphics graphics) { this.preferences = new ObjectMap(); LwjglNativesLoader.load(); if (config.title == null)
/*     */       config.title = listener.getClass().getSimpleName();  this.graphics = graphics; if (!LwjglApplicationConfiguration.disableAudio)
/*     */       try { this.audio = new OpenALAudio(config.audioDeviceSimultaneousSources, config.audioDeviceBufferCount, config.audioDeviceBufferSize); }
/*     */       catch (Throwable t) { log("LwjglApplication", "Couldn't initialize audio, disabling audio", t); LwjglApplicationConfiguration.disableAudio = true; }
/* 324 */         this.files = new LwjglFiles(); this.input = new LwjglInput(); this.net = new LwjglNet(); this.listener = listener; this.preferencesdir = config.preferencesDirectory; this.preferencesFileType = config.preferencesFileType; Gdx.app = this; Gdx.graphics = graphics; Gdx.audio = (Audio)this.audio; Gdx.files = this.files; Gdx.input = this.input; Gdx.net = this.net; initialize(); } public Preferences getPreferences(String name) { if (this.preferences.containsKey(name)) {
/* 325 */       return (Preferences)this.preferences.get(name);
/*     */     }
/* 327 */     Preferences prefs = new LwjglPreferences(new LwjglFileHandle(new File(this.preferencesdir, name), this.preferencesFileType));
/* 328 */     this.preferences.put(name, prefs);
/* 329 */     return prefs; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Clipboard getClipboard() {
/* 335 */     return new LwjglClipboard();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRunnable(Runnable runnable) {
/* 340 */     synchronized (this.runnables) {
/* 341 */       this.runnables.add(runnable);
/* 342 */       Gdx.graphics.requestRendering();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message) {
/* 348 */     if (this.logLevel >= 3) {
/* 349 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String tag, String message, Throwable exception) {
/* 355 */     if (this.logLevel >= 3) {
/* 356 */       System.out.println(tag + ": " + message);
/* 357 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String tag, String message) {
/* 363 */     if (this.logLevel >= 2) {
/* 364 */       System.out.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(String tag, String message, Throwable exception) {
/* 370 */     if (this.logLevel >= 2) {
/* 371 */       System.out.println(tag + ": " + message);
/* 372 */       exception.printStackTrace(System.out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message) {
/* 378 */     if (this.logLevel >= 1) {
/* 379 */       System.err.println(tag + ": " + message);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String tag, String message, Throwable exception) {
/* 385 */     if (this.logLevel >= 1) {
/* 386 */       System.err.println(tag + ": " + message);
/* 387 */       exception.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogLevel(int logLevel) {
/* 393 */     this.logLevel = logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLogLevel() {
/* 398 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void exit() {
/* 403 */     postRunnable(new Runnable()
/*     */         {
/*     */           public void run() {
/* 406 */             LwjglApplication.this.running = false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLifecycleListener(LifecycleListener listener) {
/* 413 */     synchronized (this.lifecycleListeners) {
/* 414 */       this.lifecycleListeners.add(listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeLifecycleListener(LifecycleListener listener) {
/* 420 */     synchronized (this.lifecycleListeners) {
/* 421 */       this.lifecycleListeners.removeValue(listener, true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\LwjglApplication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */