package org.pollub.catalog.command;

//start L3 Command
public interface Command<T> {
    T execute();
}
//end L3 Command