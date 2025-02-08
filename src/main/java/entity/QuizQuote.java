package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class QuizQuote {
    private int id;
    private String text;
    private ArrayList<String> options;
    private String correctOption;
}
