package es.grupocmc.exceptionhandlerutil.util;

import es.grupocmc.commons.error.StatusError;
import org.springframework.http.HttpStatus;

public class StatusErrorResponse {

    private StatusErrorResponse() {
    }

    public static StatusError getStatus(int status) {
        String title = HttpStatus.valueOf(status).getReasonPhrase();
        String code = String.format("BP%s", status);
        String detail = getDetail(status, title);
        return StatusError.builder().status(status).title(title).code(code).detail(detail).build();
    }

    private static String getDetail(int status, String title) {
        switch (status) {
            case HttpCodeExceptions.STATUS_BAD_REQUEST:
                return "Error en la solicitud";
            case HttpCodeExceptions.STATUS_UNAUTHORIZED:
                return "Credenciales incorrectas";
            case HttpCodeExceptions.STATUS_FORBIDDEN:
                return "No tiene permisos para acceder al recurso";
            case HttpCodeExceptions.STATUS_NOT_FOUND:
                return "Recurso no encontrado";
            case HttpCodeExceptions.STATUS_CONFLICT:
                return "El estado del recurso presenta conflictos con los datos de la solicitud";
            case HttpCodeExceptions.STATUS_BAD_GATEWAY:
                return "Error en la respuesta del proveedor del servicio";
            case HttpCodeExceptions.STATUS_SERVICE_UNAVAILABLE:
                return "Servicio no disponible";
            case HttpCodeExceptions.STATUS_GATEWAY_TIMEOUT:
                return "Tiempo de respuesta del proveedor del servicio excedido";
            case HttpCodeExceptions.STATUS_INTERNAL_SERVER_ERROR:
                return "Error interno del servidor";
            default:
                return title;
        }
    }
}
