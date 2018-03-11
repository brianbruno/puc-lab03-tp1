package arquivo;

import estruturas.Cliente;

import java.io.*;
import java.util.Iterator;

import estruturas.PessoaFisica;
import lista.ListaClientes;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Arquivo {

	public void gravarPF (PessoaFisica cliente) {
		File arq = null;
		FileOutputStream saida = null;
		OutputStreamWriter gravador = null;
		BufferedWriter buffer_saida = null;

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
			String separadorDeLinha = System.getProperty ("line.separator");

            JSONObject obj = new JSONObject();
            obj.put("codigo", cliente.getCodigo());
            obj.put("nome", cliente.getNome());
            obj.put("endereco", cliente.getEndereco());
            obj.put("cpf", cliente.getCPF());
            obj.put("cf", cliente.getCapitalFinanceiro());

            buffer_saida.write(obj.toJSONString() + separadorDeLinha);

			buffer_saida.flush();

		} catch (Exception e) {
			System.out.println ("ERRO ao gravar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
			e.printStackTrace ();
		} finally {
			try {
				if (buffer_saida != null)
					buffer_saida.close ();
				if (gravador != null)
					gravador.close ();
				if (saida != null)
					saida.close ();
			} catch (Exception e) {
				System.out.println ("ERRO ao fechar os manipuladores de escrita do arquivo clientes.txt");
				e.printStackTrace ();
			}
		}
	}

	public ListaClientes lerPF () {
        JSONParser parser = new JSONParser();

        ListaClientes listaClientes = new ListaClientes();

        try {
            FileInputStream entrada = null;
            InputStreamReader leitor = null;
            BufferedReader buffer_entrada = null;

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
}
