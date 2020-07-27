package com.sangdaero.walab.common.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Notification extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 255)
    private String message;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable=true)
    private Request request;

    @Builder
    public Notification(Long id, User user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }
}
