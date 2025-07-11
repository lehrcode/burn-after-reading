# Burn After Reading

Small secret notes exchange application with RESTful API
and simple user interface

```plain
POST /api/v1/notes (Create note)
    consumes: text/plain (Content of note)
    produces: text/plain (Generated key)
GET /api/v1/notes/{key} (Peek note)
    produces: text/plain (Content of note)
DELETE /api/v1/notes/{key} (Read and delete note)
    produces: text/plain (Content of note)
```

**Bonus-Features:**
- UI with JavaScript
- Encryption of note content when stored in database
- Deletion of notes after 5 min.

## License

[MIT](LICENSE)
