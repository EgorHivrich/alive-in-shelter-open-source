/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.OrderedSet;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Selection<T>
/*     */   implements Disableable, Iterable<T>
/*     */ {
/*     */   private Actor actor;
/*  17 */   final OrderedSet<T> selected = new OrderedSet();
/*  18 */   private final OrderedSet<T> old = new OrderedSet();
/*     */   
/*     */   boolean isDisabled;
/*     */   private boolean toggle;
/*     */   boolean multiple;
/*     */   boolean required;
/*     */   private boolean programmaticChangeEvents = true;
/*     */   T lastSelected;
/*     */   
/*     */   public void setActor(Actor actor) {
/*  28 */     this.actor = actor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void choose(T item) {
/*  34 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/*  35 */     if (this.isDisabled)
/*  36 */       return;  snapshot();
/*     */     try {
/*  38 */       if ((this.toggle || (!this.required && this.selected.size == 1) || UIUtils.ctrl()) && this.selected.contains(item)) {
/*  39 */         if (this.required && this.selected.size == 1)
/*  40 */           return;  this.selected.remove(item);
/*  41 */         this.lastSelected = null;
/*     */       } else {
/*  43 */         boolean modified = false;
/*  44 */         if (!this.multiple || (!this.toggle && !UIUtils.ctrl())) {
/*  45 */           if (this.selected.size == 1 && this.selected.contains(item))
/*  46 */             return;  modified = (this.selected.size > 0);
/*  47 */           this.selected.clear();
/*     */         } 
/*  49 */         if (!this.selected.add(item) && !modified)
/*  50 */           return;  this.lastSelected = item;
/*     */       } 
/*  52 */       if (fireChangeEvent()) revert(); 
/*     */     } finally {
/*  54 */       cleanup();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasItems() {
/*  59 */     return (this.selected.size > 0);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  63 */     return (this.selected.size == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/*  67 */     return this.selected.size;
/*     */   }
/*     */   
/*     */   public OrderedSet<T> items() {
/*  71 */     return this.selected;
/*     */   }
/*     */ 
/*     */   
/*     */   public T first() {
/*  76 */     return (this.selected.size == 0) ? null : (T)this.selected.first();
/*     */   }
/*     */   
/*     */   void snapshot() {
/*  80 */     this.old.clear();
/*  81 */     this.old.addAll((ObjectSet)this.selected);
/*     */   }
/*     */   
/*     */   void revert() {
/*  85 */     this.selected.clear();
/*  86 */     this.selected.addAll((ObjectSet)this.old);
/*     */   }
/*     */   
/*     */   void cleanup() {
/*  90 */     this.old.clear(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(T item) {
/*  95 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/*  96 */     if (this.selected.size == 1 && this.selected.first() == item)
/*  97 */       return;  snapshot();
/*  98 */     this.selected.clear();
/*  99 */     this.selected.add(item);
/* 100 */     if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 101 */       revert();
/*     */     } else {
/* 103 */       this.lastSelected = item;
/* 104 */     }  cleanup();
/*     */   }
/*     */   
/*     */   public void setAll(Array<T> items) {
/* 108 */     boolean added = false;
/* 109 */     snapshot();
/* 110 */     this.selected.clear();
/* 111 */     for (int i = 0, n = items.size; i < n; i++) {
/* 112 */       T item = (T)items.get(i);
/* 113 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 114 */       if (this.selected.add(item)) added = true; 
/*     */     } 
/* 116 */     if (added && this.programmaticChangeEvents && fireChangeEvent()) {
/* 117 */       revert();
/*     */     } else {
/* 119 */       this.lastSelected = (T)items.peek();
/* 120 */     }  cleanup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(T item) {
/* 125 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 126 */     if (!this.selected.add(item))
/* 127 */       return;  if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 128 */       this.selected.remove(item);
/*     */     } else {
/* 130 */       this.lastSelected = item;
/*     */     } 
/*     */   }
/*     */   public void addAll(Array<T> items) {
/* 134 */     boolean added = false;
/* 135 */     snapshot();
/* 136 */     for (int i = 0, n = items.size; i < n; i++) {
/* 137 */       T item = (T)items.get(i);
/* 138 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 139 */       if (this.selected.add(item)) added = true; 
/*     */     } 
/* 141 */     if (added && this.programmaticChangeEvents && fireChangeEvent()) {
/* 142 */       revert();
/*     */     } else {
/* 144 */       this.lastSelected = (T)items.peek();
/* 145 */     }  cleanup();
/*     */   }
/*     */   
/*     */   public void remove(T item) {
/* 149 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 150 */     if (!this.selected.remove(item))
/* 151 */       return;  if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 152 */       this.selected.add(item);
/*     */     } else {
/* 154 */       this.lastSelected = null;
/*     */     } 
/*     */   }
/*     */   public void removeAll(Array<T> items) {
/* 158 */     boolean removed = false;
/* 159 */     snapshot();
/* 160 */     for (int i = 0, n = items.size; i < n; i++) {
/* 161 */       T item = (T)items.get(i);
/* 162 */       if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 163 */       if (this.selected.remove(item)) removed = true; 
/*     */     } 
/* 165 */     if (removed && this.programmaticChangeEvents && fireChangeEvent()) {
/* 166 */       revert();
/*     */     } else {
/* 168 */       this.lastSelected = null;
/* 169 */     }  cleanup();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 173 */     if (this.selected.size == 0)
/* 174 */       return;  snapshot();
/* 175 */     this.selected.clear();
/* 176 */     if (this.programmaticChangeEvents && fireChangeEvent()) {
/* 177 */       revert();
/*     */     } else {
/* 179 */       this.lastSelected = null;
/* 180 */     }  cleanup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fireChangeEvent() {
/* 187 */     if (this.actor == null) return false; 
/* 188 */     ChangeListener.ChangeEvent changeEvent = (ChangeListener.ChangeEvent)Pools.obtain(ChangeListener.ChangeEvent.class);
/*     */     try {
/* 190 */       return this.actor.fire(changeEvent);
/*     */     } finally {
/* 192 */       Pools.free(changeEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(T item) {
/* 197 */     if (item == null) return false; 
/* 198 */     return this.selected.contains(item);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getLastSelected() {
/* 203 */     if (this.lastSelected != null)
/* 204 */       return this.lastSelected; 
/* 205 */     if (this.selected.size > 0) {
/* 206 */       return (T)this.selected.first();
/*     */     }
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 212 */     return (Iterator<T>)this.selected.iterator();
/*     */   }
/*     */   
/*     */   public Array<T> toArray() {
/* 216 */     return this.selected.iterator().toArray();
/*     */   }
/*     */   
/*     */   public Array<T> toArray(Array<T> array) {
/* 220 */     return this.selected.iterator().toArray(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean isDisabled) {
/* 225 */     this.isDisabled = isDisabled;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/* 229 */     return this.isDisabled;
/*     */   }
/*     */   
/*     */   public boolean getToggle() {
/* 233 */     return this.toggle;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setToggle(boolean toggle) {
/* 238 */     this.toggle = toggle;
/*     */   }
/*     */   
/*     */   public boolean getMultiple() {
/* 242 */     return this.multiple;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMultiple(boolean multiple) {
/* 247 */     this.multiple = multiple;
/*     */   }
/*     */   
/*     */   public boolean getRequired() {
/* 251 */     return this.required;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequired(boolean required) {
/* 256 */     this.required = required;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
/* 261 */     this.programmaticChangeEvents = programmaticChangeEvents;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 265 */     return this.selected.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\utils\Selection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */