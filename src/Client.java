
public class Client {

    public static void main(String[] args) {
        try {
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            long start1 = System.currentTimeMillis();
            dm.insertUser();
            long end1 = System.currentTimeMillis();
            System.out.println("insert耗时：" + (end1 - start1) + " 毫秒");

            long start2 = System.currentTimeMillis();
            dm.selectUser();
            long end2 = System.currentTimeMillis();
            System.out.println("select耗时：" + (end2 - start2) + " 毫秒");

            long start3 = System.currentTimeMillis();
            dm.updateUser();
            long end3 = System.currentTimeMillis();
            System.out.println("update耗时：" + (end3 - start3) + " 毫秒");

            long start4 = System.currentTimeMillis();
            dm.deleteUser();
            long end4 = System.currentTimeMillis();
            System.out.println("delete耗时：" + (end4 - start4) + " 毫秒");

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}


