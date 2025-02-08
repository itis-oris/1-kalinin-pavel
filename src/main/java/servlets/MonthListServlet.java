package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/month/list")
public class MonthListServlet extends HttpServlet {

    private List<Month> monthList = Arrays.asList(new Month[]{
            new Month(1L, "Январь", "Зима"),
            new Month(2L, "Февраль","Зима"),
            new Month(3L, "Март","Весна"),
            new Month(4L, "Апрель","Весна"),
            new Month(5l, "Май","Весна"),
            new Month(6l, "Июнь","Лето"),
            new Month(7l, "Июль","Лето"),
            new Month(8l, "Август","Лето"),
            new Month(9l, "Сентябрь","Осень"),
            new Month(10l, "Октябрь","Осень"),
            new Month(11l, "Ноябрь","Осень"),
            new Month(12l, "Декабрь","Зима")
    });


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        mapper.writeValue(response.getWriter(), monthList);
    }

}
