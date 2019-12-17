package com.worksvn.student_service.modules.student.models.entities;

import com.worksvn.common.base.json_converter.JsonMapConverter;
import com.worksvn.common.services.notification.models.NotificationType;
import com.worksvn.common.services.notification.models.UserNotification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "student_notification")
@NoArgsConstructor
@Getter
@Setter
public class StudentNotification {
    public static final String ID = "id";
    public static final String STUDENT_ID = "studentID";
    public static final String CREATED_DATE = "createdDate";

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id")
    private String id;
    @Column(name = "student_id")
    private String studentID;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "data")
    private Map<String, Object> data;

    @Column(name = "seen")
    private boolean seen;
    @Column(name = "created_date")
    private Date createdDate = new Date();

    public StudentNotification(NotificationType type, String body,
                               String candidateID,
                               Map<String, Object> data) {
        this(type, body, candidateID, data, new Date());
    }

    public StudentNotification(UserNotification notification) {
        this(notification.getType(), notification.getBody(), notification.getUserID(),
                notification.getData(), notification.getCreatedDate());
    }

    public StudentNotification(NotificationType type, String body,
                               String studentID,
                               Map<String, Object> data,
                               Date createdDate) {
        this.type = type;
        this.title = type.getTitle();
        this.body = body;
        this.studentID = studentID;
        this.data = data;
        this.seen = false;
        this.createdDate = createdDate;
    }
}
