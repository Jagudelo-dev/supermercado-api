package com.josemiguelagudelo.supermercado_api.exception;

/**
 * Excepción lanzada cuando una operación viola una regla de negocio.
 * Capturada por {@link ManejadorGlobalExcepciones} para retornar HTTP 409.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
