import functions.*;

public class TestFactory {
    public static void main(String[] args) {
        try {
            System.out.println("=== ТЕСТИРОВАНИЕ ФАБРИК ===\n");

            // Создаем фабрики
            TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();
            TabulatedFunctionFactory listFactory = new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory();

            System.out.println("Array фабрика создана: " + arrayFactory.getClass().getSimpleName());
            System.out.println("LinkedList фабрика создана: " + listFactory.getClass().getSimpleName());

            // Создаем функции через фабрики
            TabulatedFunction tf1 = arrayFactory.createTabulatedFunction(0, Math.PI, 5);
            TabulatedFunction tf2 = listFactory.createTabulatedFunction(0, Math.PI, 5);

            System.out.println("ArrayTabulatedFunction создана: " + tf1.getClass().getSimpleName());
            System.out.println("LinkedListTabulatedFunction создана: " + tf2.getClass().getSimpleName());

            System.out.println("\nТест завершен успешно!");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}