package br.com.uniciv.rest.livraria;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("livro")
public class LivroResource {
	
	private LivroRepositorio livroRepo = LivroRepositorio.getInstance();
	
	@GET
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Livros getLivros() {
		Livros livros = new Livros();
		livros.setLivros(livroRepo.getLivros());
		return livros;
	}
	
	@GET
	@Path("/{isbn}")
	public Livro getLivroPorIsbn(@PathParam("isbn") String isbn) {
		try {
		    return livroRepo.getLivroPorIsbn(isbn);
		} catch (LivroNaoEncontradoException e) {
		    throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Response criaLivro(Livro livro) {
		try {
		    livroRepo.adicionaLivro(livro);
		} catch (LivroExistenteException e) {
		    throw new WebApplicationException(Status.CONFLICT);
		}
	    livroRepo.adicionaLivro(livro);
	    URI uriLocation = UriBuilder.fromPath("livro/{isbn}").build(livro.getIsbn());
	    return Response.created(uriLocation).entity(livro).build();
	}
	
	@PUT
	@Path("/{isbn}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response atualizaLivro(@PathParam("isbn") String isbn, Livro livro) {
	    try {
	        Livro livroDoEstoque = livroRepo.getLivroPorIsbn(isbn);

	        livroDoEstoque.setAutores(livro.getAutores());
	        livroDoEstoque.setGenero(livro.getGenero());
	        livroDoEstoque.setPreco(livro.getPreco());
	        livroDoEstoque.setTitulo(livro.getTitulo());

	        livroRepo.atualizaLivro(livroDoEstoque);
	    } catch (LivroNaoEncontradoException e) {
		    throw new WebApplicationException(Status.NOT_FOUND);
		}    
	    return Response.ok().entity(livro).build();
	}
	
}
