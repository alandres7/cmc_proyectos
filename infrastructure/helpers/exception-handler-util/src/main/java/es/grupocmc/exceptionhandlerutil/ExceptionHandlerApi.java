package es.grupocmc.exceptionhandlerutil;

import es.grupocmc.commons.error.ErrorMessage;
import es.grupocmc.commons.error.ErrorResponse;
import es.grupocmc.commons.error.StatusError;
import es.grupocmc.commons.exception.ExistsException;
import es.grupocmc.commons.exception.NotContentException;
import es.grupocmc.commons.exception.NotFoundException;
import es.grupocmc.exceptionhandlerutil.util.HttpCodeExceptions;
import es.grupocmc.exceptionhandlerutil.util.StatusErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@Order(-2)
public class ExceptionHandlerApi {

    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> webExchangeBindException
            (ServerHttpRequest request, WebExchangeBindException exception) {
        this.logShow(request, exception, "warn");
        List<String> collectErrors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .limit(1)
                .map(this::getMessageProperties)
                .collect(Collectors.toList());
        StatusError statusError = StatusErrorResponse.getStatus(HttpCodeExceptions.STATUS_BAD_REQUEST);
        statusError.setDetail(collectErrors.get(0));
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors).build())
                .map(errorResponse -> getBodyHttpHeader(request, errorResponse));
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> notFoundException
            (ServerHttpRequest request, NotFoundException exception) {
        this.logShow(request, exception, "warn");
        StatusError statusError = StatusErrorResponse.getStatus(HttpCodeExceptions.STATUS_NOT_FOUND);
        statusError.setDetail(exception.getMessage());
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors)
                        .build())
                .map(errorResponse -> getBodyHttpHeader(request, errorResponse));
    }

    @ResponseBody
    @ExceptionHandler(ExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> existsException
            (ServerHttpRequest request, ExistsException exception) {
        this.logShow(request, exception, "warn");
        StatusError statusError = StatusErrorResponse.getStatus(HttpCodeExceptions.STATUS_CONFLICT);
        statusError.setDetail(exception.getMessage());
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors)
                        .build())
                .map(errorResponse -> getBodyHttpHeader(request, errorResponse));
    }

    @ResponseBody
    @ExceptionHandler(NotContentException.class)
    public Mono<ResponseEntity<ErrorResponse>> notContentException
            (ServerHttpRequest request, NotContentException exception) {
        this.logShow(request, exception, "warn");
        StatusError statusError = StatusErrorResponse.getStatus(HttpCodeExceptions.STATUS_NOT_CONTENT);
        statusError.setDetail(exception.getMessage());
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors)
                        .build())
                .map(errorResponse -> ResponseEntity
                                            .status(HttpStatus.valueOf(errorResponse.getStatus()))
                                            .headers(httpHeaders -> httpHeaders.add("result",
                                                    errorResponse.getErrorMessages().get(0).getDetail()))
                                            .body(errorResponse));
    }

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public Mono<ResponseEntity<ErrorResponse>> nullPointerException
            (ServerHttpRequest request, NullPointerException exception) {
        this.logShow(request, exception, "error");
        StatusError statusError = StatusErrorResponse.getStatus(HttpCodeExceptions.STATUS_INTERNAL_SERVER_ERROR);
        statusError.setDetail(exception.getMessage());
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors)
                        .build())
                .map(errorResponse -> getBodyHttpHeader(request, errorResponse));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> globalException
            (ServerHttpRequest request, Exception exception) {
        this.logShow(request, exception, "error");
        StatusError statusError = getStatusError(exception);
        return Mono.just(exception)
                .map(e -> ErrorMessage.builder()
                        .code(statusError.getCode())
                        .detail(statusError.getDetail())
                        .build())
                .map(this::getErrorMessages)
                .map(errors -> ErrorResponse.builder()
                        .title(statusError.getTitle())
                        .status(statusError.getStatus())
                        .errorMessages(errors).build())
                .map(errorResponse -> getBodyHttpHeader(request, errorResponse));
    }

    private StatusError getStatusError(Exception exception) {
        final int maxSubstring = 3;
        String substring = exception.getMessage().substring(0, maxSubstring);
        int status = HttpCodeExceptions.STATUS_INTERNAL_SERVER_ERROR;
        String detail = "";
        String exceptionMessage = exception.getMessage();
        try {
            status = Integer.parseInt(substring);
            String[] parts = exceptionMessage.split("\"");
            detail = parts[1];
        } catch (RuntimeException e) {
        }
        StatusError statusError = StatusErrorResponse.getStatus(status);
        if (!ObjectUtils.isEmpty(detail)) {
            statusError.setDetail(detail);
        } else {
            statusError.setDetail(exceptionMessage);
        }
        return statusError;
    }

    private ResponseEntity<ErrorResponse> getBodyHttpHeader(ServerHttpRequest request,
                                                            ErrorResponse errorResponse) {
        return ResponseEntity
                .status(HttpStatus.valueOf(errorResponse.getStatus()))
                .body(errorResponse);
    }

    private String getMessageProperties(ObjectError err) {
        Optional<Object> first = Arrays.stream(err.getArguments()).findFirst();
        String property = "";
        if (first.isPresent()) {
            DefaultMessageSourceResolvable defaultMessageSourceResolvable =
                    (DefaultMessageSourceResolvable) first.get();
            property = defaultMessageSourceResolvable.getDefaultMessage();
        }
        return String.format("La propiedad %s %s",
                property,
                err.getDefaultMessage());
    }

    private List<ErrorMessage> getErrorMessages(ErrorMessage error) {
        List<ErrorMessage> errors = new ArrayList<>();
        errors.add(error);
        return errors;
    }

    private void logShow(ServerHttpRequest request, Exception exception, String typeLog) {
        String message = String.format("MESSAGE: %s, CLASS: %s, PATH: %s, METHOD: %s",
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                request.getPath().pathWithinApplication().value(),
                request.getMethodValue());
        switch (typeLog) {
            case "warn":
                log.warn(message);
                break;
            default:
                log.error(message);
                break;
        }
    }



}
