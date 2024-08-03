package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;

interface Context {
  boolean isCurrent() throws LWJGLException;
  
  void makeCurrent() throws LWJGLException;
  
  void releaseCurrent() throws LWJGLException;
  
  void releaseDrawable() throws LWJGLException;
}


/* Location:              C:\Users\egorh\Downloads\Alive In Shelter.jar!\org\lwjgl\opengl\Context.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */