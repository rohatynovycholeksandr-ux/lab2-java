import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ЛАБОРАТОРНА РОБОТА №2: КЛАС TRIANGLE ===");
        
        // 1. Введення даних від користувача
        System.out.print("Введіть першу сторону (a): ");
        double a = scanner.nextDouble();

        System.out.print("Введіть другу сторону (b): ");
        double b = scanner.nextDouble();

        System.out.print("Введіть третю сторону (c): ");
        double c = scanner.nextDouble();

        // Перевірка існування трикутника
        if (!Triangle.isValid(a, b, c)) {
            System.out.println("\n[Помилка] Трикутник із такими сторонами не існує!");
            scanner.close();
            return;
        }

        // 2. Створення об'єкта користувача
        Triangle userTriangle = new Triangle(a, b, c);

        System.out.println("\n--- Результати для вашого трикутника ---");
        System.out.println(userTriangle);
        System.out.printf("Периметр: %.2f\n", userTriangle.getPerimeter());
        System.out.printf("Площа: %.2f\n", userTriangle.getArea());
        System.out.printf("Висоти: h_a = %.2f, h_b = %.2f, h_c = %.2f\n", 
                          userTriangle.getHeightA(), userTriangle.getHeightB(), userTriangle.getHeightC());

        // 3. Демонстрація інших способів ініціалізації та методів
        System.out.println("\n--- Демонстрація інших конструкторів та методів ---");
        Triangle defaultTriangle = new Triangle();       // За замовчуванням (3, 4, 5)
        Triangle eqTriangle = new Triangle(5.0);          // Рівносторонній (5, 5, 5)

        System.out.println("Стандартний трикутник (3, 4, 5): " + defaultTriangle);
        System.out.println("Рівносторонній трикутник (5, 5, 5): " + eqTriangle);

        // Демонстрація equals()
        System.out.println("\nВаш трикутник такий самий, як стандартний (3,4,5)?: " + userTriangle.equals(defaultTriangle));

        // Демонстрація статичного поля
        System.out.println("Всього створено об'єктів Triangle у програмі: " + Triangle.getInstanceCount());

        scanner.close();
    }
}

class Triangle {
    private static int instanceCount = 0;
    private static final double EPS = 1e-5;

    private double a, b, c;
    private double angleA, angleB, angleC;

    // Блок ініціалізації (виконується при кожному створенні)
    {
        instanceCount++;
    }

    // Перевантажені конструктори
    public Triangle() { 
        this(3.0, 4.0, 5.0); 
    }

    public Triangle(double side) { 
        this(side, side, side); 
    }

    public Triangle(double a, double b, double c) { 
        setSides(a, b, c); 
    }

    // Статичні методи
    public static int getInstanceCount() { 
        return instanceCount; 
    }

    public static boolean isValid(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0 && (a + b > c) && (a + c > b) && (b + c > a);
    }

    // Геттери та сеттери
    public void setSides(double a, double b, double c) {
        if (!isValid(a, b, c)) {
            throw new IllegalArgumentException("Трикутник з такими сторонами не існує!");
        }
        this.a = a;
        this.b = b;
        this.c = c;
        recalculateAngles();
    }

    private void recalculateAngles() {
        this.angleA = Math.toDegrees(Math.acos((b * b + c * c - a * a) / (2 * b * c)));
        this.angleB = Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
        this.angleC = 180.0 - angleA - angleB;
    }

    public double getA() { return a; }
    public double getB() { return b; }
    public double getC() { return c; }

    // Розрахунки
    public double getPerimeter() { 
        return a + b + c; 
    }

    public double getArea() {
        double p = getPerimeter() / 2.0;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public double getHeightA() { return (2 * getArea()) / a; }
    public double getHeightB() { return (2 * getArea()) / b; }
    public double getHeightC() { return (2 * getArea()) / c; }

    public String getType() {
        boolean ab = Math.abs(a - b) < EPS;
        boolean bc = Math.abs(b - c) < EPS;
        boolean ac = Math.abs(a - c) < EPS;

        if (ab && bc) return "Рівносторонній";

        boolean isRight = Math.abs(angleA - 90) < EPS || 
                         Math.abs(angleB - 90) < EPS || 
                         Math.abs(angleC - 90) < EPS;

        if (ab || bc || ac) return isRight ? "Рівнобедрений прямокутний" : "Рівнобедрений";
        return isRight ? "Прямокутний" : "Різносторонній";
    }

    // Перевизначення toString та equals
    @Override
    public String toString() {
        return String.format("Triangle [a=%.1f, b=%.1f, c=%.1f | ∠A=%.1f°, ∠B=%.1f°, ∠C=%.1f° | Вид: %s]",
                a, b, c, angleA, angleB, angleC, getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Triangle)) return false;
        Triangle t = (Triangle) obj;
        return Math.abs(t.a - a) < EPS && Math.abs(t.b - b) < EPS && Math.abs(t.c - c) < EPS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(a * 1000), Math.round(b * 1000), Math.round(c * 1000));
    }
}