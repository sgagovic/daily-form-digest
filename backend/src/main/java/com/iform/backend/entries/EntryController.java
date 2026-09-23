package com.iform.backend.entries;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entries")
public class EntryController {
    @PostMapping
    public EntryResponse submit(@Valid @RequestBody EntryRequest request) {
        // Validation is the first milestone; never imply persistence before storage exists.
        return new EntryResponse(true, false, "Validation passed. Database storage is not connected yet.");
    }
}
