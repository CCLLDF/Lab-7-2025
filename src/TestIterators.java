import functions.*;
import functions.basic.Sin;
import functions.basic.Cos;

public class TestIterators {
    public static void main(String[] args) {
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ ИТЕРАТОРОВ ===\n");

            // Создание ArrayTabulatedFunction
            System.out.println("1. Тестирование ArrayTabulatedFunction:");
            Function sin = new Sin();
            TabulatedFunction arrayFunction = TabulatedFunctions.tabulate(sin, 0, Math.PI, 5);

            System.out.println("Исходная функция (sin(x) на [0, π] с 5 точками):");
            for (FunctionPoint p : arrayFunction) {
                System.out.println(p);
            }

            System.out.println("\n2. Тестирование LinkedListTabulatedFunction:");
            Function cos = new Cos();
            TabulatedFunction listFunction = TabulatedFunctions.tabulate(cos, 0, Math.PI, 5);

            System.out.println("Исходная функция (cos(x) на [0, π] с 5 точками):");
            for (FunctionPoint p : listFunction) {
                System.out.println(p);
            }

            System.out.println("\nТест завершен успешно!");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
