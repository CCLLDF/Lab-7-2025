package functions;

import java.io.*;
import java.lang.reflect.*;

/**
 * Утилитарный класс, содержащий вспомогательные статические методы
 * для работы с табулированными функциями. Нельзя создать объект этого класса.
 */
public class TabulatedFunctions {

    private static final double EPSILON = 1e-10;
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private TabulatedFunctions() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Устанавливает фабрику для создания табулированных функций.
     * @param factory фабрика табулированных функций
     */
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        TabulatedFunctions.factory = factory;
    }

    /**
     * Создает табулированную функцию с равномерно распределенными точками.
     * @param leftX левая граница отрезка
     * @param rightX правая граница отрезка
     * @param pointsCount количество точек
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    /**
     * Создает табулированную функцию с заданными значениями Y для равномерно распределенных X.
     * @param leftX левая граница отрезка
     * @param rightX правая граница отрезка
     * @param values массив значений Y
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    /**
     * Создает табулированную функцию из массива точек.
     * @param points массив точек функции
     * @return табулированная функция
     */
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    /**
     * Табулирует функцию на заданном отрезке с заданным количеством точек.
     * @param function функция для табулирования
     * @param leftX левая граница отрезка табулирования
     * @param rightX правая граница отрезка табулирования
     * @param pointsCount количество точек табуляции
     * @return табулированная функция
     * @throws IllegalArgumentException если границы табулирования выходят за область определения функции
     */
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Points count must be at least 2");
        }

        // Проверка, что границы табулирования находятся в области определения функции
        double functionLeftBorder = function.getLeftDomainBorder();
        double functionRightBorder = function.getRightDomainBorder();

        // Проверка с учетом машинного эпсилона
        if (leftX < functionLeftBorder - EPSILON || rightX > functionRightBorder + EPSILON) {
            throw new IllegalArgumentException(
                String.format("Tabulation boundaries [%.10f, %.10f] are out of function domain [%.10f, %.10f]",
                    leftX, rightX, functionLeftBorder, functionRightBorder)
            );
        }

        // Создание табулированной функции
        TabulatedFunction tabulated = createTabulatedFunction(leftX, rightX, pointsCount);

        // Заполнение значений функции в каждой точке
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulated.setPointY(i, y);
        }

        return tabulated;
    }

    /**
     * Табулирует функцию на заданном отрезке с заданным количеством точек с использованием рефлексии.
     * @param clazz класс табулированной функции
     * @param function функция для табулирования
     * @param leftX левая граница отрезка табулирования
     * @param rightX правая граница отрезка табулирования
     * @param pointsCount количество точек табуляции
     * @return табулированная функция
     * @throws IllegalArgumentException если границы табулирования выходят за область определения функции
     */
    public static <T extends TabulatedFunction> TabulatedFunction tabulate(
            Class<T> clazz, Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Points count must be at least 2");
        }

        // Проверка, что границы табулирования находятся в области определения функции
        double functionLeftBorder = function.getLeftDomainBorder();
        double functionRightBorder = function.getRightDomainBorder();

        // Проверка с учетом машинного эпсилона
        if (leftX < functionLeftBorder - EPSILON || rightX > functionRightBorder + EPSILON) {
            throw new IllegalArgumentException(
                String.format("Tabulation boundaries [%.10f, %.10f] are out of function domain [%.10f, %.10f]",
                    leftX, rightX, functionLeftBorder, functionRightBorder)
            );
        }

        // Создание табулированной функции с использованием рефлексии
        TabulatedFunction tabulated = createTabulatedFunction(clazz, leftX, rightX, pointsCount);

        // Заполнение значений функции в каждой точке
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            tabulated.setPointY(i, y);
        }

        return tabulated;
    }

    /**
     * Выводит табулированную функцию в байтовый поток.
     * Формат: количество точек (int), затем для каждой точки: x (double), y (double).
     * 
     * @param function табулированная функция для вывода
     * @param out выходной байтовый поток
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null");
        }

        DataOutputStream dataOut = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dataOut.writeInt(pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }

    /**
     * Считывает табулированную функцию из байтового потока.
     * Формат: количество точек (int), затем для каждой точки: x (double), y (double).
     * 
     * @param in входной байтовый поток
     * @return восстановленная табулированная функция
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }

        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();

        if (pointsCount < 2) {
            throw new IOException("Invalid points count: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    /**
     * Создает табулированную функцию с равномерно распределенными точками с использованием рефлексии.
     * @param clazz класс табулированной функции
     * @param leftX левая граница отрезка
     * @param rightX правая граница отрезка
     * @param pointsCount количество точек
     * @return табулированная функция
     */
    public static <T extends TabulatedFunction> TabulatedFunction createTabulatedFunction(
            Class<T> clazz, double leftX, double rightX, int pointsCount) {
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction interface");
        }

        try {
            Constructor<T> constructor = clazz.getConstructor(double.class, double.class, int.class);
            return constructor.newInstance(leftX, rightX, pointsCount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance using reflection", e);
        }
    }

    /**
     * Создает табулированную функцию с заданными значениями Y для равномерно распределенных X с использованием рефлексии.
     * @param clazz класс табулированной функции
     * @param leftX левая граница отрезка
     * @param rightX правая граница отрезка
     * @param values массив значений Y
     * @return табулированная функция
     */
    public static <T extends TabulatedFunction> TabulatedFunction createTabulatedFunction(
            Class<T> clazz, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction interface");
        }

        try {
            Constructor<T> constructor = clazz.getConstructor(double.class, double.class, double[].class);
            return constructor.newInstance(leftX, rightX, values);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance using reflection", e);
        }
    }

    /**
     * Создает табулированную функцию из массива точек с использованием рефлексии.
     * @param clazz класс табулированной функции
     * @param points массив точек функции
     * @return табулированная функция
     */
    public static <T extends TabulatedFunction> TabulatedFunction createTabulatedFunction(
            Class<T> clazz, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class must implement TabulatedFunction interface");
        }

        try {
            Constructor<T> constructor = clazz.getConstructor(FunctionPoint[].class);
            return constructor.newInstance((Object) points);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create instance using reflection", e);
        }
    }

    /**
     * Записывает табулированную функцию в символьный поток.
     * Формат: количество точек, затем для каждой точки: x y (значения разделены пробелами).
     * 
     * @param function табулированная функция для записи
     * @param out выходной символьный поток
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Writer must not be null");
        }

        PrintWriter printWriter = new PrintWriter(out);
        int pointsCount = function.getPointsCount();
        printWriter.print(pointsCount);

        for (int i = 0; i < pointsCount; i++) {
            printWriter.print(" ");
            printWriter.print(function.getPointX(i));
            printWriter.print(" ");
            printWriter.print(function.getPointY(i));
        }
        printWriter.flush();
    }

    /**
     * Считывает табулированную функцию из символьного потока.
     * Формат: количество точек, затем для каждой точки: x y (значения разделены пробелами).
     * 
     * @param in входной символьный поток
     * @return восстановленная табулированная функция
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();

        // Чтение количества точек
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Expected number of points");
        }
        int pointsCount = (int) tokenizer.nval;

        if (pointsCount < 2) {
            throw new IOException("Invalid points count: " + pointsCount);
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            // Чтение x
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Expected x coordinate at point " + i);
            }
            double x = tokenizer.nval;

            // Чтение y
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Expected y coordinate at point " + i);
            }
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
}

