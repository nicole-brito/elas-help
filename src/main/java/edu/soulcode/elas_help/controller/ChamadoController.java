package edu.soulcode.elas_help.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.soulcode.elas_help.domain.Cliente;
import edu.soulcode.elas_help.domain.Tecnico;
import edu.soulcode.elas_help.model.ChamadoDTO;
import edu.soulcode.elas_help.repos.ClienteRepository;
import edu.soulcode.elas_help.repos.TecnicoRepository;
import edu.soulcode.elas_help.service.ChamadoService;
import edu.soulcode.elas_help.util.CustomCollectors;
import edu.soulcode.elas_help.util.JsonStringFormatter;
import edu.soulcode.elas_help.util.WebUtils;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final ObjectMapper objectMapper;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoController(final ChamadoService chamadoService, final ObjectMapper objectMapper,
            final ClienteRepository clienteRepository, final TecnicoRepository tecnicoRepository) {
        this.chamadoService = chamadoService;
        this.objectMapper = objectMapper;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @InitBinder
    public void jsonFormatting(final WebDataBinder binder) {
        binder.addCustomFormatter(new JsonStringFormatter<List<String>>(objectMapper) {
        }, "setor");
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("clienteValues", clienteRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cliente::getId, Cliente::getNome)));
        model.addAttribute("tecnicoValues", tecnicoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Tecnico::getId, Tecnico::getNome)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("chamadoes", chamadoService.findAll());
        return "chamado/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("chamado") final ChamadoDTO chamadoDTO) {
        return "chamado/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("chamado") @Valid final ChamadoDTO chamadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamado/add";
        }
        chamadoService.create(chamadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("chamado.create.success"));
        return "redirect:/chamados";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("chamado", chamadoService.get(id));
        return "chamado/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("chamado") @Valid final ChamadoDTO chamadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "chamado/edit";
        }
        chamadoService.update(id, chamadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("chamado.update.success"));
        return "redirect:/chamados";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        chamadoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("chamado.delete.success"));
        return "redirect:/chamados";
    }

}
