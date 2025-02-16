package motloung.koena.kovsiesbanking.data;

public class ListOfUsers
{
    private String name;
    private String location;
    private int imageResource;

    public ListOfUsers(String name, String location, int imageResource) {
        this.name = name;
        this.location = location;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getImageResource() {
        return imageResource;
    }
}
