package krista.test_task.entities;

public class Plant {
    public final String common;
    public final String botanical;
    public final Integer zone;
    public final String light;
    public final Double price;
    public final Integer availability;

    public Plant(String common, String botanical, Integer zone, String light, Double price, Integer availability) {
        this.common = common;
        this.botanical = botanical;
        this.zone = zone;
        this.light = light;
        this.price = price;
        this.availability = availability;
    }
}
