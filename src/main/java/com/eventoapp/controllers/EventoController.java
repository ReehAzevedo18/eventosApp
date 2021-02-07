package com.eventoapp.controllers;

import javax.naming.Binding;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.IConvidadoRepository;
import com.eventoapp.repository.IEventoRepository;

@Controller
public class EventoController {

	@Autowired // injeção de dependencias
	private IEventoRepository er;
	
	@Autowired
	private IConvidadoRepository cr;

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}

	@RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
	public String form(Evento evento) {

		er.save(evento);
		return "redirect:/cadastrarEvento";
	}

	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index"); // redenrizar a view index.html
		Iterable<Evento> eventos = er.findAll();
		mv.addObject("eventos", eventos); // primeira prop: nome do foreach do index.html, segunda prop: nom da lista de
											// eventos
		return mv;
	}

	@RequestMapping(value = "/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento evento = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		
		//Lista de convidados gerada automaticamente
		Iterable<Convidado> convidados = cr.findByEvento(evento);
		mv.addObject("convidados", convidados);

		return mv;

	}
	
	  @RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	  //BindingResult result, RedirectAttributes attributes, servem para verificar  as validações e mostrar a mensagem caso esteja errado 
	  public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado,BindingResult result, RedirectAttributes attributes) { 
			/*
			 * if(result.hasErrors()) {
			 * attributes.addFlashAttribute("mensagem","Verifique os campos!"); }
			 */
		  Evento evento = er.findByCodigo(codigo); //é preciso add um evento antes de inserir um convidado 
		  convidado.setEvento(evento); //inserindo o evento no convidado
		  cr.save(convidado);
		/*
		 * attributes.addFlashAttribute("mensagem","Convidado adicionado com sucesso!");
		 */
	  return "redirect:/{codigo}"; 
	  }
	 
	  @RequestMapping("/deletar")
	  public String deletarEvento(long codigo) {
		  Evento evento = er.findByCodigo(codigo);
		  er.delete(evento);
		  return "redirect:/eventos";
	  }
	  
	  @RequestMapping("/deletarConvidado")
	  public String deletarConvidado(String RG) {
		  Convidado convidado = cr.findByRG(RG);
		  cr.delete(convidado);
		  
		  //Para retornar pro evento que teve o convidado deletado
		  Evento evento = convidado.getEvento();
		  long codigoEv = evento.getCodigo();
		  String codigo = "" + codigoEv;
		  
		  return "redirect:/" + codigo;
	  }
}
