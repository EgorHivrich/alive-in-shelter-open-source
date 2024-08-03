/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Colors;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.FloatArray;
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
/*     */ public class GlyphLayout
/*     */   implements Pool.Poolable
/*     */ {
/*  35 */   public final Array<GlyphRun> runs = new Array();
/*     */   public float width;
/*     */   public float height;
/*  38 */   private final Array<Color> colorStack = new Array(4);
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout() {}
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str) {
/*  46 */     setText(font, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
/*  51 */     setText(font, str, color, targetWidth, halign, wrap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str, int start, int end, Color color, float targetWidth, int halign, boolean wrap, String truncate) {
/*  57 */     setText(font, str, start, end, color, targetWidth, halign, wrap, truncate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(BitmapFont font, CharSequence str) {
/*  63 */     setText(font, str, 0, str.length(), font.getColor(), 0.0F, 8, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
/*  69 */     setText(font, str, 0, str.length(), color, targetWidth, halign, wrap, null);
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
/*     */   public void setText(BitmapFont font, CharSequence str, int start, int end, Color color, float targetWidth, int halign, boolean wrap, String truncate) {
/*  82 */     if (truncate != null) {
/*  83 */       wrap = true;
/*  84 */     } else if (targetWidth <= font.data.spaceWidth) {
/*  85 */       wrap = false;
/*     */     } 
/*  87 */     BitmapFont.BitmapFontData fontData = font.data;
/*  88 */     boolean markupEnabled = fontData.markupEnabled;
/*     */     
/*  90 */     Pool<GlyphRun> glyphRunPool = Pools.get(GlyphRun.class);
/*  91 */     Array<GlyphRun> runs = this.runs;
/*  92 */     glyphRunPool.freeAll(runs);
/*  93 */     runs.clear();
/*     */     
/*  95 */     float x = 0.0F, y = 0.0F, width = 0.0F;
/*  96 */     int lines = 0;
/*     */     
/*  98 */     Array<Color> colorStack = this.colorStack;
/*  99 */     Color nextColor = color;
/* 100 */     colorStack.add(color);
/* 101 */     Pool<Color> colorPool = Pools.get(Color.class);
/*     */     
/* 103 */     int runStart = start;
/*     */ 
/*     */     
/*     */     label92: while (true) {
/* 107 */       int runEnd = -1;
/* 108 */       boolean newline = false, colorRun = false;
/* 109 */       if (start == end) {
/* 110 */         if (runStart == end)
/* 111 */           break;  runEnd = end;
/*     */       } else {
/* 113 */         switch (str.charAt(start++)) {
/*     */           
/*     */           case '\n':
/* 116 */             runEnd = start - 1;
/* 117 */             newline = true;
/*     */             break;
/*     */           
/*     */           case '[':
/* 121 */             if (markupEnabled) {
/* 122 */               int length = parseColorMarkup(str, start, end, colorPool);
/* 123 */               if (length >= 0) {
/* 124 */                 runEnd = start - 1;
/* 125 */                 start += length + 1;
/* 126 */                 nextColor = (Color)colorStack.peek();
/* 127 */                 colorRun = true;
/*     */               } 
/*     */             } 
/*     */             break;
/*     */         } 
/*     */       
/*     */       } 
/* 134 */       if (runEnd != -1) {
/* 135 */         if (runEnd != runStart) {
/*     */           
/* 137 */           GlyphRun run = (GlyphRun)glyphRunPool.obtain();
/* 138 */           run.color.set(color);
/* 139 */           run.x = x;
/* 140 */           run.y = y;
/* 141 */           fontData.getGlyphs(run, str, runStart, runEnd, colorRun);
/* 142 */           if (run.glyphs.size == 0) {
/* 143 */             glyphRunPool.free(run);
/*     */           } else {
/* 145 */             runs.add(run);
/*     */ 
/*     */             
/* 148 */             float[] xAdvances = run.xAdvances.items;
/* 149 */             for (int j = 0, k = run.xAdvances.size; j < k; j++) {
/* 150 */               float xAdvance = xAdvances[j];
/* 151 */               x += xAdvance;
/*     */ 
/*     */               
/* 154 */               if (wrap && x > targetWidth && j > 1 && x - xAdvance + (((BitmapFont.Glyph)run.glyphs
/* 155 */                 .get(j - 1)).xoffset + ((BitmapFont.Glyph)run.glyphs.get(j - 1)).width) * fontData.scaleX - 1.0E-4F > targetWidth) {
/*     */                 GlyphRun next;
/*     */                 
/* 158 */                 if (truncate != null) {
/* 159 */                   truncate(fontData, run, targetWidth, truncate, j, glyphRunPool);
/* 160 */                   x = run.x + run.width;
/*     */                   
/*     */                   break label92;
/*     */                 } 
/* 164 */                 int wrapIndex = fontData.getWrapIndex(run.glyphs, j);
/* 165 */                 if ((run.x == 0.0F && wrapIndex == 0) || wrapIndex >= run.glyphs.size)
/*     */                 {
/* 167 */                   wrapIndex = j - 1;
/*     */                 }
/*     */                 
/* 170 */                 if (wrapIndex == 0) {
/* 171 */                   next = run;
/*     */                 } else {
/* 173 */                   next = wrap(fontData, run, glyphRunPool, wrapIndex, j);
/* 174 */                   runs.add(next);
/*     */                 } 
/*     */ 
/*     */                 
/* 178 */                 width = Math.max(width, run.x + run.width);
/* 179 */                 x = 0.0F;
/* 180 */                 y += fontData.down;
/* 181 */                 lines++;
/* 182 */                 next.x = 0.0F;
/* 183 */                 next.y = y;
/* 184 */                 j = -1;
/* 185 */                 k = next.xAdvances.size;
/* 186 */                 xAdvances = next.xAdvances.items;
/* 187 */                 run = next;
/*     */               } else {
/* 189 */                 run.width += xAdvance;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 194 */         if (newline) {
/*     */           
/* 196 */           width = Math.max(width, x);
/* 197 */           x = 0.0F;
/* 198 */           y += fontData.down;
/* 199 */           lines++;
/*     */         } 
/*     */         
/* 202 */         runStart = start;
/* 203 */         color = nextColor;
/*     */       } 
/*     */     } 
/* 206 */     width = Math.max(width, x);
/*     */     
/* 208 */     for (int i = 1, n = colorStack.size; i < n; i++)
/* 209 */       colorPool.free(colorStack.get(i)); 
/* 210 */     colorStack.clear();
/*     */ 
/*     */     
/* 213 */     if ((halign & 0x8) == 0) {
/* 214 */       boolean center = ((halign & 0x1) != 0);
/* 215 */       float lineWidth = 0.0F, lineY = -2.14748365E9F;
/* 216 */       int lineStart = 0, j = runs.size;
/* 217 */       for (int k = 0; k < j; k++) {
/* 218 */         GlyphRun run = (GlyphRun)runs.get(k);
/* 219 */         if (run.y != lineY) {
/* 220 */           lineY = run.y;
/* 221 */           float f = targetWidth - lineWidth;
/* 222 */           if (center) f /= 2.0F; 
/* 223 */           while (lineStart < k)
/* 224 */             ((GlyphRun)runs.get(lineStart++)).x += f; 
/* 225 */           lineWidth = 0.0F;
/*     */         } 
/* 227 */         lineWidth += run.width;
/*     */       } 
/* 229 */       float shift = targetWidth - lineWidth;
/* 230 */       if (center) shift /= 2.0F; 
/* 231 */       while (lineStart < j) {
/* 232 */         ((GlyphRun)runs.get(lineStart++)).x += shift;
/*     */       }
/*     */     } 
/* 235 */     this.width = width;
/* 236 */     this.height = fontData.capHeight + lines * fontData.lineHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void truncate(BitmapFont.BitmapFontData fontData, GlyphRun run, float targetWidth, String truncate, int widthIndex, Pool<GlyphRun> glyphRunPool) {
/* 243 */     GlyphRun truncateRun = (GlyphRun)glyphRunPool.obtain();
/* 244 */     fontData.getGlyphs(truncateRun, truncate, 0, truncate.length(), true);
/* 245 */     float truncateWidth = 0.0F;
/* 246 */     for (int i = 1, n = truncateRun.xAdvances.size; i < n; i++)
/* 247 */       truncateWidth += truncateRun.xAdvances.get(i); 
/* 248 */     targetWidth -= truncateWidth;
/*     */ 
/*     */     
/* 251 */     int count = 0;
/* 252 */     float width = run.x;
/* 253 */     while (count < run.xAdvances.size) {
/* 254 */       float xAdvance = run.xAdvances.get(count);
/* 255 */       width += xAdvance;
/* 256 */       if (width > targetWidth) {
/* 257 */         run.width = width - run.x - xAdvance;
/*     */         break;
/*     */       } 
/* 260 */       count++;
/*     */     } 
/*     */     
/* 263 */     if (count > 1) {
/*     */       
/* 265 */       run.glyphs.truncate(count - 1);
/* 266 */       run.xAdvances.truncate(count);
/* 267 */       adjustLastGlyph(fontData, run);
/* 268 */       if (truncateRun.xAdvances.size > 0) run.xAdvances.addAll(truncateRun.xAdvances, 1, truncateRun.xAdvances.size - 1);
/*     */     
/*     */     } else {
/* 271 */       run.glyphs.clear();
/* 272 */       run.xAdvances.clear();
/* 273 */       run.xAdvances.addAll(truncateRun.xAdvances);
/* 274 */       if (truncateRun.xAdvances.size > 0) run.width += truncateRun.xAdvances.get(0); 
/*     */     } 
/* 276 */     run.glyphs.addAll(truncateRun.glyphs);
/* 277 */     run.width += truncateWidth;
/*     */     
/* 279 */     glyphRunPool.free(truncateRun);
/*     */   }
/*     */   
/*     */   private GlyphRun wrap(BitmapFont.BitmapFontData fontData, GlyphRun first, Pool<GlyphRun> glyphRunPool, int wrapIndex, int widthIndex) {
/* 283 */     GlyphRun second = (GlyphRun)glyphRunPool.obtain();
/* 284 */     second.color.set(first.color);
/* 285 */     int glyphCount = first.glyphs.size;
/*     */ 
/*     */     
/* 288 */     while (widthIndex < wrapIndex) {
/* 289 */       first.width += first.xAdvances.get(widthIndex++);
/*     */     }
/*     */     
/* 292 */     while (widthIndex > wrapIndex + 1) {
/* 293 */       first.width -= first.xAdvances.get(--widthIndex);
/*     */     }
/*     */ 
/*     */     
/* 297 */     if (wrapIndex < glyphCount) {
/* 298 */       Array<BitmapFont.Glyph> glyphs1 = second.glyphs;
/* 299 */       Array<BitmapFont.Glyph> glyphs2 = first.glyphs;
/* 300 */       glyphs1.addAll(glyphs2, 0, wrapIndex);
/* 301 */       glyphs2.removeRange(0, wrapIndex - 1);
/* 302 */       first.glyphs = glyphs1;
/* 303 */       second.glyphs = glyphs2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 308 */       FloatArray xAdvances1 = second.xAdvances;
/* 309 */       FloatArray xAdvances2 = first.xAdvances;
/* 310 */       xAdvances1.addAll(xAdvances2, 0, wrapIndex + 1);
/* 311 */       xAdvances2.removeRange(1, wrapIndex);
/* 312 */       xAdvances2.set(0, -((BitmapFont.Glyph)glyphs2.first()).xoffset * fontData.scaleX - fontData.padLeft);
/* 313 */       first.xAdvances = xAdvances1;
/* 314 */       second.xAdvances = xAdvances2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     if (wrapIndex == 0) {
/*     */       
/* 323 */       glyphRunPool.free(first);
/* 324 */       this.runs.pop();
/*     */     } else {
/* 326 */       adjustLastGlyph(fontData, first);
/*     */     } 
/* 328 */     return second;
/*     */   }
/*     */ 
/*     */   
/*     */   private void adjustLastGlyph(BitmapFont.BitmapFontData fontData, GlyphRun run) {
/* 333 */     BitmapFont.Glyph last = (BitmapFont.Glyph)run.glyphs.peek();
/* 334 */     if (fontData.isWhitespace((char)last.id))
/* 335 */       return;  float width = (last.xoffset + last.width) * fontData.scaleX - fontData.padRight;
/* 336 */     run.width += width - run.xAdvances.peek();
/* 337 */     run.xAdvances.set(run.xAdvances.size - 1, width);
/*     */   }
/*     */   private int parseColorMarkup(CharSequence str, int start, int end, Pool<Color> colorPool) {
/*     */     int colorInt;
/* 341 */     if (start == end) return -1; 
/* 342 */     switch (str.charAt(start)) {
/*     */       
/*     */       case '#':
/* 345 */         colorInt = 0;
/* 346 */         for (i = start + 1; i < end; i++) {
/* 347 */           char ch = str.charAt(i);
/* 348 */           if (ch == ']') {
/* 349 */             if (i < start + 2 || i > start + 9)
/* 350 */               break;  if (i - start <= 7) {
/* 351 */               for (int ii = 0, nn = 9 - i - start; ii < nn; ii++)
/* 352 */                 colorInt <<= 4; 
/* 353 */               colorInt |= 0xFF;
/*     */             } 
/* 355 */             Color color = (Color)colorPool.obtain();
/* 356 */             this.colorStack.add(color);
/* 357 */             Color.rgba8888ToColor(color, colorInt);
/* 358 */             return i - start;
/*     */           } 
/* 360 */           if (ch >= '0' && ch <= '9') {
/* 361 */             colorInt = colorInt * 16 + ch - 48;
/* 362 */           } else if (ch >= 'a' && ch <= 'f') {
/* 363 */             colorInt = colorInt * 16 + ch - 87;
/* 364 */           } else if (ch >= 'A' && ch <= 'F') {
/* 365 */             colorInt = colorInt * 16 + ch - 55;
/*     */           } else {
/*     */             break;
/*     */           } 
/* 369 */         }  return -1;
/*     */       case '[':
/* 371 */         return -1;
/*     */       case ']':
/* 373 */         if (this.colorStack.size > 1) colorPool.free(this.colorStack.pop()); 
/* 374 */         return 0;
/*     */     } 
/*     */     
/* 377 */     int colorStart = start;
/* 378 */     for (int i = start + 1; i < end; ) {
/* 379 */       char ch = str.charAt(i);
/* 380 */       if (ch != ']') { i++; continue; }
/* 381 */        Color namedColor = Colors.get(str.subSequence(colorStart, i).toString());
/* 382 */       if (namedColor == null) return -1; 
/* 383 */       Color color = (Color)colorPool.obtain();
/* 384 */       this.colorStack.add(color);
/* 385 */       color.set(namedColor);
/* 386 */       return i - start;
/*     */     } 
/* 388 */     return -1;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 392 */     Pools.get(GlyphRun.class).freeAll(this.runs);
/* 393 */     this.runs.clear();
/*     */     
/* 395 */     this.width = 0.0F;
/* 396 */     this.height = 0.0F;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 400 */     if (this.runs.size == 0) return ""; 
/* 401 */     StringBuilder buffer = new StringBuilder(128);
/* 402 */     buffer.append(this.width);
/* 403 */     buffer.append('x');
/* 404 */     buffer.append(this.height);
/* 405 */     buffer.append('\n');
/* 406 */     for (int i = 0, n = this.runs.size; i < n; i++) {
/* 407 */       buffer.append(((GlyphRun)this.runs.get(i)).toString());
/* 408 */       buffer.append('\n');
/*     */     } 
/* 410 */     buffer.setLength(buffer.length() - 1);
/* 411 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public static class GlyphRun
/*     */     implements Pool.Poolable
/*     */   {
/* 417 */     public Array<BitmapFont.Glyph> glyphs = new Array();
/*     */ 
/*     */     
/* 420 */     public FloatArray xAdvances = new FloatArray(); public float x; public float y;
/*     */     public float width;
/* 422 */     public final Color color = new Color();
/*     */     
/*     */     public void reset() {
/* 425 */       this.glyphs.clear();
/* 426 */       this.xAdvances.clear();
/* 427 */       this.width = 0.0F;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 431 */       StringBuilder buffer = new StringBuilder(this.glyphs.size);
/* 432 */       Array<BitmapFont.Glyph> glyphs = this.glyphs;
/* 433 */       for (int i = 0, n = glyphs.size; i < n; i++) {
/* 434 */         BitmapFont.Glyph g = (BitmapFont.Glyph)glyphs.get(i);
/* 435 */         buffer.append((char)g.id);
/*     */       } 
/* 437 */       buffer.append(", #");
/* 438 */       buffer.append(this.color);
/* 439 */       buffer.append(", ");
/* 440 */       buffer.append(this.x);
/* 441 */       buffer.append(", ");
/* 442 */       buffer.append(this.y);
/* 443 */       buffer.append(", ");
/* 444 */       buffer.append(this.width);
/* 445 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g2d\GlyphLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */