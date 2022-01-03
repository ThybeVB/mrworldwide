package com.monstahhh.mrworldwide.database;

import com.monstahhh.mrworldwide.MrWorldWide;
import com.monstahhh.mrworldwide.commands.weather.ChangeClock;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile extends Database {

    private User user;

    public Profile(long userId) {
        this.user = MrWorldWide.jda.getUserById(userId);
    }

    public ChangeClock.Time getTimeSetting() {
        ChangeClock.Time userTime = null;

        try {
            String sql = "SELECT clockType FROM users WHERE userId=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getIdLong());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String result = rs.getString("clockType");
                if (!result.isEmpty())
                    userTime = ChangeClock.Time.valueOf(result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userTime;
    }
}
