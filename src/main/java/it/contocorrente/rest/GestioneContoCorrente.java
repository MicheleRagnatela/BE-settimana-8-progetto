package it.contocorrente.rest;


import java.util.ArrayList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import it.contocorrente.pojo.ContoCorrente;
import it.contocorrente.pojo.Movimento;




@Path("/contocorrente")
public class GestioneContoCorrente {

	private static List<ContoCorrente> contocorr = new ArrayList<ContoCorrente>();
	
	private static List<Movimento> movimenti = new ArrayList<Movimento>();
	
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteConto(ContoCorrente c) {
		for(ContoCorrente cr : contocorr) {
			if(cr.getIban().equals(c.getIban())) {
				contocorr.remove(c);
				break;
			}
		}
		return Response.status(200).entity("*** Conto eliminato correttamente ***").build();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertConto(ContoCorrente c) {
		contocorr.add(c);
		return Response.status(200).entity("*** Conto inserito correttamente ***").build();
		}
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateConto(ContoCorrente c) {
		for(ContoCorrente cr : contocorr) {
			if(cr.getIban().equals(c.getIban())) {
				int index = contocorr.lastIndexOf(cr);
				contocorr.set(index, c);
			}
		}
		return Response.status(200).entity("*** Aggiornamento avvenuto con successo ***").build();
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContoCorrente> retrieveConto() {
		
		return contocorr;
	}
	
	
	@PUT
	@Path("/preleva/{iban}/{importo}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response prelevaConto(@PathParam("iban")String iban, @PathParam("importo") double importo, ContoCorrente c) {
		
		for(ContoCorrente cr : contocorr) {
			if(cr.getIban().equals(iban) && cr.getSaldo() > importo) {
				
				int index = contocorr.lastIndexOf(cr);
				contocorr.set(index, c);
				c.setSaldo(cr.getSaldo() - importo);
				
				
				Movimento m = new Movimento();
				m.setIban(iban);
				m.setImporto(importo);
				m.setTipoMovimento("prelievo");
				movimenti.add(m);
			}
		}
		return Response.status(200).entity("*** Prelievo effettuato ***").build();
	}
	
	@PUT
	@Path("/versa/{iban}/{importo}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response versaConto(@PathParam("iban")String iban, @PathParam("importo") double importo, ContoCorrente c) {
		
		for(ContoCorrente cr : contocorr) {
			if(cr.getIban().equals(iban)) {
				
				int index = contocorr.lastIndexOf(cr);
				contocorr.set(index, c);
				c.setSaldo(cr.getSaldo() + importo);
				
				Movimento m = new Movimento();
				m.setIban(iban);
				m.setImporto(importo);
				m.setTipoMovimento("versamento");
				movimenti.add(m);
			}
		}
		return Response.status(200).entity("*** Versamento effettuato ***").build();
	}
	
	@GET
	@Path("/movimenti")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movimento> retrieveMovimenti() {
		
		return movimenti;
	}
}
