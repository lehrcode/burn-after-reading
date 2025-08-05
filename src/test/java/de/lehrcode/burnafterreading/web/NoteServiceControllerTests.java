package de.lehrcode.burnafterreading.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.lehrcode.burnafterreading.service.NoteService;
import de.lehrcode.burnafterreading.store.Note;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = NoteServiceController.class)
class NoteServiceControllerTests {
    @MockitoBean
    NoteService noteServiceMock;

    @Autowired
    MockMvc mockMvc;

    @Test
    void createNote() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";
        var note = Note.builder()
                       .withKey(key)
                       .build();
        when(noteServiceMock.createNote(any())).thenReturn(note);

        mockMvc.perform(post("/api/v1/notes").contentType(NoteServiceController.TEXT_PLAIN_UTF8).content("Nachricht!!!"))
               .andExpect(status().isCreated())
               .andExpect(header().string("Content-Type", NoteServiceController.TEXT_PLAIN_UTF8))
               .andExpect(header().exists("Location"))
               .andExpect(content().string(key));
    }

    @Test
    void getNote() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";
        var note = Note.builder()
                       .withKey(key)
                       .withContent("Nachricht!!!")
                       .build();

        when(noteServiceMock.getNote(eq(key))).thenReturn(Optional.of(note));

        mockMvc.perform(get("/api/v1/notes/" + key))
               .andExpect(status().isOk())
               .andExpect(header().string("Content-Type", NoteServiceController.TEXT_PLAIN_UTF8))
               .andExpect(content().string("Nachricht!!!"));

        verify(noteServiceMock, times(1)).getNote(eq(key));
    }

    @Test
    void getNote_GONE() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";

        when(noteServiceMock.getNote(eq(key))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/notes/" + key))
               .andExpect(status().isGone());

        verify(noteServiceMock, times(1)).getNote(eq(key));
    }

    @Test
    void getNote_BAD_REQUEST() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";

        when(noteServiceMock.getNote(eq(key))).thenThrow(ConstraintViolationException.class);

        mockMvc.perform(get("/api/v1/notes/" + key))
               .andExpect(status().isBadRequest());

        verify(noteServiceMock, times(1)).getNote(eq(key));
    }

    @Test
    void deleteNote() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";
        var note = Note.builder()
                       .withKey(key)
                       .withContent("Nachricht!!!")
                       .build();

        when(noteServiceMock.deleteNote(eq(key))).thenReturn(Optional.of(note));

        mockMvc.perform(delete("/api/v1/notes/" + key))
               .andExpect(status().isOk())
               .andExpect(header().string("Content-Type", NoteServiceController.TEXT_PLAIN_UTF8))
               .andExpect(content().string("Nachricht!!!"));

        verify(noteServiceMock, times(1)).deleteNote(eq(key));
    }

    @Test
    void deleteNote_GONE() throws Exception {
        var key = "RE3JsJBSTTDQER-h43xnuOsIbjfE5Uot";

        when(noteServiceMock.deleteNote(eq(key))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/notes/" + key))
               .andExpect(status().isGone());

        verify(noteServiceMock, times(1)).deleteNote(eq(key));
    }
}
