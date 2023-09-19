package com.github.furkanzhlp.orbalance.player;

import java.util.UUID;

public class PlayerData {
    private Double balance;
    private final UUID playerUUID;
    public PlayerData(UUID playerUUID,Double balance){
        this.playerUUID = playerUUID;
        this.balance = balance;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = Math.max(0,balance);
    }
    public void addBalance(Double amount){
        this.balance = Math.max((this.balance+amount),0);
    }
    public void removeBalance(Double amount){
        this.balance = Math.max((this.balance-amount),0);
    }
}
