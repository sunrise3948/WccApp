package com.chasingdns.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = true, unique = true)
    private long phone;

    @Column(nullable = true)
    private URL url;
    @Column(nullable = true)
    private PLAYER_STATUS status;

    @Column(nullable = true)
    private SIZE shirtSize;

    @Column(nullable = true)
    private SIZE pantSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true)
    private Date joiningDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = true)
    private Date dob;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "team_roster",
            joinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "team_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Team> teams = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "player_transactions",
            joinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "transaction_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Transaction> transactions = new HashSet<>();

    public SIZE getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(SIZE shirtSize) {
        this.shirtSize = shirtSize;
    }

    public SIZE getPantSize() {
        return pantSize;
    }

    public void setPantSize(SIZE pantSize) {
        this.pantSize = pantSize;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public PLAYER_STATUS getStatus() {
        return status;
    }

    public void setStatus(PLAYER_STATUS status) {
        this.status = status;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public enum SIZE {
        XS, S, M, L, XL, XXL;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public enum PLAYER_STATUS {
        ACTIVE, INACTIVE;
    }
}
