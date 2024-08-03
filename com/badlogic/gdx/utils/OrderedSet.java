/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class OrderedSet<T>
/*     */   extends ObjectSet<T>
/*     */ {
/*     */   final Array<T> items;
/*     */   OrderedSetIterator iterator1;
/*     */   OrderedSetIterator iterator2;
/*     */   
/*     */   public OrderedSet() {
/*  30 */     this.items = new Array<T>();
/*     */   }
/*     */   
/*     */   public OrderedSet(int initialCapacity, float loadFactor) {
/*  34 */     super(initialCapacity, loadFactor);
/*  35 */     this.items = new Array<T>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedSet(int initialCapacity) {
/*  39 */     super(initialCapacity);
/*  40 */     this.items = new Array<T>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedSet(OrderedSet set) {
/*  44 */     super(set);
/*  45 */     this.items = new Array<T>(this.capacity);
/*  46 */     this.items.addAll(set.items);
/*     */   }
/*     */   
/*     */   public boolean add(T key) {
/*  50 */     if (!contains(key)) this.items.add(key); 
/*  51 */     return super.add(key);
/*     */   }
/*     */   
/*     */   public boolean remove(T key) {
/*  55 */     this.items.removeValue(key, false);
/*  56 */     return super.remove(key);
/*     */   }
/*     */   
/*     */   public void clear(int maximumCapacity) {
/*  60 */     this.items.clear();
/*  61 */     super.clear(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  65 */     this.items.clear();
/*  66 */     super.clear();
/*     */   }
/*     */   
/*     */   public Array<T> orderedItems() {
/*  70 */     return this.items;
/*     */   }
/*     */   
/*     */   public OrderedSetIterator<T> iterator() {
/*  74 */     if (this.iterator1 == null) {
/*  75 */       this.iterator1 = new OrderedSetIterator<T>(this);
/*  76 */       this.iterator2 = new OrderedSetIterator<T>(this);
/*     */     } 
/*  78 */     if (!this.iterator1.valid) {
/*  79 */       this.iterator1.reset();
/*  80 */       this.iterator1.valid = true;
/*  81 */       this.iterator2.valid = false;
/*  82 */       return this.iterator1;
/*     */     } 
/*  84 */     this.iterator2.reset();
/*  85 */     this.iterator2.valid = true;
/*  86 */     this.iterator1.valid = false;
/*  87 */     return this.iterator2;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  91 */     if (this.size == 0) return "{}"; 
/*  92 */     T[] items = this.items.items;
/*  93 */     StringBuilder buffer = new StringBuilder(32);
/*  94 */     buffer.append('{');
/*  95 */     buffer.append(items[0]);
/*  96 */     for (int i = 1; i < this.size; i++) {
/*  97 */       buffer.append(", ");
/*  98 */       buffer.append(items[i]);
/*     */     } 
/* 100 */     buffer.append('}');
/* 101 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 105 */     return this.items.toString(separator);
/*     */   }
/*     */   
/*     */   public static class OrderedSetIterator<T> extends ObjectSet.ObjectSetIterator<T> {
/*     */     private Array<T> items;
/*     */     
/*     */     public OrderedSetIterator(OrderedSet<T> set) {
/* 112 */       super(set);
/* 113 */       this.items = set.items;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 117 */       this.nextIndex = 0;
/* 118 */       this.hasNext = (this.set.size > 0);
/*     */     }
/*     */     
/*     */     public T next() {
/* 122 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 123 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 124 */       T key = this.items.get(this.nextIndex);
/* 125 */       this.nextIndex++;
/* 126 */       this.hasNext = (this.nextIndex < this.set.size);
/* 127 */       return key;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 131 */       if (this.nextIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 132 */       this.nextIndex--;
/* 133 */       this.set.remove(this.items.get(this.nextIndex));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gd\\utils\OrderedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */