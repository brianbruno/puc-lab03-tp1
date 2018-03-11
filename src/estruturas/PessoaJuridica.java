package estruturas;

public class PessoaJuridica extends Cliente {

    private int funcionarios;
    private String CNPJ;

    public PessoaJuridica(String nome, String endereco, int funcionarios, String CNPJ) {
        super(nome, endereco);
        this.funcionarios = funcionarios;
        this.CNPJ = CNPJ;
    }
}
