package lista;

import estruturas.*;

public class NoCliente {

    PessoaFisica cliente;
    NoCliente prox;

    public NoCliente() {
        this.cliente = null;
        this.prox = null;
    }

    public PessoaFisica getCliente() {
        return cliente;
    }

    public void setCliente(PessoaFisica cliente) {
        this.cliente = cliente;
    }

    public NoCliente getProx() {
        return prox;
    }

    public void setProx(NoCliente prox) {
        this.prox = prox;
    }
}
