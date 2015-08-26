package labuonapastafx.model;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private StringProperty login, nomeCompleto, senha;
    private IntegerProperty userID;
    private ObjectProperty<Byte> ativo; 
    private ObjectProperty<AcessoEnum> tipoAcesso;
 
    /**
     * Construtor padrao
     */
    public Usuario() {
        this(0,"","",AcessoEnum.PEDIDO,"",(byte)0);
    }

    /**
     * Construtor com dados iniciais
     *
     * @param userID        Número único que identifica o usuario dentro do sistema
     * @param login         Login do usuário dentro do sistema
     * @param nomeCompleto  Nome completo do usuário
     * @param tipoAcesso    Perfil de acesso para esse usuário
     * @param senha         Senha cadastrada para ele
     * @param ativo         Informa se o usuário está ativo ou não no sistema
     */
    public Usuario(int userID, String login, String nomeCompleto, AcessoEnum tipoAcesso, String senha, byte ativo) {
        this.userID = new SimpleIntegerProperty(userID);
        this.login = new SimpleStringProperty(login);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.tipoAcesso = new SimpleObjectProperty<>(tipoAcesso);
        this.senha = new SimpleStringProperty(senha);
        this.ativo = new SimpleObjectProperty<>(ativo);
    }

    public int getUserID() {
        return userID.get();
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }
    
    public IntegerProperty userIDProperty() {
        return userID;
    }

    public String getSenha() {
        return senha.get();
    }

    public void setSenha(String senha) {
        this.senha.set(senha);
    }
    
    public StringProperty senhaProperty() {
        return senha;
    }

    public String getNomeCompleto() {
        return nomeCompleto.get();
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto.set(nomeCompleto);
    }
    
    /**
     * Obter o objeto Property do nome completo do usuário
     * 
     * @return Objeto StringProperty da propriedade NomeCompleto.
     */
    public StringProperty nomeCompletoProperty() {
        return nomeCompleto;
    }

    public AcessoEnum getTipoAcesso() {
        return tipoAcesso.get();
    }

    public void setTipoAcesso(AcessoEnum tipoAcesso) {
        this.tipoAcesso.set(tipoAcesso);
    }
    
    public ObjectProperty<AcessoEnum> tipoAcessoProperty() {
        return this.tipoAcesso;
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }
    
    public StringProperty loginProperty() {
        return this.login;
    }

    public byte getAtivo() {
        return ativo.get();
    }

    public void setAtivo(byte ativo) {
        this.ativo.set(ativo);
    }
    
    public ObjectProperty<Byte> ativoProperty() {
        return this.ativo;
    }

    public boolean isAtivo() {
        return getAtivo() == 1;
    }

    @Override
    public String toString() {

        return "Usuario " + getNomeCompleto() + " possui o login " + getLogin()
                + ". Esse usuario tem permissao de " + getTipoAcesso().obterDescricao()
                + ". Ele atualmente esta " + (getAtivo() == 0 ? "inativo" : "ativo");

    }

    @Override
    public boolean equals(Object o) {
        //se o objeto recebido for diferente de null e a sua classe for um Usuario verifica
        //igualdade no codigo do usuario.
        if (o != null && o.getClass() == this.getClass()) {
            Usuario user = (Usuario) o;
            if (this.getUserID() == user.getUserID()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getLogin().hashCode();
    }

}
