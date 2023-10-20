package krista.test_task;

import krista.test_task.entities.Catalog;
import krista.test_task.entities.Plant;
import java.sql.*;

public class DBWorker {
    public static final String URL = "jdbc:postgresql://localhost:5432/plant";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "v";
    private static final Connection connection = connect();
    private static Connection connect(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Подключение к базе данных выполнено успешно.");
        } catch (SQLException e) {
            System.out.println("При подключении к базе данных возникли проблемы.");
            e.printStackTrace();
        }
        return con;
    }

    public static boolean insertCatalog(Catalog catalog){
        if(checkCatalogInBD(catalog.uuid)){
            System.out.println("Каталог " + catalog.uuid + " уже находится в базе данных.");
            return false;
        }
        String insert_cat_sql = "INSERT INTO d_cat_catalog (uuid, delivery_date, company) VALUES (?, ?, ?) RETURNING id";
        String insert_plant_sql = "INSERT INTO f_cat_plants (common, botanical, zone, light, price, availability, catalog_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            //отключение автоматических изменений в базе для выполнения транзакции
            connection.setAutoCommit(false);
            PreparedStatement insertCatStatement = connection.prepareStatement(insert_cat_sql, Statement.RETURN_GENERATED_KEYS);
            insertCatStatement.setString(1, catalog.uuid);
            insertCatStatement.setTimestamp(2, catalog.date);
            insertCatStatement.setString(3, catalog.company);
            insertCatStatement.executeUpdate();
            ResultSet generatedKeys = insertCatStatement.getGeneratedKeys();
            int generatedCatId = -1;
            if (generatedKeys.next()) {
                generatedCatId = generatedKeys.getInt(1);
            }
            for(Plant plant: catalog.plants){
                PreparedStatement insertPlantStatement = connection.prepareStatement(insert_plant_sql);
                insertPlantStatement.setString(1, plant.common);
                insertPlantStatement.setString(2, plant.botanical);
                insertPlantStatement.setInt(3, plant.zone);
                insertPlantStatement.setString(4, plant.light);
                insertPlantStatement.setDouble(5, plant.price);
                insertPlantStatement.setInt(6, plant.availability);
                insertPlantStatement.setInt(7, generatedCatId);

                insertPlantStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                //откат транзакции в случае ошибки
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    private static boolean checkCatalogInBD(String uuid){
        String select_cat_sql = "SELECT uuid from d_cat_catalog where uuid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(select_cat_sql);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean checkConnection(){
        return connection == null;
    }

    public static void closeConnection(){
        try {
            connection.close();
            System.out.println("Соединение с БД завершено.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
