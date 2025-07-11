package de.lehrcode.burnafterreading.store;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notes")
public class Note {
    @Id
    private Long id;

    @Transient
    private String key;

    @ToString.Exclude
    @Column("key_hash")
    private byte[] keyHash;

    @ToString.Exclude
    @Column("enc_content")
    private byte[] encryptedContent;

    @ToString.Exclude
    @Transient
    private String content;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;
}
