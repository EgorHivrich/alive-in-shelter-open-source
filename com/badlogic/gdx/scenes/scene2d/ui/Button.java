/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Event;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class Button
/*     */   extends Table
/*     */   implements Disableable
/*     */ {
/*     */   private ButtonStyle style;
/*     */   boolean isChecked;
/*     */   boolean isDisabled;
/*     */   ButtonGroup buttonGroup;
/*     */   private ClickListener clickListener;
/*     */   private boolean programmaticChangeEvents = true;
/*     */   
/*     */   public Button(Skin skin) {
/*  51 */     super(skin);
/*  52 */     initialize();
/*  53 */     setStyle(skin.<ButtonStyle>get(ButtonStyle.class));
/*  54 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(Skin skin, String styleName) {
/*  58 */     super(skin);
/*  59 */     initialize();
/*  60 */     setStyle(skin.<ButtonStyle>get(styleName, ButtonStyle.class));
/*  61 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(Actor child, Skin skin, String styleName) {
/*  65 */     this(child, skin.<ButtonStyle>get(styleName, ButtonStyle.class));
/*     */   }
/*     */   
/*     */   public Button(Actor child, ButtonStyle style) {
/*  69 */     initialize();
/*  70 */     add(child);
/*  71 */     setStyle(style);
/*  72 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public Button(ButtonStyle style) {
/*  76 */     initialize();
/*  77 */     setStyle(style);
/*  78 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public Button() {
/*  83 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  87 */     setTouchable(Touchable.enabled);
/*  88 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public void clicked(InputEvent event, float x, float y) {
/*  90 */             if (Button.this.isDisabled())
/*  91 */               return;  Button.this.setChecked(!Button.this.isChecked, true);
/*     */           }
/*     */         }));
/*     */   }
/*     */   
/*     */   public Button(Drawable up) {
/*  97 */     this(new ButtonStyle(up, null, null));
/*     */   }
/*     */   
/*     */   public Button(Drawable up, Drawable down) {
/* 101 */     this(new ButtonStyle(up, down, null));
/*     */   }
/*     */   
/*     */   public Button(Drawable up, Drawable down, Drawable checked) {
/* 105 */     this(new ButtonStyle(up, down, checked));
/*     */   }
/*     */   
/*     */   public Button(Actor child, Skin skin) {
/* 109 */     this(child, skin.<ButtonStyle>get(ButtonStyle.class));
/*     */   }
/*     */   
/*     */   public void setChecked(boolean isChecked) {
/* 113 */     setChecked(isChecked, this.programmaticChangeEvents);
/*     */   }
/*     */   
/*     */   void setChecked(boolean isChecked, boolean fireEvent) {
/* 117 */     if (this.isChecked == isChecked)
/* 118 */       return;  if (this.buttonGroup != null && !this.buttonGroup.canCheck(this, isChecked))
/* 119 */       return;  this.isChecked = isChecked;
/*     */     
/* 121 */     if (fireEvent) {
/* 122 */       ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/* 123 */       if (fire((Event)changeEvent)) this.isChecked = !isChecked; 
/* 124 */       Pools.free(changeEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toggle() {
/* 131 */     setChecked(!this.isChecked);
/*     */   }
/*     */   
/*     */   public boolean isChecked() {
/* 135 */     return this.isChecked;
/*     */   }
/*     */   
/*     */   public boolean isPressed() {
/* 139 */     return this.clickListener.isVisualPressed();
/*     */   }
/*     */   
/*     */   public boolean isOver() {
/* 143 */     return this.clickListener.isOver();
/*     */   }
/*     */   
/*     */   public ClickListener getClickListener() {
/* 147 */     return this.clickListener;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 151 */     return this.isDisabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean isDisabled) {
/* 156 */     this.isDisabled = isDisabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/* 161 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*     */   }
/*     */   
/*     */   public void setStyle(ButtonStyle style) {
/* 165 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/* 166 */     this.style = style;
/*     */     
/* 168 */     Drawable background = null;
/* 169 */     if (isPressed() && !isDisabled()) {
/* 170 */       background = (style.down == null) ? style.up : style.down;
/*     */     }
/* 172 */     else if (isDisabled() && style.disabled != null) {
/* 173 */       background = style.disabled;
/* 174 */     } else if (this.isChecked && style.checked != null) {
/* 175 */       background = (isOver() && style.checkedOver != null) ? style.checkedOver : style.checked;
/* 176 */     } else if (isOver() && style.over != null) {
/* 177 */       background = style.over;
/*     */     } else {
/* 179 */       background = style.up;
/*     */     } 
/* 181 */     setBackground(background);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ButtonStyle getStyle() {
/* 187 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public ButtonGroup getButtonGroup() {
/* 192 */     return this.buttonGroup;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 196 */     validate();
/*     */     
/* 198 */     boolean isPressed = isPressed();
/* 199 */     boolean isDisabled = isDisabled();
/*     */     
/* 201 */     Drawable background = null;
/* 202 */     if (isDisabled && this.style.disabled != null) {
/* 203 */       background = this.style.disabled;
/* 204 */     } else if (isPressed && this.style.down != null) {
/* 205 */       background = this.style.down;
/* 206 */     } else if (this.isChecked && this.style.checked != null) {
/* 207 */       background = (this.style.checkedOver != null && isOver()) ? this.style.checkedOver : this.style.checked;
/* 208 */     } else if (isOver() && this.style.over != null) {
/* 209 */       background = this.style.over;
/* 210 */     } else if (this.style.up != null) {
/* 211 */       background = this.style.up;
/* 212 */     }  setBackground(background);
/*     */     
/* 214 */     float offsetX = 0.0F, offsetY = 0.0F;
/* 215 */     if (isPressed && !isDisabled) {
/* 216 */       offsetX = this.style.pressedOffsetX;
/* 217 */       offsetY = this.style.pressedOffsetY;
/* 218 */     } else if (this.isChecked && !isDisabled) {
/* 219 */       offsetX = this.style.checkedOffsetX;
/* 220 */       offsetY = this.style.checkedOffsetY;
/*     */     } else {
/* 222 */       offsetX = this.style.unpressedOffsetX;
/* 223 */       offsetY = this.style.unpressedOffsetY;
/*     */     } 
/*     */     
/* 226 */     SnapshotArray snapshotArray = getChildren(); int i;
/* 227 */     for (i = 0; i < ((Array)snapshotArray).size; i++)
/* 228 */       ((Actor)snapshotArray.get(i)).moveBy(offsetX, offsetY); 
/* 229 */     super.draw(batch, parentAlpha);
/* 230 */     for (i = 0; i < ((Array)snapshotArray).size; i++) {
/* 231 */       ((Actor)snapshotArray.get(i)).moveBy(-offsetX, -offsetY);
/*     */     }
/* 233 */     Stage stage = getStage();
/* 234 */     if (stage != null && stage.getActionsRequestRendering() && isPressed != this.clickListener.isPressed())
/* 235 */       Gdx.graphics.requestRendering(); 
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 239 */     float width = super.getPrefWidth();
/* 240 */     if (this.style.up != null) width = Math.max(width, this.style.up.getMinWidth()); 
/* 241 */     if (this.style.down != null) width = Math.max(width, this.style.down.getMinWidth()); 
/* 242 */     if (this.style.checked != null) width = Math.max(width, this.style.checked.getMinWidth()); 
/* 243 */     return width;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 247 */     float height = super.getPrefHeight();
/* 248 */     if (this.style.up != null) height = Math.max(height, this.style.up.getMinHeight()); 
/* 249 */     if (this.style.down != null) height = Math.max(height, this.style.down.getMinHeight()); 
/* 250 */     if (this.style.checked != null) height = Math.max(height, this.style.checked.getMinHeight()); 
/* 251 */     return height;
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 255 */     return getPrefWidth();
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 259 */     return getPrefHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ButtonStyle
/*     */   {
/*     */     public Drawable up;
/*     */     
/*     */     public Drawable down;
/*     */     public Drawable over;
/*     */     public Drawable checked;
/*     */     public Drawable checkedOver;
/*     */     public Drawable disabled;
/*     */     
/*     */     public ButtonStyle(Drawable up, Drawable down, Drawable checked) {
/* 274 */       this.up = up;
/* 275 */       this.down = down;
/* 276 */       this.checked = checked;
/*     */     } public float pressedOffsetX; public float pressedOffsetY; public float unpressedOffsetX; public float unpressedOffsetY; public float checkedOffsetX; public float checkedOffsetY;
/*     */     public ButtonStyle() {}
/*     */     public ButtonStyle(ButtonStyle style) {
/* 280 */       this.up = style.up;
/* 281 */       this.down = style.down;
/* 282 */       this.over = style.over;
/* 283 */       this.checked = style.checked;
/* 284 */       this.checkedOver = style.checkedOver;
/* 285 */       this.disabled = style.disabled;
/* 286 */       this.pressedOffsetX = style.pressedOffsetX;
/* 287 */       this.pressedOffsetY = style.pressedOffsetY;
/* 288 */       this.unpressedOffsetX = style.unpressedOffsetX;
/* 289 */       this.unpressedOffsetY = style.unpressedOffsetY;
/* 290 */       this.checkedOffsetX = style.checkedOffsetX;
/* 291 */       this.checkedOffsetY = style.checkedOffsetY;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\Button.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */