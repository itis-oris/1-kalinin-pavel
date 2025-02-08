package servlets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Month {

    private Long id;
    private String name;
    private String season;

    public Month() {}

    public Month(String name, String season) {
        this.name = name;
        this.season = season;
    }

    public Month(Long id, String name, String season) {
        this.id = id;
        this.name = name;
        this.season = season;
    }

    public String toString() {
        return name;
    }

    @JsonIgnore
    public String getFullInfo() {
        return name + " - " + season;
    }
}
