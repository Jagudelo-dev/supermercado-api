package com.josemiguelagudelo.supermercado_api.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en la base de datos.
 * Capturada por {@link ManejadorGlobalExcepciones} para retornar HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}