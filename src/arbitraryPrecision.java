// Codigo desenvolvido por Renan Oliveira.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class arbitraryPrecision {
	public static void main (String []args) throws IOException{
		
		BufferedReader teste = new BufferedReader ( new InputStreamReader(System.in));
		System.out.println("Digite numero 1: ");
		String st1 = teste.readLine();
		System.out.println("Digite numero 2: ");
		String st2 = teste.readLine();		
				
		int arquitetura = 64;
		
		int size;
		
		size = definirTamanhoColunas(arquitetura,"soma");

		size = definirTamanhoColunas(arquitetura,"multiplicacao");

		// TESTE
//		size = 2;
		
//		Arquitetura que vou testar vai ser x10, 10 bits, numero maior e 1024, 
//		entao o numero maximo pra soma vai usar 9 bits: 512, somando eles chegam a 1024;
//		e eu tenho que tirar 1 digito usando os decimais.
//		Exemplo: Arquitetura suporta maior numero 1024 = 2^10, vou usar	cada numero
//		então até 512 = 2^9, 3 digitos, só que se eu separar em intervalos de 3 digitos,
//		corre o risco de eu pegar números 999, que quando somados extrapolem o 1024
//		que é o maior suportado pela máquina. Então vou pegar o número de digitos de
//		(2^n-1) e tirar 1 digito. Assim sendo, o exemplo de 1024, separaria em intervalos
//		de 2 dígitos.		
		
//		Arquitetura máxima suportada é 64x.	
		
		String resultadoSoma;
		
		long tempoInicio = System.currentTimeMillis();
		
		resultadoSoma = somar(st1,st2,size);
		
		System.out.println("Resultado soma: "+resultadoSoma);
		System.out.println("Tempo da soma : "+(System.currentTimeMillis()-tempoInicio)+" ms");	
		
		String resultadoMult;
		
		tempoInicio = System.currentTimeMillis();
		
		multiplicar(st1,st2,size);
		
		System.out.println("Tempo da multiplicação: "+(System.currentTimeMillis()-tempoInicio)+" ms");
		//System.out.println("Resultado multiplicação sequenc. : "+resultadoMult);

	}
	
	public static int definirTamanhoColunas(int arquitetura, String operacao){
		if (operacao == "soma"){
			long numeroCasasDecimais = (long)Math.pow(2, arquitetura-1);
			int size = (Long.toString(numeroCasasDecimais).length())-1;
			return size;
		}
		else {
			// definir tamanho da coluna em decimal
			long numeroCasasDecimais = (long)Math.pow(2, arquitetura/2);
			int size = (Long.toString(numeroCasasDecimais).length() - 1);
			
			return size;
			// definir tamanho da coluna em HEXA
//			long numeroCasasDecimais = (long)Math.pow(2, arquitetura/2) - 1;
//			int size = (Long.toHexString(numeroCasasDecimais).length());
//			
//			return size;
		}
	}

	public static long[] separarNumeros(String mensagem, int size){
		int tamanhoNum = mensagem.length(); // Tamanho total do número
		
		int restoNum = tamanhoNum % size;		
		int um = 0;
		if (restoNum != 0)
			um = 1;
		
		long[] numero = new long[( tamanhoNum / size)+ um]; // Número de colunas
		
		char[] aux = new char[size];
		String aux2 = new String();
		
		for (int i = 0; i < (tamanhoNum / size) ; i++){ // Faz o loop, percorrendo do fim do número (mensagem) até quase o inicio, separando em colunas, e bota no vetor de long.
			int fim = mensagem.length()-(i * size);
			int comeco = fim - size;
			mensagem.getChars(comeco, fim, aux, 0); // Retorna um vetor de char com o intervalo
			
			aux2 = new String(aux); // Transforma de vetor de char para String
			
			numero[i] = Long.parseLong(aux2);		// E o vetor de string para long
			//numero[i] = Long.parseLong(aux2,16);	// Hexa 
			//System.out.println(i+": "+numero[i]); // É eficiente? provavelmente não!
		}		
		
		aux = new char[restoNum];

		if (restoNum != 0){ // Pega primeira parte do número (mensagem) 
				 mensagem.getChars(0, restoNum, aux, 0);
				 aux2 = new String(aux);
				 numero[numero.length-1] = Long.parseLong(aux2);
				 //numero[numero.length-1] = Long.parseLong(aux2,16); // Hexa
				 //System.out.println(numero[numero.length-1]);
		}

		return numero;
	}
	
	public static String somar(String st1, String st2,int size){
		
		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);

//		SOMA SEQUENCIAL
		
//		long[] numeroParcial = fazerSomaEmColunasSequencial(numero1,numero2);	

//		String[] numero = passarCarry(numeroParcial,size);		

//		String soma = concatenar(numero);	
//		return soma;

		
//		SOMA PARALELO
		
		long[] numeroParcial2 = fazerSomaEmColunasParalelo(numero1, numero2);

		String[] numerox = passarCarry(numeroParcial2,size);		

		String soma2 = concatenar(numerox);
		return soma2;

		
	}
		
	public static long[] fazerSomaEmColunasParalelo(long[] numero1,long[] numero2){

		long[] numeroParcial;

		numeroParcial = Paralelo.somaParalelo(numero1,numero2);

		return numeroParcial;
	}
	
	public static long[] fazerSomaEmColunasSequencial(long[] numero1, long[] numero2){
		
		long[] numeroFinal;
		if (numero1.length < numero2.length){
			numeroFinal = new long[numero2.length];
			for (int i = numero1.length; i < numero2.length; i++){
				numeroFinal[i] = numero2[i];
			}
			
			for (int i = 0; i < numero1.length; i++){
				//System.out.print(numero1[i]+ " + "+numero2[i]+" = ");
				numeroFinal[i] = numero1[i] + numero2[i];
				//System.out.println(numeroFinal[i]);
			}
		}else{
			numeroFinal = new long[numero1.length];
			for (int i = numero2.length; i < numero1.length; i++){
				numeroFinal[i] = numero1[i];
			}
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
		for (int i = 0; i < numero.length-1; i++){
			
			numeroString[i] = Long.toString(numero[i]);
			
			int tamanhoNumero = numeroString[i].length();

			// Confere se possui Carry
			if (tamanhoNumero > size){ 
				int diferencaCarry = tamanhoNumero - size;
				
				// seleciono o pedaço que é carry e somo com o número posterior
				numero[i+1] = numero[i+1] + Integer.parseInt(numeroString[i].substring(0,diferencaCarry));
				// armazeno o pedaço sem a parte do carry
				numeroString[i] = numeroString[i].substring(diferencaCarry);
				
			}			
			
			// Confere se o número precisa de 0 ( 2 casos: quando o tamanho do número é inferior ao intervalo e ele não tá no fim-1 ou quando ele é inferior ao intervalo, ele tá no fim-1 e o fim não é 0
			else if ( (tamanhoNumero < size) && ( ( i < numero.length-2) || (( i == numero.length-2) && (numero[numero.length-1] != 0)))){
				numeroString[i] = completarComZero(numeroString[i], size);
			}
		}
		
		if (numero[numero.length-1] != 0){
			numeroString[numero.length-1] = Long.toString(numero[numero.length-1]);
		}
		else{
			numeroString[numero.length-1] = "";
		}
		return numeroString;
	}

//		Devido a transformada de string para long, o que era 100|012 pode virar 100 | 12, 
//		podendo gerar inconsistencia	depois para realizar a concatenação.	
//		Então é necessário preencher com 0 os números que não tem o tamanho da coluna.
	
	public static String completarComZero(String numero, int size){
		StringBuilder concatenador;	
		concatenador = new StringBuilder();
		for (int j = 0; j < size - numero.length(); j++){
			concatenador.append("0");					
		}
		concatenador.append(numero);
		numero = concatenador.toString();		
		return numero;
	}
	
	public static String concatenar(String[] numero){
		StringBuilder saida = new StringBuilder();
		for (int i = numero.length -1; i >= 0 ; i--){
			saida.append(numero[i]);
		}
		return saida.toString();
	}	
	
	public static void multiplicar (String st1, String st2, int size){

		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);
		
		//return multiplicarSequencial(numero1, numero2, size);
		multiplicarParalelo(numero1, numero2, size);
	}
	
	public static String multiplicarSequencial(long[] numero1, long[] numero2,int size){
		String[] resultadoParcial;
		long[] multiplicacao = new long[numero1.length];
		String resultado;
		String resultadoMult = "0";
		int contadorGeral = 0;
		
		for (int i=0; i < numero2.length; i++){ // pega coluna por coluna do multiplicador

			long coluna = numero2[i];
			
			while (coluna >= 1){ // itera digito por digito do multiplicador
								
				long digito = coluna % 10; // pega o ultimo digito				
				
				for (int j=0; j < numero1.length; j++){
					multiplicacao[j] = digito * numero1[j]; // multiplica o digito pelo numero 1
				}
				resultadoParcial = passarCarry(multiplicacao, size); // passa o carry entre os numeros

				for (int z = 0 ; z < resultadoParcial.length-1; z++){
					resultadoParcial[z] = completarComZero(resultadoParcial[z], size); // completa com 0
				}
				
				resultado = concatenar(resultadoParcial); // concatena encontrando a mult. do digito pelo numero 1
				
				StringBuilder aux = new StringBuilder();
				aux.append(resultado);
				for (int k = 0 ; k < contadorGeral ; k++){ // coloca X números de 0 ao final da multiplicacao encontrada
					aux.append("0");
				}
				resultado = aux.toString();
				
				//System.out.println(resultado +" + "+ resultadoMult );

				resultadoMult = somar(resultado,resultadoMult,size); // Soma a multiplicacao com a soma total ja encontrada
																	// Necessario utilizar soma de bignums pois a soma total
																	// pode extrapolar o limite máximo que o tipo long em java armazena
				coluna = coluna / 10;
				contadorGeral++;
			}

		}
		return resultadoMult;
		
	}
	
	public static void multiplicarParalelo(long[] numero1, long[] numero2, int size){
		System.out.println(numero1[0]);
		System.out.println(numero1[1]);
		System.out.println(numero1[2]);
		System.out.println(numero1[3]);
		System.out.println("a");
		long[] resultadoMultParalelo = Paralelo.multiplicaParalelo(numero1, numero2);
		
//		for (int i = 0; i < resultadoMultParalelo.length; i++){
//			System.out.println(resultadoMultParalelo[i]);
//		}
		
		
		/*
		 *  casos de teste
		 * 
		 * 42187782417843887943789487242187421877824178438879437894872421874218778241784388794378948724218742187782417843887943789487242187
		 *  X
		 * 42187782417843887943789487242187421877824178438879437894872421874218778241784388794378948724218742187782417843887943789487242187
		 * 
		 * 218742187782417843887943789487242187
		 * X
		 * 218742187782417843887943789487242187
		 * 
		 * 
		 */
		for (int i = 0; i < resultadoMultParalelo.length ; i++){
			System.out.println(resultadoMultParalelo[i]);
		}
		String[] resultadoCarry = passarCarry(resultadoMultParalelo, size);	
		System.out.println("a");
		for (int i = 0; i < resultadoCarry.length ; i++){
			System.out.println(resultadoCarry[i]);
		}
		String resultadoFinal = concatenar(resultadoCarry);
		System.out.println("Resultado multiplicação: "+resultadoFinal);
		
	}
	
}
