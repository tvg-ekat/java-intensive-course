package home_work_1;

public class TestCustomHashMap {
    public static void main( String[] args ) {

        var products = new CustomHashMap<Integer, String>(); // <id, name>

        // 1: Вставка и получение значения
        products.put(2, "Apple");
        products.put(7, "Banana");
        check("Test put/get", "Banana".equals(products.get(7)));

        // 2: Обновление значения
        var previous = products.put(7, "Orange");
        check("Test update", "Orange".equals(products.get(7)));
        check("Test get previous", "Banana".equals(previous));

        // 3: Удаление
        var removed = products.remove(7);
        check("Test remove value", "Orange".equals(removed));
        check("Test remove size", products.get(7) == null);

        // 4: Коллизии
        products.put(10, "Peach");
        check("Test collision, key=2", "Apple".equals(products.get(2)));
        check("Test collision, key=10", "Peach".equals(products.get(10)));

        // 5: Очистка и размер
        var sizeBeforeClear = products.size();
        products.clear();
        var sizeAfterClear = products.size();
        check("Test size before clear", sizeBeforeClear == 2);
        check("Test size after clear", sizeAfterClear == 0);
    }

    private static void check(String testName, boolean condition){
            System.out.println((condition ? "[ OK ]\t" :
                                            "[ FAIL ]\t") + testName);
    }
}
