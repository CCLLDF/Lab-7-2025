import functions.*;
import functions.basic.*;
import java.util.Iterator;

public class Main {
    private static final double PI = Math.PI;

    public static void main(String[] args) {
        try {
            // Тест итераторов
            testIterators();

            // Тест фабрик
            testFactories();

            // Тест рефлексии
            testReflection();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Тест итераторов табулированных функций
     */
    private static void testIterators() {
        System.out.println("=== ТЕСТ ИТЕРАТОРОВ ===\n");

        // Создание ArrayTabulatedFunction
        System.out.println("1. Тестирование ArrayTabulatedFunction:");
        Function sin = new Sin();
        TabulatedFunction arrayFunction = TabulatedFunctions.tabulate(sin, 0, PI, 5);

        System.out.println("Исходная функция (sin(x) на [0, π] с 5 точками):");
        for (FunctionPoint p : arrayFunction) {
            System.out.println(p);
        }

        System.out.println("\n2. Тестирование LinkedListTabulatedFunction:");
        Function cos = new Cos();
        TabulatedFunction listFunction = TabulatedFunctions.tabulate(cos, 0, PI, 5);

        System.out.println("Исходная функция (cos(x) на [0, π] с 5 точками):");
        for (FunctionPoint p : listFunction) {
            System.out.println(p);
        }

        System.out.println("\nТест завершен успешно!");
        System.out.println();
    }

    /**
     * Тест фабрик табулированных функций
     */
    private static void testFactories() {
        System.out.println("=== ТЕСТ ФАБРИК ===\n");

        Function f = new Cos();
        TabulatedFunction tf;

        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("По умолчанию (ArrayTabulatedFunction): " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(new
            LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки LinkedList фабрики: " + tf.getClass().getSimpleName());

        TabulatedFunctions.setTabulatedFunctionFactory(new
            ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("После установки Array фабрики: " + tf.getClass().getSimpleName());

        System.out.println("\nТест завершен успешно!");
        System.out.println();
    }

    /**
     * Тест рефлексивного создания объектов табулированных функций
     */
    private static void testReflection() {
        System.out.println("=== ТЕСТ РЕФЛЕКСИИ ===\n");

        TabulatedFunction f;

        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class,
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(10, 10)
            }
        );
        System.out.println(f.getClass());
        System.out.println(f);

        f = TabulatedFunctions.tabulate(
            LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(f.getClass());
        System.out.println(f);

        System.out.println("\nТест завершен успешно!");
        System.out.println();
    }

}