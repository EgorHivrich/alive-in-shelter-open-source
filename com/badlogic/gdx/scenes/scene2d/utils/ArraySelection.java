/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.OrderedSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArraySelection<T>
/*    */   extends Selection<T>
/*    */ {
/*    */   private Array<T> array;
/*    */   private boolean rangeSelect = true;
/*    */   
/*    */   public ArraySelection(Array<T> array) {
/* 17 */     this.array = array;
/*    */   }
/*    */   
/*    */   public void choose(T item) {
/* 21 */     if (item == null) throw new IllegalArgumentException("item cannot be null."); 
/* 22 */     if (this.isDisabled)
/* 23 */       return;  if (this.selected.size > 0 && this.rangeSelect && this.multiple && (Gdx.input
/* 24 */       .isKeyPressed(59) || Gdx.input.isKeyPressed(60))) {
/* 25 */       int low = this.array.indexOf(getLastSelected(), false);
/* 26 */       int high = this.array.indexOf(item, false);
/* 27 */       if (low > high) {
/* 28 */         int temp = low;
/* 29 */         low = high;
/* 30 */         high = temp;
/*    */       } 
/* 32 */       snapshot();
/* 33 */       if (!UIUtils.ctrl()) this.selected.clear(); 
/* 34 */       for (; low <= high; low++)
/* 35 */         this.selected.add(this.array.get(low)); 
/* 36 */       if (fireChangeEvent()) revert(); 
/* 37 */       cleanup();
/*    */       return;
/*    */     } 
/* 40 */     super.choose(item);
/*    */   }
/*    */   
/*    */   public boolean getRangeSelect() {
/* 44 */     return this.rangeSelect;
/*    */   }
/*    */   
/*    */   public void setRangeSelect(boolean rangeSelect) {
/* 48 */     this.rangeSelect = rangeSelect;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate() {
/* 54 */     Array<T> array = this.array;
/* 55 */     if (array.size == 0) {
/* 56 */       clear();
/*    */       return;
/*    */     } 
/* 59 */     for (OrderedSet.OrderedSetIterator<T> orderedSetIterator = items().iterator(); orderedSetIterator.hasNext(); ) {
/* 60 */       T selected = orderedSetIterator.next();
/* 61 */       if (!array.contains(selected, false)) orderedSetIterator.remove(); 
/*    */     } 
/* 63 */     if (this.required && this.selected.size == 0) set((T)array.first()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\scenes\scene2\\utils\ArraySelection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */