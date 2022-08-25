package br.com.alura.forum.config.validacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource; // ajuda a pegar mensagem de erro

    //interceptador que é configurado para identificar alguma axecption de qualquer controller, logo,
    // se caso tiver algum exception ele chama esse interceptador.
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)//vai retornar o erro 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {

        List<ErroDeFormularioDto> dto = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {

            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
            dto.add(erro);
        });

        return dto;

    }
//    o Handler é um interceptador, em que estou configurando o Spring para, sempre que houver
//    um erro - alguma exception em algum método de qualquer Controller - ele chamar automaticamente
//    o interceptador, passando a lista com todos os erros que aconteceram. Sendo assim, eu pego
//    essa lista e transformo no meu objeto ErroDeFormularioDto, que só tem o nome do campo e a
//    mensagem para simplificar.

}
