/*    */ package org.lwjgl.opencl;
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
/*    */ public class OpenCLException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public OpenCLException() {}
/*    */   
/*    */   public OpenCLException(String message) {
/* 43 */     super(message);
/*    */   }
/*    */   
/*    */   public OpenCLException(String message, Throwable cause) {
/* 47 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public OpenCLException(Throwable cause) {
/* 51 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\org\lwjgl\opencl\OpenCLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */