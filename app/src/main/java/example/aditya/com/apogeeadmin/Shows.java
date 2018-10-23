package example.aditya.com.apogeeadmin;

/**
 * Created by aditya on 2/14/2018.
 */

public class Shows {
    int id;
    String name;
    int price;
    String date;
    String time;
    String venue;

    public Shows(int id, String name, int price, String date, String time, String venue) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.time = time;
        this.venue = venue;
    }

    public int getShowId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return "Shows{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", venue='" + venue + '\'' +
                '}';
    }
}
