package com.chasingdns.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="sub_events")
public class SubEvent {
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
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date submittedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Event event;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public enum EVENT_TYPE {
        TOURNAMENT_REGISTRATION,
        GAME_EXPENSE,
        CLUB_EXPENSE,
        GROUND_FEES,
        EQUIPMENT_PURCHASE,
        OTHERS;
    }

    public enum EVENT_STATUS {
        IN_PROGRESS, COMPLETED, CANCELLED;
    }

}

