package project.finalproject1backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class TestController {
    private final DataSource dataSource;

    @GetMapping("/now")
    public Map<String,String> getNow(){
        String now = "";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select now()");
             ResultSet resultSet = preparedStatement.executeQuery(); )
        {
            resultSet.next();
            now = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of("Now",now);
    }
    @GetMapping("/test")
    public Map<String,String> login(){
        String now = "";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select now()");
             ResultSet resultSet = preparedStatement.executeQuery(); )
        {
            resultSet.next();
            now = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Map.of("Now",now);
    }
}
