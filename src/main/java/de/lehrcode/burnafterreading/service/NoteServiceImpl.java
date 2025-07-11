package de.lehrcode.burnafterreading.service;

import de.lehrcode.burnafterreading.store.Note;
import de.lehrcode.burnafterreading.store.NoteRepository;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final Cryptor cryptor;

    @Override
    public Note createNote(String content) {
        logger.debug("createNote(content={})", content);
        SecretKey secretKey = cryptor.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        logger.debug("{}", keyBytes);
        var keyHash = cryptor.hash(keyBytes);
        byte[] encryptedContent = cryptor.encrypt(content.getBytes(StandardCharsets.UTF_8), secretKey);
        Note note = Note.builder()
                        .withKey(Base64.getUrlEncoder()
                                       .withoutPadding()
                                       .encodeToString(keyBytes))
                        .withKeyHash(keyHash)
                        .withEncryptedContent(encryptedContent)
                        .build();
        note = noteRepository.save(note);

        return note;
    }

    @Override
    public Optional<Note> getNote(String key) {
        logger.debug("getNote(key={})", key);
        var keyBytes = Base64.getUrlDecoder().decode(key);
        logger.debug("{}", keyBytes);
        var keyHash = cryptor.hash(keyBytes);
        SecretKey secretKey = cryptor.parseKey(keyBytes);
        var foundNote = noteRepository.findByKeyHash(keyHash);
        foundNote.map(note -> {
            note.setKey(key);
            note.setContent(new String(cryptor.decrypt(note.getEncryptedContent(), secretKey), StandardCharsets.UTF_8));
            return note;
        });
        return foundNote;
    }

    @Override
    public Optional<Note> deleteNote(String key) {
        logger.debug("deleteNote(key={})", key);
        var foundNote = getNote(key);
        foundNote.ifPresent(noteRepository::delete);
        return foundNote;
    }
}
