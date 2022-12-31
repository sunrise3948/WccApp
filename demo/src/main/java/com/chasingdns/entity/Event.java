package com.chasingdns.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EVENT_TYPE type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EVENT_STATUS status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EVENT_MODE mode;

    @Column(nullable = false)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date eventDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date submittedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date updatedDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "event_transactions",
            joinColumns = {
                    @JoinColumn(name = "event_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "transaction_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Transaction> transactions = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EVENT_TYPE getType() {
        return type;
    }

    public void setType(EVENT_TYPE type) {
        this.type = type;
    }

    public EVENT_STATUS getStatus() {
        return status;
    }

    public void setStatus(EVENT_STATUS status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean hasNoPendingTransactions(){
        for(Transaction txn: this.transactions){
            if(txn.getStatus().equals(Transaction.TXN_STATUS.PENDING) || txn.getStatus().equals(Transaction.TXN_STATUS.ACKNOWLEDGED)) {
                return false;
            }
        }
        return true;
    }

    public EVENT_MODE getMode() {
        return mode;
    }

    public void setMode(EVENT_MODE mode) {
        this.mode = mode;
    }

    public enum EVENT_TYPE {
        GAME,
        PRACTICE,
        PARTY,
        CLUB_EXPENSE,
        OTHERS;
    }

    public enum EVENT_STATUS {
        IN_PROGRESS, COMPLETED, CANCELLED;
    }

    public enum EVENT_MODE {
        INTERNAL, EXTERNAL;
    }

    public static<Event> Set<Event> mergeSets(Set<Event> a, Set<Event> b)
    {
        Set<Event> set = new HashSet<>();

        set.addAll(a);
        set.addAll(b);

        return set;
    }

}

