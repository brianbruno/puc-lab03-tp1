package estruturas;

public class PessoaFisica extends Cliente {

    private String CPF;
    private float capitalFinanceiro;

    public PessoaFisica(String nome, String endereco, String CPF, float capitalFinanceiro) {
        super(nome, endereco);
        this.CPF = CPF;
        this.capitalFinanceiro = capitalFinanceiro;
    }

    public PessoaFisica(String codigo, String nome, String endereco, String CPF, float capitalFinanceiro) {
        super(codigo, nome, endereco);
        this.CPF = CPF;
        this.capitalFinanceiro = capitalFinanceiro;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public void setCapitalFinanceiro(float capitalFinanceiro) {
        this.capitalFinanceiro = capitalFinanceiro;
    }

    public float getCapitalFinanceiro() {
        return capitalFinanceiro;
    }
}
