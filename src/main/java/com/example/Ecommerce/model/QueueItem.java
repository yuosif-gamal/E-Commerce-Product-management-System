package com.example.Ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QueueItem implements Comparable<QueueItem> {
    private LocalDateTime addedAt;
    private Long cartItemId;

    @Override
    public int compareTo(QueueItem other) {
        return this.addedAt.compareTo(other.addedAt);
    }
}
