abstract class Shape {
    private static int counter = 1000;
    private final String shapeId;
    protected double xScale = 1.0;
    protected double yScale = 1.0;

    public Shape() {
        shapeId = "SH-" + (++counter);
    }

    public abstract double calculateArea();

    public void scale(double factor) {
        scale(factor, factor);
    }

    public void scale(double xFactor, double yFactor) {
        xScale *= xFactor;
        yScale *= yFactor;
    }

    public String getShapeId() {
        return shapeId;
    }
}

class CircleShape extends Shape {
    private final double radius;

    public CircleShape(double radius) {
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * Math.pow(radius * xScale, 2);
    }
}

class SquareShape extends Shape {
    private final double side;

    public SquareShape(double side) {
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * xScale * side * yScale;
    }
}

public class Basic_Drawing_Canvas {
    static void printArea(Shape s) {
        System.out.println(s.calculateArea());
    }

    public static void main(String[] args) {
        CircleShape c = new CircleShape(5.0);
        System.out.printf("%.2f%n", c.calculateArea());

        SquareShape sq = new SquareShape(4.0);
        System.out.println(sq.calculateArea());

        sq.scale(2.0);
        System.out.println(sq.calculateArea());

        sq.scale(2.0, 1.0);
        System.out.println(sq.calculateArea());

        printArea(c);
        System.out.println(c.getShapeId());
    }
}
