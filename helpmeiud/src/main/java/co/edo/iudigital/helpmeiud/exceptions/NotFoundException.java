package co.edo.iudigital.helpmeiud.exceptions;

/**
 * Exception de NotFound en el Rest
 * 
 */
public class NotFoundException extends RestException{
    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(ErrorDto errorDto) {
        super(errorDto);
    }
}
