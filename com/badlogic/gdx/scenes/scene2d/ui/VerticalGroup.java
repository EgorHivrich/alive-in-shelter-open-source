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
/*     */ public class VerticalGroup
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
/*     */   public VerticalGroup() {
/*  41 */     setTouchable(Touchable.childrenOnly);
/*     */   }
/*     */   
/*     */   public void invalidate() {
/*  45 */     super.invalidate();
/*  46 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/*  50 */     this.sizeInvalid = false;
/*  51 */     SnapshotArray<Actor> children = getChildren();
/*  52 */     int n = children.size;
/*  53 */     this.prefWidth = 0.0F;
/*  54 */     this.prefHeight = this.padTop + this.padBottom + this.spacing * (n - 1);
/*  55 */     for (int i = 0; i < n; i++) {
/*  56 */       Actor child = (Actor)children.get(i);
/*  57 */       if (child instanceof Layout) {
/*  58 */         Layout layout = (Layout)child;
/*  59 */         this.prefWidth = Math.max(this.prefWidth, layout.getPrefWidth());
/*  60 */         this.prefHeight += layout.getPrefHeight();
/*     */       } else {
/*  62 */         this.prefWidth = Math.max(this.prefWidth, child.getWidth());
/*  63 */         this.prefHeight += child.getHeight();
/*     */       } 
/*     */     } 
/*  66 */     this.prefWidth += this.padLeft + this.padRight;
/*  67 */     if (this.round) {
/*  68 */       this.prefWidth = Math.round(this.prefWidth);
/*  69 */       this.prefHeight = Math.round(this.prefHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void layout() {
/*  74 */     float spacing = this.spacing, padLeft = this.padLeft;
/*  75 */     int align = this.align;
/*  76 */     boolean reverse = this.reverse, round = this.round;
/*     */     
/*  78 */     float groupWidth = getWidth() - padLeft - this.padRight;
/*  79 */     float y = reverse ? this.padBottom : (getHeight() - this.padTop + spacing);
/*  80 */     SnapshotArray<Actor> children = getChildren();
/*  81 */     for (int i = 0, n = children.size; i < n; i++) {
/*  82 */       float width, height; Actor child = (Actor)children.get(i);
/*     */       
/*  84 */       Layout layout = null;
/*  85 */       if (child instanceof Layout) {
/*  86 */         layout = (Layout)child;
/*  87 */         if (this.fill > 0.0F) {
/*  88 */           width = groupWidth * this.fill;
/*     */         } else {
/*  90 */           width = Math.min(layout.getPrefWidth(), groupWidth);
/*  91 */         }  width = Math.max(width, layout.getMinWidth());
/*  92 */         float maxWidth = layout.getMaxWidth();
/*  93 */         if (maxWidth > 0.0F && width > maxWidth) width = maxWidth; 
/*  94 */         height = layout.getPrefHeight();
/*     */       } else {
/*  96 */         width = child.getWidth();
/*  97 */         height = child.getHeight();
/*  98 */         if (this.fill > 0.0F) width *= this.fill;
/*     */       
/*     */       } 
/* 101 */       float x = padLeft;
/* 102 */       if ((align & 0x10) != 0) {
/* 103 */         x += groupWidth - width;
/* 104 */       } else if ((align & 0x8) == 0) {
/* 105 */         x += (groupWidth - width) / 2.0F;
/*     */       } 
/* 107 */       if (!reverse) y -= height + spacing; 
/* 108 */       if (round) {
/* 109 */         child.setBounds(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
/*     */       } else {
/* 111 */         child.setBounds(x, y, width, height);
/* 112 */       }  if (reverse) y += height + spacing;
/*     */       
/* 114 */       if (layout != null) layout.validate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 119 */     if (this.sizeInvalid) computeSize(); 
/* 120 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 124 */     if (this.sizeInvalid) computeSize(); 
/* 125 */     return this.prefHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRound(boolean round) {
/* 130 */     this.round = round;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup reverse() {
/* 135 */     reverse(true);
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup reverse(boolean reverse) {
/* 141 */     this.reverse = reverse;
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getReverse() {
/* 146 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup space(float spacing) {
/* 151 */     this.spacing = spacing;
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public float getSpace() {
/* 156 */     return this.spacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup pad(float pad) {
/* 161 */     this.padTop = pad;
/* 162 */     this.padLeft = pad;
/* 163 */     this.padBottom = pad;
/* 164 */     this.padRight = pad;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup pad(float top, float left, float bottom, float right) {
/* 169 */     this.padTop = top;
/* 170 */     this.padLeft = left;
/* 171 */     this.padBottom = bottom;
/* 172 */     this.padRight = right;
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padTop(float padTop) {
/* 177 */     this.padTop = padTop;
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padLeft(float padLeft) {
/* 182 */     this.padLeft = padLeft;
/* 183 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padBottom(float padBottom) {
/* 187 */     this.padBottom = padBottom;
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public VerticalGroup padRight(float padRight) {
/* 192 */     this.padRight = padRight;
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public float getPadTop() {
/* 197 */     return this.padTop;
/*     */   }
/*     */   
/*     */   public float getPadLeft() {
/* 201 */     return this.padLeft;
/*     */   }
/*     */   
/*     */   public float getPadBottom() {
/* 205 */     return this.padBottom;
/*     */   }
/*     */   
/*     */   public float getPadRight() {
/* 209 */     return this.padRight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalGroup align(int align) {
/* 215 */     this.align = align;
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup center() {
/* 221 */     this.align = 1;
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup left() {
/* 227 */     this.align |= 0x8;
/* 228 */     this.align &= 0xFFFFFFEF;
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup right() {
/* 234 */     this.align |= 0x10;
/* 235 */     this.align &= 0xFFFFFFF7;
/* 236 */     return this;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/* 240 */     return this.align;
/*     */   }
/*     */   
/*     */   public VerticalGroup fill() {
/* 244 */     this.fill = 1.0F;
/* 245 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VerticalGroup fill(float fill) {
/* 250 */     this.fill = fill;
/* 251 */     return this;
/*     */   }
/*     */   
/*     */   public float getFill() {
/* 255 */     return this.fill;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\VerticalGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */