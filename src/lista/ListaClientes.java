package lista;

import com.seuproject.App;
import estruturas.Cliente;

public class ListaClientes {

    private NoCliente lista;

    public ListaClientes() {
        this.lista = new NoCliente();
    }

    public void inserir (Cliente dados) {
        NoCliente clientes = getLista();
        inserirMetodo(dados, clientes);
    }

    private void inserirMetodo (Cliente dados, NoCliente clientes) {

        if (clientes.getProx() == null) {
            clientes.setProx(new NoCliente());
            clientes.getProx().setCliente(dados);
        }
        else {
            inserirMetodo(dados, clientes.getProx());
        }
    }

    public NoCliente getLista() {
        return lista;
    }

    public void listarClientes () {

        NoCliente listaClientes = lista.getProx();
        System.out.println("");
        System.out.println("Total clientes: " + Cliente.clientes);
        System.out.println("---------------------------------------------------------------");

        while(listaClientes != null) {
            Cliente cliente = listaClientes.getCliente();

            System.out.println("Código: " + cliente.getCodigo());
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("Endereço: " + cliente.getEndereco());
            System.out.println("---------------------------------------------------------------");

            listaClientes = listaClientes.getProx();

        }
    }

    public Cliente buscaClienteCodigo (String codigo) {

        NoCliente listaClientes = lista.getProx();
        Cliente clienteBusca = null;

        while(listaClientes != null) {
            Cliente cliente = listaClientes.getCliente();

            if(cliente.getCodigo().equals(codigo)) {
                clienteBusca = cliente;
                break;
            }
            else
                listaClientes = listaClientes.getProx();
        }

        return clienteBusca;
    }
}
