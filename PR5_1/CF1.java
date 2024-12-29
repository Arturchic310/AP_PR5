package PR5_1;

import java.util.concurrent.*;

public class CF1 {
    public static void main(String[] args) {
        // Завдання 1: Отримання даних з першого джерела
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1000); // Затримка 1 секунда
            return "Результат із завдання 1";
        });

        // Завдання 2: Отримання даних з другого джерела
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(2000); // Затримка 2 секунди
            return "Результат із завдання 2";
        });

        // Завдання 3: Комбінування результатів task1 і task2
        CompletableFuture<String> combinedTask = task1.thenCombine(task2, (result1, result2) -> {
            return result1 + " і " + result2;
        });

        // Завдання 4: Використання thenCompose для послідовного виконання
        CompletableFuture<String> composedTask = task1.thenCompose(result1 -> CompletableFuture.supplyAsync(() -> {
            simulateDelay(500); // Додаткова затримка
            return result1 + " (доповнено)";
        }));

        // Виконання всіх завдань одночасно
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, combinedTask, composedTask);

        // Отримання першого успішного результату
        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(task1, task2, combinedTask, composedTask);

        try {
            // Очікуємо завершення всіх завдань
            allTasks.join();

            // Вивід результату першого завершеного завдання
            System.out.println("Перший завершений результат: " + firstCompleted.get());

            // Вивід для перевірки результату комбінованого завдання
            // System.out.println("Результат комбінованого завдання: " + combinedTask.get());
            // Вивід для перевірки результату складеного завдання
            // System.out.println("Результат складеного завдання: " + composedTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Метод для симуляції затримки
    private static void simulateDelay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
