package org.example.lab2;

public record Line(double a, double b, double c) {
    public double eval(double x, double y) {
        return a * x + b * y + c;
    }

    public double distance(double x, double y) {
        var norm = Math.hypot(a, b);
        if (norm == 0) {
            return 0;
        }
        return Math.abs(eval(x, y)) / norm;
    }
}
