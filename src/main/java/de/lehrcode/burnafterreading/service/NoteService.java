package de.lehrcode.burnafterreading.service;

import de.lehrcode.burnafterreading.store.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Optional;

public interface NoteService {
    Note createNote(@NotBlank @Size(max = 65_535) String content);
    Optional<Note> getNote(@NotBlank String key);
    Optional<Note> deleteNote(@NotBlank String key);
}
