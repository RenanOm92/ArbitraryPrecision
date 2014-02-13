// Código desenvolvido por Renan Oliveira.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class arbitraryPrecision {
	public static void main (String []args) throws IOException{
		BufferedReader teste = new BufferedReader ( new InputStreamReader(System.in));
		String st1 = teste.readLine();
		String st2 = teste.readLine();
		
		int number1 = Integer.parseInt(st1); 
		int number2 = Integer.parseInt(st2); 
		// arquitetura que vou testar vai ser x10, 10 bits, numero maior 1024, tirando 1 digito, 999.
		
		int aux = st1.length();
		//separando numero 1 em colunas de 3 digitos.
		
		String numero1[] = new String[(aux / 3)+1];
		
		for (int i = 0; i < (aux / 3) ; i++){
			int fim = st1.length()-(i*3);
			int comeco = fim - 3;
			char[] aux2 = new char[3];
			st1.getChars(comeco, fim, aux2, 0);
			
			numero1[i] = new String(aux2);
			System.out.println(numero1[i]);
		}
		
		
		
	}
	
	
}
