package PR5_2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class CF2 {
    public static void main(String[] args) {
        // Перевірка наявності місць
        CompletableFuture<Boolean> checkAvailability = CompletableFuture.supplyAsync(() -> {
            System.out.println("Перевіряємо наявність місць...");
            simulateDelay(2);
            return true; // Місця доступні
        });

        // Знаходження найкращої ціни
        CompletableFuture<Double> findBestPrice = CompletableFuture.supplyAsync(() -> {
            System.out.println("Знаходимо найкращу ціну...");
            simulateDelay(3);
            return 250.75; // Найкраща ціна
        });

        // Об'єднуємо перевірку наявності місць і пошук ціни
        CompletableFuture<String> bookingProcess = checkAvailability.thenCombine(findBestPrice, (availability, price) -> {
            if (availability) {
                System.out.println("Місця є. Найкраща ціна: " + price + " EUR");
                return "Місця доступні. Найкраща ціна: " + price + " EUR";
            } else {
                return "Місць немає.";
            }
        });

        // Використовуємо thenCompose для здійснення оплати
        CompletableFuture<String> paymentProcess = bookingProcess.thenCompose(result -> {
            if (result.contains("Місця доступні")) {
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("Оплачуємо бронювання...");
                    simulateDelay(2);
                    return "Оплата успішна. Бронювання завершено!";
                });
            } else {
                return CompletableFuture.completedFuture("Не вдалося завершити бронювання.");
            }
        });

        // Використовуємо allOf для виконання всіх етапів разом
        CompletableFuture<Void> allProcesses = CompletableFuture.allOf(checkAvailability, findBestPrice, paymentProcess);

        try {
            // Очікуємо завершення всіх процесів
            allProcesses.get();
            System.out.println("Результат бронювання: " + paymentProcess.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void simulateDelay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}

