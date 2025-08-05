package de.lehrcode.burnafterreading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.lehrcode.burnafterreading.store.Note;
import de.lehrcode.burnafterreading.store.NoteRepository;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NoteServiceImpl.class)
class NoteServiceImplTests {
    @MockitoBean
    NoteRepository noteRepositoryMock;

    @MockitoBean
    Cryptor cryptorMock;

    @Autowired
    NoteService noteService;

    @Test
    void createNote() {
        SecretKey expectedKey = new SecretKeySpec(new byte[] {1,2,3,4,5}, "AES");
        when(cryptorMock.generateKey()).thenReturn(new SecretKeySpec(new byte[] {1,2,3,4,5}, "AES"));
        when(cryptorMock.hash(any(byte[].class))).thenReturn(new byte[] {1,2,3,4,5});
        when(cryptorMock.encrypt(any(byte[].class), eq(expectedKey))).thenReturn(new byte[] {1,2,3,4,5});
        when(noteRepositoryMock.save(any())).thenAnswer(i -> i.getArgument(0));

        var note = noteService.createNote("hello");

        assertNotNull(note);

        verify(cryptorMock, times(1)).generateKey();
        verify(cryptorMock, times(1)).hash(any(byte[].class));
        verify(cryptorMock, times(1)).encrypt(any(byte[].class), eq(expectedKey));
        verify(noteRepositoryMock, times(1)).save(note);
    }

    @Test
    void getNote() {
        var keyBase64 = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";
        var keyHashBytes = new byte[] {1,2,3,4,5};
        var foundNote = Note.builder()
                            .withEncryptedContent(new byte[] {1,2,3,4,5})
                            .build();

        when(cryptorMock.hash(any(byte[].class))).thenReturn(keyHashBytes);
        when(cryptorMock.parseKey(any())).thenAnswer(i -> new SecretKeySpec(i.getArgument(0), "AES"));
        when(noteRepositoryMock.findByKeyHash(any())).thenReturn(Optional.of(foundNote));
        when(cryptorMock.decrypt(any(), any())).thenReturn("nachricht".getBytes());

        var note = noteService.getNote(keyBase64);

        assertNotNull(note);
        assertTrue(note.isPresent());
        assertEquals(keyBase64, note.get().getKey());
        assertEquals("nachricht", note.get().getContent());

        verify(cryptorMock, times(1)).hash(any(byte[].class));
        verify(cryptorMock, times(1)).parseKey(any(byte[].class));
        verify(noteRepositoryMock, times(1)).findByKeyHash(any());
        verify(cryptorMock, times(1)).decrypt(any(byte[].class),any());
    }

    @Test
    void deleteNote() {
        var keyBase64 = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";
        var keyHashBytes = new byte[] {1,2,3,4,5};
        var foundNote = Note.builder()
                            .withEncryptedContent(new byte[] {1,2,3,4,5})
                            .build();

        when(cryptorMock.hash(any(byte[].class))).thenReturn(keyHashBytes);
        when(cryptorMock.parseKey(any())).thenAnswer(i -> new SecretKeySpec(i.getArgument(0), "AES"));
        when(noteRepositoryMock.findByKeyHash(any())).thenReturn(Optional.of(foundNote));
        when(cryptorMock.decrypt(any(), any())).thenReturn("nachricht".getBytes());

        var note = noteService.deleteNote(keyBase64);

        assertNotNull(note);
        assertTrue(note.isPresent());
        assertEquals(keyBase64, note.get().getKey());
        assertEquals("nachricht", note.get().getContent());

        verify(cryptorMock, times(1)).hash(any(byte[].class));
        verify(cryptorMock, times(1)).parseKey(any(byte[].class));
        verify(noteRepositoryMock, times(1)).findByKeyHash(any());
        verify(cryptorMock, times(1)).decrypt(any(byte[].class),any());
        verify(noteRepositoryMock, times(1)).delete(any());
    }
}
