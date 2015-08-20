package labuonapastafx.model;
//Teste no elipse
public enum ProdutoEnum {
	
	MASSA("Massa", 01), MOLHO("Molho", 02), BEBIDA("Bebida", 03), SALADA("Salada", 04),
	CARNE("Carne", 05), DIVERSOS("Diversos", 99);
	
	private String descricao;
	private int codigo;
	
	ProdutoEnum(String descricao, int codigo) {
		this.descricao = descricao;
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public int getCodigo() {
		return this.codigo;
	}
	
	public static ProdutoEnum valueOf(int codigo) {
		for (ProdutoEnum prodEnum : ProdutoEnum.values()) {
			if (prodEnum.getCodigo() == codigo) 
				return prodEnum;
		}
		return null;
	}
 
}
