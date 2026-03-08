package org.pollub.catalog.command;

//start L5 Command
public interface Command<T> {
    T execute();
}
//end L5 Command