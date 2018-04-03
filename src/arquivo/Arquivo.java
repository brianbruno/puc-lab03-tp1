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

public class Arquivo extends ArquivoUtil {

	public boolean gravarPF (PessoaFisica cliente) {
	    boolean resultado = false;

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
            resultado = true;
		} catch (Exception e) {
			System.out.println ("ERRO ao gravar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
			e.printStackTrace ();
		}  finally {
            fecharManipuladoresEscrita();
        }

        return resultado;
	}

	public boolean atualizarPF (PessoaFisica cliente) {
        boolean resultado = false;
        JSONParser parser = new JSONParser();

        try {
            File arqTemp = new File ("clientes_temp.json");
            File arq = new File (NOMEARQUIVO);
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
            fecharManipuladoresEscrita();

            /*entrada.close();
            saida.close();
            buffer_entrada.close();
            buffer_saida.close();
            leitor.close();
            gravador.close();*/

            if(delete(NOMEARQUIVO)) {
                if (arqTemp.renameTo(new File(NOMEARQUIVO))) ;
                    resultado = true;
            }

        } catch (Exception e) {
            System.err.println ("ERRO ao atualizar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
            resultado = false;
            e.printStackTrace ();
        }

        return resultado;
    }

    public boolean deletarPF (PessoaFisica cliente) {
        boolean resultado = false;
        JSONParser parser = new JSONParser();

        try {
            File arqTemp = new File ("clientes_temp.json");
            File arq = new File ("clientes.json");
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
                    linha = buffer_entrada.readLine();
                    if(linha != null)
                        buffer_saida.write(linha + separadorDeLinha);
                    else
                        continue;
                } else {
                    buffer_saida.write(linha + separadorDeLinha);
                    buffer_saida.flush();
                }
            }
            fecharManipuladoresEscrita();

            if(delete(arq.getAbsoluteFile())) {
                if (arqTemp.renameTo(new File(NOMEARQUIVO))) ;
                resultado = true;
            }

        } catch (Exception e) {
            System.err.println ("ERRO ao atualizar o cliente [" + cliente.getCodigo() + "] no disco rígido!");
            resultado = false;
            e.printStackTrace ();
        } finally {
            fecharManipuladoresEscrita();
        }

        return resultado;
    }

	public ListaClientes lerPF (boolean sort) {
        JSONParser parser = new JSONParser();

        String nomeArquivo = NOMEARQUIVO;

        if(sort)
            nomeArquivo = NOMEARQUIVO_SORT;

        ListaClientes listaClientes = new ListaClientes();
        Integer totalClientes = 0;

        try {
            entrada = new FileInputStream (nomeArquivo);
            leitor = new InputStreamReader (entrada);
            buffer_entrada = new BufferedReader (leitor);
            String linha;

            while ((linha = buffer_entrada.readLine()) != null && !linha.equals(SEPARADOR_BLOCO_INTERCALACAO)) {
                Object obj = parser.parse(linha);

                JSONObject jsonObject = (JSONObject) obj;

                String nome = (String) jsonObject.get("nome");
                String endereco = (String) jsonObject.get("endereco");
                String codigo = (String)  jsonObject.get("codigo");
                String cpf = (String)  jsonObject.get("cpf");
                String ativo = (String)  jsonObject.get("ativo");
                double cfdouble = Double.parseDouble((String) jsonObject.get("cf"));
                String valor = String.valueOf(cfdouble);
                float cf = Float.parseFloat(valor);

                PessoaFisica pf = new PessoaFisica(codigo.trim(), nome.trim(), endereco.trim(), cpf.trim(), cf);
                pf.setAtivo(ativo);
                listaClientes.inserir(pf);

                ultimoCod = codigo.substring(0, 7);

                totalClientes++;
            }

            Cliente.clientes = totalClientes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }
        return listaClientes;
    }

    public ListaClientes buscarPF (String codigo) {

	    PessoaFisica cliente = null;

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
                String cod = (String)  jsonObject.get("codigo");

                if(linha.contains(codigo)) {
                    cliente = montarObjeto(linha);
                    listaClientes.inserir(cliente);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }

	    return listaClientes;
    }

    public PessoaFisica getPessoaFisica (String codigo) {

        PessoaFisica cliente = null;

        JSONParser parser = new JSONParser();

        try {
            entrada = new FileInputStream ("clientes.json");
            leitor = new InputStreamReader (entrada);
            buffer_entrada = new BufferedReader (leitor);
            String linha;

            while ((linha = buffer_entrada.readLine()) != null) {

                Object obj = parser.parse(linha);
                JSONObject jsonObject = (JSONObject) obj;
                String cod = (String)  jsonObject.get("codigo");

                if(cod.equals(codigo)) {
                    cliente = montarObjeto(linha);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }

        return cliente;
    }

    public PessoaFisica buscaBinariaCodigo (String codigo) {
        JSONParser parser = new JSONParser();
        PessoaFisica cliente = null;
        codigo = codigo.substring(0, 7);
        RandomAccessFile arq = null;
        try {

            arq = new RandomAccessFile ("clientes.json", "r");

            long esq = 0;
            long dir = arq.length()/TAMANHO_STRING_BYTES;
            long meio = 0;
            int comp = 0;
            JSONObject jsonObject = null;

            while (esq <= dir) {
                meio = esq + (dir - esq)/2;
                arq.seek(meio*TAMANHO_STRING_BYTES);
                String jsonLine = arq.readLine();
                String cod = "";

                if(jsonLine.length() > 0) {
//                    System.out.println(jsonLine);
                    Object obj = parser.parse(jsonLine);
                    jsonObject = (JSONObject) obj;
                    cod = (String) jsonObject.get("codigo");
                    cod = cod.substring(0, 7);
                    comp = Integer.parseInt(cod);
                }
                if(Integer.parseInt(codigo) > Integer.parseInt(cod)) {
                    esq = meio + 1;
//                    System.out.println("Nova Dir: " +  dir + " Esquerda: " + esq);
                }
                else if(Integer.parseInt(codigo) < Integer.parseInt(cod)) {
                    dir = meio - 1;
//                    System.out.println("Nova Esq: " +  esq + " Direita: " + dir);
                }
                else {
                    cliente = montarObjeto(jsonLine);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            try {
                if (arq != null)
                    arq.close();
            } catch (Exception e) {
                System.err.println("Erro ao fechar arquivo.");
            }
            fecharManipuladoresEscrita();
        }

        return cliente;
    }


    private String[] ordenarVetor (String[] linhas) {

        JSONParser parser = new JSONParser();

        try {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Object obj = parser.parse(linhas[i]);
                JSONObject jsonObject = (JSONObject) obj;
                String codigo = (String)  jsonObject.get("codigo");
                codigo = codigo.substring(0, 6);

                Object obj2 = parser.parse(linhas[j]);
                JSONObject jsonObject2 = (JSONObject) obj2;
                String codigo2 = (String)  jsonObject2.get("codigo");
                codigo2 = codigo.substring(0, 6);

                if (codigo.compareTo(codigo2) < 0) {
                    //aqui acontece a troca, do maior cara vai para a direita e o menor para a esquerda
                    String aux = linhas[i];
                    linhas[i] = linhas[j];
                    linhas[j] = aux;
                }
            }
        }
        } catch (Exception e ){
            e.printStackTrace();
        }

        return linhas;
    }

    /*File arq1 = new File ("clientes_1.json");
            FileOutputStream saida1 = new FileOutputStream (arq1, true);
            OutputStreamWriter gravador1 = new OutputStreamWriter (saida1);
            BufferedWriter buffer_saida1 = new BufferedWriter (gravador1);

            File arq2 = new File ("clientes_2.json");
            FileOutputStream saida2 = new FileOutputStream (arq2, true);
            OutputStreamWriter gravador2 = new OutputStreamWriter (saida2);
            BufferedWriter buffer_saida2 = new BufferedWriter (gravador2);

            File arq3 = new File ("clientes_3.json");
            FileOutputStream saida3 = new FileOutputStream (arq3, true);
            OutputStreamWriter gravador3 = new OutputStreamWriter (saida3);
            BufferedWriter buffer_saida3= new BufferedWriter (gravador3);*/
}
