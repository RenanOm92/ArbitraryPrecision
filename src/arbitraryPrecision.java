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
				
		int arquitetura = 64;
		long numeroCasasDecimais = (long)Math.pow(2, arquitetura-1);
	
		int size = (Long.toString(numeroCasasDecimais).length())-1;
	
//		Arquitetura que vou testar vai ser x10, 10 bits, numero maior é 1024, 
//		então o numero maximo vai usar 9 bits: 512, somando eles chegam a 1024;
//		e eu tenho que tirar 1 digito usando os decimais.
//		Exemplo: Arquitetura suporta maior numero 1024 = 2^10, vou usar	cada número
//		então até 512 = 2^9, 3 digitos, só que se eu separar em intervalos de 3 digitos,
//		corre o risco de eu pegar números 999, que quando somados extrapolem o 1024
//		que é o maior suportado pela máquina. Então vou pegar o número de digitos de
//		(2^n-1) e tirar 1 digito. Assim sendo, o exemplo de 1024, separaria em intervalos
//		de 2 dígitos.		
		
//		Arquitetura máxima suportada é 64x.
		
		long[] numero1 = separarNumeros(st1, size);
		long[] numero2 = separarNumeros(st2, size);
		
		
		long tempoInicio = System.currentTimeMillis();
		long[] numeroParcial = fazerSomaEmColunasSequencial(numero1,numero2);
		System.out.println("Tempo da soma sequencial: "+(System.currentTimeMillis()-tempoInicio));
		
		tempoInicio = System.currentTimeMillis();
		long[] numeroParcial2 = fazerSomaEmColunasParalelo(numero1, numero2);
		System.out.println("Tempo da soma paralelo em openCL: "+(System.currentTimeMillis()-tempoInicio));
		
		
//		long tempoInicio = System.currentTimeMillis();
		String[] numero = passarCarry(numeroParcial,size);
		String[] numerox = passarCarry(numeroParcial2,size);
//		System.out.println("Tempo Total: "+(System.currentTimeMillis()-tempoInicio));
		
		
//		tempoInicio = System.currentTimeMillis();
//		passarCarry2(numeroParcial,size);
//		System.out.println("Tempo Total: "+(System.currentTimeMillis()-tempoInicio)); 
		
// 		Devido a transformada de string para long, o que era 100012 pode virar 100 | 12, 
// 		podendo gerar inconsistencia	depois para realizar a concatenação.	
// 		Então é necessário preencher com 0 os números que não tem o tamanho da coluna.	
		numero = completarComZero(numero, size);
		numerox = completarComZero(numerox, size);
		
		
		String soma = concatenar(numero);
		String soma2 = concatenar(numerox);
		System.out.println("Resultado sequenc. : "+soma);
		System.out.println("Resultado paralelo : "+soma2);
	}
	
	

	
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
	
	
	public static long[] fazerSomaEmColunasParalelo(long[] numero1,long[] numero2){
		
		long[] numeroFinal;
		long[] numeroParcial;
		int maior;
		if (numero1.length < numero2.length){
			maior = 2;
			numeroFinal = numero2;
		}else{
			maior = 1;
			numeroFinal = numero1;
		}
		numeroParcial = Paralelo.somaParalelo(numero1,numero2,maior);
		for (int i = 0; i < numeroParcial.length; i++){
			numeroFinal[i] = numeroParcial[i];
		}
		return numeroFinal;
	}
	
	public static long[] fazerSomaEmColunasSequencial(long[] numero1, long[] numero2){ //Sequencial, problema do 0 ainda
		//System.out.println(numero1.length);
		//System.out.println(numero2.length);
		
		long[] numeroFinal;
		if (numero1.length < numero2.length){
			numeroFinal = numero2;
			for (int i = 0; i < numero1.length; i++){
				//System.out.print(numero1[i]+ " + "+numero2[i]+" = ");
				numeroFinal[i] = numero1[i] + numero2[i];
				//System.out.println(numeroFinal[i]);
			}
		}else{
			numeroFinal = numero1;
			for (int i = 0; i < numero2.length; i++){
				//System.out.print(numero1[i]+ " + "+numero2[i]+" = ");
				numeroFinal[i] = numero1[i] + numero2[i];
				//System.out.println(numeroFinal[i]);
			}
		}
		
//		for (int i = 0; i < numeroFinal.length; i++){ // Tem que pegar e completar de 0.
//			System.out.println(numeroFinal[i]);
//		}
		
		return numeroFinal;
		
	}
	
	public static String[] passarCarry(long[] numero, int size){
		String[] numeroString = new String[numero.length];
		for (int i = 0; i < numero.length; i++){
			numeroString[i] = Long.toString(numero[i]);
			//System.out.println("number "+numeroString[i]);
			if ((numeroString[i].length() > size) && (i < numero.length-1)){ // Se for lento transformar para string, dá pra calcular o (Math.log10 (n) + 1) e ver quantos digitos o numero tem.
				numeroString[i] = numeroString[i].substring(1,size+1); // Remove 1º posição
				numero[i+1] = numero[i+1] + 1; // Carry passado
				//System.out.println(numeroString[i]);
			}	
		}
		return numeroString;
	}

//	public static void passarCarry2(long[] numero, int size){
//		for (int i = 0; i < numero.length; i++){		
//			if (qtdDigitos(numero[i]) > size){
//				
//			}
//		}
//	}
//	
//	public static int qtdDigitos (long n) {  
//		n = Math.abs(n);  
//	    if (n == 0) 
//	    	return 1;  
//	    else 
//	    	return (int) (Math.log10 (n) + 1);   
//	}
	
	public static String[] completarComZero(String[] numero, int size){
		StringBuilder concatenador;
		for (int i = 0; i < numero.length-1; i++){
			if (numero[i].length() < size){
				
				concatenador = new StringBuilder();
				for (int j = 0; j < size - numero[i].length(); j++){
					concatenador.append("0");					
				}
				concatenador.append(numero[i]);
				numero[i] = concatenador.toString();
			}
		}
		return numero;
	}
	
	public static String concatenar(String[] numero){
		StringBuilder saida = new StringBuilder();
		for (int i = numero.length -1; i >= 0 ; i--){
			saida.append(numero[i]);
		}
		return saida.toString();
	}
	
}
