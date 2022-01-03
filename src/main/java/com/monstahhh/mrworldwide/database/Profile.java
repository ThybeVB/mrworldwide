package com.monstahhh.mrworldwide.database;

import com.monstahhh.mrworldwide.MrWorldWide;
import com.monstahhh.mrworldwide.commands.weather.ChangeClock;
import net.dv8tion.jda.api.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.monstahhh.mrworldwide.database.Database.connection;

public class Profile {

    private User user;

    public Profile(long userId) {
        this.user = MrWorldWide.jda.getUserById(userId);
        if (!this.recordExists())
            this.createProfile();
    }

    private boolean recordExists() {
        boolean exists = false;
        String sql = String.format("SELECT userId FROM users WHERE userId=%s", user.getIdLong());

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getLong("userId") != 0L)
                    exists = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    private void createProfile() {
        String sql = "INSERT INTO users (userId) VALUES (?);";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getIdLong());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ChangeClock.Time getTimeSetting() {
        ChangeClock.Time userTime = null;
        String sql = "SELECT clockType FROM users WHERE userId=?;";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, user.getIdLong());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String result = rs.getString("clockType");
                if (result != null) {
                    if (!result.isEmpty())
                        userTime = ChangeClock.Time.valueOf(result);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userTime;
    }

    public void setTimeSetting(ChangeClock.Time newTime) {
        String sql = "UPDATE users SET clockType=? WHERE userId=?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, newTime.toString());
            ps.setLong(2, user.getIdLong());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
