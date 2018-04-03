package lista;

import arquivo.Arquivo;
import com.seuproject.App;
import estruturas.Cliente;
import estruturas.PessoaFisica;

public class ListaClientes {

    private NoCliente lista;

    public ListaClientes() {
        this.lista = new NoCliente();
    }

    public void inserir (PessoaFisica dados) {
        NoCliente clientes = getLista();
        inserirMetodo(dados, clientes);
    }

    private void inserirMetodo (PessoaFisica dados, NoCliente clientes) {

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

    public PessoaFisica buscaClienteCodigo (String codigo) {

        Arquivo arquivo = new Arquivo();
        return arquivo.buscaBinariaCodigo(codigo);
    }

    public ListaClientes buscaClienteQualquerCampo (String codigo) {
        Arquivo arquivo = new Arquivo();
        return arquivo.buscarPF(codigo);
    }

    public PessoaFisica buscaClienteCodigoMemoria (String codigo) {

        NoCliente listaClientes = lista.getProx();
        PessoaFisica clienteBusca = null;

        while(listaClientes != null) {
            PessoaFisica cliente = listaClientes.getCliente();

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
