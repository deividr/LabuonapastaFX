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
    private IntegerProperty userId;
    private ObjectProperty<Byte> ativo;
    private ObjectProperty<AcessoEnum> tipoAcesso;

    /**
     * Construtor padrao
     */
    public Usuario() {
        this(0, "", "", AcessoEnum.PEDIDO, "", (byte) 0);
    }

    /**
     * Construtor com dados iniciais
     *
     * @param userId Número único que identifica o usuario dentro do sistema
     * @param login Login do usuário dentro do sistema
     * @param nomeCompleto Nome completo do usuário
     * @param tipoAcesso Perfil de acesso para esse usuário
     * @param senha Senha cadastrada para ele
     * @param ativo Informa se o usuário está ativo ou não no sistema
     */
    public Usuario(int userId, String login, String nomeCompleto, AcessoEnum tipoAcesso, String senha, byte ativo) {
        this.userId = new SimpleIntegerProperty(userId);
        this.login = new SimpleStringProperty(login);
        this.nomeCompleto = new SimpleStringProperty(nomeCompleto);
        this.tipoAcesso = new SimpleObjectProperty<>(tipoAcesso);
        this.senha = new SimpleStringProperty(senha);
        this.ativo = new SimpleObjectProperty<>(ativo);
    }

    public final StringProperty loginProperty() {
        return this.login;
    }

    public final String getLogin() {
        return this.loginProperty().get();
    }

    public final void setLogin(final String login) {
        this.loginProperty().set(login);
    }

    public final StringProperty nomeCompletoProperty() {
        return this.nomeCompleto;
    }

    public final String getNomeCompleto() {
        return this.nomeCompletoProperty().get();
    }

    public final void setNomeCompleto(final String nomeCompleto) {
        this.nomeCompletoProperty().set(nomeCompleto);
    }

    public final StringProperty senhaProperty() {
        return this.senha;
    }

    public final java.lang.String getSenha() {
        return this.senhaProperty().get();
    }

    public final void setSenha(final String senha) {
        this.senhaProperty().set(senha);
    }

    public final IntegerProperty userIdProperty() {
        return this.userId;
    }

    public final int getUserId() {
        return this.userIdProperty().get();
    }

    public final void setUserId(final int userId) {
        this.userIdProperty().set(userId);
    }

    public final ObjectProperty<Byte> ativoProperty() {
        return this.ativo;
    }

    public final byte getAtivo() {
        return this.ativoProperty().get();
    }

    public final void setAtivo(final Byte tipoAcesso) {
        this.ativoProperty().set(tipoAcesso);
    }

    public boolean isAtivo() {
        return getAtivo() == 1;
    }

    public final ObjectProperty<AcessoEnum> tipoAcessoProperty() {
        return this.tipoAcesso;
    }

    public final AcessoEnum getTipoAcesso() {
        return this.tipoAcessoProperty().get();
    }

    public final void setTipoAcesso(final AcessoEnum tipoAcesso) {
        this.tipoAcessoProperty().set(tipoAcesso);
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
            if (this.getUserId() == user.getUserId()) {
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
