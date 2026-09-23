package com.iform.backend.entries;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EntryRequest(
        @NotBlank(message = "Please enter your name.")
        @Size(max = 120, message = "Name must be 120 characters or fewer.")
        String name,

        @NotBlank(message = "Please enter your email.")
        @Email(message = "Please enter a valid email address.")
        @Size(max = 254, message = "Email must be 254 characters or fewer.")
        String email,

        @Size(max = 160, message = "Company must be 160 characters or fewer.")
        String company,

        @NotBlank(message = "Please enter a message.")
        @Size(max = 5000, message = "Message must be 5,000 characters or fewer.")
        String message,

        @Pattern(regexp = "direct|website-1|website-2", message = "Unknown website source.")
        String source
) {
}
