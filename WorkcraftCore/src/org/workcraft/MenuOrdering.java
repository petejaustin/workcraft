package org.workcraft;

public interface MenuOrdering {
    enum Position { TOP, TOP_MIDDLE, MIDDLE, BOTTOM_MIDDLE, BOTTOM };
    int getPriority();
    Position getPosition();
}
