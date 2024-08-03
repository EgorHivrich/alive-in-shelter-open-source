/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Scaling;
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
/*     */ public class ImageButton
/*     */   extends Button
/*     */ {
/*     */   private final Image image;
/*     */   private ImageButtonStyle style;
/*     */   
/*     */   public ImageButton(Skin skin) {
/*  33 */     this(skin.<ImageButtonStyle>get(ImageButtonStyle.class));
/*     */   }
/*     */   
/*     */   public ImageButton(Skin skin, String styleName) {
/*  37 */     this(skin.<ImageButtonStyle>get(styleName, ImageButtonStyle.class));
/*     */   }
/*     */   
/*     */   public ImageButton(ImageButtonStyle style) {
/*  41 */     super(style);
/*  42 */     this.image = new Image();
/*  43 */     this.image.setScaling(Scaling.fit);
/*  44 */     add(this.image);
/*  45 */     setStyle(style);
/*  46 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp) {
/*  50 */     this(new ImageButtonStyle(null, null, null, imageUp, null, null));
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp, Drawable imageDown) {
/*  54 */     this(new ImageButtonStyle(null, null, null, imageUp, imageDown, null));
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
/*  58 */     this(new ImageButtonStyle(null, null, null, imageUp, imageDown, imageChecked));
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  62 */     if (!(style instanceof ImageButtonStyle)) throw new IllegalArgumentException("style must be an ImageButtonStyle."); 
/*  63 */     super.setStyle(style);
/*  64 */     this.style = (ImageButtonStyle)style;
/*  65 */     if (this.image != null) updateImage(); 
/*     */   }
/*     */   
/*     */   public ImageButtonStyle getStyle() {
/*  69 */     return this.style;
/*     */   }
/*     */   
/*     */   private void updateImage() {
/*  73 */     Drawable drawable = null;
/*  74 */     if (isDisabled() && this.style.imageDisabled != null) {
/*  75 */       drawable = this.style.imageDisabled;
/*  76 */     } else if (isPressed() && this.style.imageDown != null) {
/*  77 */       drawable = this.style.imageDown;
/*  78 */     } else if (this.isChecked && this.style.imageChecked != null) {
/*  79 */       drawable = (this.style.imageCheckedOver != null && isOver()) ? this.style.imageCheckedOver : this.style.imageChecked;
/*  80 */     } else if (isOver() && this.style.imageOver != null) {
/*  81 */       drawable = this.style.imageOver;
/*  82 */     } else if (this.style.imageUp != null) {
/*  83 */       drawable = this.style.imageUp;
/*  84 */     }  this.image.setDrawable(drawable);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  88 */     updateImage();
/*  89 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  93 */     return this.image;
/*     */   }
/*     */   
/*     */   public Cell getImageCell() {
/*  97 */     return getCell(this.image);
/*     */   }
/*     */   
/*     */   public static class ImageButtonStyle extends Button.ButtonStyle {
/*     */     public Drawable imageUp;
/*     */     public Drawable imageDown;
/*     */     public Drawable imageOver;
/*     */     public Drawable imageChecked;
/*     */     public Drawable imageCheckedOver;
/*     */     public Drawable imageDisabled;
/*     */     
/*     */     public ImageButtonStyle() {}
/*     */     
/*     */     public ImageButtonStyle(Drawable up, Drawable down, Drawable checked, Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
/* 111 */       super(up, down, checked);
/* 112 */       this.imageUp = imageUp;
/* 113 */       this.imageDown = imageDown;
/* 114 */       this.imageChecked = imageChecked;
/*     */     }
/*     */     
/*     */     public ImageButtonStyle(ImageButtonStyle style) {
/* 118 */       super(style);
/* 119 */       this.imageUp = style.imageUp;
/* 120 */       this.imageDown = style.imageDown;
/* 121 */       this.imageOver = style.imageOver;
/* 122 */       this.imageChecked = style.imageChecked;
/* 123 */       this.imageCheckedOver = style.imageCheckedOver;
/* 124 */       this.imageDisabled = style.imageDisabled;
/*     */     }
/*     */     
/*     */     public ImageButtonStyle(Button.ButtonStyle style) {
/* 128 */       super(style);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\ImageButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */