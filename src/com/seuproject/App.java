package com.seuproject;

import arquivo.Arquivo;
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
    public static DefaultListModel clientesList;

    private static ListaClientes lista = new ListaClientes();

    public App() {
        Arquivo arquivo = new Arquivo();

        // Lendo os dados do arquivo
        lista = arquivo.lerPF();
        popularLista();

        // Listener do botão de cadastrar
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNomeCliente.getText();
                String endereco = txtEndCliente.getText();
                String cpf = txtCPFCliente.getText();
                Float cf = Float.parseFloat(txtCapFinanCliente.getText());
                PessoaFisica pf = new PessoaFisica(nome, endereco, cpf, cf);

                getLista().inserir(pf);
                arquivo.gravarPF(pf);

                txtNomeCliente.setText("");
                txtEndCliente.setText("");
                txtCPFCliente.setText("");
                txtCapFinanCliente.setText("");

                lista = arquivo.lerPF();
                popularLista();
            }
        });

        // Botão de buscar
        listaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String s = (String) listaClientes.getSelectedValue();
                String codigo = s.substring(0, 10);
                PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);
                if(cliente.getCodigo() != null) {
                    txtListClie.setText("Cliente: " + cliente.getCodigo());
                    txtNomeClie.setText("Nome: " + cliente.getNome());
                    txtEndClie.setText("Endereço: " + cliente.getEndereco());
                    txtCPFClie.setText("CPF: " + cliente.getCPF());
                    txtCapFinClie.setText("Cap. Financeiro: " + cliente.getCapitalFinanceiro());
                } else
                    JOptionPane.showMessageDialog(null, "Cliente não encontrado");
            }
        });

        // Listener do clique na lista
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = txtBuscaClie.getText();
                if(codigo != "") {
                    PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);
                    if(cliente != null) {
                        txtListClie.setText("Cliente: " + cliente.getCodigo());
                        txtNomeClie.setText("Nome: " + cliente.getNome());
                        txtEndClie.setText("Endereço: " + cliente.getEndereco());
                        txtCPFClie.setText("CPF: " + cliente.getCPF());
                        txtCapFinClie.setText("Cap. Financeiro: " + cliente.getCapitalFinanceiro());
                    } else
                        JOptionPane.showMessageDialog(null, "Cliente não encontrado");
                }  else
                    JOptionPane.showMessageDialog(null, "Digite uma busca");
            }
        });

        // Botão de editar cliente
        btnEditarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) listaClientes.getSelectedValue();
                if(s != null) {
                    String codigo = s.substring(0, 10);
                    PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);

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
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            cliente.setNome(nomeField.getText());
                            cliente.setEndereco(endeField.getText());
                            cliente.setCPF(cpfField.getText());
                            cliente.setCapitalFinanceiro(Float.parseFloat(capfiField.getText()));
                            if(arquivo.atualizarPF(cliente))
                                JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso.");
                            else
                                JOptionPane.showMessageDialog(null, "Não foi possível atualizar o cliente.");
                            lista = arquivo.lerPF();
                            popularLista();
                        } catch (Exception erro) {
                            JOptionPane.showMessageDialog(null, "Verifique se digitou todos os campos corretamente. " + erro);
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Selecione um cliente");
            }
        });

        // Botão de excluir cliente
        excluirClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) listaClientes.getSelectedValue();
                if(s != null) {
                    String codigo = s.substring(0, 10);
                    PessoaFisica cliente = getLista().buscaClienteCodigo(codigo);

                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Deseja deletar o cliente " + cliente.getNome() + "?", "Atenção", dialogButton);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if(arquivo.deletarPF(cliente))
                            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso.");
                        else
                            JOptionPane.showMessageDialog(null, "Não foi possível excluir o cliente.");
                        lista = arquivo.lerPF();
                        popularLista();
                    }

                }
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

        this.listaClientes.setModel(clientesList);
    }
}
