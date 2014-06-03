package com.fujitsu.us.oovn;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface EventHandler
{
    void handleEvent(SelectionKey key) throws IOException;
}