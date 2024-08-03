/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.actions.Actions;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectBox<T>
/*     */   extends Widget
/*     */   implements Disableable
/*     */ {
/*  59 */   static final Vector2 temp = new Vector2();
/*     */   
/*     */   SelectBoxStyle style;
/*  62 */   final Array<T> items = new Array();
/*  63 */   final ArraySelection<T> selection = new ArraySelection(this.items);
/*     */   
/*     */   SelectBoxList<T> selectBoxList;
/*     */   
/*     */   private float prefWidth;
/*  68 */   private GlyphLayout layout = new GlyphLayout(); private float prefHeight; private ClickListener clickListener; boolean disabled;
/*     */   
/*     */   public SelectBox(Skin skin) {
/*  71 */     this(skin.<SelectBoxStyle>get(SelectBoxStyle.class));
/*     */   }
/*     */   
/*     */   public SelectBox(Skin skin, String styleName) {
/*  75 */     this(skin.<SelectBoxStyle>get(styleName, SelectBoxStyle.class));
/*     */   }
/*     */   
/*     */   public SelectBox(SelectBoxStyle style) {
/*  79 */     setStyle(style);
/*  80 */     setSize(getPrefWidth(), getPrefHeight());
/*     */     
/*  82 */     this.selection.setActor(this);
/*  83 */     this.selection.setRequired(true);
/*     */     
/*  85 */     this.selectBoxList = new SelectBoxList<T>(this);
/*     */     
/*  87 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/*  89 */             if (pointer == 0 && button != 0) return false; 
/*  90 */             if (SelectBox.this.disabled) return false; 
/*  91 */             if (SelectBox.this.selectBoxList.hasParent()) {
/*  92 */               SelectBox.this.hideList();
/*     */             } else {
/*  94 */               SelectBox.this.showList();
/*  95 */             }  return true;
/*     */           }
/*     */         }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxListCount(int maxListCount) {
/* 103 */     this.selectBoxList.maxListCount = maxListCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxListCount() {
/* 108 */     return this.selectBoxList.maxListCount;
/*     */   }
/*     */   
/*     */   protected void setStage(Stage stage) {
/* 112 */     if (stage == null) this.selectBoxList.hide(); 
/* 113 */     super.setStage(stage);
/*     */   }
/*     */   
/*     */   public void setStyle(SelectBoxStyle style) {
/* 117 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 118 */     this.style = style;
/* 119 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectBoxStyle getStyle() {
/* 125 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItems(T... newItems) {
/* 130 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 131 */     float oldPrefWidth = getPrefWidth();
/*     */     
/* 133 */     this.items.clear();
/* 134 */     this.items.addAll((Object[])newItems);
/* 135 */     this.selection.validate();
/* 136 */     this.selectBoxList.list.setItems(this.items);
/*     */     
/* 138 */     invalidate();
/* 139 */     if (oldPrefWidth != getPrefWidth()) invalidateHierarchy();
/*     */   
/*     */   }
/*     */   
/*     */   public void setItems(Array<T> newItems) {
/* 144 */     if (newItems == null) throw new IllegalArgumentException("newItems cannot be null."); 
/* 145 */     float oldPrefWidth = getPrefWidth();
/*     */     
/* 147 */     this.items.clear();
/* 148 */     this.items.addAll(newItems);
/* 149 */     this.selection.validate();
/* 150 */     this.selectBoxList.list.setItems(this.items);
/*     */     
/* 152 */     invalidate();
/* 153 */     if (oldPrefWidth != getPrefWidth()) invalidateHierarchy(); 
/*     */   }
/*     */   
/*     */   public void clearItems() {
/* 157 */     if (this.items.size == 0)
/* 158 */       return;  this.items.clear();
/* 159 */     this.selection.clear();
/* 160 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<T> getItems() {
/* 165 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public void layout() {
/* 170 */     Drawable bg = this.style.background;
/* 171 */     BitmapFont font = this.style.font;
/*     */     
/* 173 */     if (bg != null) {
/* 174 */       this.prefHeight = Math.max(bg.getTopHeight() + bg.getBottomHeight() + font.getCapHeight() - font.getDescent() * 2.0F, bg
/* 175 */           .getMinHeight());
/*     */     } else {
/* 177 */       this.prefHeight = font.getCapHeight() - font.getDescent() * 2.0F;
/*     */     } 
/* 179 */     float maxItemWidth = 0.0F;
/* 180 */     Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
/* 181 */     GlyphLayout layout = (GlyphLayout)layoutPool.obtain();
/* 182 */     for (int i = 0; i < this.items.size; i++) {
/* 183 */       layout.setText(font, toString((T)this.items.get(i)));
/* 184 */       maxItemWidth = Math.max(layout.width, maxItemWidth);
/*     */     } 
/* 186 */     layoutPool.free(layout);
/*     */     
/* 188 */     this.prefWidth = maxItemWidth;
/* 189 */     if (bg != null) this.prefWidth += bg.getLeftWidth() + bg.getRightWidth();
/*     */     
/* 191 */     List.ListStyle listStyle = this.style.listStyle;
/* 192 */     ScrollPane.ScrollPaneStyle scrollStyle = this.style.scrollStyle;
/* 193 */     this.prefWidth = Math.max(this.prefWidth, maxItemWidth + ((scrollStyle.background == null) ? 0.0F : (scrollStyle.background
/*     */ 
/*     */         
/* 196 */         .getLeftWidth() + scrollStyle.background
/* 197 */         .getRightWidth())) + listStyle.selection
/* 198 */         .getLeftWidth() + listStyle.selection
/* 199 */         .getRightWidth() + 
/* 200 */         Math.max((this.style.scrollStyle.vScroll != null) ? this.style.scrollStyle.vScroll.getMinWidth() : 0.0F, (this.style.scrollStyle.vScrollKnob != null) ? this.style.scrollStyle.vScrollKnob
/* 201 */           .getMinWidth() : 0.0F));
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*     */     Drawable background;
/* 206 */     validate();
/*     */ 
/*     */     
/* 209 */     if (this.disabled && this.style.backgroundDisabled != null) {
/* 210 */       background = this.style.backgroundDisabled;
/* 211 */     } else if (this.selectBoxList.hasParent() && this.style.backgroundOpen != null) {
/* 212 */       background = this.style.backgroundOpen;
/* 213 */     } else if (this.clickListener.isOver() && this.style.backgroundOver != null) {
/* 214 */       background = this.style.backgroundOver;
/* 215 */     } else if (this.style.background != null) {
/* 216 */       background = this.style.background;
/*     */     } else {
/* 218 */       background = null;
/* 219 */     }  BitmapFont font = this.style.font;
/* 220 */     Color fontColor = (this.disabled && this.style.disabledFontColor != null) ? this.style.disabledFontColor : this.style.fontColor;
/*     */     
/* 222 */     Color color = getColor();
/* 223 */     float x = getX();
/* 224 */     float y = getY();
/* 225 */     float width = getWidth();
/* 226 */     float height = getHeight();
/*     */     
/* 228 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 229 */     if (background != null) background.draw(batch, x, y, width, height);
/*     */     
/* 231 */     T selected = (T)this.selection.first();
/* 232 */     if (selected != null) {
/* 233 */       String string = toString(selected);
/* 234 */       if (background != null) {
/* 235 */         width -= background.getLeftWidth() + background.getRightWidth();
/* 236 */         height -= background.getBottomHeight() + background.getTopHeight();
/* 237 */         x += background.getLeftWidth();
/* 238 */         y += (int)(height / 2.0F + background.getBottomHeight() + (font.getData()).capHeight / 2.0F);
/*     */       } else {
/* 240 */         y += (int)(height / 2.0F + (font.getData()).capHeight / 2.0F);
/*     */       } 
/* 242 */       font.setColor(fontColor.r, fontColor.g, fontColor.b, fontColor.a * parentAlpha);
/* 243 */       this.layout.setText(font, string, 0, string.length(), font.getColor(), width, 8, false, "...");
/* 244 */       font.draw(batch, this.layout, x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArraySelection<T> getSelection() {
/* 251 */     return this.selection;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getSelected() {
/* 256 */     return (T)this.selection.first();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(T item) {
/* 261 */     if (this.items.contains(item, false)) {
/* 262 */       this.selection.set(item);
/* 263 */     } else if (this.items.size > 0) {
/* 264 */       this.selection.set(this.items.first());
/*     */     } else {
/* 266 */       this.selection.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSelectedIndex() {
/* 271 */     OrderedSet orderedSet = this.selection.items();
/* 272 */     return (((ObjectSet)orderedSet).size == 0) ? -1 : this.items.indexOf(orderedSet.first(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedIndex(int index) {
/* 277 */     this.selection.set(this.items.get(index));
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 281 */     if (disabled && !this.disabled) hideList(); 
/* 282 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 286 */     return this.disabled;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 290 */     validate();
/* 291 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 295 */     validate();
/* 296 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   protected String toString(T obj) {
/* 300 */     return obj.toString();
/*     */   }
/*     */   
/*     */   public void showList() {
/* 304 */     if (this.items.size == 0)
/* 305 */       return;  this.selectBoxList.show(getStage());
/*     */   }
/*     */   
/*     */   public void hideList() {
/* 309 */     this.selectBoxList.hide();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<T> getList() {
/* 314 */     return this.selectBoxList.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScrollPane getScrollPane() {
/* 319 */     return this.selectBoxList;
/*     */   }
/*     */   
/*     */   protected void onShow(Actor selectBoxList, boolean below) {
/* 323 */     (selectBoxList.getColor()).a = 0.0F;
/* 324 */     selectBoxList.addAction((Action)Actions.fadeIn(0.3F, Interpolation.fade));
/*     */   }
/*     */   
/*     */   protected void onHide(Actor selectBoxList) {
/* 328 */     (selectBoxList.getColor()).a = 1.0F;
/* 329 */     selectBoxList.addAction((Action)Actions.sequence((Action)Actions.fadeOut(0.15F, Interpolation.fade), (Action)Actions.removeActor()));
/*     */   }
/*     */   
/*     */   static class SelectBoxList<T>
/*     */     extends ScrollPane {
/*     */     private final SelectBox<T> selectBox;
/*     */     int maxListCount;
/* 336 */     private final Vector2 screenPosition = new Vector2();
/*     */     final List<T> list;
/*     */     private InputListener hideListener;
/*     */     private Actor previousScrollFocus;
/*     */     
/*     */     public SelectBoxList(final SelectBox<T> selectBox) {
/* 342 */       super((Actor)null, selectBox.style.scrollStyle);
/* 343 */       this.selectBox = selectBox;
/*     */       
/* 345 */       setOverscroll(false, false);
/* 346 */       setFadeScrollBars(false);
/* 347 */       setScrollingDisabled(true, false);
/*     */       
/* 349 */       this.list = new List<T>(selectBox.style.listStyle)
/*     */         {
/*     */           protected String toString(T obj) {
/* 352 */             return selectBox.toString(obj);
/*     */           }
/*     */         };
/* 355 */       this.list.setTouchable(Touchable.disabled);
/* 356 */       setWidget(this.list);
/*     */       
/* 358 */       this.list.addListener((EventListener)new ClickListener() {
/*     */             public void clicked(InputEvent event, float x, float y) {
/* 360 */               selectBox.selection.choose(SelectBox.SelectBoxList.this.list.getSelected());
/* 361 */               SelectBox.SelectBoxList.this.hide();
/*     */             }
/*     */             
/*     */             public boolean mouseMoved(InputEvent event, float x, float y) {
/* 365 */               SelectBox.SelectBoxList.this.list.setSelectedIndex(Math.min(selectBox.items.size - 1, (int)((SelectBox.SelectBoxList.this.list.getHeight() - y) / SelectBox.SelectBoxList.this.list.getItemHeight())));
/* 366 */               return true;
/*     */             }
/*     */           });
/*     */       
/* 370 */       addListener((EventListener)new InputListener() {
/*     */             public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 372 */               if (toActor == null || !SelectBox.SelectBoxList.this.isAscendantOf(toActor)) SelectBox.SelectBoxList.this.list.selection.set(selectBox.getSelected());
/*     */             
/*     */             }
/*     */           });
/* 376 */       this.hideListener = new InputListener() {
/*     */           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
/* 378 */             Actor target = event.getTarget();
/* 379 */             if (SelectBox.SelectBoxList.this.isAscendantOf(target)) return false; 
/* 380 */             SelectBox.SelectBoxList.this.list.selection.set(selectBox.getSelected());
/* 381 */             SelectBox.SelectBoxList.this.hide();
/* 382 */             return false;
/*     */           }
/*     */           
/*     */           public boolean keyDown(InputEvent event, int keycode) {
/* 386 */             if (keycode == 131) SelectBox.SelectBoxList.this.hide(); 
/* 387 */             return false;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public void show(Stage stage) {
/* 393 */       if (this.list.isTouchable())
/*     */         return; 
/* 395 */       stage.removeCaptureListener((EventListener)this.hideListener);
/* 396 */       stage.addCaptureListener((EventListener)this.hideListener);
/* 397 */       stage.addActor((Actor)this);
/*     */       
/* 399 */       this.selectBox.localToStageCoordinates(this.screenPosition.set(0.0F, 0.0F));
/*     */ 
/*     */       
/* 402 */       float itemHeight = this.list.getItemHeight();
/* 403 */       float height = itemHeight * ((this.maxListCount <= 0) ? this.selectBox.items.size : Math.min(this.maxListCount, this.selectBox.items.size));
/* 404 */       Drawable scrollPaneBackground = (getStyle()).background;
/* 405 */       if (scrollPaneBackground != null)
/* 406 */         height += scrollPaneBackground.getTopHeight() + scrollPaneBackground.getBottomHeight(); 
/* 407 */       Drawable listBackground = (this.list.getStyle()).background;
/* 408 */       if (listBackground != null) height += listBackground.getTopHeight() + listBackground.getBottomHeight();
/*     */       
/* 410 */       float heightBelow = this.screenPosition.y;
/* 411 */       float heightAbove = (stage.getCamera()).viewportHeight - this.screenPosition.y - this.selectBox.getHeight();
/* 412 */       boolean below = true;
/* 413 */       if (height > heightBelow) {
/* 414 */         if (heightAbove > heightBelow) {
/* 415 */           below = false;
/* 416 */           height = Math.min(height, heightAbove);
/*     */         } else {
/* 418 */           height = heightBelow;
/*     */         } 
/*     */       }
/* 421 */       if (below) {
/* 422 */         setY(this.screenPosition.y - height);
/*     */       } else {
/* 424 */         setY(this.screenPosition.y + this.selectBox.getHeight());
/* 425 */       }  setX(this.screenPosition.x);
/* 426 */       setHeight(height);
/* 427 */       validate();
/* 428 */       float width = Math.max(getPrefWidth(), this.selectBox.getWidth());
/* 429 */       if (getPrefHeight() > height) width += getScrollBarWidth(); 
/* 430 */       if (scrollPaneBackground != null)
/*     */       {
/* 432 */         width += Math.max(0.0F, scrollPaneBackground.getRightWidth() - scrollPaneBackground.getLeftWidth());
/*     */       }
/* 434 */       setWidth(width);
/*     */       
/* 436 */       validate();
/* 437 */       scrollTo(0.0F, this.list.getHeight() - this.selectBox.getSelectedIndex() * itemHeight - itemHeight / 2.0F, 0.0F, 0.0F, true, true);
/* 438 */       updateVisualScroll();
/*     */       
/* 440 */       this.previousScrollFocus = null;
/* 441 */       Actor actor = stage.getScrollFocus();
/* 442 */       if (actor != null && !actor.isDescendantOf((Actor)this)) this.previousScrollFocus = actor; 
/* 443 */       stage.setScrollFocus((Actor)this);
/*     */       
/* 445 */       this.list.selection.set(this.selectBox.getSelected());
/* 446 */       this.list.setTouchable(Touchable.enabled);
/* 447 */       clearActions();
/* 448 */       this.selectBox.onShow((Actor)this, below);
/*     */     }
/*     */     
/*     */     public void hide() {
/* 452 */       if (!this.list.isTouchable() || !hasParent())
/* 453 */         return;  this.list.setTouchable(Touchable.disabled);
/*     */       
/* 455 */       Stage stage = getStage();
/* 456 */       if (stage != null) {
/* 457 */         stage.removeCaptureListener((EventListener)this.hideListener);
/* 458 */         if (this.previousScrollFocus != null && this.previousScrollFocus.getStage() == null) this.previousScrollFocus = null; 
/* 459 */         Actor actor = stage.getScrollFocus();
/* 460 */         if (actor == null || isAscendantOf(actor)) stage.setScrollFocus(this.previousScrollFocus);
/*     */       
/*     */       } 
/* 463 */       clearActions();
/* 464 */       this.selectBox.onHide((Actor)this);
/*     */     }
/*     */     
/*     */     public void draw(Batch batch, float parentAlpha) {
/* 468 */       this.selectBox.localToStageCoordinates(SelectBox.temp.set(0.0F, 0.0F));
/* 469 */       if (!SelectBox.temp.equals(this.screenPosition)) hide(); 
/* 470 */       super.draw(batch, parentAlpha);
/*     */     }
/*     */     
/*     */     public void act(float delta) {
/* 474 */       super.act(delta);
/* 475 */       toFront();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SelectBoxStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/* 484 */     public Color fontColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     
/*     */     public Color disabledFontColor;
/*     */     
/*     */     public Drawable background;
/*     */     
/*     */     public ScrollPane.ScrollPaneStyle scrollStyle;
/*     */     public List.ListStyle listStyle;
/*     */     public Drawable backgroundOver;
/*     */     public Drawable backgroundOpen;
/*     */     public Drawable backgroundDisabled;
/*     */     
/*     */     public SelectBoxStyle() {}
/*     */     
/*     */     public SelectBoxStyle(BitmapFont font, Color fontColor, Drawable background, ScrollPane.ScrollPaneStyle scrollStyle, List.ListStyle listStyle) {
/* 499 */       this.font = font;
/* 500 */       this.fontColor.set(fontColor);
/* 501 */       this.background = background;
/* 502 */       this.scrollStyle = scrollStyle;
/* 503 */       this.listStyle = listStyle;
/*     */     }
/*     */     
/*     */     public SelectBoxStyle(SelectBoxStyle style) {
/* 507 */       this.font = style.font;
/* 508 */       this.fontColor.set(style.fontColor);
/* 509 */       if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor); 
/* 510 */       this.background = style.background;
/* 511 */       this.backgroundOver = style.backgroundOver;
/* 512 */       this.backgroundOpen = style.backgroundOpen;
/* 513 */       this.backgroundDisabled = style.backgroundDisabled;
/* 514 */       this.scrollStyle = new ScrollPane.ScrollPaneStyle(style.scrollStyle);
/* 515 */       this.listStyle = new List.ListStyle(style.listStyle);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\SelectBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */