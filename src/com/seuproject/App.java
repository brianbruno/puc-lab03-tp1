package com.seuproject;

import arquivo.Arquivo;
import arquivo.Intercalacao;
import arquivo.IntercalacaoBalanceada;
import estruturas.Cliente;
import estruturas.PessoaFisica;
import lista.ListaClientes;
import lista.NoCliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {

    private JPanel panelMain;
    private JTabbedPane tabMain;
    private JList listaClientes;
    private JTextField txtNomeCliente;
    private JTextField txtEndCliente;
    private JTextField txtCPFCliente;
    private JTextField txtCapFinanCliente;
    private JButton cadastrarButton;
    private JLabel txtListClie;
    private JLabel txtNomeClie;
    private JLabel txtEndClie;
    private JTextField txtBuscaClie;
    private JButton btnBuscar;
    private JButton ordenarPorIDButton;
    private JButton ordenarPorNomeButton;
    private JButton btnEditarCliente;
    private JLabel txtCPFClie;
    private JLabel txtCapFinClie;
    private JButton excluirClienteButton;
    private JLabel txtTotalClientes;
    private JButton btnAtivarDesativar;
    private JLabel txtStatus;
    public static DefaultListModel clientesList;

    private static final String MSG_NENHUM_CLIENTE = "Selecione um Cliente";
    private static final String MSG_ATUALIZADO_SUS = "Cliente atualizado com sucesso";
    private static final String MSG_ATUALIZADO_ERR = "Não foi possível atualizar o cliente";
    private static final String MSG_CLI_NAO_ENCONT = "Cliente não encontrado";

    private static ListaClientes lista = new ListaClientes();

    public App() {
        Arquivo arquivo = new Arquivo();
        IntercalacaoBalanceada intercalcao = new IntercalacaoBalanceada();
        intercalcao.intercalar();

        // Lendo os dados do arquivo
        lista = arquivo.lerPF(false);
        popularLista();

        // Listener do botão de cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Pega os dados inseridos no formulário
                String nome = txtNomeCliente.getText();
                String endereco = txtEndCliente.getText();
                String cpf = txtCPFCliente.getText();
                Float cf = Float.parseFloat(txtCapFinanCliente.getText());
                PessoaFisica pf = new PessoaFisica(nome, endereco, cpf, cf);

                // Pega a lista e insere na memória e depois no arquivo
                getLista().inserir(pf);
                if(arquivo.gravarPF(pf))
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso");

                // Limpa os campos
                txtNomeCliente.setText("");
                txtEndCliente.setText("");
                txtCPFCliente.setText("");
                txtCapFinanCliente.setText("");

                // Realiza a intercalação e atualiza a lista
                intercalcao.intercalar();
                lista = arquivo.lerPF(false);
                popularLista();


            }
        });

        // Clique na lista de clientes
        listaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // Pega o valor selecionado
                String s = (String) listaClientes.getSelectedValue();
                String codigo = s.substring(0, 10);
                // Realiza a busca pelo método na classe de Lista de Clientes BUSCA BINÁRIA
                PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);

                // Se encontrar algum cliente preenche a lista.
                if(cliente != null) {
                    preencherDados(cliente);
                } else
                    JOptionPane.showMessageDialog(null, MSG_CLI_NAO_ENCONT);
            }
        });

        // Botão de buscar
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // pega o código digitado
                String codigo = txtBuscaClie.getText();

                // verifica se está vazio
                if(!codigo.equals("")) {

                    // realiza a busca
                    NoCliente lista = arquivo.buscarPF(codigo).getLista();
                    if(listaClientes != null) {
                        popularListaComClientes(lista);
                    } else
                        popularListaComClientes(getLista().getLista());

                    /*PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);
                    if(cliente != null) {
                        preencherDados(cliente);
                    } else
                        JOptionPane.showMessageDialog(null, MSG_CLI_NAO_ENCONT);*/
                }  else
                    JOptionPane.showMessageDialog(null, "Digite uma busca");
            }
        });

        // Botão de editar cliente
        btnEditarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Recupera o cliente selecionado e anota o index para depois continuar selecionado
                String s = (String) listaClientes.getSelectedValue();
                int index = listaClientes.getSelectedIndex();

                // Verifica se não está vazia
                if(s != null) {

                    String codigo = s.substring(0, 10);
                    // Realiza a pesquisa binária
                    PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);

                    // Monta um JOptionPane com as informações para serem editadas
                    JTextField nomeField = new JTextField(25);
                    JTextField endeField = new JTextField(25);
                    JTextField cpfField = new JTextField(25);
                    JTextField capfiField = new JTextField(25);

                    nomeField.setText(cliente.getNome());
                    endeField.setText(cliente.getEndereco());
                    cpfField.setText(cliente.getCPF());
                    capfiField.setText(String.valueOf(cliente.getCapitalFinanceiro()));

                    Object[] inputFields = {"Nome", nomeField,
                            "Endereço", endeField,
                            "CPF", cpfField,
                            "Capital Financeiro", capfiField};

                    int result = JOptionPane.showConfirmDialog(null, inputFields,
                            "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);

                    // Se ele dá ok salva, se cancelar não faz nada.
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            cliente.setNome(nomeField.getText());
                            cliente.setEndereco(endeField.getText());
                            cliente.setCPF(cpfField.getText());
                            cliente.setCapitalFinanceiro(Float.parseFloat(capfiField.getText()));
                            if(arquivo.atualizarPF(cliente))
                                JOptionPane.showMessageDialog(null, MSG_ATUALIZADO_SUS);
                            else
                                JOptionPane.showMessageDialog(null, MSG_ATUALIZADO_ERR);

                            String textoBusca = txtBuscaClie.getText();

                            if(textoBusca.isEmpty()) {
                                lista = arquivo.lerPF(false);
                            } else {
                                lista = arquivo.buscarPF(codigo);
                            }
                            intercalcao.intercalar();
                            popularLista();
                            preencherDados(cliente);
                            listaClientes.setSelectedIndex(index);
                        } catch (Exception erro) {
                            JOptionPane.showMessageDialog(null, "Verifique se digitou todos os campos corretamente. " + erro);
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(null, MSG_NENHUM_CLIENTE);
            }
        });

        // Botão de excluir cliente
        excluirClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) listaClientes.getSelectedValue();
                if(s != null) {
                    String codigo = s.substring(0, 10);
                    // recupera o cliente via pesquisa binária
                    PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);

                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Deseja deletar o cliente " + cliente.getNome() + "?", "Atenção", dialogButton);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // deleta o cliente caso o usuário confirme a exclusão
                        if(arquivo.deletarPF(cliente))
                            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso.");
                        else
                            JOptionPane.showMessageDialog(null, "Não foi possível excluir o cliente.");

                        intercalcao.intercalar();
                        lista = arquivo.lerPF(false);
                        popularLista();
                    }
                }
            }
        });

        // Botão de desativar/ativar cliente. (Exclusao Logica)
        btnAtivarDesativar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PessoaFisica cliente = getItemSelecionado();

                if(cliente != null) {
                    int index = listaClientes.getSelectedIndex();
                    if(cliente.getAtivo().equals("S")) {
                        cliente.setAtivo("N");
                    } else if (cliente.getAtivo().equals("N")){
                        cliente.setAtivo("S");
                    }
                    // Marca o cliente como inativo
                    if(arquivo.atualizarPF(cliente))
                        JOptionPane.showMessageDialog(null, MSG_ATUALIZADO_SUS);
                    else {
                        System.out.println("erro");
                        JOptionPane.showMessageDialog(null, MSG_ATUALIZADO_ERR);
                    }

                    popularLista();
                    preencherDados(cliente);
                    listaClientes.setSelectedIndex(index);
                } else
                    JOptionPane.showMessageDialog(null, MSG_NENHUM_CLIENTE);

            }
        });

        // Botão de Ordenar Pelo Nome
        ordenarPorNomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lista = arquivo.lerPF(true);
                popularLista();
            }
        });

        // Botão de Ordenar Pelo ID
        ordenarPorIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lista = arquivo.lerPF(false);
                popularLista();
            }
        });
    }

    public static void main(String[] args) {

        JFrame janela = new JFrame("SEU - Sistema Empresarial Uniformes");

        janela.setContentPane(new App().panelMain);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
        janela.pack();
        janela.setVisible(true);
    }

    public static ListaClientes getLista() {
        return lista;
    }

    public void popularLista () {
        clientesList = new DefaultListModel();
        NoCliente listaClientes = getLista().getLista();
        preencherLista(listaClientes);

        this.listaClientes.setModel(clientesList);
    }

    public void popularListaComClientes (NoCliente listaClientes) {
        clientesList = new DefaultListModel();
        preencherLista(listaClientes);
    }

    public void preencherLista (NoCliente listaClientes) {
        while(listaClientes != null) {
            Cliente cliente = listaClientes.getCliente();
            if(cliente != null) {
                clientesList.addElement(cliente.getCodigo() + " - " + cliente.getNome());
            }
            listaClientes = listaClientes.getProx();
        }

        txtListClie.setText("Cliente: ");
        txtNomeClie.setText("Nome: ");
        txtEndClie.setText("Endereço: ");
        txtCPFClie.setText("CPF: ");
        txtCapFinClie.setText("Cap. Financeiro: ");
        txtStatus.setText("Status: ");
        txtTotalClientes.setText("Total de clientes cadastrados: " + Cliente.clientes);


        this.listaClientes.setModel(clientesList);
    }

    public void preencherDados (PessoaFisica cliente) {
        String status = "Inativo";
        if(cliente.getAtivo().equals("S"))
            status = "Ativo";

        txtListClie.setText("Cliente: " + cliente.getCodigo());
        txtNomeClie.setText("Nome: " + cliente.getNome());
        txtEndClie.setText("Endereço: " + cliente.getEndereco());
        txtCPFClie.setText("CPF: " + cliente.getCPF());
        txtCapFinClie.setText("Cap. Financeiro: " + cliente.getCapitalFinanceiro());
        txtStatus.setText("Status: " + status);
    }

    public PessoaFisica getItemSelecionado () {
        String s = (String) listaClientes.getSelectedValue();
        PessoaFisica cliente = null;
        if(s != null) {
            String codigo = s.substring(0, 10);
            cliente = getLista().buscaClienteCodigo(codigo);
        }
        return cliente;
    }

}
