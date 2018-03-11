package estruturas;

import java.util.Random;

public abstract class Cliente {

   public static Integer clientes = 0;
   private String codigo;
   private String nome;
   private String endereco;
   private String ativo;
   private int prioridade;

    public Cliente(String nome, String endereco) {
        clientes++;
        setNome(nome);
        setEndereco(endereco);
        setAtivo("S");
        this.codigo = this.gerarCodigo();
    }

    public Cliente(String codigo, String nome, String endereco) {
        clientes++;
        setNome(nome);
        setEndereco(endereco);
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    private String gerarCodigo() {
        Random gerador = new Random();
        Integer aleatorio = 10 + gerador.nextInt(90);
        String codigoInicial = clientes.toString() + "-" + aleatorio.toString();
        String codigo = String.format("%10s", codigoInicial).replace(' ', '0');
        return codigo;
    }
}