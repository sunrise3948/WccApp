package com.chasingdns.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private USER_ROLE role;

    @Column(nullable = true)
    private String resetPasswordToken;

    @Column(nullable = true)
    private USER_TYPE type;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date createdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date updatedDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", referencedColumnName = "id", nullable = false)
    private Player player;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public USER_TYPE getType() {
        return type;
    }

    public void setType(USER_TYPE type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }


    public enum USER_TYPE {
        INTERNAL, EXTERNAL;
    }

    public enum USER_ROLE {
        USER, ADMIN, TREASURER;
    }


}
