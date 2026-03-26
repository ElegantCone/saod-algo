package org.example.lab2.figure;

import org.example.lab2.Line;

public interface Figure {
    Position getPosition(Line splitter);
    Line getRepresentativeLine();
    double distance(double x, double y);
    boolean inside(double x, double y);

    enum Position {
        FRONT,
        BACK,
        COPLANAR,
        SPANNING
    }
}
