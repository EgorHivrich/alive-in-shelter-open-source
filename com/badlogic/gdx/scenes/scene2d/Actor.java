/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.DelayedRemovalArray;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import com.badlogic.gdx.utils.SnapshotArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Actor
/*     */ {
/*     */   private Stage stage;
/*     */   Group parent;
/*  59 */   private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray(0);
/*  60 */   private final DelayedRemovalArray<EventListener> captureListeners = new DelayedRemovalArray(0);
/*  61 */   private final Array<Action> actions = new Array(0);
/*     */   private String name;
/*     */   private boolean visible = true;
/*  64 */   private Touchable touchable = Touchable.enabled; private boolean debug; float x; float y;
/*     */   float width;
/*     */   float height;
/*     */   float originX;
/*     */   float originY;
/*  69 */   float scaleX = 1.0F; float scaleY = 1.0F;
/*     */   float rotation;
/*  71 */   final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object userObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/*  90 */     Array<Action> actions = this.actions;
/*  91 */     if (actions.size > 0) {
/*  92 */       if (this.stage != null && this.stage.getActionsRequestRendering()) Gdx.graphics.requestRendering(); 
/*  93 */       for (int i = 0; i < actions.size; i++) {
/*  94 */         Action action = (Action)actions.get(i);
/*  95 */         if (action.act(delta) && i < actions.size) {
/*  96 */           Action current = (Action)actions.get(i);
/*  97 */           int actionIndex = (current == action) ? i : actions.indexOf(action, true);
/*  98 */           if (actionIndex != -1) {
/*  99 */             actions.removeIndex(actionIndex);
/* 100 */             action.setActor(null);
/* 101 */             i--;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */   public boolean fire(Event event) {
/* 121 */     if (event.getStage() == null) event.setStage(getStage()); 
/* 122 */     event.setTarget(this);
/*     */ 
/*     */     
/* 125 */     Array<Group> ancestors = (Array<Group>)Pools.obtain(Array.class);
/* 126 */     Group parent = this.parent;
/* 127 */     while (parent != null) {
/* 128 */       ancestors.add(parent);
/* 129 */       parent = parent.parent;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 134 */       Object[] ancestorsArray = ancestors.items; int i;
/* 135 */       for (i = ancestors.size - 1; i >= 0; i--) {
/* 136 */         Group currentTarget = (Group)ancestorsArray[i];
/* 137 */         currentTarget.notify(event, true);
/* 138 */         if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       } 
/*     */       
/* 142 */       notify(event, true);
/* 143 */       if (event.isStopped()) return event.isCancelled();
/*     */ 
/*     */       
/* 146 */       notify(event, false);
/* 147 */       if (!event.getBubbles()) return event.isCancelled(); 
/* 148 */       if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       int n;
/* 151 */       for (i = 0, n = ancestors.size; i < n; i++) {
/* 152 */         ((Group)ancestorsArray[i]).notify(event, false);
/* 153 */         if (event.isStopped()) return event.isCancelled();
/*     */       
/*     */       } 
/* 156 */       return event.isCancelled();
/*     */     } finally {
/* 158 */       ancestors.clear();
/* 159 */       Pools.free(ancestors);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean notify(Event event, boolean capture) {
/* 169 */     if (event.getTarget() == null) throw new IllegalArgumentException("The event target cannot be null.");
/*     */     
/* 171 */     DelayedRemovalArray<EventListener> listeners = capture ? this.captureListeners : this.listeners;
/* 172 */     if (listeners.size == 0) return event.isCancelled();
/*     */     
/* 174 */     event.setListenerActor(this);
/* 175 */     event.setCapture(capture);
/* 176 */     if (event.getStage() == null) event.setStage(this.stage);
/*     */     
/* 178 */     listeners.begin();
/* 179 */     for (int i = 0, n = listeners.size; i < n; i++) {
/* 180 */       EventListener listener = (EventListener)listeners.get(i);
/* 181 */       if (listener.handle(event)) {
/* 182 */         event.handle();
/* 183 */         if (event instanceof InputEvent) {
/* 184 */           InputEvent inputEvent = (InputEvent)event;
/* 185 */           if (inputEvent.getType() == InputEvent.Type.touchDown) {
/* 186 */             event.getStage().addTouchFocus(listener, this, inputEvent.getTarget(), inputEvent.getPointer(), inputEvent
/* 187 */                 .getButton());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 192 */     listeners.end();
/*     */     
/* 194 */     return event.isCancelled();
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
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 208 */     if (touchable && this.touchable != Touchable.enabled) return null; 
/* 209 */     return (x >= 0.0F && x < this.width && y >= 0.0F && y < this.height) ? this : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove() {
/* 215 */     if (this.parent != null) return this.parent.removeActor(this, true); 
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addListener(EventListener listener) {
/* 223 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 224 */     if (!this.listeners.contains(listener, true)) {
/* 225 */       this.listeners.add(listener);
/* 226 */       return true;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public boolean removeListener(EventListener listener) {
/* 232 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 233 */     return this.listeners.removeValue(listener, true);
/*     */   }
/*     */   
/*     */   public Array<EventListener> getListeners() {
/* 237 */     return (Array<EventListener>)this.listeners;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addCaptureListener(EventListener listener) {
/* 243 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 244 */     if (!this.captureListeners.contains(listener, true)) this.captureListeners.add(listener); 
/* 245 */     return true;
/*     */   }
/*     */   
/*     */   public boolean removeCaptureListener(EventListener listener) {
/* 249 */     if (listener == null) throw new IllegalArgumentException("listener cannot be null."); 
/* 250 */     return this.captureListeners.removeValue(listener, true);
/*     */   }
/*     */   
/*     */   public Array<EventListener> getCaptureListeners() {
/* 254 */     return (Array<EventListener>)this.captureListeners;
/*     */   }
/*     */   
/*     */   public void addAction(Action action) {
/* 258 */     action.setActor(this);
/* 259 */     this.actions.add(action);
/*     */     
/* 261 */     if (this.stage != null && this.stage.getActionsRequestRendering()) Gdx.graphics.requestRendering(); 
/*     */   }
/*     */   
/*     */   public void removeAction(Action action) {
/* 265 */     if (this.actions.removeValue(action, true)) action.setActor(null); 
/*     */   }
/*     */   
/*     */   public Array<Action> getActions() {
/* 269 */     return this.actions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasActions() {
/* 274 */     return (this.actions.size > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearActions() {
/* 279 */     for (int i = this.actions.size - 1; i >= 0; i--)
/* 280 */       ((Action)this.actions.get(i)).setActor(null); 
/* 281 */     this.actions.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearListeners() {
/* 286 */     this.listeners.clear();
/* 287 */     this.captureListeners.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 292 */     clearActions();
/* 293 */     clearListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stage getStage() {
/* 298 */     return this.stage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 304 */     this.stage = stage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDescendantOf(Actor actor) {
/* 309 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 310 */     Actor parent = this;
/*     */     while (true) {
/* 312 */       if (parent == null) return false; 
/* 313 */       if (parent == actor) return true; 
/* 314 */       parent = parent.parent;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAscendantOf(Actor actor) {
/* 320 */     if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/*     */     while (true) {
/* 322 */       if (actor == null) return false; 
/* 323 */       if (actor == this) return true; 
/* 324 */       actor = actor.parent;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 330 */     return (this.parent != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Group getParent() {
/* 335 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setParent(Group parent) {
/* 341 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouchable() {
/* 346 */     return (this.touchable == Touchable.enabled);
/*     */   }
/*     */   
/*     */   public Touchable getTouchable() {
/* 350 */     return this.touchable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTouchable(Touchable touchable) {
/* 355 */     this.touchable = touchable;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 359 */     return this.visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 364 */     this.visible = visible;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getUserObject() {
/* 369 */     return this.userObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUserObject(Object userObject) {
/* 374 */     this.userObject = userObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 379 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX(int alignment) {
/* 384 */     float x = this.x;
/* 385 */     if ((alignment & 0x10) != 0) {
/* 386 */       x += this.width;
/* 387 */     } else if ((alignment & 0x8) == 0) {
/* 388 */       x += this.width / 2.0F;
/* 389 */     }  return x;
/*     */   }
/*     */   
/*     */   public void setX(float x) {
/* 393 */     if (this.x != x) {
/* 394 */       this.x = x;
/* 395 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 401 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(float y) {
/* 405 */     if (this.y != y) {
/* 406 */       this.y = y;
/* 407 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY(int alignment) {
/* 413 */     float y = this.y;
/* 414 */     if ((alignment & 0x2) != 0) {
/* 415 */       y += this.height;
/* 416 */     } else if ((alignment & 0x4) == 0) {
/* 417 */       y += this.height / 2.0F;
/* 418 */     }  return y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 423 */     if (this.x != x || this.y != y) {
/* 424 */       this.x = x;
/* 425 */       this.y = y;
/* 426 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y, int alignment) {
/* 433 */     if ((alignment & 0x10) != 0) {
/* 434 */       x -= this.width;
/* 435 */     } else if ((alignment & 0x8) == 0) {
/* 436 */       x -= this.width / 2.0F;
/*     */     } 
/* 438 */     if ((alignment & 0x2) != 0) {
/* 439 */       y -= this.height;
/* 440 */     } else if ((alignment & 0x4) == 0) {
/* 441 */       y -= this.height / 2.0F;
/*     */     } 
/* 443 */     if (this.x != x || this.y != y) {
/* 444 */       this.x = x;
/* 445 */       this.y = y;
/* 446 */       positionChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveBy(float x, float y) {
/* 452 */     if (x != 0.0F || y != 0.0F) {
/* 453 */       this.x += x;
/* 454 */       this.y += y;
/* 455 */       positionChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getWidth() {
/* 460 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(float width) {
/* 464 */     if (this.width != width) {
/* 465 */       this.width = width;
/* 466 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 471 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(float height) {
/* 475 */     if (this.height != height) {
/* 476 */       this.height = height;
/* 477 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTop() {
/* 483 */     return this.y + this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRight() {
/* 488 */     return this.x + this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void positionChanged() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sizeChanged() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rotationChanged() {}
/*     */ 
/*     */   
/*     */   public void setSize(float width, float height) {
/* 505 */     if (this.width != width || this.height != height) {
/* 506 */       this.width = width;
/* 507 */       this.height = height;
/* 508 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sizeBy(float size) {
/* 514 */     if (size != 0.0F) {
/* 515 */       this.width += size;
/* 516 */       this.height += size;
/* 517 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sizeBy(float width, float height) {
/* 523 */     if (width != 0.0F || height != 0.0F) {
/* 524 */       this.width += width;
/* 525 */       this.height += height;
/* 526 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBounds(float x, float y, float width, float height) {
/* 532 */     if (this.x != x || this.y != y) {
/* 533 */       this.x = x;
/* 534 */       this.y = y;
/* 535 */       positionChanged();
/*     */     } 
/* 537 */     if (this.width != width || this.height != height) {
/* 538 */       this.width = width;
/* 539 */       this.height = height;
/* 540 */       sizeChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getOriginX() {
/* 545 */     return this.originX;
/*     */   }
/*     */   
/*     */   public void setOriginX(float originX) {
/* 549 */     this.originX = originX;
/*     */   }
/*     */   
/*     */   public float getOriginY() {
/* 553 */     return this.originY;
/*     */   }
/*     */   
/*     */   public void setOriginY(float originY) {
/* 557 */     this.originY = originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/* 562 */     this.originX = originX;
/* 563 */     this.originY = originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(int alignment) {
/* 568 */     if ((alignment & 0x8) != 0) {
/* 569 */       this.originX = 0.0F;
/* 570 */     } else if ((alignment & 0x10) != 0) {
/* 571 */       this.originX = this.width;
/*     */     } else {
/* 573 */       this.originX = this.width / 2.0F;
/*     */     } 
/* 575 */     if ((alignment & 0x4) != 0) {
/* 576 */       this.originY = 0.0F;
/* 577 */     } else if ((alignment & 0x2) != 0) {
/* 578 */       this.originY = this.height;
/*     */     } else {
/* 580 */       this.originY = this.height / 2.0F;
/*     */     } 
/*     */   }
/*     */   public float getScaleX() {
/* 584 */     return this.scaleX;
/*     */   }
/*     */   
/*     */   public void setScaleX(float scaleX) {
/* 588 */     this.scaleX = scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 592 */     return this.scaleY;
/*     */   }
/*     */   
/*     */   public void setScaleY(float scaleY) {
/* 596 */     this.scaleY = scaleY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scaleXY) {
/* 601 */     this.scaleX = scaleXY;
/* 602 */     this.scaleY = scaleXY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 607 */     this.scaleX = scaleX;
/* 608 */     this.scaleY = scaleY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scaleBy(float scale) {
/* 613 */     this.scaleX += scale;
/* 614 */     this.scaleY += scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scaleBy(float scaleX, float scaleY) {
/* 619 */     this.scaleX += scaleX;
/* 620 */     this.scaleY += scaleY;
/*     */   }
/*     */   
/*     */   public float getRotation() {
/* 624 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public void setRotation(float degrees) {
/* 628 */     if (this.rotation != degrees) {
/* 629 */       this.rotation = degrees;
/* 630 */       rotationChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotateBy(float amountInDegrees) {
/* 636 */     if (amountInDegrees != 0.0F) {
/* 637 */       this.rotation += amountInDegrees;
/* 638 */       rotationChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 643 */     this.color.set(color);
/*     */   }
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 647 */     this.color.set(r, g, b, a);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 652 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 658 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 665 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toFront() {
/* 670 */     setZIndex(2147483647);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toBack() {
/* 675 */     setZIndex(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZIndex(int index) {
/* 682 */     if (index < 0) throw new IllegalArgumentException("ZIndex cannot be < 0."); 
/* 683 */     Group parent = this.parent;
/* 684 */     if (parent == null)
/* 685 */       return;  SnapshotArray<Actor> snapshotArray = parent.children;
/* 686 */     if (((Array)snapshotArray).size == 1)
/* 687 */       return;  index = Math.min(index, ((Array)snapshotArray).size - 1);
/* 688 */     if (index == snapshotArray.indexOf(this, true))
/* 689 */       return;  if (!snapshotArray.removeValue(this, true))
/* 690 */       return;  snapshotArray.insert(index, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getZIndex() {
/* 696 */     Group parent = this.parent;
/* 697 */     if (parent == null) return -1; 
/* 698 */     return parent.children.indexOf(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean clipBegin() {
/* 703 */     return clipBegin(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean clipBegin(float x, float y, float width, float height) {
/* 712 */     if (width <= 0.0F || height <= 0.0F) return false; 
/* 713 */     Rectangle tableBounds = Rectangle.tmp;
/* 714 */     tableBounds.x = x;
/* 715 */     tableBounds.y = y;
/* 716 */     tableBounds.width = width;
/* 717 */     tableBounds.height = height;
/* 718 */     Stage stage = this.stage;
/* 719 */     Rectangle scissorBounds = (Rectangle)Pools.obtain(Rectangle.class);
/* 720 */     stage.calculateScissors(tableBounds, scissorBounds);
/* 721 */     if (ScissorStack.pushScissors(scissorBounds)) return true; 
/* 722 */     Pools.free(scissorBounds);
/* 723 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clipEnd() {
/* 728 */     Pools.free(ScissorStack.popScissors());
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
/* 733 */     Stage stage = this.stage;
/* 734 */     if (stage == null) return screenCoords; 
/* 735 */     return stageToLocalCoordinates(stage.screenToStageCoordinates(screenCoords));
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
/* 740 */     if (this.parent != null) this.parent.stageToLocalCoordinates(stageCoords); 
/* 741 */     parentToLocalCoordinates(stageCoords);
/* 742 */     return stageCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 localToStageCoordinates(Vector2 localCoords) {
/* 748 */     return localToAscendantCoordinates(null, localCoords);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 localToParentCoordinates(Vector2 localCoords) {
/* 753 */     float rotation = -this.rotation;
/* 754 */     float scaleX = this.scaleX;
/* 755 */     float scaleY = this.scaleY;
/* 756 */     float x = this.x;
/* 757 */     float y = this.y;
/* 758 */     if (rotation == 0.0F) {
/* 759 */       if (scaleX == 1.0F && scaleY == 1.0F) {
/* 760 */         localCoords.x += x;
/* 761 */         localCoords.y += y;
/*     */       } else {
/* 763 */         float originX = this.originX;
/* 764 */         float originY = this.originY;
/* 765 */         localCoords.x = (localCoords.x - originX) * scaleX + originX + x;
/* 766 */         localCoords.y = (localCoords.y - originY) * scaleY + originY + y;
/*     */       } 
/*     */     } else {
/* 769 */       float cos = (float)Math.cos((rotation * 0.017453292F));
/* 770 */       float sin = (float)Math.sin((rotation * 0.017453292F));
/* 771 */       float originX = this.originX;
/* 772 */       float originY = this.originY;
/* 773 */       float tox = (localCoords.x - originX) * scaleX;
/* 774 */       float toy = (localCoords.y - originY) * scaleY;
/* 775 */       localCoords.x = tox * cos + toy * sin + originX + x;
/* 776 */       localCoords.y = tox * -sin + toy * cos + originY + y;
/*     */     } 
/* 778 */     return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
/* 783 */     Actor actor = this;
/* 784 */     while (actor != null) {
/* 785 */       actor.localToParentCoordinates(localCoords);
/* 786 */       actor = actor.parent;
/* 787 */       if (actor == ascendant)
/*     */         break; 
/* 789 */     }  return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
/* 794 */     float rotation = this.rotation;
/* 795 */     float scaleX = this.scaleX;
/* 796 */     float scaleY = this.scaleY;
/* 797 */     float childX = this.x;
/* 798 */     float childY = this.y;
/* 799 */     if (rotation == 0.0F) {
/* 800 */       if (scaleX == 1.0F && scaleY == 1.0F) {
/* 801 */         parentCoords.x -= childX;
/* 802 */         parentCoords.y -= childY;
/*     */       } else {
/* 804 */         float originX = this.originX;
/* 805 */         float originY = this.originY;
/* 806 */         parentCoords.x = (parentCoords.x - childX - originX) / scaleX + originX;
/* 807 */         parentCoords.y = (parentCoords.y - childY - originY) / scaleY + originY;
/*     */       } 
/*     */     } else {
/* 810 */       float cos = (float)Math.cos((rotation * 0.017453292F));
/* 811 */       float sin = (float)Math.sin((rotation * 0.017453292F));
/* 812 */       float originX = this.originX;
/* 813 */       float originY = this.originY;
/* 814 */       float tox = parentCoords.x - childX - originX;
/* 815 */       float toy = parentCoords.y - childY - originY;
/* 816 */       parentCoords.x = (tox * cos + toy * sin) / scaleX + originX;
/* 817 */       parentCoords.y = (tox * -sin + toy * cos) / scaleY + originY;
/*     */     } 
/* 819 */     return parentCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawDebug(ShapeRenderer shapes) {
/* 824 */     drawDebugBounds(shapes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawDebugBounds(ShapeRenderer shapes) {
/* 829 */     if (!this.debug)
/* 830 */       return;  shapes.set(ShapeRenderer.ShapeType.Line);
/* 831 */     shapes.setColor(this.stage.getDebugColor());
/* 832 */     shapes.rect(this.x, this.y, this.originX, this.originY, this.width, this.height, this.scaleX, this.scaleY, this.rotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebug(boolean enabled) {
/* 837 */     this.debug = enabled;
/* 838 */     if (enabled) Stage.debug = true; 
/*     */   }
/*     */   
/*     */   public boolean getDebug() {
/* 842 */     return this.debug;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor debug() {
/* 847 */     setDebug(true);
/* 848 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 852 */     String name = this.name;
/* 853 */     if (name == null) {
/* 854 */       name = getClass().getName();
/* 855 */       int dotIndex = name.lastIndexOf('.');
/* 856 */       if (dotIndex != -1) name = name.substring(dotIndex + 1); 
/*     */     } 
/* 858 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2d\Actor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */