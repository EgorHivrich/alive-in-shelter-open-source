/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Affine2;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Group
/*     */   extends Actor
/*     */   implements Cullable
/*     */ {
/*  36 */   private static final Vector2 tmp = new Vector2();
/*     */   
/*  38 */   final SnapshotArray<Actor> children = new SnapshotArray(true, 4, Actor.class);
/*  39 */   private final Affine2 worldTransform = new Affine2();
/*  40 */   private final Matrix4 computedTransform = new Matrix4();
/*  41 */   private final Matrix4 oldTransform = new Matrix4();
/*     */   boolean transform = true;
/*     */   private Rectangle cullingArea;
/*     */   
/*     */   public void act(float delta) {
/*  46 */     super.act(delta);
/*  47 */     Actor[] actors = (Actor[])this.children.begin();
/*  48 */     for (int i = 0, n = this.children.size; i < n; i++)
/*  49 */       actors[i].act(delta); 
/*  50 */     this.children.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  56 */     if (this.transform) applyTransform(batch, computeTransform()); 
/*  57 */     drawChildren(batch, parentAlpha);
/*  58 */     if (this.transform) resetTransform(batch);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawChildren(Batch batch, float parentAlpha) {
/*  66 */     parentAlpha *= this.color.a;
/*  67 */     SnapshotArray<Actor> children = this.children;
/*  68 */     Actor[] actors = (Actor[])children.begin();
/*  69 */     Rectangle cullingArea = this.cullingArea;
/*  70 */     if (cullingArea != null) {
/*     */       
/*  72 */       float cullLeft = cullingArea.x;
/*  73 */       float cullRight = cullLeft + cullingArea.width;
/*  74 */       float cullBottom = cullingArea.y;
/*  75 */       float cullTop = cullBottom + cullingArea.height;
/*  76 */       if (this.transform) {
/*  77 */         for (int i = 0, n = children.size; i < n; i++) {
/*  78 */           Actor child = actors[i];
/*  79 */           if (child.isVisible()) {
/*  80 */             float cx = child.x, cy = child.y;
/*  81 */             if (cx <= cullRight && cy <= cullTop && cx + child.width >= cullLeft && cy + child.height >= cullBottom)
/*  82 */               child.draw(batch, parentAlpha); 
/*     */           } 
/*     */         } 
/*     */       } else {
/*  86 */         float offsetX = this.x, offsetY = this.y;
/*  87 */         this.x = 0.0F;
/*  88 */         this.y = 0.0F;
/*  89 */         for (int i = 0, n = children.size; i < n; i++) {
/*  90 */           Actor child = actors[i];
/*  91 */           if (child.isVisible()) {
/*  92 */             float cx = child.x, cy = child.y;
/*  93 */             if (cx <= cullRight && cy <= cullTop && cx + child.width >= cullLeft && cy + child.height >= cullBottom) {
/*  94 */               child.x = cx + offsetX;
/*  95 */               child.y = cy + offsetY;
/*  96 */               child.draw(batch, parentAlpha);
/*  97 */               child.x = cx;
/*  98 */               child.y = cy;
/*     */             } 
/*     */           } 
/* 101 */         }  this.x = offsetX;
/* 102 */         this.y = offsetY;
/*     */       }
/*     */     
/*     */     }
/* 106 */     else if (this.transform) {
/* 107 */       for (int i = 0, n = children.size; i < n; i++) {
/* 108 */         Actor child = actors[i];
/* 109 */         if (child.isVisible()) {
/* 110 */           child.draw(batch, parentAlpha);
/*     */         }
/*     */       } 
/*     */     } else {
/* 114 */       float offsetX = this.x, offsetY = this.y;
/* 115 */       this.x = 0.0F;
/* 116 */       this.y = 0.0F;
/* 117 */       for (int i = 0, n = children.size; i < n; i++) {
/* 118 */         Actor child = actors[i];
/* 119 */         if (child.isVisible()) {
/* 120 */           float cx = child.x, cy = child.y;
/* 121 */           child.x = cx + offsetX;
/* 122 */           child.y = cy + offsetY;
/* 123 */           child.draw(batch, parentAlpha);
/* 124 */           child.x = cx;
/* 125 */           child.y = cy;
/*     */         } 
/* 127 */       }  this.x = offsetX;
/* 128 */       this.y = offsetY;
/*     */     } 
/*     */     
/* 131 */     children.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawDebug(ShapeRenderer shapes) {
/* 137 */     drawDebugBounds(shapes);
/* 138 */     if (this.transform) applyTransform(shapes, computeTransform()); 
/* 139 */     drawDebugChildren(shapes);
/* 140 */     if (this.transform) resetTransform(shapes);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawDebugChildren(ShapeRenderer shapes) {
/* 148 */     SnapshotArray<Actor> children = this.children;
/* 149 */     Actor[] actors = (Actor[])children.begin();
/*     */     
/* 151 */     if (this.transform) {
/* 152 */       for (int i = 0, n = children.size; i < n; i++) {
/* 153 */         Actor child = actors[i];
/* 154 */         if (child.isVisible() && (
/* 155 */           child.getDebug() || child instanceof Group))
/* 156 */           child.drawDebug(shapes); 
/*     */       } 
/* 158 */       shapes.flush();
/*     */     } else {
/*     */       
/* 161 */       float offsetX = this.x, offsetY = this.y;
/* 162 */       this.x = 0.0F;
/* 163 */       this.y = 0.0F;
/* 164 */       for (int i = 0, n = children.size; i < n; i++) {
/* 165 */         Actor child = actors[i];
/* 166 */         if (child.isVisible() && (
/* 167 */           child.getDebug() || child instanceof Group)) {
/* 168 */           float cx = child.x, cy = child.y;
/* 169 */           child.x = cx + offsetX;
/* 170 */           child.y = cy + offsetY;
/* 171 */           child.drawDebug(shapes);
/* 172 */           child.x = cx;
/* 173 */           child.y = cy;
/*     */         } 
/* 175 */       }  this.x = offsetX;
/* 176 */       this.y = offsetY;
/*     */     } 
/* 178 */     children.end();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Matrix4 computeTransform() {
/* 183 */     Affine2 worldTransform = this.worldTransform;
/* 184 */     float originX = this.originX, originY = this.originY;
/* 185 */     worldTransform.setToTrnRotScl(this.x + originX, this.y + originY, this.rotation, this.scaleX, this.scaleY);
/* 186 */     if (originX != 0.0F || originY != 0.0F) worldTransform.translate(-originX, -originY);
/*     */ 
/*     */     
/* 189 */     Group parentGroup = this.parent;
/* 190 */     while (parentGroup != null && 
/* 191 */       !parentGroup.transform) {
/* 192 */       parentGroup = parentGroup.parent;
/*     */     }
/* 194 */     if (parentGroup != null) worldTransform.preMul(parentGroup.worldTransform);
/*     */     
/* 196 */     this.computedTransform.set(worldTransform);
/* 197 */     return this.computedTransform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyTransform(Batch batch, Matrix4 transform) {
/* 203 */     this.oldTransform.set(batch.getTransformMatrix());
/* 204 */     batch.setTransformMatrix(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetTransform(Batch batch) {
/* 210 */     batch.setTransformMatrix(this.oldTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyTransform(ShapeRenderer shapes, Matrix4 transform) {
/* 217 */     this.oldTransform.set(shapes.getTransformMatrix());
/* 218 */     shapes.setTransformMatrix(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetTransform(ShapeRenderer shapes) {
/* 224 */     shapes.setTransformMatrix(this.oldTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCullingArea(Rectangle cullingArea) {
/* 231 */     this.cullingArea = cullingArea;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getCullingArea() {
/* 237 */     return this.cullingArea;
/*     */   }
/*     */   
/*     */   public Actor hit(float x, float y, boolean touchable) {
/* 241 */     if (touchable && getTouchable() == Touchable.disabled) return null; 
/* 242 */     Vector2 point = tmp;
/* 243 */     Actor[] childrenArray = (Actor[])this.children.items;
/* 244 */     for (int i = this.children.size - 1; i >= 0; i--) {
/* 245 */       Actor child = childrenArray[i];
/* 246 */       if (child.isVisible()) {
/* 247 */         child.parentToLocalCoordinates(point.set(x, y));
/* 248 */         Actor hit = child.hit(point.x, point.y, touchable);
/* 249 */         if (hit != null) return hit; 
/*     */       } 
/* 251 */     }  return super.hit(x, y, touchable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void childrenChanged() {}
/*     */ 
/*     */   
/*     */   public void addActor(Actor actor) {
/* 260 */     if (actor.parent != null) actor.parent.removeActor(actor, false); 
/* 261 */     this.children.add(actor);
/* 262 */     actor.setParent(this);
/* 263 */     actor.setStage(getStage());
/* 264 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAt(int index, Actor actor) {
/* 270 */     if (actor.parent != null) actor.parent.removeActor(actor, false); 
/* 271 */     if (index >= this.children.size) {
/* 272 */       this.children.add(actor);
/*     */     } else {
/* 274 */       this.children.insert(index, actor);
/* 275 */     }  actor.setParent(this);
/* 276 */     actor.setStage(getStage());
/* 277 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorBefore(Actor actorBefore, Actor actor) {
/* 283 */     if (actor.parent != null) actor.parent.removeActor(actor, false); 
/* 284 */     int index = this.children.indexOf(actorBefore, true);
/* 285 */     this.children.insert(index, actor);
/* 286 */     actor.setParent(this);
/* 287 */     actor.setStage(getStage());
/* 288 */     childrenChanged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addActorAfter(Actor actorAfter, Actor actor) {
/* 294 */     if (actor.parent != null) actor.parent.removeActor(actor, false); 
/* 295 */     int index = this.children.indexOf(actorAfter, true);
/* 296 */     if (index == this.children.size) {
/* 297 */       this.children.add(actor);
/*     */     } else {
/* 299 */       this.children.insert(index + 1, actor);
/* 300 */     }  actor.setParent(this);
/* 301 */     actor.setStage(getStage());
/* 302 */     childrenChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeActor(Actor actor) {
/* 307 */     return removeActor(actor, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeActor(Actor actor, boolean unfocus) {
/* 316 */     if (!this.children.removeValue(actor, true)) return false; 
/* 317 */     if (unfocus) {
/* 318 */       Stage stage = getStage();
/* 319 */       if (stage != null) stage.unfocus(actor); 
/*     */     } 
/* 321 */     actor.setParent(null);
/* 322 */     actor.setStage(null);
/* 323 */     childrenChanged();
/* 324 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearChildren() {
/* 329 */     Actor[] actors = (Actor[])this.children.begin();
/* 330 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 331 */       Actor child = actors[i];
/* 332 */       child.setStage(null);
/* 333 */       child.setParent(null);
/*     */     } 
/* 335 */     this.children.end();
/* 336 */     this.children.clear();
/* 337 */     childrenChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 342 */     super.clear();
/* 343 */     clearChildren();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Actor> T findActor(String name) {
/* 349 */     SnapshotArray<Actor> snapshotArray = this.children; int i, n;
/* 350 */     for (i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/* 351 */       if (name.equals(((Actor)snapshotArray.get(i)).getName())) return (T)snapshotArray.get(i); 
/* 352 */     }  for (i = 0, n = ((Array)snapshotArray).size; i < n; i++) {
/* 353 */       Actor child = (Actor)snapshotArray.get(i);
/* 354 */       if (child instanceof Group) {
/* 355 */         Actor actor = ((Group)child).findActor(name);
/* 356 */         if (actor != null) return (T)actor; 
/*     */       } 
/*     */     } 
/* 359 */     return null;
/*     */   }
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 363 */     super.setStage(stage);
/* 364 */     Actor[] childrenArray = (Actor[])this.children.items;
/* 365 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 366 */       childrenArray[i].setStage(stage);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean swapActor(int first, int second) {
/* 371 */     int maxIndex = this.children.size;
/* 372 */     if (first < 0 || first >= maxIndex) return false; 
/* 373 */     if (second < 0 || second >= maxIndex) return false; 
/* 374 */     this.children.swap(first, second);
/* 375 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean swapActor(Actor first, Actor second) {
/* 380 */     int firstIndex = this.children.indexOf(first, true);
/* 381 */     int secondIndex = this.children.indexOf(second, true);
/* 382 */     if (firstIndex == -1 || secondIndex == -1) return false; 
/* 383 */     this.children.swap(firstIndex, secondIndex);
/* 384 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public SnapshotArray<Actor> getChildren() {
/* 389 */     return this.children;
/*     */   }
/*     */   
/*     */   public boolean hasChildren() {
/* 393 */     return (this.children.size > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransform(boolean transform) {
/* 402 */     this.transform = transform;
/*     */   }
/*     */   
/*     */   public boolean isTransform() {
/* 406 */     return this.transform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 localToDescendantCoordinates(Actor descendant, Vector2 localCoords) {
/* 412 */     Group parent = descendant.parent;
/* 413 */     if (parent == null) throw new IllegalArgumentException("Child is not a descendant: " + descendant);
/*     */     
/* 415 */     if (parent != this) localToDescendantCoordinates(parent, localCoords);
/*     */     
/* 417 */     descendant.parentToLocalCoordinates(localCoords);
/* 418 */     return localCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebug(boolean enabled, boolean recursively) {
/* 423 */     setDebug(enabled);
/* 424 */     if (recursively) {
/* 425 */       for (Actor child : this.children) {
/* 426 */         if (child instanceof Group) {
/* 427 */           ((Group)child).setDebug(enabled, recursively); continue;
/*     */         } 
/* 429 */         child.setDebug(enabled);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Group debugAll() {
/* 437 */     setDebug(true, true);
/* 438 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 443 */     StringBuilder buffer = new StringBuilder(128);
/* 444 */     toString(buffer, 1);
/* 445 */     buffer.setLength(buffer.length() - 1);
/* 446 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   void toString(StringBuilder buffer, int indent) {
/* 450 */     buffer.append(super.toString());
/* 451 */     buffer.append('\n');
/*     */     
/* 453 */     Actor[] actors = (Actor[])this.children.begin();
/* 454 */     for (int i = 0, n = this.children.size; i < n; i++) {
/* 455 */       for (int ii = 0; ii < indent; ii++)
/* 456 */         buffer.append("|  "); 
/* 457 */       Actor actor = actors[i];
/* 458 */       if (actor instanceof Group) {
/* 459 */         ((Group)actor).toString(buffer, indent + 1);
/*     */       } else {
/* 461 */         buffer.append(actor);
/* 462 */         buffer.append('\n');
/*     */       } 
/*     */     } 
/* 465 */     this.children.end();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2d\Group.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */