package org.marshallbaby.julaasyncconnector.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID taskId;

    @Lazy
    private String requestPayload;

    private String responsePayload;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private TaskStatus taskStatus;

    @JsonIgnore
    private LocalDateTime creationTimestamp;

}
