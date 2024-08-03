/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.StringBuilder;
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
/*     */ public class Label
/*     */   extends Widget
/*     */ {
/*  34 */   private static final Color tempColor = new Color();
/*  35 */   private static final GlyphLayout prefSizeLayout = new GlyphLayout();
/*     */   
/*     */   private LabelStyle style;
/*  38 */   private final GlyphLayout layout = new GlyphLayout();
/*  39 */   private final Vector2 prefSize = new Vector2();
/*  40 */   private final StringBuilder text = new StringBuilder();
/*     */   private BitmapFontCache cache;
/*  42 */   private int labelAlign = 8;
/*  43 */   private int lineAlign = 8;
/*     */   private boolean wrap;
/*     */   private float lastPrefHeight;
/*     */   private boolean prefSizeInvalid = true;
/*  47 */   private float fontScaleX = 1.0F; private float fontScaleY = 1.0F;
/*     */   private String ellipsis;
/*     */   
/*     */   public Label(CharSequence text, Skin skin) {
/*  51 */     this(text, skin.<LabelStyle>get(LabelStyle.class));
/*     */   }
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String styleName) {
/*  55 */     this(text, skin.<LabelStyle>get(styleName, LabelStyle.class));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String fontName, Color color) {
/*  61 */     this(text, new LabelStyle(skin.getFont(fontName), color));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String fontName, String colorName) {
/*  67 */     this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
/*     */   }
/*     */   
/*     */   public Label(CharSequence text, LabelStyle style) {
/*  71 */     if (text != null) this.text.append(text); 
/*  72 */     setStyle(style);
/*  73 */     if (text != null && text.length() > 0) setSize(getPrefWidth(), getPrefHeight()); 
/*     */   }
/*     */   
/*     */   public void setStyle(LabelStyle style) {
/*  77 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  78 */     if (style.font == null) throw new IllegalArgumentException("Missing LabelStyle font."); 
/*  79 */     this.style = style;
/*  80 */     this.cache = style.font.newFontCache();
/*  81 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LabelStyle getStyle() {
/*  87 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(CharSequence newText) {
/*  92 */     if (newText == null) newText = ""; 
/*  93 */     if (newText instanceof StringBuilder) {
/*  94 */       if (this.text.equals(newText))
/*  95 */         return;  this.text.setLength(0);
/*  96 */       this.text.append((StringBuilder)newText);
/*     */     } else {
/*  98 */       if (textEquals(newText))
/*  99 */         return;  this.text.setLength(0);
/* 100 */       this.text.append(newText);
/*     */     } 
/* 102 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public boolean textEquals(CharSequence other) {
/* 106 */     int length = this.text.length;
/* 107 */     char[] chars = this.text.chars;
/* 108 */     if (length != other.length()) return false; 
/* 109 */     for (int i = 0; i < length; i++) {
/* 110 */       if (chars[i] != other.charAt(i)) return false; 
/* 111 */     }  return true;
/*     */   }
/*     */   
/*     */   public StringBuilder getText() {
/* 115 */     return this.text;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 119 */     super.invalidate();
/* 120 */     this.prefSizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void scaleAndComputePrefSize() {
/* 124 */     BitmapFont font = this.cache.getFont();
/* 125 */     float oldScaleX = font.getScaleX();
/* 126 */     float oldScaleY = font.getScaleY();
/* 127 */     if (this.fontScaleX != 1.0F || this.fontScaleY != 1.0F) font.getData().setScale(this.fontScaleX, this.fontScaleY);
/*     */     
/* 129 */     computePrefSize();
/*     */     
/* 131 */     if (this.fontScaleX != 1.0F || this.fontScaleY != 1.0F) font.getData().setScale(oldScaleX, oldScaleY); 
/*     */   }
/*     */   
/*     */   private void computePrefSize() {
/* 135 */     this.prefSizeInvalid = false;
/* 136 */     GlyphLayout prefSizeLayout = Label.prefSizeLayout;
/* 137 */     if (this.wrap && this.ellipsis == null) {
/* 138 */       float width = getWidth();
/* 139 */       if (this.style.background != null) width -= this.style.background.getLeftWidth() + this.style.background.getRightWidth(); 
/* 140 */       prefSizeLayout.setText(this.cache.getFont(), (CharSequence)this.text, Color.WHITE, width, 8, true);
/*     */     } else {
/* 142 */       prefSizeLayout.setText(this.cache.getFont(), (CharSequence)this.text);
/* 143 */     }  this.prefSize.set(prefSizeLayout.width, prefSizeLayout.height);
/*     */   }
/*     */   public void layout() {
/*     */     float textWidth, textHeight;
/* 147 */     BitmapFont font = this.cache.getFont();
/* 148 */     float oldScaleX = font.getScaleX();
/* 149 */     float oldScaleY = font.getScaleY();
/* 150 */     if (this.fontScaleX != 1.0F || this.fontScaleY != 1.0F) font.getData().setScale(this.fontScaleX, this.fontScaleY);
/*     */     
/* 152 */     boolean wrap = (this.wrap && this.ellipsis == null);
/* 153 */     if (wrap) {
/* 154 */       float prefHeight = getPrefHeight();
/* 155 */       if (prefHeight != this.lastPrefHeight) {
/* 156 */         this.lastPrefHeight = prefHeight;
/* 157 */         invalidateHierarchy();
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     float width = getWidth(), height = getHeight();
/* 162 */     Drawable background = this.style.background;
/* 163 */     float x = 0.0F, y = 0.0F;
/* 164 */     if (background != null) {
/* 165 */       x = background.getLeftWidth();
/* 166 */       y = background.getBottomHeight();
/* 167 */       width -= background.getLeftWidth() + background.getRightWidth();
/* 168 */       height -= background.getBottomHeight() + background.getTopHeight();
/*     */     } 
/*     */     
/* 171 */     GlyphLayout layout = this.layout;
/*     */     
/* 173 */     if (wrap || this.text.indexOf("\n") != -1) {
/*     */       
/* 175 */       layout.setText(font, (CharSequence)this.text, 0, this.text.length, Color.WHITE, width, this.lineAlign, wrap, this.ellipsis);
/* 176 */       textWidth = layout.width;
/* 177 */       textHeight = layout.height;
/*     */       
/* 179 */       if ((this.labelAlign & 0x8) == 0)
/* 180 */         if ((this.labelAlign & 0x10) != 0) {
/* 181 */           x += width - textWidth;
/*     */         } else {
/* 183 */           x += (width - textWidth) / 2.0F;
/*     */         }  
/*     */     } else {
/* 186 */       textWidth = width;
/* 187 */       textHeight = (font.getData()).capHeight;
/*     */     } 
/*     */     
/* 190 */     if ((this.labelAlign & 0x2) != 0) {
/* 191 */       y += this.cache.getFont().isFlipped() ? 0.0F : (height - textHeight);
/* 192 */       y += this.style.font.getDescent();
/* 193 */     } else if ((this.labelAlign & 0x4) != 0) {
/* 194 */       y += this.cache.getFont().isFlipped() ? (height - textHeight) : 0.0F;
/* 195 */       y -= this.style.font.getDescent();
/*     */     } else {
/* 197 */       y += (height - textHeight) / 2.0F;
/*     */     } 
/* 199 */     if (!this.cache.getFont().isFlipped()) y += textHeight;
/*     */     
/* 201 */     layout.setText(font, (CharSequence)this.text, 0, this.text.length, Color.WHITE, textWidth, this.lineAlign, wrap, this.ellipsis);
/* 202 */     this.cache.setText(layout, x, y);
/*     */     
/* 204 */     if (this.fontScaleX != 1.0F || this.fontScaleY != 1.0F) font.getData().setScale(oldScaleX, oldScaleY); 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 208 */     validate();
/* 209 */     Color color = tempColor.set(getColor());
/* 210 */     color.a *= parentAlpha;
/* 211 */     if (this.style.background != null) {
/* 212 */       batch.setColor(color.r, color.g, color.b, color.a);
/* 213 */       this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
/*     */     } 
/* 215 */     if (this.style.fontColor != null) color.mul(this.style.fontColor); 
/* 216 */     this.cache.tint(color);
/* 217 */     this.cache.setPosition(getX(), getY());
/* 218 */     this.cache.draw(batch);
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 222 */     if (this.wrap) return 0.0F; 
/* 223 */     if (this.prefSizeInvalid) scaleAndComputePrefSize(); 
/* 224 */     float width = this.prefSize.x;
/* 225 */     Drawable background = this.style.background;
/* 226 */     if (background != null) width += background.getLeftWidth() + background.getRightWidth(); 
/* 227 */     return width;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 231 */     if (this.prefSizeInvalid) scaleAndComputePrefSize(); 
/* 232 */     float height = this.prefSize.y - this.style.font.getDescent() * this.fontScaleY * 2.0F;
/* 233 */     Drawable background = this.style.background;
/* 234 */     if (background != null) height += background.getTopHeight() + background.getBottomHeight(); 
/* 235 */     return height;
/*     */   }
/*     */   
/*     */   public GlyphLayout getGlyphLayout() {
/* 239 */     return this.layout;
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
/*     */   public void setWrap(boolean wrap) {
/* 251 */     this.wrap = wrap;
/* 252 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public int getLabelAlign() {
/* 256 */     return this.labelAlign;
/*     */   }
/*     */   
/*     */   public int getLineAlign() {
/* 260 */     return this.lineAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int alignment) {
/* 267 */     setAlignment(alignment, alignment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int labelAlign, int lineAlign) {
/* 274 */     this.labelAlign = labelAlign;
/*     */     
/* 276 */     if ((lineAlign & 0x8) != 0) {
/* 277 */       this.lineAlign = 8;
/* 278 */     } else if ((lineAlign & 0x10) != 0) {
/* 279 */       this.lineAlign = 16;
/*     */     } else {
/* 281 */       this.lineAlign = 1;
/*     */     } 
/* 283 */     invalidate();
/*     */   }
/*     */   
/*     */   public void setFontScale(float fontScale) {
/* 287 */     this.fontScaleX = fontScale;
/* 288 */     this.fontScaleY = fontScale;
/* 289 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public void setFontScale(float fontScaleX, float fontScaleY) {
/* 293 */     this.fontScaleX = fontScaleX;
/* 294 */     this.fontScaleY = fontScaleY;
/* 295 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public float getFontScaleX() {
/* 299 */     return this.fontScaleX;
/*     */   }
/*     */   
/*     */   public void setFontScaleX(float fontScaleX) {
/* 303 */     this.fontScaleX = fontScaleX;
/* 304 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public float getFontScaleY() {
/* 308 */     return this.fontScaleY;
/*     */   }
/*     */   
/*     */   public void setFontScaleY(float fontScaleY) {
/* 312 */     this.fontScaleY = fontScaleY;
/* 313 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEllipsis(String ellipsis) {
/* 319 */     this.ellipsis = ellipsis;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEllipsis(boolean ellipsis) {
/* 325 */     if (ellipsis) {
/* 326 */       this.ellipsis = "...";
/*     */     } else {
/* 328 */       this.ellipsis = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BitmapFontCache getBitmapFontCache() {
/* 333 */     return this.cache;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 337 */     return super.toString() + ": " + this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class LabelStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/*     */     public Color fontColor;
/*     */     
/*     */     public Drawable background;
/*     */ 
/*     */     
/*     */     public LabelStyle() {}
/*     */     
/*     */     public LabelStyle(BitmapFont font, Color fontColor) {
/* 353 */       this.font = font;
/* 354 */       this.fontColor = fontColor;
/*     */     }
/*     */     
/*     */     public LabelStyle(LabelStyle style) {
/* 358 */       this.font = style.font;
/* 359 */       if (style.fontColor != null) this.fontColor = new Color(style.fontColor); 
/* 360 */       this.background = style.background;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\ui\Label.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */