/*     */ package com.badlogic.gdx.graphics.g3d;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ public class Attributes
/*     */   implements Iterable<Attribute>, Comparator<Attribute>, Comparable<Attributes>
/*     */ {
/*     */   protected long mask;
/*  26 */   protected final Array<Attribute> attributes = new Array();
/*     */   
/*     */   protected boolean sorted = true;
/*     */ 
/*     */   
/*     */   public final void sort() {
/*  32 */     if (!this.sorted) {
/*  33 */       this.attributes.sort(this);
/*  34 */       this.sorted = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getMask() {
/*  40 */     return this.mask;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Attribute get(long type) {
/*  46 */     if (has(type)) for (int i = 0; i < this.attributes.size; i++) {
/*  47 */         if (((Attribute)this.attributes.get(i)).type == type) return (Attribute)this.attributes.get(i); 
/*  48 */       }   return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T extends Attribute> T get(Class<T> clazz, long type) {
/*  54 */     return (T)get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Array<Attribute> get(Array<Attribute> out, long type) {
/*  60 */     for (int i = 0; i < this.attributes.size; i++) {
/*  61 */       if ((((Attribute)this.attributes.get(i)).type & type) != 0L) out.add(this.attributes.get(i)); 
/*  62 */     }  return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  67 */     this.mask = 0L;
/*  68 */     this.attributes.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  73 */     return this.attributes.size;
/*     */   }
/*     */   
/*     */   private final void enable(long mask) {
/*  77 */     this.mask |= mask;
/*     */   }
/*     */   
/*     */   private final void disable(long mask) {
/*  81 */     this.mask &= mask ^ 0xFFFFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute) {
/*  86 */     int idx = indexOf(attribute.type);
/*  87 */     if (idx < 0) {
/*  88 */       enable(attribute.type);
/*  89 */       this.attributes.add(attribute);
/*  90 */       this.sorted = false;
/*     */     } else {
/*  92 */       this.attributes.set(idx, attribute);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2) {
/*  98 */     set(attribute1);
/*  99 */     set(attribute2);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2, Attribute attribute3) {
/* 104 */     set(attribute1);
/* 105 */     set(attribute2);
/* 106 */     set(attribute3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(Attribute attribute1, Attribute attribute2, Attribute attribute3, Attribute attribute4) {
/* 112 */     set(attribute1);
/* 113 */     set(attribute2);
/* 114 */     set(attribute3);
/* 115 */     set(attribute4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(Attribute... attributes) {
/* 121 */     for (Attribute attr : attributes) {
/* 122 */       set(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(Iterable<Attribute> attributes) {
/* 128 */     for (Attribute attr : attributes) {
/* 129 */       set(attr);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void remove(long mask) {
/* 135 */     for (int i = this.attributes.size - 1; i >= 0; i--) {
/* 136 */       long type = ((Attribute)this.attributes.get(i)).type;
/* 137 */       if ((mask & type) == type) {
/* 138 */         this.attributes.removeIndex(i);
/* 139 */         disable(type);
/* 140 */         this.sorted = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean has(long type) {
/* 149 */     return (type != 0L && (this.mask & type) == type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int indexOf(long type) {
/* 154 */     if (has(type)) for (int i = 0; i < this.attributes.size; i++) {
/* 155 */         if (((Attribute)this.attributes.get(i)).type == type) return i; 
/* 156 */       }   return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean same(Attributes other, boolean compareValues) {
/* 164 */     if (other == this) return true; 
/* 165 */     if (other == null || this.mask != other.mask) return false; 
/* 166 */     if (!compareValues) return true; 
/* 167 */     sort();
/* 168 */     other.sort();
/* 169 */     for (int i = 0; i < this.attributes.size; i++) {
/* 170 */       if (!((Attribute)this.attributes.get(i)).equals((Attribute)other.attributes.get(i))) return false; 
/* 171 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean same(Attributes other) {
/* 177 */     return same(other, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int compare(Attribute arg0, Attribute arg1) {
/* 183 */     return (int)(arg0.type - arg1.type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<Attribute> iterator() {
/* 189 */     return this.attributes.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int attributesHash() {
/* 195 */     sort();
/* 196 */     int n = this.attributes.size;
/* 197 */     long result = 71L + this.mask;
/* 198 */     int m = 1;
/* 199 */     for (int i = 0; i < n; i++)
/* 200 */       result += this.mask * ((Attribute)this.attributes.get(i)).hashCode() * (m = m * 7 & 0xFFFF); 
/* 201 */     return (int)(result ^ result >> 32L);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 206 */     return attributesHash();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 211 */     if (!(other instanceof Attributes)) return false; 
/* 212 */     if (other == this) return true; 
/* 213 */     return same((Attributes)other, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Attributes other) {
/* 218 */     if (other == this)
/* 219 */       return 0; 
/* 220 */     if (this.mask != other.mask)
/* 221 */       return (this.mask < other.mask) ? -1 : 1; 
/* 222 */     sort();
/* 223 */     other.sort();
/* 224 */     for (int i = 0; i < this.attributes.size; i++) {
/* 225 */       int c = ((Attribute)this.attributes.get(i)).compareTo((Attribute)other.attributes.get(i));
/* 226 */       if (c != 0)
/* 227 */         return c; 
/*     */     } 
/* 229 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\graphics\g3d\Attributes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */