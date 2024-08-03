/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.InputAdapter;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.ui.Table;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import com.badlogic.gdx.utils.Scaling;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ import com.badlogic.gdx.utils.viewport.ScalingViewport;
/*     */ import com.badlogic.gdx.utils.viewport.Viewport;
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
/*     */ public class Stage
/*     */   extends InputAdapter
/*     */   implements Disposable
/*     */ {
/*     */   static boolean debug;
/*     */   private Viewport viewport;
/*     */   private final Batch batch;
/*     */   private boolean ownsBatch;
/*     */   private final Group root;
/*  72 */   private final Vector2 tempCoords = new Vector2();
/*  73 */   private final Actor[] pointerOverActors = new Actor[20];
/*  74 */   private final boolean[] pointerTouched = new boolean[20];
/*  75 */   private final int[] pointerScreenX = new int[20]; private int mouseScreenX; private int mouseScreenY; private Actor mouseOverActor;
/*  76 */   private final int[] pointerScreenY = new int[20];
/*     */   
/*     */   private Actor keyboardFocus;
/*     */   private Actor scrollFocus;
/*  80 */   private final SnapshotArray<TouchFocus> touchFocuses = new SnapshotArray(true, 4, TouchFocus.class); private boolean actionsRequestRendering = true; private ShapeRenderer debugShapes;
/*     */   private boolean debugInvisible;
/*     */   private boolean debugAll;
/*     */   private boolean debugUnderMouse;
/*     */   private boolean debugParentUnderMouse;
/*  85 */   private Table.Debug debugTableUnderMouse = Table.Debug.none;
/*  86 */   private final Color debugColor = new Color(0.0F, 1.0F, 0.0F, 0.85F);
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage() {
/*  91 */     this((Viewport)new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), (Camera)new OrthographicCamera()), (Batch)new SpriteBatch());
/*     */     
/*  93 */     this.ownsBatch = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage(Viewport viewport) {
/*  99 */     this(viewport, (Batch)new SpriteBatch());
/* 100 */     this.ownsBatch = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stage(Viewport viewport, Batch batch) {
/* 107 */     if (viewport == null) throw new IllegalArgumentException("viewport cannot be null."); 
/* 108 */     if (batch == null) throw new IllegalArgumentException("batch cannot be null."); 
/* 109 */     this.viewport = viewport;
/* 110 */     this.batch = batch;
/*     */     
/* 112 */     this.root = new Group();
/* 113 */     this.root.setStage(this);
/*     */     
/* 115 */     viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
/*     */   }
/*     */   
/*     */   public void draw() {
/* 119 */     Camera camera = this.viewport.getCamera();
/* 120 */     camera.update();
/*     */     
/* 122 */     if (!this.root.isVisible())
/*     */       return; 
/* 124 */     Batch batch = this.batch;
/* 125 */     if (batch != null) {
/* 126 */       batch.setProjectionMatrix(camera.combined);
/* 127 */       batch.begin();
/* 128 */       this.root.draw(batch, 1.0F);
/* 129 */       batch.end();
/*     */     } 
/*     */     
/* 132 */     if (debug) drawDebug(); 
/*     */   }
/*     */   
/*     */   private void drawDebug() {
/* 136 */     if (this.debugShapes == null) {
/* 137 */       this.debugShapes = new ShapeRenderer();
/* 138 */       this.debugShapes.setAutoShapeType(true);
/*     */     } 
/*     */     
/* 141 */     if (this.debugUnderMouse || this.debugParentUnderMouse || this.debugTableUnderMouse != Table.Debug.none)
/* 142 */     { screenToStageCoordinates(this.tempCoords.set(Gdx.input.getX(), Gdx.input.getY()));
/* 143 */       Actor actor = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 144 */       if (actor == null)
/*     */         return; 
/* 146 */       if (this.debugParentUnderMouse && actor.parent != null) actor = actor.parent;
/*     */       
/* 148 */       if (this.debugTableUnderMouse == Table.Debug.none) {
/* 149 */         actor.setDebug(true);
/*     */       } else {
/* 151 */         while (actor != null && 
/* 152 */           !(actor instanceof Table)) {
/* 153 */           actor = actor.parent;
/*     */         }
/* 155 */         if (actor == null)
/* 156 */           return;  ((Table)actor).debug(this.debugTableUnderMouse);
/*     */       } 
/*     */       
/* 159 */       if (this.debugAll && actor instanceof Group) ((Group)actor).debugAll();
/*     */       
/* 161 */       disableDebug(this.root, actor); }
/*     */     
/* 163 */     else if (this.debugAll) { this.root.debugAll(); }
/*     */ 
/*     */     
/* 166 */     Gdx.gl.glEnable(3042);
/* 167 */     this.debugShapes.setProjectionMatrix((this.viewport.getCamera()).combined);
/* 168 */     this.debugShapes.begin();
/* 169 */     this.root.drawDebug(this.debugShapes);
/* 170 */     this.debugShapes.end();
/*     */   }
/*     */ 
/*     */   
/*     */   private void disableDebug(Actor actor, Actor except) {
/* 175 */     if (actor == except)
/* 176 */       return;  actor.setDebug(false);
/* 177 */     if (actor instanceof Group) {
/* 178 */       SnapshotArray<Actor> children = ((Group)actor).children;
/* 179 */       for (int i = 0, n = children.size; i < n; i++) {
/* 180 */         disableDebug((Actor)children.get(i), except);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void act() {
/* 186 */     act(Math.min(Gdx.graphics.getDeltaTime(), 0.033333335F));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/* 194 */     for (int pointer = 0, n = this.pointerOverActors.length; pointer < n; pointer++) {
/* 195 */       Actor overLast = this.pointerOverActors[pointer];
/*     */       
/* 197 */       if (!this.pointerTouched[pointer]) {
/* 198 */         if (overLast != null) {
/* 199 */           this.pointerOverActors[pointer] = null;
/* 200 */           screenToStageCoordinates(this.tempCoords.set(this.pointerScreenX[pointer], this.pointerScreenY[pointer]));
/*     */           
/* 202 */           InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 203 */           event.setType(InputEvent.Type.exit);
/* 204 */           event.setStage(this);
/* 205 */           event.setStageX(this.tempCoords.x);
/* 206 */           event.setStageY(this.tempCoords.y);
/* 207 */           event.setRelatedActor(overLast);
/* 208 */           event.setPointer(pointer);
/* 209 */           overLast.fire(event);
/* 210 */           Pools.free(event);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 215 */         this.pointerOverActors[pointer] = fireEnterAndExit(overLast, this.pointerScreenX[pointer], this.pointerScreenY[pointer], pointer);
/*     */       } 
/*     */     } 
/* 218 */     Application.ApplicationType type = Gdx.app.getType();
/* 219 */     if (type == Application.ApplicationType.Desktop || type == Application.ApplicationType.Applet || type == Application.ApplicationType.WebGL) {
/* 220 */       this.mouseOverActor = fireEnterAndExit(this.mouseOverActor, this.mouseScreenX, this.mouseScreenY, -1);
/*     */     }
/* 222 */     this.root.act(delta);
/*     */   }
/*     */ 
/*     */   
/*     */   private Actor fireEnterAndExit(Actor overLast, int screenX, int screenY, int pointer) {
/* 227 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/* 228 */     Actor over = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 229 */     if (over == overLast) return overLast;
/*     */ 
/*     */     
/* 232 */     if (overLast != null) {
/* 233 */       InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 234 */       event.setStage(this);
/* 235 */       event.setStageX(this.tempCoords.x);
/* 236 */       event.setStageY(this.tempCoords.y);
/* 237 */       event.setPointer(pointer);
/* 238 */       event.setType(InputEvent.Type.exit);
/* 239 */       event.setRelatedActor(over);
/* 240 */       overLast.fire(event);
/* 241 */       Pools.free(event);
/*     */     } 
/*     */     
/* 244 */     if (over != null) {
/* 245 */       InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 246 */       event.setStage(this);
/* 247 */       event.setStageX(this.tempCoords.x);
/* 248 */       event.setStageY(this.tempCoords.y);
/* 249 */       event.setPointer(pointer);
/* 250 */       event.setType(InputEvent.Type.enter);
/* 251 */       event.setRelatedActor(overLast);
/* 252 */       over.fire(event);
/* 253 */       Pools.free(event);
/*     */     } 
/* 255 */     return over;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/* 260 */     if (screenX < this.viewport.getScreenX() || screenX >= this.viewport.getScreenX() + this.viewport.getScreenWidth()) return false; 
/* 261 */     if (Gdx.graphics.getHeight() - screenY < this.viewport.getScreenY() || Gdx.graphics
/* 262 */       .getHeight() - screenY >= this.viewport.getScreenY() + this.viewport.getScreenHeight()) return false;
/*     */     
/* 264 */     this.pointerTouched[pointer] = true;
/* 265 */     this.pointerScreenX[pointer] = screenX;
/* 266 */     this.pointerScreenY[pointer] = screenY;
/*     */     
/* 268 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 270 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 271 */     event.setType(InputEvent.Type.touchDown);
/* 272 */     event.setStage(this);
/* 273 */     event.setStageX(this.tempCoords.x);
/* 274 */     event.setStageY(this.tempCoords.y);
/* 275 */     event.setPointer(pointer);
/* 276 */     event.setButton(button);
/*     */     
/* 278 */     Actor target = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 279 */     if (target == null) {
/* 280 */       if (this.root.getTouchable() == Touchable.enabled) this.root.fire(event); 
/*     */     } else {
/* 282 */       target.fire(event);
/*     */     } 
/*     */     
/* 285 */     boolean handled = event.isHandled();
/* 286 */     Pools.free(event);
/* 287 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/* 293 */     this.pointerScreenX[pointer] = screenX;
/* 294 */     this.pointerScreenY[pointer] = screenY;
/* 295 */     this.mouseScreenX = screenX;
/* 296 */     this.mouseScreenY = screenY;
/*     */     
/* 298 */     if (this.touchFocuses.size == 0) return false;
/*     */     
/* 300 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 302 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 303 */     event.setType(InputEvent.Type.touchDragged);
/* 304 */     event.setStage(this);
/* 305 */     event.setStageX(this.tempCoords.x);
/* 306 */     event.setStageY(this.tempCoords.y);
/* 307 */     event.setPointer(pointer);
/*     */     
/* 309 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 310 */     TouchFocus[] focuses = (TouchFocus[])touchFocuses.begin();
/* 311 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 312 */       TouchFocus focus = focuses[i];
/* 313 */       if (focus.pointer == pointer && 
/* 314 */         touchFocuses.contains(focus, true)) {
/* 315 */         event.setTarget(focus.target);
/* 316 */         event.setListenerActor(focus.listenerActor);
/* 317 */         if (focus.listener.handle(event)) event.handle(); 
/*     */       } 
/* 319 */     }  touchFocuses.end();
/*     */     
/* 321 */     boolean handled = event.isHandled();
/* 322 */     Pools.free(event);
/* 323 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
/* 329 */     this.pointerTouched[pointer] = false;
/* 330 */     this.pointerScreenX[pointer] = screenX;
/* 331 */     this.pointerScreenY[pointer] = screenY;
/*     */     
/* 333 */     if (this.touchFocuses.size == 0) return false;
/*     */     
/* 335 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 337 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 338 */     event.setType(InputEvent.Type.touchUp);
/* 339 */     event.setStage(this);
/* 340 */     event.setStageX(this.tempCoords.x);
/* 341 */     event.setStageY(this.tempCoords.y);
/* 342 */     event.setPointer(pointer);
/* 343 */     event.setButton(button);
/*     */     
/* 345 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 346 */     TouchFocus[] focuses = (TouchFocus[])touchFocuses.begin();
/* 347 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 348 */       TouchFocus focus = focuses[i];
/* 349 */       if (focus.pointer == pointer && focus.button == button && 
/* 350 */         touchFocuses.removeValue(focus, true)) {
/* 351 */         event.setTarget(focus.target);
/* 352 */         event.setListenerActor(focus.listenerActor);
/* 353 */         if (focus.listener.handle(event)) event.handle(); 
/* 354 */         Pools.free(focus);
/*     */       } 
/* 356 */     }  touchFocuses.end();
/*     */     
/* 358 */     boolean handled = event.isHandled();
/* 359 */     Pools.free(event);
/* 360 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseMoved(int screenX, int screenY) {
/* 366 */     if (screenX < this.viewport.getScreenX() || screenX >= this.viewport.getScreenX() + this.viewport.getScreenWidth()) return false; 
/* 367 */     if (Gdx.graphics.getHeight() - screenY < this.viewport.getScreenY() || Gdx.graphics
/* 368 */       .getHeight() - screenY >= this.viewport.getScreenY() + this.viewport.getScreenHeight()) return false;
/*     */     
/* 370 */     this.mouseScreenX = screenX;
/* 371 */     this.mouseScreenY = screenY;
/*     */     
/* 373 */     screenToStageCoordinates(this.tempCoords.set(screenX, screenY));
/*     */     
/* 375 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 376 */     event.setStage(this);
/* 377 */     event.setType(InputEvent.Type.mouseMoved);
/* 378 */     event.setStageX(this.tempCoords.x);
/* 379 */     event.setStageY(this.tempCoords.y);
/*     */     
/* 381 */     Actor target = hit(this.tempCoords.x, this.tempCoords.y, true);
/* 382 */     if (target == null) target = this.root;
/*     */     
/* 384 */     target.fire(event);
/* 385 */     boolean handled = event.isHandled();
/* 386 */     Pools.free(event);
/* 387 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean scrolled(int amount) {
/* 393 */     Actor target = (this.scrollFocus == null) ? this.root : this.scrollFocus;
/*     */     
/* 395 */     screenToStageCoordinates(this.tempCoords.set(this.mouseScreenX, this.mouseScreenY));
/*     */     
/* 397 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 398 */     event.setStage(this);
/* 399 */     event.setType(InputEvent.Type.scrolled);
/* 400 */     event.setScrollAmount(amount);
/* 401 */     event.setStageX(this.tempCoords.x);
/* 402 */     event.setStageY(this.tempCoords.y);
/* 403 */     target.fire(event);
/* 404 */     boolean handled = event.isHandled();
/* 405 */     Pools.free(event);
/* 406 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyDown(int keyCode) {
/* 412 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 413 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 414 */     event.setStage(this);
/* 415 */     event.setType(InputEvent.Type.keyDown);
/* 416 */     event.setKeyCode(keyCode);
/* 417 */     target.fire(event);
/* 418 */     boolean handled = event.isHandled();
/* 419 */     Pools.free(event);
/* 420 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyUp(int keyCode) {
/* 426 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 427 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 428 */     event.setStage(this);
/* 429 */     event.setType(InputEvent.Type.keyUp);
/* 430 */     event.setKeyCode(keyCode);
/* 431 */     target.fire(event);
/* 432 */     boolean handled = event.isHandled();
/* 433 */     Pools.free(event);
/* 434 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keyTyped(char character) {
/* 440 */     Actor target = (this.keyboardFocus == null) ? this.root : this.keyboardFocus;
/* 441 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 442 */     event.setStage(this);
/* 443 */     event.setType(InputEvent.Type.keyTyped);
/* 444 */     event.setCharacter(character);
/* 445 */     target.fire(event);
/* 446 */     boolean handled = event.isHandled();
/* 447 */     Pools.free(event);
/* 448 */     return handled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
/* 454 */     TouchFocus focus = (TouchFocus)Pools.obtain(TouchFocus.class);
/* 455 */     focus.listenerActor = listenerActor;
/* 456 */     focus.target = target;
/* 457 */     focus.listener = listener;
/* 458 */     focus.pointer = pointer;
/* 459 */     focus.button = button;
/* 460 */     this.touchFocuses.add(focus);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTouchFocus(EventListener listener, Actor listenerActor, Actor target, int pointer, int button) {
/* 466 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 467 */     for (int i = touchFocuses.size - 1; i >= 0; i--) {
/* 468 */       TouchFocus focus = (TouchFocus)touchFocuses.get(i);
/* 469 */       if (focus.listener == listener && focus.listenerActor == listenerActor && focus.target == target && focus.pointer == pointer && focus.button == button) {
/*     */         
/* 471 */         touchFocuses.removeIndex(i);
/* 472 */         Pools.free(focus);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocus(Actor actor) {
/* 480 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 481 */     event.setStage(this);
/* 482 */     event.setType(InputEvent.Type.touchUp);
/* 483 */     event.setStageX(-2.14748365E9F);
/* 484 */     event.setStageY(-2.14748365E9F);
/*     */ 
/*     */ 
/*     */     
/* 488 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 489 */     TouchFocus[] items = (TouchFocus[])touchFocuses.begin();
/* 490 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 491 */       TouchFocus focus = items[i];
/* 492 */       if (focus.listenerActor == actor && 
/* 493 */         touchFocuses.removeValue(focus, true)) {
/* 494 */         event.setTarget(focus.target);
/* 495 */         event.setListenerActor(focus.listenerActor);
/* 496 */         event.setPointer(focus.pointer);
/* 497 */         event.setButton(focus.button);
/* 498 */         focus.listener.handle(event);
/*     */       } 
/*     */     } 
/* 501 */     touchFocuses.end();
/*     */     
/* 503 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocus() {
/* 511 */     cancelTouchFocusExcept(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelTouchFocusExcept(EventListener exceptListener, Actor exceptActor) {
/* 517 */     InputEvent event = (InputEvent)Pools.obtain(InputEvent.class);
/* 518 */     event.setStage(this);
/* 519 */     event.setType(InputEvent.Type.touchUp);
/* 520 */     event.setStageX(-2.14748365E9F);
/* 521 */     event.setStageY(-2.14748365E9F);
/*     */ 
/*     */ 
/*     */     
/* 525 */     SnapshotArray<TouchFocus> touchFocuses = this.touchFocuses;
/* 526 */     TouchFocus[] items = (TouchFocus[])touchFocuses.begin();
/* 527 */     for (int i = 0, n = touchFocuses.size; i < n; i++) {
/* 528 */       TouchFocus focus = items[i];
/* 529 */       if ((focus.listener != exceptListener || focus.listenerActor != exceptActor) && 
/* 530 */         touchFocuses.removeValue(focus, true)) {
/* 531 */         event.setTarget(focus.target);
/* 532 */         event.setListenerActor(focus.listenerActor);
/* 533 */         event.setPointer(focus.pointer);
/* 534 */         event.setButton(focus.button);
/* 535 */         focus.listener.handle(event);
/*     */       } 
/*     */     } 
/* 538 */     touchFocuses.end();
/*     */     
/* 540 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActor(Actor actor) {
/* 546 */     this.root.addActor(actor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAction(Action action) {
/* 552 */     this.root.addAction(action);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<Actor> getActors() {
/* 558 */     return (Array<Actor>)this.root.children;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addListener(EventListener listener) {
/* 564 */     return this.root.addListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeListener(EventListener listener) {
/* 570 */     return this.root.removeListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addCaptureListener(EventListener listener) {
/* 576 */     return this.root.addCaptureListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeCaptureListener(EventListener listener) {
/* 582 */     return this.root.removeCaptureListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 587 */     unfocusAll();
/* 588 */     this.root.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unfocusAll() {
/* 593 */     this.scrollFocus = null;
/* 594 */     this.keyboardFocus = null;
/* 595 */     cancelTouchFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unfocus(Actor actor) {
/* 600 */     cancelTouchFocus(actor);
/* 601 */     if (this.scrollFocus != null && this.scrollFocus.isDescendantOf(actor)) this.scrollFocus = null; 
/* 602 */     if (this.keyboardFocus != null && this.keyboardFocus.isDescendantOf(actor)) this.keyboardFocus = null;
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setKeyboardFocus(Actor actor) {
/* 608 */     if (this.keyboardFocus == actor)
/* 609 */       return;  FocusListener.FocusEvent event = (FocusListener.FocusEvent)Pools.obtain(FocusListener.FocusEvent.class);
/* 610 */     event.setStage(this);
/* 611 */     event.setType(FocusListener.FocusEvent.Type.keyboard);
/* 612 */     Actor oldKeyboardFocus = this.keyboardFocus;
/* 613 */     if (oldKeyboardFocus != null) {
/* 614 */       event.setFocused(false);
/* 615 */       event.setRelatedActor(actor);
/* 616 */       oldKeyboardFocus.fire((Event)event);
/*     */     } 
/* 618 */     if (!event.isCancelled()) {
/* 619 */       this.keyboardFocus = actor;
/* 620 */       if (actor != null) {
/* 621 */         event.setFocused(true);
/* 622 */         event.setRelatedActor(oldKeyboardFocus);
/* 623 */         actor.fire((Event)event);
/* 624 */         if (event.isCancelled()) setKeyboardFocus(oldKeyboardFocus); 
/*     */       } 
/*     */     } 
/* 627 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getKeyboardFocus() {
/* 633 */     return this.keyboardFocus;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScrollFocus(Actor actor) {
/* 639 */     if (this.scrollFocus == actor)
/* 640 */       return;  FocusListener.FocusEvent event = (FocusListener.FocusEvent)Pools.obtain(FocusListener.FocusEvent.class);
/* 641 */     event.setStage(this);
/* 642 */     event.setType(FocusListener.FocusEvent.Type.scroll);
/* 643 */     Actor oldScrollFocus = this.scrollFocus;
/* 644 */     if (oldScrollFocus != null) {
/* 645 */       event.setFocused(false);
/* 646 */       event.setRelatedActor(actor);
/* 647 */       oldScrollFocus.fire((Event)event);
/*     */     } 
/* 649 */     if (!event.isCancelled()) {
/* 650 */       this.scrollFocus = actor;
/* 651 */       if (actor != null) {
/* 652 */         event.setFocused(true);
/* 653 */         event.setRelatedActor(oldScrollFocus);
/* 654 */         actor.fire((Event)event);
/* 655 */         if (event.isCancelled()) setScrollFocus(oldScrollFocus); 
/*     */       } 
/*     */     } 
/* 658 */     Pools.free(event);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getScrollFocus() {
/* 664 */     return this.scrollFocus;
/*     */   }
/*     */   
/*     */   public Batch getBatch() {
/* 668 */     return this.batch;
/*     */   }
/*     */   
/*     */   public Viewport getViewport() {
/* 672 */     return this.viewport;
/*     */   }
/*     */   
/*     */   public void setViewport(Viewport viewport) {
/* 676 */     this.viewport = viewport;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 681 */     return this.viewport.getWorldWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 686 */     return this.viewport.getWorldHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public Camera getCamera() {
/* 691 */     return this.viewport.getCamera();
/*     */   }
/*     */ 
/*     */   
/*     */   public Group getRoot() {
/* 696 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor hit(float stageX, float stageY, boolean touchable) {
/* 705 */     this.root.parentToLocalCoordinates(this.tempCoords.set(stageX, stageY));
/* 706 */     return this.root.hit(this.tempCoords.x, this.tempCoords.y, touchable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 screenToStageCoordinates(Vector2 screenCoords) {
/* 712 */     this.viewport.unproject(screenCoords);
/* 713 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 stageToScreenCoordinates(Vector2 stageCoords) {
/* 719 */     this.viewport.project(stageCoords);
/* 720 */     stageCoords.y = this.viewport.getScreenHeight() - stageCoords.y;
/* 721 */     return stageCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 toScreenCoordinates(Vector2 coords, Matrix4 transformMatrix) {
/* 729 */     return this.viewport.toScreenCoordinates(coords, transformMatrix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateScissors(Rectangle localRect, Rectangle scissorRect) {
/*     */     Matrix4 transformMatrix;
/* 735 */     this.viewport.calculateScissors(this.batch.getTransformMatrix(), localRect, scissorRect);
/*     */     
/* 737 */     if (this.debugShapes != null && this.debugShapes.isDrawing()) {
/* 738 */       transformMatrix = this.debugShapes.getTransformMatrix();
/*     */     } else {
/* 740 */       transformMatrix = this.batch.getTransformMatrix();
/* 741 */     }  this.viewport.calculateScissors(transformMatrix, localRect, scissorRect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setActionsRequestRendering(boolean actionsRequestRendering) {
/* 748 */     this.actionsRequestRendering = actionsRequestRendering;
/*     */   }
/*     */   
/*     */   public boolean getActionsRequestRendering() {
/* 752 */     return this.actionsRequestRendering;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getDebugColor() {
/* 757 */     return this.debugColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugInvisible(boolean debugInvisible) {
/* 762 */     this.debugInvisible = debugInvisible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugAll(boolean debugAll) {
/* 767 */     if (this.debugAll == debugAll)
/* 768 */       return;  this.debugAll = debugAll;
/* 769 */     if (debugAll) {
/* 770 */       debug = true;
/*     */     } else {
/* 772 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDebugUnderMouse(boolean debugUnderMouse) {
/* 777 */     if (this.debugUnderMouse == debugUnderMouse)
/* 778 */       return;  this.debugUnderMouse = debugUnderMouse;
/* 779 */     if (debugUnderMouse) {
/* 780 */       debug = true;
/*     */     } else {
/* 782 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugParentUnderMouse(boolean debugParentUnderMouse) {
/* 788 */     if (this.debugParentUnderMouse == debugParentUnderMouse)
/* 789 */       return;  this.debugParentUnderMouse = debugParentUnderMouse;
/* 790 */     if (debugParentUnderMouse) {
/* 791 */       debug = true;
/*     */     } else {
/* 793 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebugTableUnderMouse(Table.Debug debugTableUnderMouse) {
/* 800 */     if (debugTableUnderMouse == null) debugTableUnderMouse = Table.Debug.none; 
/* 801 */     if (this.debugTableUnderMouse == debugTableUnderMouse)
/* 802 */       return;  this.debugTableUnderMouse = debugTableUnderMouse;
/* 803 */     if (debugTableUnderMouse != Table.Debug.none) {
/* 804 */       debug = true;
/*     */     } else {
/* 806 */       this.root.setDebug(false, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugTableUnderMouse(boolean debugTableUnderMouse) {
/* 812 */     setDebugTableUnderMouse(debugTableUnderMouse ? Table.Debug.all : Table.Debug.none);
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 816 */     clear();
/* 817 */     if (this.ownsBatch) this.batch.dispose(); 
/*     */   }
/*     */   
/*     */   public static final class TouchFocus implements Pool.Poolable {
/*     */     EventListener listener;
/*     */     Actor listenerActor;
/*     */     Actor target;
/*     */     int pointer;
/*     */     int button;
/*     */     
/*     */     public void reset() {
/* 828 */       this.listenerActor = null;
/* 829 */       this.listener = null;
/* 830 */       this.target = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2d\Stage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */