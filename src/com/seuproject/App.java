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
                Cliente cliente = getLista().buscaClienteCodigo(codigo);
                if(cliente.getCodigo() != null) {
                    txtListClie.setText("Cliente: " + cliente.getCodigo());
                    txtNomeClie.setText("Nome: " + cliente.getNome());
                    txtEndClie.setText("Endereço: " + cliente.getEndereco());
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
                    Cliente cliente = getLista().buscaClienteCodigo(codigo);
                    if(cliente != null) {
                        txtListClie.setText("Cliente: " + cliente.getCodigo());
                        txtNomeClie.setText("Nome: " + cliente.getNome());
                        txtEndClie.setText("Endereço: " + cliente.getEndereco());
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
                    Cliente cliente = getLista().buscaClienteCodigo(codigo);



                    JTextField nomeField = new JTextField(25);
                    JTextField endeField = new JTextField(25);
                    JTextField cpfField = new JTextField(25);
                    JTextField capfiField = new JTextField(25);

                    Object[] inputFields = {"Nome", nomeField,
                            "Endereço", endeField,
                            "CPF", cpfField,
                            "Capital Financeiro", capfiField};

                    int result = JOptionPane.showConfirmDialog(null, inputFields,
                            "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        System.out.println("x value: " + nomeField.getText());
                        System.out.println("y value: " + endeField.getText());
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Selecione um cliente");
            }
        });
    }

    public static void main(String[] args) {

        JFrame janela = new JFrame("SEU");

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

        this.listaClientes.setModel(clientesList);
    }
}
