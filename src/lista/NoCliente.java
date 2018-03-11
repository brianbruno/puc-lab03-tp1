package lista;

import estruturas.*;

public class NoCliente {

    Cliente cliente;
    NoCliente prox;

    public NoCliente() {
        this.cliente = null;
        this.prox = null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public NoCliente getProx() {
        return prox;
    }

    public void setProx(NoCliente prox) {
        this.prox = prox;
    }
}
