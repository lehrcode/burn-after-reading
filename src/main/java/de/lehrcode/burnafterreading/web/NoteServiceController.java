package de.lehrcode.burnafterreading.web;

import de.lehrcode.burnafterreading.service.NoteService;
import de.lehrcode.burnafterreading.store.Note;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Tag(name = "Note Service Controller")
@RestController
@RequiredArgsConstructor
public class NoteServiceController {
    public static final String TEXT_PLAIN_UTF8 = "text/plain;charset=UTF-8";
    private final NoteService noteService;

    @Operation(summary = "createNote", description = "Create and store a new note")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Note created")})
    @PostMapping(path = "/api/v1/notes", produces = TEXT_PLAIN_UTF8, consumes = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> createNote(@RequestBody String content) {
        logger.debug("createNote(content={})", content);
        Note note = noteService.createNote(content);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                                                                 .pathSegment(note.getKey())
                                                                 .build()
                                                                 .toUri())
                             .contentType(MediaType.parseMediaType(TEXT_PLAIN_UTF8))
                             .body(note.getKey());
    }

    @Operation(summary = "getNote")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Note decrypted"),
        @ApiResponse(responseCode = "410", description = "Note does not exist any longer or never existed"),
    })
    @GetMapping(path = "/api/v1/notes/{key}", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> getNote(@PathVariable String key) {
        logger.debug("getNote(key={})", key);
        Optional<Note> foundNote = noteService.getNote(key);
        return foundNote.map(note -> ResponseEntity.ok()
                                                   .contentType(MediaType.parseMediaType(TEXT_PLAIN_UTF8))
                                                   .body(note.getContent()))
                        .orElseGet(ResponseEntity.status(HttpStatus.GONE)::build);
    }

    @Operation(summary = "deleteNote")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Note decrypted and deleted"),
        @ApiResponse(responseCode = "410", description = "Note does not exist any longer or never existed"),
    })
    @DeleteMapping(path = "/api/v1/notes/{key}", produces = TEXT_PLAIN_UTF8)
    public ResponseEntity<String> deleteNote(@PathVariable String key) {
        logger.debug("deleteNote(key={})", key);
        Optional<Note> foundNote = noteService.deleteNote(key);
        return foundNote.map(note -> ResponseEntity.ok()
                                                   .contentType(MediaType.parseMediaType(TEXT_PLAIN_UTF8))
                                                   .body(note.getContent()))
                        .orElseGet(ResponseEntity.status(HttpStatus.GONE)::build);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleException(ConstraintViolationException ex) {
        logger.error("handleException(ex=...)", ex);
        return ex.getMessage();
    }
}
