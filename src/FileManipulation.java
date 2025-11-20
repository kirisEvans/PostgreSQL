import java.io.*;
import java.util.*;

public class FileManipulation implements DataManipulation {

    @Override
    public int insertUser() {
        int count = 0;
        ArrayList<Integer> existingIds = new ArrayList<>();
        ArrayList<String> newLines = new ArrayList<>();

        try (BufferedReader br1 = new BufferedReader(new FileReader("user.txt"));
             BufferedReader br2 = new BufferedReader(new FileReader("user_10000.txt"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("user.txt", true))
        ) {
            String line;

            while ((line = br1.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].matches("\\d+")) {
                    existingIds.add(Integer.parseInt(parts[0].trim()));
                }
            }

            while ((line = br2.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 0 || !parts[0].matches("\\d+")) continue;

                int id = Integer.parseInt(parts[0].trim());
                if (existingIds.contains(id)) {
                    continue;
                }

                newLines.add(line);
                existingIds.add(id);
                count++;
            }

            for (String newLine : newLines) {
                bw.write(newLine);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        return count;
    }

    @Override
    public String deleteUser() {
        String inputPath = "user.txt";
        ArrayList<String> keptLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length == 0) continue;

                int authorid = Integer.parseInt(parts[0].trim());
                if (authorid < 300000) {
                    keptLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "delete failed";
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(inputPath))) {
            for (String keptLine : keptLines) {
                bw.write(keptLine);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "delete failed";
        }

        return "delete finished";
    }



    @Override
    public String updateUser() {
        String inputPath = "user.txt";
        ArrayList<String> updatedLines = new ArrayList<>();
        HashSet<Integer> updatedIds = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");

                int authorid = Integer.parseInt(parts[0].trim());

                if (authorid <= 300000 && !updatedIds.contains(authorid)) {
                    int age = Integer.parseInt(parts[3].trim());
                    age = age / 2;

                    if (age < 0) age = 0;
                    if (age > 130) age = 130;

                    parts[3] = String.valueOf(age);
                    line = String.join(",", parts);

                    updatedIds.add(authorid);
                }

                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "update failed";
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(inputPath))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "update failed";
        }

        return "update finished";
    }



    @Override
    public String selectUser() {

        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))
        ) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                int authorid = Integer.parseInt(parts[0].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "select failed";
        }

        return "select finished";
    }
}
