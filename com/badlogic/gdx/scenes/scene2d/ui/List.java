/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.OrderedSet;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class List<T>
/*     */   extends Widget
/*     */   implements Cullable
/*     */ {
/*     */   private ListStyle style;
/*  44 */   private final Array<T> items = new Array();
/*  45 */   final ArraySelection<T> selection = new ArraySelection(this.items);
/*     */   
/*     */   private Rectangle cullingArea;
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   
/*     */   public List(Skin skin) {
/*  52 */     this(skin.<ListStyle>get(ListStyle.class));
/*     */   }
/*     */   private float itemHeight; private float textOffsetX; private float textOffsetY;
/*     */   public List(Skin skin, String styleName) {
/*  56 */     this(skin.<ListStyle>get(styleName, ListStyle.class));
/*     */   }
/*     */   
/*     */   public List(ListStyle style) {
/*  60 */     this.selection.setActor(this);
/*  61 */     this.selection.setRequired(true);
/*     */     
/*  63 */     setStyle(style);
/*  64 */     setSize(getPrefWidth(), getPrefHeight());
/*     */     
/*  66 */     addListener((EventListener)new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  68 */             if (pointer == 0 && button != 0) return false; 
/*  69 */             if (List.this.selection.isDisabled()) return false; 
/*  70 */             List.this.touchDown(y);
/*  71 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   void touchDown(float y) {
/*  77 */     if (this.items.size == 0)
/*  78 */       return;  float height = getHeight();
/*  79 */     if (this.style.background != null) {
/*  80 */       height -= this.style.background.getTopHeight() + this.style.background.getBottomHeight();
/*  81 */       y -= this.style.background.getBottomHeight();
/*     */     } 
/*  83 */     int index = (int)((height - y) / this.itemHeight);
/*  84 */     index = Math.max(0, index);
/*  85 */     index = Math.min(this.items.size - 1, index);
/*  86 */     this.selection.choose(this.items.get(index));
/*     */   }
/*     */   
/*     */   public void setStyle(ListStyle style) {
/*  90 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  91 */     this.style = style;
/*  92 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public ListStyle getStyle() {
/*  97 */     return this.style;
/*     */   }
/*     */   
/*     */   public void layout() {
/* 101 */     BitmapFont font = this.style.font;
/* 102 */     Drawable selectedDrawable = this.style.selection;
/*     */     
/* 104 */     this.itemHeight = font.getCapHeight() - font.getDescent() * 2.0F;
/* 105 */     this.itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();
/*     */     
/* 107 */     this.textOffsetX = selectedDrawable.getLeftWidth();
/* 108 */     this.textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();
/*     */     
/* 110 */     this.prefWidth = 0.0F;
/* 111 */     Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
/* 112 */     GlyphLayout layout = (GlyphLayout)layoutPool.obtain();
/* 113 */     for (int i = 0; i < this.items.size; i++) {
/* 114 */       layout.setText(font, toString((T)this.items.get(i)));
/* 115 */       this.prefWidth = Math.max(layout.width, this.prefWidth);
/*     */     } 
/* 117 */     layoutPool.free(layout);
/* 118 */     this.prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
/* 119 */     this.prefHeight = this.items.size * this.itemHeight;
/*     */     
/* 121 */     Drawable background = this.style.background;
/* 122 */     if (background != null) {
/* 123 */       this.prefWidth += background.getLeftWidth() + background.getRightWidth();
/* 124 */       this.prefHeight += background.getTopHeight() + background.getBottomHeight();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 130 */     validate();
/*     */     
/* 132 */     BitmapFont font = this.style.font;
/* 133 */     Drawable selectedDrawable = this.style.selection;
/* 134 */     Color fontColorSelected = this.style.fontColorSelected;
/* 135 */     Color fontColorUnselected = this.style.fontColorUnselected;
/*     */     
/* 137 */     Color color = getColor();
/* 138 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*     */     
/* 140 */     float x = getX(), y = getY(), width = getWidth(), height = getHeight();
/* 141 */     float itemY = height;
/*     */     
/* 143 */     Drawable background = this.style.background;
/* 144 */     if (background != null) {
/* 145 */       background.draw(batch, x, y, width, height);
/* 146 */       float leftWidth = background.getLeftWidth();
/* 147 */       x += leftWidth;
/* 148 */       itemY -= background.getTopHeight();
/* 149 */       width -= leftWidth + background.getRightWidth();
/*     */     } 
/*     */     
/* 152 */     font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
/* 153 */     for (int i = 0; i < this.items.size; i++) {
/* 154 */       if (this.cullingArea == null || (itemY - this.itemHeight <= this.cullingArea.y + this.cullingArea.height && itemY >= this.cullingArea.y)) {
/* 155 */         T item = (T)this.items.get(i);
/* 156 */         boolean selected = this.selection.contains(item);
/* 157 */         if (selected) {
/* 158 */           selectedDrawable.draw(batch, x, y + itemY - this.itemHeight, width, this.itemHeight);
/* 159 */           font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
/*     */         } 
/* 161 */         font.draw(batch, toString(item), x + this.textOffsetX, y + itemY - this.textOffsetY);
/* 162 */         if (selected) {
/* 163 */           font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
/*     */         }
/*     */       }
/* 166 */       else if (itemY < this.cullingArea.y) {
/*     */         break;
/*     */       } 
/* 169 */       itemY -= this.itemHeight;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ArraySelection<T> getSelection() {
/* 174 */     return this.selection;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getSelected() {
/* 179 */     return (T)this.selection.first();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(T item) {
/* 184 */     if (this.items.contains(item, false)) {
/* 185 */       this.selection.set(item);
/* 186 */     } else if (this.selection.getRequired() && this.items.size > 0) {
/* 187 */       this.selection.set(this.items.first());
/*     */     } else {
/* 189 */       this.selection.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSelectedIndex() {
/* 194 */     OrderedSet orderedSet = this.selection.items();
/* 195 */     return (((ObjectSet)orderedSet).size == 0) ? -1 : this.items.indexOf(orderedSet.first(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedIndex(int index) {
/* 200 */     if (index < -1 || index >= this.items.size)
/* 201 */       throw new IllegalArgumentException("index must be >= -1 and < " + this.items.size + ": " + index); 
/* 202 */     if (index == -1) {
/* 203 */       this.selection.clear();
/*     */     } else {
/* 205 */       this.selection.set(this.items.get(index));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setItems(T... newItems) {
/* 210 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 211 */     float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();
/*     */     
/* 213 */     this.items.clear();
/* 214 */     this.items.addAll((Object[])newItems);
/* 215 */     this.selection.validate();
/*     */     
/* 217 */     invalidate();
/* 218 */     if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems(Array newItems) {
/* 224 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 225 */     float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();
/*     */     
/* 227 */     this.items.clear();
/* 228 */     this.items.addAll(newItems);
/* 229 */     this.selection.validate();
/*     */     
/* 231 */     invalidate();
/* 232 */     if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   public void clearItems() {
/* 236 */     if (this.items.size == 0)
/* 237 */       return;  this.items.clear();
/* 238 */     this.selection.clear();
/* 239 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<T> getItems() {
/* 244 */     return this.items;
/*     */   }
/*     */   
/*     */   public float getItemHeight() {
/* 248 */     return this.itemHeight;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 252 */     validate();
/* 253 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 257 */     validate();
/* 258 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   protected String toString(T obj) {
/* 262 */     return obj.toString();
/*     */   }
/*     */   
/*     */   public void setCullingArea(Rectangle cullingArea) {
/* 266 */     this.cullingArea = cullingArea;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ListStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/* 274 */     public Color fontColorSelected = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/* 275 */     public Color fontColorUnselected = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Drawable selection;
/*     */     
/*     */     public Drawable background;
/*     */     
/*     */     public ListStyle() {}
/*     */     
/*     */     public ListStyle(BitmapFont font, Color fontColorSelected, Color fontColorUnselected, Drawable selection) {
/* 284 */       this.font = font;
/* 285 */       this.fontColorSelected.set(fontColorSelected);
/* 286 */       this.fontColorUnselected.set(fontColorUnselected);
/* 287 */       this.selection = selection;
/*     */     }
/*     */     
/*     */     public ListStyle(ListStyle style) {
/* 291 */       this.font = style.font;
/* 292 */       this.fontColorSelected.set(style.fontColorSelected);
/* 293 */       this.fontColorUnselected.set(style.fontColorUnselected);
/* 294 */       this.selection = style.selection;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\List.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */