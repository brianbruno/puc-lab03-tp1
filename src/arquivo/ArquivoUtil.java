package arquivo;

import estruturas.Cliente;
import estruturas.PessoaFisica;
import lista.ListaClientes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ArquivoUtil {

    static File arq = null;
    static FileOutputStream saida = null;
    static OutputStreamWriter gravador = null;
    static BufferedWriter buffer_saida = null;
    static FileInputStream entrada = null;
    static InputStreamReader leitor = null;
    static BufferedReader buffer_entrada = null;
    static final String separadorDeLinha = System.getProperty ("line.separator");
    static final String NOMEARQUIVO = "clientes.json";
    static final String NOMEARQUIVO_SORT = "clientes_sorted.json";
    static final long TAMANHO_BYTE_STRING = 1;
    static final int TAMANHO_STRING_BYTES = 189;
    static final String SEPARADOR_BLOCO_INTERCALACAO = "- SEPARACAO BLOCO -";
    public static int quantidadeLinhas = 0;
    public static String ultimoCod = "0000000";

    public ArquivoUtil() {
        contarLinhas();
    }

    public PessoaFisica montarObjeto (String linha) {
        PessoaFisica cliente = null;

        try {
            if(linha != null && !linha.equals("")) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(linha);
                JSONObject jsonObject = (JSONObject) obj;
                String cod = (String) jsonObject.get("codigo");
                String nome = (String) jsonObject.get("nome");
                String endereco = (String) jsonObject.get("endereco");
                String codg = (String) jsonObject.get("codigo");
                String cpf = (String) jsonObject.get("cpf");
                String ativo = (String) jsonObject.get("ativo");

                double cfdouble = Double.parseDouble((String) jsonObject.get("cf"));
                String valor = String.valueOf(cfdouble);
                float cf = Float.parseFloat(valor);

                cliente = new PessoaFisica(codg.trim(), nome.trim(), endereco.trim(), cpf.trim(), cf);
                cliente.setAtivo(ativo);
            }
        } catch (Exception e) {
            System.err.println("Erro ao montar objeto.");
            e.printStackTrace();
        }

        return cliente;
    }


    public String montarString (PessoaFisica cliente) {
        JSONObject novoDado = new JSONObject();
        String novaString = "";
        if(cliente != null) {
            novoDado.put("codigo", rightpad(cliente.getCodigo(), 10));
            novoDado.put("nome", rightpad(cliente.getNome(), 40));
            novoDado.put("endereco", rightpad(cliente.getEndereco(), 50));
            novoDado.put("cpf", leftpad(cliente.getCPF(), 11));
            novoDado.put("cf", leftpad(String.valueOf(cliente.getCapitalFinanceiro()),10));
            novoDado.put("ativo", cliente.getAtivo());
            novaString = novoDado.toJSONString() + separadorDeLinha;
        }
        return novaString;
    }

    private String leftpad(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    private String rightpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    public static void fecharManipuladoresEscrita() {
        try {
            if (buffer_saida != null) {
                buffer_saida.close();
            }
            if (buffer_entrada != null) {
                buffer_entrada.close();
            }
            if (gravador != null) {
                gravador.close();
            }
            if (leitor != null) {
                leitor.close();
            }
            if (saida != null) {
                saida.close();
            }
            if (entrada != null) {
                entrada.close();
            }

        } catch (IOException e) {
            System.out.println("ERRO ao fechar os manipuladores de escrita do arquivo");
        }
    }

    public void contarLinhas () {
        try {
            quantidadeLinhas = 0;

            entrada = new FileInputStream (NOMEARQUIVO);
            leitor = new InputStreamReader (entrada);
            buffer_entrada = new BufferedReader (leitor);

            while (buffer_entrada.readLine() != null) {
                quantidadeLinhas++;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }
    }

    public boolean delete(File file) {
        boolean success;

        success = file.delete();

        return success;
    }

    public boolean delete(String arquivo) {
        File arqTemp = new File (arquivo);
        return delete(arqTemp);
    }
}
