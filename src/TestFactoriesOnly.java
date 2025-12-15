import functions.*;
import functions.basic.*;

public class TestFactoriesOnly {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ФАБРИК ===\n");

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
    }
}
