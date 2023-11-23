package com.example.servingwebcontent.models;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "account_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "currency")
    private String currency;
    @Column(name = "money_available")
    private int moneyAvailable;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Person owner;

    public Account() {
    }

    public Account(String currency, int moneyAvailable, Person owner) {
        this.currency = currency;
        this.moneyAvailable = moneyAvailable;
        this.owner = owner;
    }

    public Account(String currency, int moneyAvailable) {
        this.currency = currency;
        this.moneyAvailable = moneyAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getMoneyAvailable() {
        return moneyAvailable;
    }

    public void setMoneyAvailable(int moneyAvailable) {
        this.moneyAvailable = moneyAvailable;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
