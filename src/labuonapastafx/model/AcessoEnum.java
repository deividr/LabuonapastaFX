package labuonapastafx.model;

import java.util.ArrayList;

public enum AcessoEnum {
	
	ADMINISTRADOR("Administrador", 0), CADASTRO("Cadastro", 1), PEDIDO("Pedido", 2);

	private final String descricao;
	private final int tipoInt;
	
	AcessoEnum(String descricao, int tipoInt) {
		this.descricao = descricao;
		this.tipoInt = tipoInt;
	}
	
	public String obterDescricao() {
		return this.descricao;
	}
	
	public int obterTipoInt() {
		return this.tipoInt;
	}
	
	public static AcessoEnum valueOf(int tipoInt) {
		for (AcessoEnum acesso : AcessoEnum.values()) {
			if (acesso.obterTipoInt() == tipoInt)
				return acesso;
		}
		return null;
	}

	public static ArrayList<String> obterDescricoes() {

		ArrayList<String> acessos = new ArrayList<>();

		for (AcessoEnum acesso : AcessoEnum.values()) {
			acessos.add(acesso.obterDescricao());
		}

		return acessos;
	}
        
        public static ArrayList<String> valuesDescricoes() {
		ArrayList<String> acessos = new ArrayList<>();

		for (AcessoEnum acesso : AcessoEnum.values()) {
			acessos.add(acesso.obterDescricao());
		}

		return acessos;            
        }

}
