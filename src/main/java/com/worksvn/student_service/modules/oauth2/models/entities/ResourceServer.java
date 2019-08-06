package com.worksvn.student_service.modules.oauth2.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "resource_server")
public class ResourceServer {

    @Id
    @Column(name = "resource_id")
    private String resourceID;

    public ResourceServer() {
    }

    public ResourceServer(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }
}

