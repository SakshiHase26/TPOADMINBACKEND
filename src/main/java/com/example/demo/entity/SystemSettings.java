package com.example.demo.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class SystemSettings {

    @Id
    private Long id = 1L;         // we’ll always use the single row with id=1

    private boolean allowRegistrations = true;
    private boolean freezeEdits = false;

    // getters & setters
    public Long getId() { return id; }
    public boolean isAllowRegistrations() { return allowRegistrations; }
    public void setAllowRegistrations(boolean allowRegistrations) {
        this.allowRegistrations = allowRegistrations;
    }
    public boolean isFreezeEdits() { return freezeEdits; }
    public void setFreezeEdits(boolean freezeEdits) {
        this.freezeEdits = freezeEdits;
    }
     public void setId(Long id) {
        this.id = id;
    }
}
