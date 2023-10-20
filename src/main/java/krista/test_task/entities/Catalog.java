package krista.test_task.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Catalog {
    public final String uuid;
    public final Timestamp date;
    public final String company;
    public final ArrayList<Plant> plants;

    public Catalog(String uuid, Timestamp date, String company, ArrayList<Plant> plants) {
        this.uuid = uuid;
        this.date = date;
        this.company = company;
        this.plants = plants;
    }
}
