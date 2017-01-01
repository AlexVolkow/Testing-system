package dbService.dataSets;

/**
 * Created by disqustingman on 27.05.16.
 */
public class ClasesDataSet {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ClasesDataSet(long id, String name) {

        this.id = id;
        this.name = name;
    }
}
