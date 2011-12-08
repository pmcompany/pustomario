package com.github.pmcompany.pustomario;

/**
 * @author dector (dector9@gmail.com)
 */
public interface InputServer {
    public void addInputHandler(InputHandler handler);
    public void removeInputHandler(InputHandler handler);
    public void checkInput();
}
