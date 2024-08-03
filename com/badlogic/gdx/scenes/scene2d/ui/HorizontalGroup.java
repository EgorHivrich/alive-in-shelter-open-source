/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
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
/*     */ public class HorizontalGroup
/*     */   extends WidgetGroup
/*     */ {
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   private boolean sizeInvalid = true;
/*     */   private int align;
/*     */   private boolean reverse;
/*     */   private boolean round = true;
/*     */   private float spacing;
/*     */   private float padTop;
/*     */   private float padLeft;
/*     */   private float padBottom;
/*     */   private float padRight;
/*     */   private float fill;
/*     */   
/*     */   public HorizontalGroup() {
/*  45 */     setTouchable(Touchable.childrenOnly);
/*     */   }
/*     */   
/*     */   public void invalidate() {
/*  49 */     super.invalidate();
/*  50 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/*  54 */     this.sizeInvalid = false;
/*  55 */     SnapshotArray<Actor> children = getChildren();
/*  56 */     int n = children.size;
/*  57 */     this.prefWidth = this.padLeft + this.padRight + this.spacing * (n - 1);
/*  58 */     this.prefHeight = 0.0F;
/*  59 */     for (int i = 0; i < n; i++) {
/*  60 */       Actor child = (Actor)children.get(i);
/*  61 */       if (child instanceof Layout) {
/*  62 */         Layout layout = (Layout)child;
/*  63 */         this.prefWidth += layout.getPrefWidth();
/*  64 */         this.prefHeight = Math.max(this.prefHeight, layout.getPrefHeight());
/*     */       } else {
/*  66 */         this.prefWidth += child.getWidth();
/*  67 */         this.prefHeight = Math.max(this.prefHeight, child.getHeight());
/*     */       } 
/*     */     } 
/*  70 */     this.prefHeight += this.padTop + this.padBottom;
/*  71 */     if (this.round) {
/*  72 */       this.prefWidth = Math.round(this.prefWidth);
/*  73 */       this.prefHeight = Math.round(this.prefHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void layout() {
/*  78 */     float spacing = this.spacing, padBottom = this.padBottom;
/*  79 */     int align = this.align;
/*  80 */     boolean reverse = this.reverse, round = this.round;
/*     */     
/*  82 */     float groupHeight = getHeight() - this.padTop - padBottom;
/*  83 */     float x = !reverse ? this.padLeft : (getWidth() - this.padRight + spacing);
/*  84 */     SnapshotArray<Actor> children = getChildren();
/*  85 */     for (int i = 0, n = children.size; i < n; i++) {
/*  86 */       float width, height; Actor child = (Actor)children.get(i);
/*     */       
/*  88 */       Layout layout = null;
/*  89 */       if (child instanceof Layout) {
/*  90 */         layout = (Layout)child;
/*  91 */         if (this.fill > 0.0F) {
/*  92 */           height = groupHeight * this.fill;
/*     */         } else {
/*  94 */           height = Math.min(layout.getPrefHeight(), groupHeight);
/*  95 */         }  height = Math.max(height, layout.getMinHeight());
/*  96 */         float maxHeight = layout.getMaxHeight();
/*  97 */         if (maxHeight > 0.0F && height > maxHeight) height = maxHeight; 
/*  98 */         width = layout.getPrefWidth();
/*     */       } else {
/* 100 */         width = child.getWidth();
/* 101 */         height = child.getHeight();
/* 102 */         if (this.fill > 0.0F) height *= this.fill;
/*     */       
/*     */       } 
/* 105 */       float y = padBottom;
/* 106 */       if ((align & 0x2) != 0) {
/* 107 */         y += groupHeight - height;
/* 108 */       } else if ((align & 0x4) == 0) {
/* 109 */         y += (groupHeight - height) / 2.0F;
/*     */       } 
/* 111 */       if (reverse) x -= width + spacing; 
/* 112 */       if (round) {
/* 113 */         child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
/*     */       } else {
/* 115 */         child.setBounds(x, y, width, height);
/* 116 */       }  if (!reverse) x += width + spacing;
/*     */       
/* 118 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 123 */     if (this.sizeInvalid) computeSize(); 
/* 124 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 128 */     if (this.sizeInvalid) computeSize(); 
/* 129 */     return this.prefHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 134 */     this.round = round;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup reverse() {
/* 139 */     reverse(true);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup reverse(boolean reverse) {
/* 145 */     this.reverse = reverse;
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getReverse() {
/* 150 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup space(float spacing) {
/* 155 */     this.spacing = spacing;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public float getSpace() {
/* 160 */     return this.spacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup pad(float pad) {
/* 165 */     this.padTop = pad;
/* 166 */     this.padLeft = pad;
/* 167 */     this.padBottom = pad;
/* 168 */     this.padRight = pad;
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup pad(float top, float left, float bottom, float right) {
/* 173 */     this.padTop = top;
/* 174 */     this.padLeft = left;
/* 175 */     this.padBottom = bottom;
/* 176 */     this.padRight = right;
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padTop(float padTop) {
/* 181 */     this.padTop = padTop;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padLeft(float padLeft) {
/* 186 */     this.padLeft = padLeft;
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padBottom(float padBottom) {
/* 191 */     this.padBottom = padBottom;
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public HorizontalGroup padRight(float padRight) {
/* 196 */     this.padRight = padRight;
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   public float getPadTop() {
/* 201 */     return this.padTop;
/*     */   }
/*     */   
/*     */   public float getPadLeft() {
/* 205 */     return this.padLeft;
/*     */   }
/*     */   
/*     */   public float getPadBottom() {
/* 209 */     return this.padBottom;
/*     */   }
/*     */   
/*     */   public float getPadRight() {
/* 213 */     return this.padRight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalGroup align(int align) {
/* 219 */     this.align = align;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup center() {
/* 225 */     this.align = 1;
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup top() {
/* 231 */     this.align |= 0x2;
/* 232 */     this.align &= 0xFFFFFFFB;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup bottom() {
/* 238 */     this.align |= 0x4;
/* 239 */     this.align &= 0xFFFFFFFD;
/* 240 */     return this;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/* 244 */     return this.align;
/*     */   }
/*     */   
/*     */   public HorizontalGroup fill() {
/* 248 */     this.fill = 1.0F;
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HorizontalGroup fill(float fill) {
/* 254 */     this.fill = fill;
/* 255 */     return this;
/*     */   }
/*     */   
/*     */   public float getFill() {
/* 259 */     return this.fill;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\HorizontalGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */