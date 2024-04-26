package edu.soulcode.elas_help.controller;

import edu.soulcode.elas_help.model.ClienteDTO;
import edu.soulcode.elas_help.service.ClienteService;
import edu.soulcode.elas_help.util.ReferencedWarning;
import edu.soulcode.elas_help.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(final ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "cliente/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cliente") final ClienteDTO clienteDTO) {
        return "cliente/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cliente") @Valid final ClienteDTO clienteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cliente/add";
        }
        clienteService.create(clienteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cliente.create.success"));
        return "redirect:/clientes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("cliente", clienteService.get(id));
        return "cliente/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("cliente") @Valid final ClienteDTO clienteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cliente/edit";
        }
        clienteService.update(id, clienteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cliente.update.success"));
        return "redirect:/clientes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = clienteService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            clienteService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cliente.delete.success"));
        }
        return "redirect:/clientes";
    }

}
