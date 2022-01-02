package com.monstahhh.mrworldwide.database;

import com.monstahhh.mrworldwide.MrWorldWide;
import com.monstahhh.mrworldwide.commands.weather.ChangeClock;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Profile extends Database {

    private User user;

    public Profile(long userId) {
        this.user = MrWorldWide.jda.getUserById(userId);
    }

    public ChangeClock.Time getTimeSetting() {
        ChangeClock.Time userTime = null;

        try {
            Statement stmt = connection.createStatement();
            String sql = String.format("SELECT clockType FROM users WHERE userId=%s);", user.getIdLong());

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String result = rs.getString("clockType");
                userTime = ChangeClock.Time.valueOf(result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return userTime;
    }
}
