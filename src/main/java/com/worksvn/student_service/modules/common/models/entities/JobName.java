package com.worksvn.student_service.modules.common.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "job_name")
public class JobName {
    public static final String ID = "id";
    public static final String JOB_GROUP = "jobGroup";
    public static final String NAME = "name";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_id")
    private JobGroup jobGroup;

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

    public JobGroup getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(JobGroup jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JobName && ((JobName) obj).id == this.id;
    }
}
