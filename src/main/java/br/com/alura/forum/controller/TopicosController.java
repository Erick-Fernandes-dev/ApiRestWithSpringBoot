package br.com.alura.forum.controller;

import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos")//todos os métodos começarão com /topicos
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

//    @RequestMapping("/topicos")
////    @ResponseBody
//    public List<TopicoDto> lista() {
//
////        Topico topico =
////                new Topico("Dúvida", "Duvida com Spring",
////                new Curso("Curso Spring", "Programação"));
//        List<Topico> topicos = topicoRepository.findAll();
//
//        return TopicoDto.converter(topicos);
//
//    }


    @GetMapping//Vai listar todos os topicos
    public List<TopicoDto> lista(String nomeCurso) {
        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDto.converter(topicos);
        }
        else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return  TopicoDto.converter(topicos);
        }
    }


    @PostMapping//Vai adicionar topicos
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }
//    @RequestBody -> corpo da requisição;  é para pegar do corpo da requisição, e
//    não das URLs, como parâmetro de URL.


}
