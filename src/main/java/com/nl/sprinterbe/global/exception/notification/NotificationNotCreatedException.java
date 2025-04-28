package com.nl.sprinterbe.global.exception.notification;

import com.nl.sprinterbe.global.exception.schedule.ScheduleException;
import org.springframework.http.HttpStatus;

public class NotificationNotCreatedException extends ScheduleException {
    private static final String ERROR_CODE = "500";
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "알림이 정상적으로 생성되지 않았습니다.";

    public NotificationNotCreatedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

    public NotificationNotCreatedException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
