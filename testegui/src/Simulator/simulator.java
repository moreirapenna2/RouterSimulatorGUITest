/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author root
 */
public class simulator {
    static String results = new String();
    
    public static String getResults(){
        return results;
    }
    
    private static double aleatorio() {
        Random rand = new Random();
        double n = rand.nextDouble();
        return (n);
    }
    
    private static double chegada_pct(double parametro_l){
        return (-1.0/parametro_l)*Math.log(aleatorio());
    }
    
    private static double gerar_tamanho_pct(){
        double a = aleatorio();
        if(a <= 0.5){
            return (550.0*8.0/(1000000));}     
        if(a <= 0.9){
            return (40.0*8.0/(1000000));}
        return (1500.0*8.0/(1000000));
    }
    
    public static double[] run(double tempo_total, double intervalo, double link){
        results="";
        double tempo = 0.0;
        double cont_pcts=0.0;
        double tam_pct;
        intervalo = 1.0/intervalo;
        //fila, onde fila == 0 indica roteador vazio;
        //fila == 1 indica 1 pacote, ja em transmissao;
        //fila > 1 indica 1 pacote em transmissao e demais em espera;
        double fila = 0.0;

        double chegada_proximo_pct = chegada_pct(intervalo);
        double saida_pct_atendimento = 0.0;

        while(tempo <= tempo_total){
            //roteador vazio, Logo avanco no tempo para a chegada do proximo pacote
            if(fila == 0.0)
                tempo = chegada_proximo_pct;
            else{
                //ha fila
                tempo = Math.min(chegada_proximo_pct, saida_pct_atendimento);
            }

            //chegada de pacote
            if(tempo == chegada_proximo_pct){
                //printf("Chegada de pacote no tempo: %lF\n", tempo);
                results = results.concat("Chegada de pacote no tempo: "+tempo+"\n");
                //roteador estava livre
                if(fila == 0.0){
                    //descobrir o tamanho do pacote
                    tam_pct = gerar_tamanho_pct();
                    //gerando o tempo em que o pacote atual saira do sistema
                    saida_pct_atendimento = tempo + tam_pct/link;
                    cont_pcts+=1;
                }
                //pacote colocado na fila
                fila++;
                results = results.concat("Fila: "+fila+"\n");
                
                //gerar o tempo de chegada do proximo
                chegada_proximo_pct = tempo + chegada_pct(intervalo);
            }else{//saida de pacote
                results = results.concat("Saida de pacote no tempo: "+tempo+"\n");
                results = results.concat("------------------------------\n");
                fila--;
                results = results.concat("Fila: "+fila+"\n");

                if(fila > 0.0){
                    //descobrir o tamanho do pacote
                    tam_pct = gerar_tamanho_pct();
                    //gerando o tempo em que o pacote atual saira do sistema
                    saida_pct_atendimento = tempo + tam_pct/link;
                }
            }
        }
        
        double[] tmp = new double[2];
        tmp[0] = cont_pcts;
        tmp[1] = tempo/cont_pcts;
        System.out.println(tmp[0]);
        System.out.println(tmp[1]);
        return tmp;
    }
    
}
