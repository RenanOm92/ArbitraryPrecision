// Código desenvolvido por Renan Oliveira.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class arbitraryPrecision {
	public static void main (String []args) throws IOException{
		
		BufferedReader teste = new BufferedReader ( new InputStreamReader(System.in));
		System.out.println("Digite numero 1: ");
		String st1 = teste.readLine();
		System.out.println("Digite numero 2: ");
		String st2 = teste.readLine();
		
		//String st1 = "1234567890";
		//String st2 = "43";	
		

		
//		colocar isso quando parar de testar os numeros...
//	  
//		int arquitetura = 64;
//		long numeroCasasDecimais = (long)Math.pow(2, arquitetura);
//				
//		int size = (Long.toString(numeroCasasDecimais).length())-1;
	
//		arquitetura que vou testar vai ser x10, 10 bits, numero maior é 1024, 
//		tirando 1 digito, 999. 3 casas decimais.
//		
		int size = 3;
		
		long[] numero1 = separarNumeros(st1, size);
		long[] numero2 = separarNumeros(st2, size);
		
		long[] numeroParcial = fazerSomaEmColunas(numero1,numero2);
		
		//System.out.println(numeroParcial);
		
		//FazerCarry do que passar
		//completarComZero(numeroFinal, size);
		//Concatenar tudo
		
	}
	
	
	// MUITO CUIDADO QUANDO TRANSFORMA DE STRING PARA LONG
	// POIS O QUE É 1012 PODE VIRAR 1 E 12, E O CERTO SERIA 1 E 012
	
	public static long[] separarNumeros(String mensagem, int size){
		int tamanhoNum = mensagem.length();
		
		int restoNum = tamanhoNum % size;		
		int um = 0;
		if (restoNum != 0)
			um = 1;
		
		long[] numero = new long[( tamanhoNum / size)+ um];
		
		char[] aux = new char[size];
		String aux2 = new String();
		
		for (int i = 0; i < (tamanhoNum / size) ; i++){ // Talvez botar isso em openCl tb?
			int fim = mensagem.length()-(i * size);
			int comeco = fim - size;
			mensagem.getChars(comeco, fim, aux, 0); // Retorna um vetor de char com o intervalo
			
			aux2 = new String(aux); // Transforma de vetor de char para String
			//System.out.println(i+": "+aux2);
			numero[i] = Long.parseLong(aux2);	// E o vetor de string para long
			//System.out.println(i+": "+numero[i]); // É eficiente? provavelmente não!
		}		
		
		aux = new char[restoNum];
		
		if (restoNum != 0){ // Pega ultima parte do número
				 mensagem.getChars(0, restoNum, aux, 0);
				 aux2 = new String(aux);
				 numero[numero.length-1] = Long.parseLong(aux2);
				 //System.out.println(numero[numero.length-1]);
		}
		
		return numero;
	}
	
	public static long[] fazerSomaEmColunas(long[] numero1, long[] numero2){ //Sequencial, problema do 0 ainda
		//System.out.println(numero1.length);
		//System.out.println(numero2.length);
		long[] numeroFinal;
		if (numero1.length < numero2.length){
			numeroFinal = numero2;
			for (int i = 0; i < numero1.length; i++){
				numeroFinal[i] = numero1[i] + numero2[i]; 
			}
		}else{
			numeroFinal = numero1;
			for (int i = 0; i < numero2.length; i++){
				numeroFinal[i] = numero1[i] + numero2[i]; 
			}
		}
		
		for (int i = 0; i < numeroFinal.length; i++){ // Tem que pegar e completar de 0.
			System.out.println(numeroFinal[i]);
		}
		
		return numeroFinal;
		
	}
	
//	public static void completarComZero(long[] numero, int size){
//		String[] numeroFinal = new String[numero.length];
//		for (int i = 0; i < numero.length-1; i++){
//			numeroFinal[i] = numero[i]
//		}
//	}
	
}
