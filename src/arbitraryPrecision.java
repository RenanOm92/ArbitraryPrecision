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
		
		
		//somar agora os 2 números
	}
	
	public static long[] separarNumeros(String mensagem, int size){
		int tamanhoNum = mensagem.length();
		
		long[] numero = new long[( tamanhoNum / size)+ (tamanhoNum % size)];
		
		char[] aux = new char[size];
		String aux2 = new String();
		
		for (int i = 0; i < (tamanhoNum / size) ; i++){
			int fim = mensagem.length()-(i * size);
			int comeco = fim - size;
			mensagem.getChars(comeco, fim, aux, 0); // Retorna um vetor de char com o intervalo
			
			aux2 = new String(aux); // Transforma de vetor de char para String
			
			numero[i] = Long.parseLong(aux2);	// E o vetor de string para long
			System.out.println(numero[i]); // É eficiente? provavelmente não!
		}
		
		int restoNum = tamanhoNum % size;
		
		aux = new char[restoNum];
		
		if (restoNum != 0){ // Pega ultima parte do número
				 mensagem.getChars(0, restoNum, aux, 0);
				 aux2 = new String(aux);
				 numero[numero.length-1] = Long.parseLong(aux2);
				 System.out.println(numero[numero.length-1]);
		}
		
		return numero;
	}
}
