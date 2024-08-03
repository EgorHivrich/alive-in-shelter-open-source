/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GdxNativesLoader
/*    */ {
/*    */   public static boolean disableNativesLoading = false;
/*    */   private static boolean nativesLoaded;
/*    */   
/*    */   public static synchronized void load() {
/* 26 */     if (nativesLoaded)
/* 27 */       return;  nativesLoaded = true;
/*    */     
/* 29 */     if (disableNativesLoading) {
/* 30 */       System.out.println("Native loading is disabled.");
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     (new SharedLibraryLoader()).load("gdx");
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gd\\utils\GdxNativesLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */