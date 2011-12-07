package com.github.pmcompany.pustomario.io;

import com.github.pmcompany.pustomario.io.InputHandler;

/**
 * @author dector (dector9@gmail.com)
 */
public interface InputServer {
    public void addInputHandler(InputHandler handler);
    public void removeInputHandler(InputHandler handler);
    public void checkInput();
}
