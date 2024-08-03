/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Pools;
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
/*     */ public class Slider
/*     */   extends ProgressBar
/*     */ {
/*  39 */   int draggingPointer = -1;
/*     */   boolean mouseOver;
/*  41 */   private Interpolation visualInterpolationInverse = Interpolation.linear;
/*     */   
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, Skin skin) {
/*  44 */     this(min, max, stepSize, vertical, skin.<SliderStyle>get("default-" + (vertical ? "vertical" : "horizontal"), SliderStyle.class));
/*     */   }
/*     */   
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName) {
/*  48 */     this(min, max, stepSize, vertical, skin.<SliderStyle>get(styleName, SliderStyle.class));
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
/*     */   public Slider(float min, float max, float stepSize, boolean vertical, SliderStyle style) {
/*  60 */     super(min, max, stepSize, vertical, style);
/*     */     
/*  62 */     this.shiftIgnoresSnap = true;
/*     */     
/*  64 */     addListener((EventListener)new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  66 */             if (Slider.this.disabled) return false; 
/*  67 */             if (Slider.this.draggingPointer != -1) return false; 
/*  68 */             Slider.this.draggingPointer = pointer;
/*  69 */             Slider.this.calculatePositionAndValue(x, y);
/*  70 */             return true;
/*     */           }
/*     */           
/*     */           public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
/*  74 */             if (pointer != Slider.this.draggingPointer)
/*  75 */               return;  Slider.this.draggingPointer = -1;
/*  76 */             if (!Slider.this.calculatePositionAndValue(x, y)) {
/*     */               
/*  78 */               ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*  79 */               Slider.this.fire((Event)changeEvent);
/*  80 */               Pools.free(changeEvent);
/*     */             } 
/*     */           }
/*     */           
/*     */           public void touchDragged(InputEvent event, float x, float y, int pointer) {
/*  85 */             Slider.this.calculatePositionAndValue(x, y);
/*     */           }
/*     */ 
/*     */           
/*     */           public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
/*  90 */             if (pointer == -1) Slider.this.mouseOver = true;
/*     */           
/*     */           }
/*     */           
/*     */           public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/*  95 */             if (pointer == -1) Slider.this.mouseOver = false; 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setStyle(SliderStyle style) {
/* 101 */     if (style == null) throw new NullPointerException("style cannot be null"); 
/* 102 */     if (!(style instanceof SliderStyle)) throw new IllegalArgumentException("style must be a SliderStyle."); 
/* 103 */     setStyle(style);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SliderStyle getStyle() {
/* 109 */     return (SliderStyle)super.getStyle();
/*     */   }
/*     */   
/*     */   protected Drawable getKnobDrawable() {
/* 113 */     SliderStyle style = getStyle();
/* 114 */     return (this.disabled && style.disabledKnob != null) ? style.disabledKnob : ((
/* 115 */       isDragging() && style.knobDown != null) ? style.knobDown : ((this.mouseOver && style.knobOver != null) ? style.knobOver : style.knob));
/*     */   }
/*     */   
/*     */   boolean calculatePositionAndValue(float x, float y) {
/*     */     float value;
/* 120 */     SliderStyle style = getStyle();
/* 121 */     Drawable knob = getKnobDrawable();
/* 122 */     Drawable bg = (this.disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
/*     */ 
/*     */     
/* 125 */     float oldPosition = this.position;
/*     */     
/* 127 */     float min = getMinValue();
/* 128 */     float max = getMaxValue();
/*     */     
/* 130 */     if (this.vertical) {
/* 131 */       float height = getHeight() - bg.getTopHeight() - bg.getBottomHeight();
/* 132 */       float knobHeight = (knob == null) ? 0.0F : knob.getMinHeight();
/* 133 */       this.position = y - bg.getBottomHeight() - knobHeight * 0.5F;
/* 134 */       value = min + (max - min) * this.visualInterpolationInverse.apply(this.position / (height - knobHeight));
/* 135 */       this.position = Math.max(0.0F, this.position);
/* 136 */       this.position = Math.min(height - knobHeight, this.position);
/*     */     } else {
/* 138 */       float width = getWidth() - bg.getLeftWidth() - bg.getRightWidth();
/* 139 */       float knobWidth = (knob == null) ? 0.0F : knob.getMinWidth();
/* 140 */       this.position = x - bg.getLeftWidth() - knobWidth * 0.5F;
/* 141 */       value = min + (max - min) * this.visualInterpolationInverse.apply(this.position / (width - knobWidth));
/* 142 */       this.position = Math.max(0.0F, this.position);
/* 143 */       this.position = Math.min(width - knobWidth, this.position);
/*     */     } 
/*     */     
/* 146 */     float oldValue = value;
/* 147 */     boolean valueSet = setValue(value);
/* 148 */     if (value == oldValue) this.position = oldPosition; 
/* 149 */     return valueSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDragging() {
/* 154 */     return (this.draggingPointer != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisualInterpolationInverse(Interpolation interpolation) {
/* 160 */     this.visualInterpolationInverse = interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SliderStyle
/*     */     extends ProgressBar.ProgressBarStyle
/*     */   {
/*     */     public Drawable knobOver;
/*     */     
/*     */     public Drawable knobDown;
/*     */     
/*     */     public SliderStyle() {}
/*     */     
/*     */     public SliderStyle(Drawable background, Drawable knob) {
/* 174 */       super(background, knob);
/*     */     }
/*     */     
/*     */     public SliderStyle(SliderStyle style) {
/* 178 */       super(style);
/* 179 */       this.knobOver = style.knobOver;
/* 180 */       this.knobDown = style.knobDown;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\Slider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */