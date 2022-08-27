package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }

//    @GetMapping("/{id}")//detalhando topicos
//    public TopicoDto detalhar(@PathVariable Long id) {
//
//        Topico topico = topicoRepository.getOne(id);
//
//        return new TopicoDto(topico);
//
//    }

//    @GetMapping("/{id}")//detalhando topicos
//    public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
//
//        Topico topico = topicoRepository.getReferenceById(id);
////        topicoRepository.getOne(id) --> o getOne está deprecated.
//
//        return new DetalhesDoTopicoDto(topico);
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")//Vai atualizar novos dados de um tópico
    @Transactional//avisa ao Spring que é pra commitar uma transação no final do método
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {

        Optional<Topico> optional = topicoRepository.findById(id);

        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            //corpo que será devolvido como resposta pelo servidor
            return ResponseEntity.ok(new TopicoDto(topico));


        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")//Vai deletar um tópico
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);

        if (topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
//    @RequestBody -> corpo da requisição;  é para pegar do corpo da requisição, e
//    não das URLs, como parâmetro de URL.
//    @Valid --> Rodar as validações do bean validation
// obs! é de suma importancia colocar a transação nos métodos adicionar, deletar e atualizar;
//  Vai que troco de provedor ou banco de dados e para de funcionar. É bom garantir que esses
//  três métodos rodarão dentro de uma transação.
}
