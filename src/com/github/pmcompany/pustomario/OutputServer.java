package com.github.pmcompany.pustomario;

/**
 * @author dector (dector9@gmail.com)
 */
public interface OutputServer {
    public void addOutputHandler(OutputHandler handler);
    public void removeOutputHandler(OutputHandler handler);
}
