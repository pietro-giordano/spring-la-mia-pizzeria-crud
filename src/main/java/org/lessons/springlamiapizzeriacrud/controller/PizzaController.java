package org.lessons.springlamiapizzeriacrud.controller;

import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizza")
public class PizzaController {

    @Autowired
    private PizzaRepository repository;

    @GetMapping
    public String index(Model model, @RequestParam(name = "keyword", required = false) String search) {
        // inizializzo pizza
        List<Pizza> pizza;
        // se non ho param prendo tutto
        if(search == null || search.isBlank()) {
            pizza = repository.findAll();
        } else {
            // altrimenti query personalizzata
            pizza = repository.findByNameContainingIgnoreCase(search);
        }
        model.addAttribute("pizzaList", pizza);
        // mando valore di search per occupare campo di input search
        model.addAttribute("search", search == null ? "" : search);
        return "/pizza/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable Integer id) {
        // recupero pizza con id corrispondente
        Optional<Pizza> pizzaId = repository.findById(id);
        // se presente la recupero e la passo come attributo col model
        if (pizzaId.isPresent()) {
            Pizza pizzaShow = pizzaId.get();
            model.addAttribute("pizza", pizzaShow);
            return "/pizza/show";
        // altrimenti lancio eccezione
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza inesistente");
        }
    }
}
