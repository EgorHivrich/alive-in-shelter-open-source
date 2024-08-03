/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DragAndDrop
/*     */ {
/*  33 */   static final Vector2 tmpVector = new Vector2();
/*     */   
/*     */   Payload payload;
/*     */   Actor dragActor;
/*     */   Target target;
/*     */   boolean isValidTarget;
/*  39 */   Array<Target> targets = new Array();
/*  40 */   ObjectMap<Source, DragListener> sourceListeners = new ObjectMap();
/*  41 */   private float tapSquareSize = 8.0F;
/*     */   private int button;
/*  43 */   float dragActorX = 14.0F; float dragActorY = -20.0F; float touchOffsetX;
/*     */   float touchOffsetY;
/*     */   long dragStartTime;
/*  46 */   int dragTime = 250;
/*  47 */   int activePointer = -1;
/*     */   boolean cancelTouchFocus = true;
/*     */   boolean keepWithinStage = true;
/*     */   
/*     */   public void addSource(final Source source) {
/*  52 */     DragListener listener = new DragListener() {
/*     */         public void dragStart(InputEvent event, float x, float y, int pointer) {
/*  54 */           if (DragAndDrop.this.activePointer != -1) {
/*  55 */             event.stop();
/*     */             
/*     */             return;
/*     */           } 
/*  59 */           DragAndDrop.this.activePointer = pointer;
/*     */           
/*  61 */           DragAndDrop.this.dragStartTime = System.currentTimeMillis();
/*  62 */           DragAndDrop.this.payload = source.dragStart(event, getTouchDownX(), getTouchDownY(), pointer);
/*  63 */           event.stop();
/*     */           
/*  65 */           if (DragAndDrop.this.cancelTouchFocus && DragAndDrop.this.payload != null) source.getActor().getStage().cancelTouchFocusExcept((EventListener)this, source.getActor()); 
/*     */         }
/*     */         
/*     */         public void drag(InputEvent event, float x, float y, int pointer) {
/*  69 */           if (DragAndDrop.this.payload == null)
/*  70 */             return;  if (pointer != DragAndDrop.this.activePointer)
/*     */             return; 
/*  72 */           Stage stage = event.getStage();
/*     */           
/*  74 */           Touchable dragActorTouchable = null;
/*  75 */           if (DragAndDrop.this.dragActor != null) {
/*  76 */             dragActorTouchable = DragAndDrop.this.dragActor.getTouchable();
/*  77 */             DragAndDrop.this.dragActor.setTouchable(Touchable.disabled);
/*     */           } 
/*     */ 
/*     */           
/*  81 */           DragAndDrop.Target newTarget = null;
/*  82 */           DragAndDrop.this.isValidTarget = false;
/*  83 */           float stageX = event.getStageX() + DragAndDrop.this.touchOffsetX, stageY = event.getStageY() + DragAndDrop.this.touchOffsetY;
/*  84 */           Actor hit = event.getStage().hit(stageX, stageY, true);
/*  85 */           if (hit == null) hit = event.getStage().hit(stageX, stageY, false); 
/*  86 */           if (hit != null) {
/*  87 */             for (int i = 0, n = DragAndDrop.this.targets.size; i < n; ) {
/*  88 */               DragAndDrop.Target target = (DragAndDrop.Target)DragAndDrop.this.targets.get(i);
/*  89 */               if (!target.actor.isAscendantOf(hit)) { i++; continue; }
/*  90 */                newTarget = target;
/*  91 */               target.actor.stageToLocalCoordinates(DragAndDrop.tmpVector.set(stageX, stageY));
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*  96 */           if (newTarget != DragAndDrop.this.target) {
/*  97 */             if (DragAndDrop.this.target != null) DragAndDrop.this.target.reset(source, DragAndDrop.this.payload); 
/*  98 */             DragAndDrop.this.target = newTarget;
/*     */           } 
/*     */           
/* 101 */           if (newTarget != null) {
/* 102 */             DragAndDrop.this.isValidTarget = newTarget.drag(source, DragAndDrop.this.payload, DragAndDrop.tmpVector.x, DragAndDrop.tmpVector.y, pointer);
/*     */           }
/*     */           
/* 105 */           if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.setTouchable(dragActorTouchable);
/*     */ 
/*     */           
/* 108 */           Actor actor = null;
/* 109 */           if (DragAndDrop.this.target != null) actor = DragAndDrop.this.isValidTarget ? DragAndDrop.this.payload.validDragActor : DragAndDrop.this.payload.invalidDragActor; 
/* 110 */           if (actor == null) actor = DragAndDrop.this.payload.dragActor; 
/* 111 */           if (actor == null)
/* 112 */             return;  if (DragAndDrop.this.dragActor != actor) {
/* 113 */             if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.remove(); 
/* 114 */             DragAndDrop.this.dragActor = actor;
/* 115 */             stage.addActor(actor);
/*     */           } 
/* 117 */           float actorX = event.getStageX() + DragAndDrop.this.dragActorX;
/* 118 */           float actorY = event.getStageY() + DragAndDrop.this.dragActorY - actor.getHeight();
/* 119 */           if (DragAndDrop.this.keepWithinStage) {
/* 120 */             if (actorX < 0.0F) actorX = 0.0F; 
/* 121 */             if (actorY < 0.0F) actorY = 0.0F; 
/* 122 */             if (actorX + actor.getWidth() > stage.getWidth()) actorX = stage.getWidth() - actor.getWidth(); 
/* 123 */             if (actorY + actor.getHeight() > stage.getHeight()) actorY = stage.getHeight() - actor.getHeight(); 
/*     */           } 
/* 125 */           actor.setPosition(actorX, actorY);
/*     */         }
/*     */         
/*     */         public void dragStop(InputEvent event, float x, float y, int pointer) {
/* 129 */           if (pointer != DragAndDrop.this.activePointer)
/* 130 */             return;  DragAndDrop.this.activePointer = -1;
/* 131 */           if (DragAndDrop.this.payload == null)
/*     */             return; 
/* 133 */           if (System.currentTimeMillis() - DragAndDrop.this.dragStartTime < DragAndDrop.this.dragTime) DragAndDrop.this.isValidTarget = false; 
/* 134 */           if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.remove(); 
/* 135 */           if (DragAndDrop.this.isValidTarget) {
/* 136 */             float stageX = event.getStageX() + DragAndDrop.this.touchOffsetX, stageY = event.getStageY() + DragAndDrop.this.touchOffsetY;
/* 137 */             DragAndDrop.this.target.actor.stageToLocalCoordinates(DragAndDrop.tmpVector.set(stageX, stageY));
/* 138 */             DragAndDrop.this.target.drop(source, DragAndDrop.this.payload, DragAndDrop.tmpVector.x, DragAndDrop.tmpVector.y, pointer);
/*     */           } 
/* 140 */           source.dragStop(event, x, y, pointer, DragAndDrop.this.payload, DragAndDrop.this.isValidTarget ? DragAndDrop.this.target : null);
/* 141 */           if (DragAndDrop.this.target != null) DragAndDrop.this.target.reset(source, DragAndDrop.this.payload); 
/* 142 */           DragAndDrop.this.payload = null;
/* 143 */           DragAndDrop.this.target = null;
/* 144 */           DragAndDrop.this.isValidTarget = false;
/* 145 */           DragAndDrop.this.dragActor = null;
/*     */         }
/*     */       };
/* 148 */     listener.setTapSquareSize(this.tapSquareSize);
/* 149 */     listener.setButton(this.button);
/* 150 */     source.actor.addCaptureListener((EventListener)listener);
/* 151 */     this.sourceListeners.put(source, listener);
/*     */   }
/*     */   
/*     */   public void removeSource(Source source) {
/* 155 */     DragListener dragListener = (DragListener)this.sourceListeners.remove(source);
/* 156 */     source.actor.removeCaptureListener((EventListener)dragListener);
/*     */   }
/*     */   
/*     */   public void addTarget(Target target) {
/* 160 */     this.targets.add(target);
/*     */   }
/*     */   
/*     */   public void removeTarget(Target target) {
/* 164 */     this.targets.removeValue(target, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 169 */     this.targets.clear();
/* 170 */     for (ObjectMap.Entries<ObjectMap.Entry<Source, DragListener>> entries = this.sourceListeners.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<Source, DragListener> entry = entries.next();
/* 171 */       ((Source)entry.key).actor.removeCaptureListener((EventListener)entry.value); }
/* 172 */      this.sourceListeners.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTapSquareSize(float halfTapSquareSize) {
/* 177 */     this.tapSquareSize = halfTapSquareSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setButton(int button) {
/* 182 */     this.button = button;
/*     */   }
/*     */   
/*     */   public void setDragActorPosition(float dragActorX, float dragActorY) {
/* 186 */     this.dragActorX = dragActorX;
/* 187 */     this.dragActorY = dragActorY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTouchOffset(float touchOffsetX, float touchOffsetY) {
/* 192 */     this.touchOffsetX = touchOffsetX;
/* 193 */     this.touchOffsetY = touchOffsetY;
/*     */   }
/*     */   
/*     */   public boolean isDragging() {
/* 197 */     return (this.payload != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor getDragActor() {
/* 202 */     return this.dragActor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDragTime(int dragMillis) {
/* 208 */     this.dragTime = dragMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCancelTouchFocus(boolean cancelTouchFocus) {
/* 215 */     this.cancelTouchFocus = cancelTouchFocus;
/*     */   }
/*     */   
/*     */   public void setKeepWithinStage(boolean keepWithinStage) {
/* 219 */     this.keepWithinStage = keepWithinStage;
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Source
/*     */   {
/*     */     final Actor actor;
/*     */     
/*     */     public Source(Actor actor) {
/* 228 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 229 */       this.actor = actor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract DragAndDrop.Payload dragStart(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */     
/*     */     public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {}
/*     */ 
/*     */     
/*     */     public Actor getActor() {
/* 241 */       return this.actor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Target
/*     */   {
/*     */     final Actor actor;
/*     */     
/*     */     public Target(Actor actor) {
/* 251 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 252 */       this.actor = actor;
/* 253 */       Stage stage = actor.getStage();
/* 254 */       if (stage != null && actor == stage.getRoot()) {
/* 255 */         throw new IllegalArgumentException("The stage root cannot be a drag and drop target.");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract boolean drag(DragAndDrop.Source param1Source, DragAndDrop.Payload param1Payload, float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */     
/*     */     public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {}
/*     */ 
/*     */     
/*     */     public abstract void drop(DragAndDrop.Source param1Source, DragAndDrop.Payload param1Payload, float param1Float1, float param1Float2, int param1Int);
/*     */     
/*     */     public Actor getActor() {
/* 269 */       return this.actor;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Payload
/*     */   {
/*     */     Actor dragActor;
/*     */     Actor validDragActor;
/*     */     Actor invalidDragActor;
/*     */     Object object;
/*     */     
/*     */     public void setDragActor(Actor dragActor) {
/* 281 */       this.dragActor = dragActor;
/*     */     }
/*     */     
/*     */     public Actor getDragActor() {
/* 285 */       return this.dragActor;
/*     */     }
/*     */     
/*     */     public void setValidDragActor(Actor validDragActor) {
/* 289 */       this.validDragActor = validDragActor;
/*     */     }
/*     */     
/*     */     public Actor getValidDragActor() {
/* 293 */       return this.validDragActor;
/*     */     }
/*     */     
/*     */     public void setInvalidDragActor(Actor invalidDragActor) {
/* 297 */       this.invalidDragActor = invalidDragActor;
/*     */     }
/*     */     
/*     */     public Actor getInvalidDragActor() {
/* 301 */       return this.invalidDragActor;
/*     */     }
/*     */     
/*     */     public Object getObject() {
/* 305 */       return this.object;
/*     */     }
/*     */     
/*     */     public void setObject(Object object) {
/* 309 */       this.object = object;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\utils\DragAndDrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */