package account.controller;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CustomErrorMessage(
        String timestamp,
        int status,
        String error,
        String message,
        String path) {
}
