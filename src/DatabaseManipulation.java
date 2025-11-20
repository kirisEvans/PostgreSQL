import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class DatabaseManipulation implements DataManipulation {
    private Connection con = null;
    private ResultSet resultSet;

    private String host = "localhost";
    private String dbname = "postgres";
    private String user = "postgres";
    private String pwd = "123456";
    private String port = "5432";


    private void getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    private void closeConnection() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int insertUser() {
        getConnection();
        int total = 0;

        String csvPath = "C:\\Program Files\\PostgreSQL\\17\\data\\final_data\\user_10000.csv";

        String insertSQL = "insert into project1.user_info (AuthorId, AuthorName, Gender, Age, Followers, Following) " +
                "values (?,?,?,?,?,?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath));
             PreparedStatement ps = con.prepareStatement(insertSQL)) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                ps.setInt(1, Integer.parseInt(parts[0]));
                ps.setString(2, parts[1]);
                String genderStr = parts[2].trim().toLowerCase();
                int gender = genderStr.equals("male") ? 0 : 1;
                ps.setInt(3, gender);
                ps.setInt(4, Integer.parseInt(parts[3]));
                ps.setInt(5, Integer.parseInt(parts[4]));
                ps.setInt(6, Integer.parseInt(parts[5]));

                ps.addBatch();
            }

            int[] result = ps.executeBatch();
            total = result.length;


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return total;
    }

    @Override
    public String deleteUser() {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "delete from project1.user_info where authorid >= 300000";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            sb.append("Users deleted.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return sb.toString();
    }

    @Override
    public String updateUser() {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "update project1.user_info set Age = Age / 2 where authorid >= 300000";
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            int rows = ps.executeUpdate();     // 返回更新行数
            sb.append("Users updated. Rows affected: ").append(rows).append("\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return sb.toString();
    }

    @Override
    public String selectUser() {
        getConnection();
        StringBuilder sb = new StringBuilder();
        String sql = "select * from project1.user_info where authorid >= 300000";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getInt("AuthorId")).append("\t");
                sb.append(String.format("%-20s", resultSet.getString("AuthorName")));
                sb.append(resultSet.getInt("Gender")).append("\t");
                sb.append(resultSet.getInt("Age")).append("\t");
                sb.append(resultSet.getInt("Followers")).append("\t");
                sb.append(resultSet.getInt("Following")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return sb.toString();
    }
}
