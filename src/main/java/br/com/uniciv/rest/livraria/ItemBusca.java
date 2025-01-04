package br.com.uniciv.rest.livraria;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "itemBusca")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemBusca {
	
	@XmlElement
    private Livro livro;

	@XmlElement
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links = new ArrayList<>();

    public void addLink(Link link) {
        this.links.add(link);
    }

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Link> getLinks() {
		return links;
	}
    
    
    
}