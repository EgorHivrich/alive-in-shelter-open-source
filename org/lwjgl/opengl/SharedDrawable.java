/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.LWJGLException;
/*    */ import org.lwjgl.PointerBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SharedDrawable
/*    */   extends DrawableGL
/*    */ {
/*    */   public SharedDrawable(Drawable drawable) throws LWJGLException {
/* 50 */     this.context = (ContextGL)((DrawableLWJGL)drawable).createSharedContext();
/*    */   }
/*    */   
/*    */   public ContextGL createSharedContext() {
/* 54 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\org\lwjgl\opengl\SharedDrawable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */