/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Clipboard;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.ClipboardOwner;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.awt.datatransfer.Transferable;
/*    */ import java.awt.datatransfer.UnsupportedFlavorException;
/*    */ import java.io.IOException;
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
/*    */ public class LwjglClipboard
/*    */   implements Clipboard, ClipboardOwner
/*    */ {
/*    */   public String getContents() {
/* 34 */     String result = "";
/* 35 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 36 */     Transferable contents = clipboard.getContents(null);
/* 37 */     boolean hasTransferableText = (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor));
/* 38 */     if (hasTransferableText) {
/*    */       try {
/* 40 */         result = (String)contents.getTransferData(DataFlavor.stringFlavor);
/* 41 */       } catch (UnsupportedFlavorException unsupportedFlavorException) {
/*    */       
/* 43 */       } catch (IOException iOException) {}
/*    */     }
/*    */ 
/*    */     
/* 47 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setContents(String content) {
/* 52 */     StringSelection stringSelection = new StringSelection(content);
/* 53 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 54 */     clipboard.setContents(stringSelection, this);
/*    */   }
/*    */   
/*    */   public void lostOwnership(Clipboard arg0, Transferable arg1) {}
/*    */ }


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\com\badlogic\gdx\backends\lwjgl\LwjglClipboard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */