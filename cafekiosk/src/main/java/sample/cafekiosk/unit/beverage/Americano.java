package sample.cafekiosk.unit.beverage;

public class Americano implements Beverage {

    @Override
    public String getName() {
        return "아메리카노";
    }

    @Override
    public int price() {
        return 4000;
    }
}
