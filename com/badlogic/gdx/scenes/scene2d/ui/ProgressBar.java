/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
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
/*     */ public class ProgressBar
/*     */   extends Widget
/*     */   implements Disableable
/*     */ {
/*     */   private ProgressBarStyle style;
/*     */   private float min;
/*     */   private float max;
/*     */   private float stepSize;
/*     */   private float value;
/*     */   private float animateFromValue;
/*     */   float position;
/*     */   final boolean vertical;
/*     */   private float animateDuration;
/*     */   private float animateTime;
/*  51 */   private Interpolation animateInterpolation = Interpolation.linear;
/*     */   private float[] snapValues;
/*     */   private float threshold;
/*     */   boolean disabled;
/*     */   boolean shiftIgnoresSnap;
/*  56 */   private Interpolation visualInterpolation = Interpolation.linear;
/*     */   
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, Skin skin) {
/*  59 */     this(min, max, stepSize, vertical, skin.<ProgressBarStyle>get("default-" + (vertical ? "vertical" : "horizontal"), ProgressBarStyle.class));
/*     */   }
/*     */   
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName) {
/*  63 */     this(min, max, stepSize, vertical, skin.<ProgressBarStyle>get(styleName, ProgressBarStyle.class));
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
/*     */   public ProgressBar(float min, float max, float stepSize, boolean vertical, ProgressBarStyle style) {
/*  77 */     if (min > max) throw new IllegalArgumentException("max must be > min. min,max: " + min + ", " + max); 
/*  78 */     if (stepSize <= 0.0F) throw new IllegalArgumentException("stepSize must be > 0: " + stepSize); 
/*  79 */     setStyle(style);
/*  80 */     this.min = min;
/*  81 */     this.max = max;
/*  82 */     this.stepSize = stepSize;
/*  83 */     this.vertical = vertical;
/*  84 */     this.value = min;
/*  85 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(ProgressBarStyle style) {
/*  89 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  90 */     this.style = style;
/*  91 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ProgressBarStyle getStyle() {
/*  97 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void act(float delta) {
/* 102 */     super.act(delta);
/* 103 */     if (this.animateTime > 0.0F) {
/* 104 */       this.animateTime -= delta;
/* 105 */       Stage stage = getStage();
/* 106 */       if (stage != null && stage.getActionsRequestRendering()) Gdx.graphics.requestRendering();
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 112 */     ProgressBarStyle style = this.style;
/* 113 */     boolean disabled = this.disabled;
/* 114 */     Drawable knob = getKnobDrawable();
/* 115 */     Drawable bg = (disabled && style.disabledBackground != null) ? style.disabledBackground : style.background;
/* 116 */     Drawable knobBefore = (disabled && style.disabledKnobBefore != null) ? style.disabledKnobBefore : style.knobBefore;
/* 117 */     Drawable knobAfter = (disabled && style.disabledKnobAfter != null) ? style.disabledKnobAfter : style.knobAfter;
/*     */     
/* 119 */     Color color = getColor();
/* 120 */     float x = getX();
/* 121 */     float y = getY();
/* 122 */     float width = getWidth();
/* 123 */     float height = getHeight();
/* 124 */     float knobHeight = (knob == null) ? 0.0F : knob.getMinHeight();
/* 125 */     float knobWidth = (knob == null) ? 0.0F : knob.getMinWidth();
/* 126 */     float percent = getVisualPercent();
/*     */     
/* 128 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*     */     
/* 130 */     if (this.vertical) {
/* 131 */       float positionHeight = height;
/*     */       
/* 133 */       float bgTopHeight = 0.0F;
/* 134 */       if (bg != null) {
/* 135 */         bg.draw(batch, x + (int)((width - bg.getMinWidth()) * 0.5F), y, bg.getMinWidth(), height);
/* 136 */         bgTopHeight = bg.getTopHeight();
/* 137 */         positionHeight -= bgTopHeight + bg.getBottomHeight();
/*     */       } 
/*     */       
/* 140 */       float knobHeightHalf = 0.0F;
/* 141 */       if (this.min != this.max) {
/* 142 */         if (knob == null) {
/* 143 */           knobHeightHalf = (knobBefore == null) ? 0.0F : (knobBefore.getMinHeight() * 0.5F);
/* 144 */           this.position = (positionHeight - knobHeightHalf) * percent;
/* 145 */           this.position = Math.min(positionHeight - knobHeightHalf, this.position);
/*     */         } else {
/* 147 */           knobHeightHalf = knobHeight * 0.5F;
/* 148 */           this.position = (positionHeight - knobHeight) * percent;
/* 149 */           this.position = Math.min(positionHeight - knobHeight, this.position) + bg.getBottomHeight();
/*     */         } 
/* 151 */         this.position = Math.max(0.0F, this.position);
/*     */       } 
/*     */       
/* 154 */       if (knobBefore != null) {
/* 155 */         float offset = 0.0F;
/* 156 */         if (bg != null) offset = bgTopHeight; 
/* 157 */         knobBefore.draw(batch, x + (int)((width - knobBefore.getMinWidth()) * 0.5F), y + offset, knobBefore.getMinWidth(), (int)(this.position + knobHeightHalf));
/*     */       } 
/*     */       
/* 160 */       if (knobAfter != null) {
/* 161 */         knobAfter.draw(batch, x + (int)((width - knobAfter.getMinWidth()) * 0.5F), y + (int)(this.position + knobHeightHalf), knobAfter
/* 162 */             .getMinWidth(), height - (int)(this.position + knobHeightHalf));
/*     */       }
/* 164 */       if (knob != null) knob.draw(batch, x + (int)((width - knobWidth) * 0.5F), (int)(y + this.position), knobWidth, knobHeight); 
/*     */     } else {
/* 166 */       float positionWidth = width;
/*     */       
/* 168 */       float bgLeftWidth = 0.0F;
/* 169 */       if (bg != null) {
/* 170 */         bg.draw(batch, x, y + (int)((height - bg.getMinHeight()) * 0.5F), width, bg.getMinHeight());
/* 171 */         bgLeftWidth = bg.getLeftWidth();
/* 172 */         positionWidth -= bgLeftWidth + bg.getRightWidth();
/*     */       } 
/*     */       
/* 175 */       float knobWidthHalf = 0.0F;
/* 176 */       if (this.min != this.max) {
/* 177 */         if (knob == null) {
/* 178 */           knobWidthHalf = (knobBefore == null) ? 0.0F : (knobBefore.getMinWidth() * 0.5F);
/* 179 */           this.position = (positionWidth - knobWidthHalf) * percent;
/* 180 */           this.position = Math.min(positionWidth - knobWidthHalf, this.position);
/*     */         } else {
/* 182 */           knobWidthHalf = knobWidth * 0.5F;
/* 183 */           this.position = (positionWidth - knobWidth) * percent;
/* 184 */           this.position = Math.min(positionWidth - knobWidth, this.position) + bgLeftWidth;
/*     */         } 
/* 186 */         this.position = Math.max(0.0F, this.position);
/*     */       } 
/*     */       
/* 189 */       if (knobBefore != null) {
/* 190 */         float offset = 0.0F;
/* 191 */         if (bg != null) offset = bgLeftWidth; 
/* 192 */         knobBefore.draw(batch, x + offset, y + (int)((height - knobBefore.getMinHeight()) * 0.5F), (int)(this.position + knobWidthHalf), knobBefore
/* 193 */             .getMinHeight());
/*     */       } 
/* 195 */       if (knobAfter != null) {
/* 196 */         knobAfter.draw(batch, x + (int)(this.position + knobWidthHalf), y + (int)((height - knobAfter.getMinHeight()) * 0.5F), width - (int)(this.position + knobWidthHalf), knobAfter
/* 197 */             .getMinHeight());
/*     */       }
/* 199 */       if (knob != null) knob.draw(batch, (int)(x + this.position), (int)(y + (height - knobHeight) * 0.5F), knobWidth, knobHeight); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getValue() {
/* 204 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVisualValue() {
/* 209 */     if (this.animateTime > 0.0F) return this.animateInterpolation.apply(this.animateFromValue, this.value, 1.0F - this.animateTime / this.animateDuration); 
/* 210 */     return this.value;
/*     */   }
/*     */   
/*     */   public float getPercent() {
/* 214 */     return (this.value - this.min) / (this.max - this.min);
/*     */   }
/*     */   
/*     */   public float getVisualPercent() {
/* 218 */     return this.visualInterpolation.apply((getVisualValue() - this.min) / (this.max - this.min));
/*     */   }
/*     */   
/*     */   protected Drawable getKnobDrawable() {
/* 222 */     return (this.disabled && this.style.disabledKnob != null) ? this.style.disabledKnob : this.style.knob;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getKnobPosition() {
/* 227 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setValue(float value) {
/* 234 */     value = clamp(Math.round(value / this.stepSize) * this.stepSize);
/* 235 */     if (!this.shiftIgnoresSnap || (!Gdx.input.isKeyPressed(59) && !Gdx.input.isKeyPressed(60)))
/* 236 */       value = snap(value); 
/* 237 */     float oldValue = this.value;
/* 238 */     if (value == oldValue) return false; 
/* 239 */     float oldVisualValue = getVisualValue();
/* 240 */     this.value = value;
/* 241 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/* 242 */     boolean cancelled = fire((Event)changeEvent);
/* 243 */     if (cancelled) {
/* 244 */       this.value = oldValue;
/* 245 */     } else if (this.animateDuration > 0.0F) {
/* 246 */       this.animateFromValue = oldVisualValue;
/* 247 */       this.animateTime = this.animateDuration;
/*     */     } 
/* 249 */     Pools.free(changeEvent);
/* 250 */     return !cancelled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float clamp(float value) {
/* 256 */     return MathUtils.clamp(value, this.min, this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRange(float min, float max) {
/* 261 */     if (min > max) throw new IllegalArgumentException("min must be <= max"); 
/* 262 */     this.min = min;
/* 263 */     this.max = max;
/* 264 */     if (this.value < min)
/* 265 */     { setValue(min); }
/* 266 */     else if (this.value > max) { setValue(max); }
/*     */   
/*     */   }
/*     */   public void setStepSize(float stepSize) {
/* 270 */     if (stepSize <= 0.0F) throw new IllegalArgumentException("steps must be > 0: " + stepSize); 
/* 271 */     this.stepSize = stepSize;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 275 */     if (this.vertical) {
/* 276 */       Drawable knob = getKnobDrawable();
/* 277 */       Drawable bg = (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : this.style.background;
/* 278 */       return Math.max((knob == null) ? 0.0F : knob.getMinWidth(), bg.getMinWidth());
/*     */     } 
/* 280 */     return 140.0F;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 284 */     if (this.vertical) {
/* 285 */       return 140.0F;
/*     */     }
/* 287 */     Drawable knob = getKnobDrawable();
/* 288 */     Drawable bg = (this.disabled && this.style.disabledBackground != null) ? this.style.disabledBackground : this.style.background;
/* 289 */     return Math.max((knob == null) ? 0.0F : knob.getMinHeight(), (bg == null) ? 0.0F : bg.getMinHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMinValue() {
/* 294 */     return this.min;
/*     */   }
/*     */   
/*     */   public float getMaxValue() {
/* 298 */     return this.max;
/*     */   }
/*     */   
/*     */   public float getStepSize() {
/* 302 */     return this.stepSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimateDuration(float duration) {
/* 307 */     this.animateDuration = duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimateInterpolation(Interpolation animateInterpolation) {
/* 312 */     if (animateInterpolation == null) throw new IllegalArgumentException("animateInterpolation cannot be null."); 
/* 313 */     this.animateInterpolation = animateInterpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisualInterpolation(Interpolation interpolation) {
/* 318 */     this.visualInterpolation = interpolation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSnapToValues(float[] values, float threshold) {
/* 323 */     this.snapValues = values;
/* 324 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */   
/*     */   private float snap(float value) {
/* 329 */     if (this.snapValues == null) return value; 
/* 330 */     for (int i = 0; i < this.snapValues.length; i++) {
/* 331 */       if (Math.abs(value - this.snapValues[i]) <= this.threshold) return this.snapValues[i]; 
/*     */     } 
/* 333 */     return value;
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 337 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 341 */     return this.disabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ProgressBarStyle
/*     */   {
/*     */     public Drawable background;
/*     */     
/*     */     public Drawable disabledBackground;
/*     */     
/*     */     public Drawable knob;
/*     */     public Drawable disabledKnob;
/*     */     public Drawable knobBefore;
/*     */     public Drawable knobAfter;
/*     */     public Drawable disabledKnobBefore;
/*     */     public Drawable disabledKnobAfter;
/*     */     
/*     */     public ProgressBarStyle() {}
/*     */     
/*     */     public ProgressBarStyle(Drawable background, Drawable knob) {
/* 361 */       this.background = background;
/* 362 */       this.knob = knob;
/*     */     }
/*     */     
/*     */     public ProgressBarStyle(ProgressBarStyle style) {
/* 366 */       this.background = style.background;
/* 367 */       this.disabledBackground = style.disabledBackground;
/* 368 */       this.knob = style.knob;
/* 369 */       this.disabledKnob = style.disabledKnob;
/* 370 */       this.knobBefore = style.knobBefore;
/* 371 */       this.knobAfter = style.knobAfter;
/* 372 */       this.disabledKnobBefore = style.disabledKnobBefore;
/* 373 */       this.disabledKnobAfter = style.disabledKnobAfter;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\ProgressBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */