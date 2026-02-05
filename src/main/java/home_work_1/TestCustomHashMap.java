package home_work_1;

public class TestCustomHashMap {
    public static void main( String[] args ) {
        var hm = new CustomHashMap<Integer, String>();

        hm.put(2, "Banana");
        hm.put(10, "Orange");

        System.out.println(hm.size());
    }
}
