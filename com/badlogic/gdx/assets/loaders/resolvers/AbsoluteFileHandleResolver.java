/*    */ package com.badlogic.gdx.assets.loaders.resolvers;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ 
/*    */ public class AbsoluteFileHandleResolver
/*    */   implements FileHandleResolver
/*    */ {
/*    */   public FileHandle resolve(String fileName) {
/* 11 */     return Gdx.files.absolute(fileName);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\assets\loaders\resolvers\AbsoluteFileHandleResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */