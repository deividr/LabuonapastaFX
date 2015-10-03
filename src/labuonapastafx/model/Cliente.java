package labuonapastafx.model;

import java.io.Serializable;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private IntegerProperty clieId;
    private StringProperty nome, telefone, endereco;
    private ObjectProperty<Date> dataCriacao;

    public Cliente() {
        this(0, "", "", "", new Date());
    }

    public Cliente(int clieId, String nome, String telefone, String endereco, Date dataCriacao) {
        this.clieId = new SimpleIntegerProperty(clieId);
        this.nome = new SimpleStringProperty(nome);
        this.telefone = new SimpleStringProperty(telefone);
        this.endereco = new SimpleStringProperty(endereco);
        this.dataCriacao = new SimpleObjectProperty<>(dataCriacao);
    }

    @Override
    public String toString() {
        return "O cliente " + getNome() + " é cliente desde " + getDataCriacao();
    }

    @Override
    public boolean equals(Object o) {
		//se o objeto recebido for diferente de null e a sua classe for um Produto verifica
        //igualdade no codigo do usuario.
        if (o != null && o.getClass() == this.getClass()) {
            Cliente prod = (Cliente) o;
            if (prod.getClieId() == this.getClieId()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getNome().hashCode();
    }

	public final IntegerProperty clieIdProperty() {
		return this.clieId;
	}
	

	public final int getClieId() {
		return this.clieIdProperty().get();
	}
	

	public final void setClieId(final int clieId) {
		this.clieIdProperty().set(clieId);
	}
	

	public final StringProperty nomeProperty() {
		return this.nome;
	}
	

	public final String getNome() {
		return this.nomeProperty().get();
	}
	

	public final void setNome(final java.lang.String nome) {
		this.nomeProperty().set(nome);
	}
	

	public final StringProperty telefoneProperty() {
		return this.telefone;
	}
	

	public final String getTelefone() {
		return this.telefoneProperty().get();
	}
	

	public final void setTelefone(final String telefone) {
		this.telefoneProperty().set(telefone);
	}
	

	public final StringProperty enderecoProperty() {
		return this.endereco;
	}
	

	public final String getEndereco() {
		return this.enderecoProperty().get();
	}
	

	public final void setEndereco(final String endereco) {
		this.enderecoProperty().set(endereco);
	}

	public final ObjectProperty<Date> dataCriacaoProperty() {
		return this.dataCriacao;
	}
	

	public final Date getDataCriacao() {
		return this.dataCriacaoProperty().get();
	}
	

	public final void setDataCriacao(final Date dataCriacao) {
		this.dataCriacaoProperty().set(dataCriacao);
	}
	
}
