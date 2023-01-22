package com.chasingdns.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="club_accounts")
public class ClubAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String paypalId;

    @Column(nullable = true)
    private String venmoId;

    @Column(nullable = false)
    private BigDecimal startingBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal suspenseBalance = BigDecimal.ZERO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaypalId() {
        return paypalId;
    }

    public void setPaypalId(String paypalId) {
        this.paypalId = paypalId;
    }

    public String getVenmoId() {
        return venmoId;
    }

    public void setVenmoId(String venmoId) {
        this.venmoId = venmoId;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
        this.availableBalance = this.currentBalance.add(this.suspenseBalance);
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getSuspenseBalance() {
        return suspenseBalance;
    }

    public void setSuspenseBalance(BigDecimal suspenseBalance) {
        this.suspenseBalance = suspenseBalance;
        this.availableBalance = this.currentBalance.add(this.suspenseBalance);
    }

}
