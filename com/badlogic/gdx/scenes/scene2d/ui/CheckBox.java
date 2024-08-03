/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*     */ public class CheckBox
/*     */   extends TextButton
/*     */ {
/*     */   private Image image;
/*     */   private Cell imageCell;
/*     */   private CheckBoxStyle style;
/*     */   
/*     */   public CheckBox(String text, Skin skin) {
/*  33 */     this(text, skin.<CheckBoxStyle>get(CheckBoxStyle.class));
/*     */   }
/*     */   
/*     */   public CheckBox(String text, Skin skin, String styleName) {
/*  37 */     this(text, skin.<CheckBoxStyle>get(styleName, CheckBoxStyle.class));
/*     */   }
/*     */   
/*     */   public CheckBox(String text, CheckBoxStyle style) {
/*  41 */     super(text, style);
/*  42 */     clearChildren();
/*  43 */     Label label = getLabel();
/*  44 */     this.imageCell = add(this.image = new Image(style.checkboxOff));
/*  45 */     add(label);
/*  46 */     label.setAlignment(8);
/*  47 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  51 */     if (!(style instanceof CheckBoxStyle)) throw new IllegalArgumentException("style must be a CheckBoxStyle."); 
/*  52 */     super.setStyle(style);
/*  53 */     this.style = (CheckBoxStyle)style;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CheckBoxStyle getStyle() {
/*  59 */     return this.style;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  63 */     Drawable checkbox = null;
/*  64 */     if (isDisabled())
/*  65 */       if (this.isChecked && this.style.checkboxOnDisabled != null) {
/*  66 */         checkbox = this.style.checkboxOnDisabled;
/*     */       } else {
/*  68 */         checkbox = this.style.checkboxOffDisabled;
/*     */       }  
/*  70 */     if (checkbox == null)
/*  71 */       if (this.isChecked && this.style.checkboxOn != null) {
/*  72 */         checkbox = this.style.checkboxOn;
/*  73 */       } else if (isOver() && this.style.checkboxOver != null && !isDisabled()) {
/*  74 */         checkbox = this.style.checkboxOver;
/*     */       } else {
/*  76 */         checkbox = this.style.checkboxOff;
/*     */       }  
/*  78 */     this.image.setDrawable(checkbox);
/*  79 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  83 */     return this.image;
/*     */   }
/*     */   
/*     */   public Cell getImageCell() {
/*  87 */     return this.imageCell;
/*     */   }
/*     */   
/*     */   public static class CheckBoxStyle
/*     */     extends TextButton.TextButtonStyle {
/*     */     public Drawable checkboxOn;
/*     */     public Drawable checkboxOff;
/*     */     public Drawable checkboxOver;
/*     */     public Drawable checkboxOnDisabled;
/*     */     public Drawable checkboxOffDisabled;
/*     */     
/*     */     public CheckBoxStyle() {}
/*     */     
/*     */     public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
/* 101 */       this.checkboxOff = checkboxOff;
/* 102 */       this.checkboxOn = checkboxOn;
/* 103 */       this.font = font;
/* 104 */       this.fontColor = fontColor;
/*     */     }
/*     */     
/*     */     public CheckBoxStyle(CheckBoxStyle style) {
/* 108 */       this.checkboxOff = style.checkboxOff;
/* 109 */       this.checkboxOn = style.checkboxOn;
/* 110 */       this.checkboxOver = style.checkboxOver;
/* 111 */       this.checkboxOffDisabled = style.checkboxOffDisabled;
/* 112 */       this.checkboxOnDisabled = style.checkboxOnDisabled;
/* 113 */       this.font = style.font;
/* 114 */       this.fontColor = new Color(style.fontColor);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\CheckBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */