package arquivo;

import estruturas.Cliente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import estruturas.PessoaFisica;
import lista.ListaClientes;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Arquivo {

    private static File arq = null;
    private static FileOutputStream saida = null;
    private static OutputStreamWriter gravador = null;
    private static BufferedWriter buffer_saida = null;
    private static FileInputStream entrada = null;
    private static InputStreamReader leitor = null;
    private static BufferedReader buffer_entrada = null;
    private static final String separadorDeLinha = System.getProperty ("line.separator");
    private static final String NOMEARQUIVO = "clientes.json";

	public void gravarPF (PessoaFisica cliente) {
		try {
			arq = new File ("clientes.json");
			/* ---------------------------------------------
			 * FileOutputStream (File file, boolean append):
			 * ---------------------------------------------
			 * 1) Se append for true, preserva o conteúdo do arquivo
			 * gravando os novos dados no final do arquivo.
			 * 2) Se append for false, grava os novos dados sobre
			 * o conteúdo prévio, sobrescrevendo-o.
			 * 3) Se o atributo append for omitido, funciona da mesma
			 * forma quando append tem valor igual a false.
			 */
			saida = new FileOutputStream (arq, true);
			gravador = new OutputStreamWriter (saida);
			buffer_saida = new BufferedWriter (gravador);

            buffer_saida.write(montarString(cliente));

			buffer_saida.flush();

		} catch (Exception e) {
			System.out.println ("ERRO ao gravar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
			e.printStackTrace ();
		}  finally {
            fecharManipuladoresEscrita();
        }
	}

	public boolean atualizarPF (PessoaFisica dadoAntigo, PessoaFisica cliente) {
        boolean resultado = false;
        JSONParser parser = new JSONParser();

        try {
            File arqTemp = new File ("clientes_temp.json");
            arq = new File ("clientes.json");
            saida = new FileOutputStream (arqTemp, true);
            gravador = new OutputStreamWriter (saida);
            buffer_saida = new BufferedWriter (gravador);

            entrada = new FileInputStream (arq);
            leitor = new InputStreamReader (entrada);
            buffer_entrada = new BufferedReader (leitor);
            String linha;

            while ((linha = buffer_entrada.readLine()) != null) {
                Object obj = parser.parse(linha);
                JSONObject jsonObject = (JSONObject) obj;
                String codigo = (String)  jsonObject.get("codigo");

                if(codigo.equals(cliente.getCodigo())) {
                    buffer_saida.write(montarString(cliente));
                } else {
                    buffer_saida.write(linha + separadorDeLinha);
                    buffer_saida.flush();
                }
            }

            boolean success = (new File(NOMEARQUIVO)).delete();
            arqTemp.renameTo(new File(NOMEARQUIVO));
            arqTemp.delete();

        } catch (Exception e) {
            System.err.println ("ERRO ao atualizar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
            resultado = false;
            e.printStackTrace ();
        }

        return resultado;
    }

	public ListaClientes lerPF () {
        JSONParser parser = new JSONParser();

        ListaClientes listaClientes = new ListaClientes();

        try {
            entrada = new FileInputStream ("clientes.json");
            leitor = new InputStreamReader (entrada);
            buffer_entrada = new BufferedReader (leitor);
            String linha;

            while ((linha = buffer_entrada.readLine()) != null) {

                Object obj = parser.parse(linha);

                JSONObject jsonObject = (JSONObject) obj;

                String nome = (String) jsonObject.get("nome");
                String endereco = (String) jsonObject.get("endereco");
                String codigo = (String)  jsonObject.get("codigo");
                String cpf = (String)  jsonObject.get("cpf");
                double cfdouble = (Double) jsonObject.get("cf");
                String valor = String.valueOf(cfdouble);
                float cf = Float.parseFloat(valor);

                PessoaFisica pf = new PessoaFisica(codigo, nome, endereco, cpf, cf);
                listaClientes.inserir(pf);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaClientes;
    }

    public void ordenarNome () {
        JSONParser parser = new JSONParser();

        try {


            File arquivoOriginal = new File("clientes.json");
            FileInputStream arquivo1 = new FileInputStream ("clientes_temp1.json");
            FileInputStream arquivo2 = new FileInputStream ("clientes_temp2.json");

            long tamanho = arquivoOriginal.length();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fecharManipuladoresEscrita() {
        try {
            if (buffer_saida != null) {
                buffer_saida.close();
            }
            if (gravador != null) {
                gravador.close();
            }
            if (saida != null) {
                saida.close();
            }
        } catch (IOException e) {
            System.out.println("ERRO ao fechar os manipuladores de escrita do arquivo");
        }
    }

    public String montarString (PessoaFisica cliente) {
        JSONObject novoDado = new JSONObject();
        novoDado.put("codigo", cliente.getCodigo());
        novoDado.put("nome", cliente.getNome());
        novoDado.put("endereco", cliente.getEndereco());
        novoDado.put("cpf", cliente.getCPF());
        novoDado.put("cf", cliente.getCapitalFinanceiro());
        String novaString = novoDado.toJSONString() + separadorDeLinha;
        return novaString;
    }

    private void delete(File file) {
        boolean success = false;

        success = file.delete();
        if (success) {
            System.out.println(file.getAbsoluteFile() + " Deleted");
        } else {
            System.out.println(file.getAbsoluteFile() + " Deletion failed!!!");
        }
    }
}
