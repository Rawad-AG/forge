package dev.forge.nexo.core.phases.templater;

public class StringWriterPlus extends java.io.Writer {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public void write(char[] cbuf, int off, int len) {
        sb.append(cbuf, off, len);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}