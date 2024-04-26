package edu.soulcode.elas_help.controller;

import edu.soulcode.elas_help.model.Setor;
import edu.soulcode.elas_help.model.TecnicoDTO;
import edu.soulcode.elas_help.service.TecnicoService;
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
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(final TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("setorValues", Setor.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("tecnicoes", tecnicoService.findAll());
        return "tecnico/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("tecnico") final TecnicoDTO tecnicoDTO) {
        return "tecnico/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("tecnico") @Valid final TecnicoDTO tecnicoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tecnico/add";
        }
        tecnicoService.create(tecnicoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tecnico.create.success"));
        return "redirect:/tecnicos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("tecnico", tecnicoService.get(id));
        return "tecnico/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("tecnico") @Valid final TecnicoDTO tecnicoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tecnico/edit";
        }
        tecnicoService.update(id, tecnicoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tecnico.update.success"));
        return "redirect:/tecnicos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = tecnicoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            tecnicoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("tecnico.delete.success"));
        }
        return "redirect:/tecnicos";
    }

}
