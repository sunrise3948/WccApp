package com.chasingdns.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TXN_TYPE type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TXN_SOURCE source;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TXN_STATUS status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TXN_MODE mode;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date txnDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date submittedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date updatedDate;

    @ManyToMany(mappedBy = "transactions", fetch = FetchType.EAGER)
    private Set<Player> players = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subEvent_id", referencedColumnName = "id")
    private SubEvent subEvent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
    private Event event;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TXN_TYPE getType() {
        return type;
    }

    public void setType(TXN_TYPE type) {
        this.type = type;
    }

    public TXN_SOURCE getSource() {
        return source;
    }

    public void setSource(TXN_SOURCE source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public TXN_STATUS getStatus() {
        return status;
    }

    public void setStatus(TXN_STATUS status) {
        this.status = status;
    }

    public TXN_MODE getMode() {
        return mode;
    }

    public void setMode(TXN_MODE mode) {
        this.mode = mode;
    }

    public SubEvent getSubEvent() {
        return subEvent;
    }

    public void setSubEvent(SubEvent subEvent) {
        this.subEvent = subEvent;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


    public enum TXN_TYPE {
        EXPENSE, CREDIT, DEBIT;
    }

    public enum TXN_SOURCE {
        CASH, PAYPAL, VENMO, ZELLE, CLUB_BUDGET, OTHERS;
    }

    public enum TXN_STATUS {
        PENDING, CANCELED, ACKNOWLEDGED, SETTLED;
    }

    public enum TXN_MODE {
        INDIVIDUAL, GROUP;
    }

}

