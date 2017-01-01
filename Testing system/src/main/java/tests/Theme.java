package tests;

/**
 * Created by disqustingman on 28.05.16.
 */
public class Theme{
    private long id;
    private String name;
    private String clas;

    public Theme(long id, String name, String clas) {
        this.id = id;
        this.name = name;
        this.clas = clas;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClas() {
        return clas;
    }
}
