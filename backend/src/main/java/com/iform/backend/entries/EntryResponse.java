package com.iform.backend.entries;

public record EntryResponse(boolean validated, boolean saved, String message) {
}
