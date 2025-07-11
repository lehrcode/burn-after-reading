package de.lehrcode.burnafterreading.store;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends CrudRepository<Note, Long> {
    @Query("SELECT id,key_hash,enc_content,created_at FROM notes WHERE key_hash = :key_hash")
    Optional<Note> findByKeyHash(@Param("key_hash") byte[] keyHash);
}
