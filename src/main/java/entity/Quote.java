package entity;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Quote {
    private Integer id;
    private String text;
    private String author;
    private Integer rating;
    private Date date_added;
    private String category;
    private boolean isLikedByUser;

    public Quote(Integer id, String text, String author, Integer rating, Date date_added, String category, boolean isLikedByUser) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.rating = rating;
        this.date_added = date_added;
        this.category = category;
        this.isLikedByUser = isLikedByUser;
    }
}

