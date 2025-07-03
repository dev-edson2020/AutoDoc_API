package com.autodoc.dto;

import java.util.Date;

public class UsuarioDTO {
    private String plan;
    private Date subscriptionExpires;
    private String fullName;
    private String email;
    private Boolean disabled;
    private Boolean isVerified;
    private String appId;
    private String appRole;
    private String role;

    // Getters and Setters
    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getSubscriptionExpires() {
        return subscriptionExpires;
    }

    public void setSubscriptionExpires(Date subscriptionExpires) {
        this.subscriptionExpires = subscriptionExpires;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppRole() {
        return appRole;
    }

    public void setAppRole(String appRole) {
        this.appRole = appRole;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}