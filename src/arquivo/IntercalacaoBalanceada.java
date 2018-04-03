package arquivo;

import estruturas.PessoaFisica;
import org.json.simple.JSONObject;

import java.io.*;

public class IntercalacaoBalanceada extends ArquivoUtil {

    /* AGORA VAI DAR CERTO EM NOME DE JESUS */
    private static final int TAM_BLOCO = 4;
    PessoaFisica[] blocoClientes = new PessoaFisica[TAM_BLOCO];
    String arquivos[] = new String[TAM_BLOCO];
    String arquivoOrdenado = "clientes-ordenado.json";

    public IntercalacaoBalanceada() {
        arquivos[0] = "temp1.json";
        arquivos[1] = "temp2.json";
        arquivos[2] = "temp3.json";
        arquivos[3] = "temp4.json";
    }

    public void intercalar() {
        try {
            contarLinhas();

            delete(arquivos[0]);
            delete(arquivos[1]);
            delete(arquivos[2]);
            delete(arquivos[3]);

            entrada = new FileInputStream("clientes.json");
            leitor = new InputStreamReader(entrada);
            buffer_entrada = new BufferedReader(leitor);
            String linha;
            int cont = 0;
            int index = 0;

            while (cont<=quantidadeLinhas) {

                blocoClientes = new PessoaFisica[TAM_BLOCO];

                for(int i = 0; i<TAM_BLOCO; i++) {
                    linha = buffer_entrada.readLine();
                    blocoClientes[i] = montarObjeto(linha);
                    ordenarBlocoClientes();
                    cont++;
                }

                for (PessoaFisica cliente : blocoClientes)
                    gravarString(montarString(cliente),arquivos[index]);

                gravarString(SEPARADOR_BLOCO_INTERCALACAO + separadorDeLinha, arquivos[index]);

                if(index == 0)
                    index = 1;
                else
                    index = 0;
            }

            cruzarArquivos(0, 1, 1);

        } catch (Exception e) {
            System.err.println("Erro ao intercalar arquivos.");
        } finally {
            fecharManipuladoresEscrita();
        }
    }

    private int cruzarArquivos (int indexArquivo1, int indexArquivo2, int intercalacao) {

        int retorno = 0;

        try {
            String linha1 = null, linha2 = null;

            int tamanhoArray = TAM_BLOCO*(2*intercalacao);
//            System.out.println("Lendo arquivos: " + indexArquivo1 + " e " + indexArquivo2);
//            System.out.println("Tamanho array: " + tamanhoArray);

            FileInputStream entrada1 = new FileInputStream(arquivos[indexArquivo1]);
            FileInputStream entrada2 = new FileInputStream(arquivos[indexArquivo2]);

            InputStreamReader leitor1 = new InputStreamReader(entrada1);
            InputStreamReader leitor2 = new InputStreamReader(entrada2);

            BufferedReader buffers[] = new BufferedReader[2];
            buffers[0] = new BufferedReader(leitor1);
            buffers[1] = new BufferedReader(leitor2);

            int[] prox = proximoIndex(indexArquivo1, indexArquivo2);
            int index = prox[0];

            linha1 = buffers[0].readLine();
            linha2 = buffers[1].readLine();

            while (linha1 != null || linha2 != null) {
                blocoClientes = new PessoaFisica[tamanhoArray];
                int j = 0;

                while (linha1 != null && !linha1.equals(SEPARADOR_BLOCO_INTERCALACAO)) {
                    blocoClientes[j] = montarObjeto(linha1);
                    j++;
                    linha1 = buffers[0].readLine();
                }

                while (linha2 != null && !linha2.equals(SEPARADOR_BLOCO_INTERCALACAO)) {
                    blocoClientes[j] = montarObjeto(linha2);
                    j++;
                    linha2 = buffers[1].readLine();
                }

                ordenarBlocoClientes();

                for (PessoaFisica cliente : blocoClientes) {

                    gravarString(montarString(cliente), arquivos[index]);
                }
                gravarString(SEPARADOR_BLOCO_INTERCALACAO + separadorDeLinha, arquivos[index]);

                index = proximoIndex(index);

                linha1 = buffers[0].readLine();
                linha2 = buffers[1].readLine();
            }

            entrada1.close();
            entrada2.close();
            leitor1.close();
            leitor2.close();
            buffers[0].close();
            buffers[1].close();
            delete(arquivos[indexArquivo1]);
            delete(arquivos[indexArquivo2]);

            if(tamanhoArray < quantidadeLinhas) {
//                System.out.println("Entrou na recursividade. Linhas: " + quantidadeLinhas);
                retorno = cruzarArquivos(prox[0], prox[0]+1, intercalacao + 1);
            } else {
                retorno = prox[0];
                fecharManipuladoresEscrita();
                File arqTemp = new File (arquivos[prox[0]]);
                delete(NOMEARQUIVO_SORT);
                if(arqTemp.renameTo(new File(NOMEARQUIVO_SORT))){
//                    System.out.println("Tudo certo com o rename.");
                } else {
//                    System.out.println("Nada certo com o rename.");
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao intercalar arquivos.");
            e.printStackTrace();
        }
        return retorno;
    }

    private boolean gravarString (String linha, String arquivo) {
        boolean resultado = false;
        File arq = null;
        FileOutputStream saida = null;
        OutputStreamWriter gravador = null;
        BufferedWriter buffer_saida = null;
        try {
            arq = new File (arquivo);
            saida = new FileOutputStream (arq, true);
            gravador = new OutputStreamWriter (saida);
            buffer_saida = new BufferedWriter (gravador);

            buffer_saida.write(linha);

            buffer_saida.flush();
            resultado = true;
        } catch (Exception e) {
            System.out.println ("ERRO ao gravar a linha da intercalação no disco rígido!");
            e.printStackTrace ();
        }  finally {
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
            } catch (Exception e) {
                System.out.println("ERRO ao fechar os manipuladores de escrita do arquivo");
            }
        }

        return resultado;
    }

    private void ordenarBlocoClientes () {

        PessoaFisica aux = null;

        for(int i = 0; i<blocoClientes.length; i++){
            for(int j = 0; j<blocoClientes.length-1; j++){
                PessoaFisica nome1 = blocoClientes[j];
                PessoaFisica nome2 = blocoClientes[j + 1];
                if(nome1 != null && nome2 != null && (nome1.getNome().compareTo(nome2.getNome()) > 0)){
                    aux = blocoClientes[j];
                    blocoClientes[j] = blocoClientes[j+1];
                    blocoClientes[j+1] = aux;
                }
            }
        }
    }

    private int proximoIndex (int index) {
        int novoIndex = 0;

        if (index == 0)
            novoIndex = 1;
        else if (index == 1)
            novoIndex = 0;
        else if (index == 2)
            novoIndex = 3;
        else if (index == 3)
            novoIndex = 2;

        return novoIndex;
    }

    private int[] proximoIndex (int index, int index2) {
        int[] novoIndex = new int[2];

        if (index == 0 && index2 == 1) {
            novoIndex[0] = 2;
            novoIndex[1] = 3;
        }
        else if (index == 3 && index2 == 4) {
            novoIndex[0] = 0;
            novoIndex[1] = 1;
        }

        return novoIndex;
    }
}
