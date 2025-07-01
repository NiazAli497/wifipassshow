package wifi.key.show.master.show.all.wifi.password.model;

public class DisplayHistoryModel {
   private String time,name,speed,strength,type,signals;

    public DisplayHistoryModel(String time, String name, String speed, String strength, String type, String signals) {
        this.time = time;
        this.name = name;
        this.speed = speed;
        this.strength = strength;
        this.type = type;
        this.signals = signals;
    }

    public String getSignals() {
        return signals;
    }

    public void setSignals(String signals) {
        this.signals = signals;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getSpeed() {
        return speed;
    }

    public String getStrength() {
        return strength;
    }

    public String getType() {
        return type;
    }
}
