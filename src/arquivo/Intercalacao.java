package arquivo;

import estruturas.PessoaFisica;
import lista.ListaClientes;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Intercalacao extends ArquivoUtil {

    PessoaFisica[] blocoClientes = null;
    private static final int TAM_BLOCO = 4;
    String arquivos[] = new String[TAM_BLOCO];
    String arquivoOrdenado = "clientes-ordenado.json";

    public Intercalacao() {
        arquivos[0] = "temp1.json";
        arquivos[1] = "temp2.json";
        arquivos[2] = "temp3.json";
        arquivos[3] = "temp4.json";
    }

    public ListaClientes intercalacaoBalanceada () {
        ListaClientes listaClientes = new ListaClientes();

        try {
            entrada = new FileInputStream("clientes.json");
            leitor = new InputStreamReader(entrada);
            buffer_entrada = new BufferedReader(leitor);
            blocoClientes = new PessoaFisica[TAM_BLOCO];
            String linha;

            int tam = 0;

            while ((linha = buffer_entrada.readLine()) != null) {
                blocoClientes[0] = montarObjeto(linha);

                for(int i = 1; i<TAM_BLOCO; i++)
                    blocoClientes[i] = montarObjeto(buffer_entrada.readLine());

//                ordenarBlocoClientes(TAM_BLOCO);

                if(tam < 4) {
                    for(PessoaFisica cliente : blocoClientes) {
//                        gravarString(montarString(cliente), arquivos[0]);
                        tam++;
                    }
                } else {
                    for(PessoaFisica cliente : blocoClientes)
//                        gravarString(montarString(cliente), arquivos[1]);
                    tam = 0;
                }
            }

            intercalarArquivos(TAM_BLOCO*2, arquivos[0], arquivos[1]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }

        return listaClientes;
    }

    private void intercalarArquivos (int tamanho, String arquivo1, String arquivo2) {
        try {
            String arquivo3, arquivo4;
            if(arquivo1.equals(arquivos[2])) {
                arquivo3 = arquivos[0];
                arquivo4 = arquivos[1];
            } else {
                arquivo3 = arquivos[2];
                arquivo4 = arquivos[3];
            }
            FileInputStream entrada1 = new FileInputStream(arquivo1);
            FileInputStream entrada2 = new FileInputStream(arquivo2);

            InputStreamReader leitor1 = new InputStreamReader(entrada1);
            InputStreamReader leitor2 = new InputStreamReader(entrada2);

            BufferedReader buffer_entrada1 = new BufferedReader(leitor1);
            BufferedReader buffer_entrada2 = new BufferedReader(leitor2);

            blocoClientes = new PessoaFisica[TAM_BLOCO];
            blocoClientes = new PessoaFisica[tamanho];
            String linha;
            int tam = 0;

            while ((linha = buffer_entrada1.readLine()) != null) {
                blocoClientes[0] = montarObjeto(linha);
                int i;

                for(i = 1; i<(tamanho/2); i++) {
                    blocoClientes[i] = montarObjeto(buffer_entrada1.readLine());
                    apagarLinha(1, arquivo1);
                }

                for(int j = 0; j<(tamanho/2); j++) {
                    blocoClientes[j] = montarObjeto(buffer_entrada2.readLine());
                    apagarLinha(1, arquivo2);
                }
//                ordenarBlocoClientes(tamanho);

                if(tam < tamanho) {
                    for(PessoaFisica cliente : blocoClientes) {
                        //gravarString(montarString(cliente), arquivo3);
                        tam++;
                    }
                } else {
                    for(PessoaFisica cliente : blocoClientes)
//                        gravarString(montarString(cliente), arquivo4);
                    tam = 0;
                }
            }
            entrada1.close();
            entrada2.close();
            leitor1.close();
            leitor2.close();
            buffer_entrada1.close();
            buffer_entrada2.close();
            File arq1 = new File (arquivo1);
            File arq2 = new File (arquivo2);
            delete(arq1.getAbsoluteFile());
            delete(arq2.getAbsoluteFile());

            if(tamanho < quantidadeLinhas) {
                intercalarArquivos(tamanho*2, arquivo3, arquivo4);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fecharManipuladoresEscrita();
        }
    }

    /*private void ordenarBlocoClientes (int tamanho) {

        PessoaFisica aux = null;

        for(int i = 0; i<tamanho; i++){
            for(int j = 0; j<tamanho-1; j++){
                PessoaFisica nome1 = blocoClientes[j];
                PessoaFisica nome2 = blocoClientes[j + 1];
                if(nome1 != null && nome2 != null && (nome1.getNome().compareTo(nome2.getNome()) > 0)){
                    aux = blocoClientes[j];
                    blocoClientes[j] = blocoClientes[j+1];
                    blocoClientes[j+1] = aux;
                }
            }
        }
    }*/

    private void apagarLinha (int tam, String arquivo) {
        try {
            File arqTemp = new File ("temporario.json");
            File arq = new File (arquivo);
            FileOutputStream saida = new FileOutputStream (arqTemp, true);
            OutputStreamWriter gravador = new OutputStreamWriter (saida);
            BufferedWriter buffer_saida = new BufferedWriter (gravador);

            FileInputStream entrada = new FileInputStream (arq);
            InputStreamReader leitor = new InputStreamReader (entrada);
            BufferedReader buffer_entrada = new BufferedReader (leitor);
            String linha;
            int i = 0;

            while ((linha = buffer_entrada.readLine()) != null) {
                if(i<tam)
                    buffer_saida.write("");
                else
                    buffer_saida.write(linha);
                i++;
            }
            buffer_saida.flush();

            saida.close();
            gravador.close();
            buffer_saida.close();
            entrada.close();
            leitor.close();
            buffer_entrada.close();

            if(delete(arq.getAbsoluteFile())) {
                arqTemp.renameTo(new File(arquivo));
            } else {
                System.err.println("Não foi possível deletar o arquivo temporário.");
            }

        } catch (Exception e) {
            System.err.println ("ERRO ao realizar operações no disco rígido!");
        }
    }
}
