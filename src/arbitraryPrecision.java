// Codigo desenvolvido por Renan Oliveira.

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.math.BigInteger;

public class arbitraryPrecision {
	public static void main (String []args) throws IOException{
		
		String operacao = args[0];
		String arquivo_numero1 = args[1];
		String arquivo_numero2 = args[2];
		BufferedReader leitor = new BufferedReader ( new FileReader(arquivo_numero1));
//		BufferedReader leitor = new BufferedReader ( new InputStreamReader(System.in));
//		System.out.println("Digite numero 1: ");
		String st1 = leitor.readLine();
		System.out.println("Numero 1 lido: \n"+st1);
		leitor = new BufferedReader ( new FileReader(arquivo_numero2));
//		System.out.println("Digite numero 2: ");
		String st2 = leitor.readLine();				 
		System.out.println("Numero 2 lido: \n"+st2);
		
		System.out.println("\n---------------------------------\n");
		
		int arquitetura = 64;
		
		int size;

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
		
		BufferedReader leitor2 = new BufferedReader ( new InputStreamReader(System.in));
		String repetir = "s";
		

		
		long tempoInicio;
		
		while (!repetir.equals("n")){
		for (int k = 0; k < 11; k++){	
		if (operacao.equals("benchmark") || operacao.equals("Benchmark")){
			for (int i = 0; i < 2; i++){
				if (i==0){
					long tempo;
					size = definirTamanhoColunas(arquitetura,"soma");
					tempo = cronometrar(st1, st2, size, "SomaS");
					tempo = cronometrar(st1, st2, size, "SomaP");
					tempo = cronometrar(st1, st2, size, "SomaBigInt");
					size = definirTamanhoColunas(arquitetura,"mult");
					tempo = cronometrar(st1, st2, size, "MultS");
					tempo = cronometrar(st1, st2, size, "MultP0D");	
					tempo = cronometrar(st1, st2, size, "MultP1D");
					tempo = cronometrar(st1, st2, size, "MultP2D");
					tempo = cronometrar(st1, st2, size, "MultBigInt");
					
				}else{
					long tempo;
					
					size = definirTamanhoColunas(arquitetura,"soma");
					
					tempo = cronometrar(st1, st2, size, "SomaS");
					System.out.println("Tempo da Soma Sequencial em Java feita pelo autor \"SomaS\": "+tempo+" ms");
					
					tempo = cronometrar(st1, st2, size, "SomaP");
					System.out.println("Tempo da Soma Paralelo em OpenCL feita pelo autor \"SomaP\": "+tempo+" ms");
					
					tempo = cronometrar(st1, st2, size, "SomaBigInt");
					System.out.println("Tempo da Soma Sequencial em Java da classe BigInteger: "+tempo+" ms");
					
					size = definirTamanhoColunas(arquitetura,"mult");
					
					tempo = cronometrar(st1, st2, size, "MultS");
					System.out.println("Tempo da Multiplicacao Sequencial em Java feita pelo autor \"MultS\": "+tempo+" ms");	
					
					tempo = cronometrar(st1, st2, size, "MultP0D");
					System.out.println("Tempo da Multiplicacao 0 dimensoes em OpenCL feita pelo autor \"MultP0D\": "+tempo+" ms");
					
					tempo = cronometrar(st1, st2, size, "MultP1D");
					System.out.println("Tempo da Multiplicacao 1 dimensao em OpenCL feita pelo autor \"MultP1D\": "+tempo+" ms");
					
					tempo = cronometrar(st1, st2, size, "MultP2D");
					System.out.println("Tempo da Multiplicacao 2 dimensoes em OpenCL feita pelo autor \"MultP2D\": "+tempo+" ms");
					
					tempo = cronometrar(st1, st2, size, "MultBigInt");
					System.out.println("Tempo da Multiplicacao Sequencial em Java da classe BigInteger: "+tempo+" ms");
					
				}
			}
			
		}else if (operacao.equals("SomaS")){
				size = definirTamanhoColunas(arquitetura,"soma");
				String resultadoSoma;
				
				tempoInicio = System.currentTimeMillis();
				resultadoSoma = somar(st1,st2,size,"sequencial");
				
//				System.out.println("Resultado da soma Sequencial: \n"+resultadoSoma);
				System.out.println("Tempo da soma Sequencial: "+(System.currentTimeMillis()-tempoInicio)+" ms");	
				
//				String resultadoSomaBigIntegerJava = SomaBigIntegerJava(st1, st2);
//				
//				if (resultadoSomaBigIntegerJava.equals(resultadoSoma)){
//					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
//				}else{
//					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
//				}
				
			}else if (operacao.equals("SomaP")){
				size = definirTamanhoColunas(arquitetura,"soma");
				String resultadoSoma;
				
				tempoInicio = System.currentTimeMillis();
				resultadoSoma = somar(st1,st2,size,"paralelo");
				
		//		System.out.println("Resultado da soma em OpenCL: \n"+resultadoSoma);
				System.out.println("Tempo da soma em OpenCL: "+(System.currentTimeMillis()-tempoInicio)+" ms");	
				
//				String resultadoSomaBigIntegerJava = SomaBigIntegerJava(st1, st2);
//				
//				if (resultadoSomaBigIntegerJava.equals(resultadoSoma)){
//					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
//				}else{
//					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
//				}
			
			}else if (operacao.equals("MultS")){
				size = definirTamanhoColunas(arquitetura,"multiplicacao");
				String resultadoMult;
				
				tempoInicio = System.currentTimeMillis();
				resultadoMult = multiplicar(st1,st2,size,"sequencial");

//				System.out.println("Resultado da multiplicacao Sequencial: \n"+resultadoMult);
				System.out.println("Tempo da multiplicacao Sequencial: "+(System.currentTimeMillis()-tempoInicio)+" ms");
				
//				String resultadoMultBigIntegerJava = MultBigIntegerJava(st1, st2);
//				
//				if (resultadoMultBigIntegerJava.equals(resultadoMult)){
//					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
//				}else{
//					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
//				}
				
			}else if (operacao.equals("MultP0D")){
				
				String resultadoMult;
				
				tempoInicio = System.currentTimeMillis();
				resultadoMult = multiplicar(st1,st2,9,"0D");
				
				System.out.println("Resultado da multiplicacao em OpenCL 0D: \n"+resultadoMult);
				System.out.println("Tempo da multiplicacao em OpenCL: "+(System.currentTimeMillis()-tempoInicio)+" ms");
			
				String resultadoMultBigIntegerJava = MultBigIntegerJava(st1, st2);
				
				if (resultadoMultBigIntegerJava.equals(resultadoMult)){
					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
				}else{
					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
				}
			
			}else if (operacao.equals("MultP1D")){
				String resultadoMult;
				
				tempoInicio = System.currentTimeMillis();
				resultadoMult = multiplicar(st1,st2,9,"1D");
				
				System.out.println("Resultado da multiplicacao em OpenCL 1D: \n"+resultadoMult);
				System.out.println("Tempo da multiplicacao em OpenCL: "+(System.currentTimeMillis()-tempoInicio)+" ms");
			
				String resultadoMultBigIntegerJava = MultBigIntegerJava(st1, st2);
				
				if (resultadoMultBigIntegerJava.equals(resultadoMult)){
					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
				}else{
					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
				}
			}else if (operacao.equals("MultP2D")){
				String resultadoMult;
				
				tempoInicio = System.currentTimeMillis();
				resultadoMult = multiplicar(st1,st2,9,"2D");
				
				System.out.println("Resultado da multiplicacao em OpenCL 2D: \n"+resultadoMult);
				System.out.println("Tempo da multiplicacao em OpenCL: "+(System.currentTimeMillis()-tempoInicio)+" ms");
			
				String resultadoMultBigIntegerJava = MultBigIntegerJava(st1, st2);
				
				if (resultadoMultBigIntegerJava.equals(resultadoMult)){
					System.out.println("Comparacao com BigInteger do Java:     A P R O V A D O");
				}else{
					System.out.println("Comparacao com BigInteger do Java:     R E P R O V A D O");
				}
			}else if (operacao.equals("SomaBigInt")){
				tempoInicio = System.currentTimeMillis();
				String resultadoMultBigIntegerJava = SomaBigIntegerJava(st1, st2);
//				System.out.println("Resultado da soma BigInteger: \n"+resultadoMultBigIntegerJava);
				System.out.println("Tempo da soma BigInteger: "+(System.currentTimeMillis()-tempoInicio)+" ms");
			}else if (operacao.equals("MultBigInt")){
				
				tempoInicio = System.currentTimeMillis();
				String resultadoMultBigIntegerJava = MultBigIntegerJava(st1, st2);
//				System.out.println("Resultado da multiplicacao em BigInteger: \n"+resultadoMultBigIntegerJava);
				System.out.println("Tempo da multiplicacao BigInteger: "+(System.currentTimeMillis()-tempoInicio)+" ms");
			
			}else{
				System.out.println("Operacao nao reconhecida! Tente novamente:\nSomaS = Soma Sequencial\nSomaP = Soma Paralelo\nMultS = Multiplicacao Sequencial\nMultP0D = Multiplicacao Paralelo 0D\nMultP1D = Multiplicacao Paralelo 1D\nMultP2D = Multiplicacao Paralelo 2D");
			}
		}
			System.out.println("\n---------------------------------\n");
		
			System.out.println("Pretende realizar outra operacao? s/n");
		
			repetir = leitor2.readLine();
			if (!repetir.equals("n")){
				System.out.println("Digite a operacao: ");
				operacao = leitor2.readLine();
			}
		}
		
	}
	
	public static long cronometrar(String numero1, String numero2, int size, String operacao){
		long tempoInicio = 0;
		
		if (operacao.equals("SomaS")){
			tempoInicio = System.currentTimeMillis();
			String resultadoSoma = somar(numero1,numero2,size,"sequencial");
		}else if (operacao.equals("SomaP")){
			tempoInicio = System.currentTimeMillis();
			String resultadoSoma = somar(numero1,numero2,size,"paralelo");
		}else if (operacao.equals("SomaBigInt")){
			tempoInicio = System.currentTimeMillis();
			String resultadoSoma = SomaBigIntegerJava(numero1,numero2);
		}else if (operacao.equals("MultS")){
			tempoInicio = System.currentTimeMillis();
			String resultadoMult = multiplicar(numero1,numero2,size,"sequencial");
		}else if (operacao.equals("MultP0D")){
			tempoInicio = System.currentTimeMillis();
			String resultadoMult = multiplicar(numero1,numero2,size,"0D");
		}else if (operacao.equals("MultP1D")){
			tempoInicio = System.currentTimeMillis();
			String resultadoMult = multiplicar(numero1,numero2,size,"1D");
		}else if (operacao.equals("MultP2D")){
			tempoInicio = System.currentTimeMillis();
			String resultadoMult = multiplicar(numero1,numero2,size,"2D");
		}else if (operacao.equals("MultBigInt")){
			tempoInicio = System.currentTimeMillis();
			String resultadoSoma = MultBigIntegerJava(numero1,numero2);
		}
		
		return System.currentTimeMillis()-tempoInicio;		
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
	
	public static String somar(String st1, String st2,int size,String tipo){
		
		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);

		long[] numeroParcial;

		if (tipo.equals("sequencial")){		//		SOMA SEQUENCIAL			
			numeroParcial = fazerSomaEmColunasSequencial(numero1,numero2);
		}else{ 								//		SOMA PARALELO
			numeroParcial = fazerSomaEmColunasParalelo(numero1, numero2);

		}
		String[] numero = passarCarry(numeroParcial,size);		

		String soma = concatenarString(numero);
		
		return soma;
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
				numeroFinal[i] = numero1[i] + numero2[i];
			}
		}else{
			numeroFinal = new long[numero1.length];
			for (int i = numero2.length; i < numero1.length; i++){
				numeroFinal[i] = numero1[i];
			}
			for (int i = 0; i < numero2.length; i++){
				numeroFinal[i] = numero1[i] + numero2[i];
			}
		}		
		
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
	
	public static String concatenarString(String[] numero){
		StringBuilder saida = new StringBuilder();
		for (int i = numero.length -1; i >= 0 ; i--){
			saida.append(numero[i]);
		}
		return saida.toString();
	}	
	
	public static String concatenarLong(long[] numero){
		StringBuilder saida = new StringBuilder();
		int tamanho = numero.length;
		
		if (numero[tamanho-1] != 0){
			saida.append(numero[tamanho-1]);
			
			for (int i = tamanho -2; i >= 0 ; i--){	
				
				// Completa com 0.
				if (numero[i] < 10)
					saida.append("00000000");
				else if (numero[i] < 100)
					saida.append("0000000");
				else if (numero[i] < 1000)
					saida.append("000000");
				else if (numero[i] < 10000)
					saida.append("00000");
				else if (numero[i] < 100000)
					saida.append("0000");
				else if (numero[i] < 1000000)
					saida.append("000");
				else if (numero[i] < 10000000)
					saida.append("00");
				else if (numero[i] < 100000000)
					saida.append("0");
						
				saida.append(numero[i]);
				
			}
		}else{
			saida.append(numero[tamanho-2]);
			
			for (int i = tamanho -3; i >= 0 ; i--){	
				
				// Completa com 0.
				if (numero[i] < 10)
					saida.append("00000000");
				else if (numero[i] < 100)
					saida.append("0000000");
				else if (numero[i] < 1000)
					saida.append("000000");
				else if (numero[i] < 10000)
					saida.append("00000");
				else if (numero[i] < 100000)
					saida.append("0000");
				else if (numero[i] < 1000000)
					saida.append("000");
				else if (numero[i] < 10000000)
					saida.append("00");
				else if (numero[i] < 100000000)
					saida.append("0");
						
				saida.append(numero[i]);
				
			}
		}

		return saida.toString();
	}
	
	public static String concatenarInt(int[] numero){
		StringBuilder saida = new StringBuilder();
		int tamanho = numero.length;
		
		if (numero[tamanho-1] != 0){
			saida.append(numero[tamanho-1]);
			
			for (int i = tamanho -2; i >= 0 ; i--){	
				
				// Completa com 0.
				if (numero[i] < 10)
					saida.append("00000000");
				else if (numero[i] < 100)
					saida.append("0000000");
				else if (numero[i] < 1000)
					saida.append("000000");
				else if (numero[i] < 10000)
					saida.append("00000");
				else if (numero[i] < 100000)
					saida.append("0000");
				else if (numero[i] < 1000000)
					saida.append("000");
				else if (numero[i] < 10000000)
					saida.append("00");
				else if (numero[i] < 100000000)
					saida.append("0");
						
				saida.append(numero[i]);
				
			}
		}else{
			saida.append(numero[tamanho-2]);
			
			for (int i = tamanho -3; i >= 0 ; i--){	
				
				// Completa com 0.
				if (numero[i] < 10)
					saida.append("00000000");
				else if (numero[i] < 100)
					saida.append("0000000");
				else if (numero[i] < 1000)
					saida.append("000000");
				else if (numero[i] < 10000)
					saida.append("00000");
				else if (numero[i] < 100000)
					saida.append("0000");
				else if (numero[i] < 1000000)
					saida.append("000");
				else if (numero[i] < 10000000)
					saida.append("00");
				else if (numero[i] < 100000000)
					saida.append("0");
						
				saida.append(numero[i]);
				
			}
		}

		return saida.toString();
	}	
	
	public static String multiplicar (String st1, String st2, int size,String tipo){

		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);
		
		if (tipo.equals("sequencial")){
			return multiplicarSequencial(numero1, numero2, size);
		}else{
			return multiplicarParalelo(numero1, numero2, size,tipo);
		}
		
	}
	
//	public static String multiplicarSequencial(long[] numero1, long[] numero2,int size){
//		String[] resultadoParcial;
//		long[] multiplicacao = new long[numero1.length];
//		String resultado;
//		String resultadoMult = "0";
//		int contadorGeral = 0;		
//		for (int i=0; i < numero2.length; i++){ // pega coluna por coluna do multiplicador
//			long coluna = numero2[i];
//			if (coluna == 0){	contadorGeral = contadorGeral + size;	}
//			while (coluna >= 1){ // itera digito por digito do multiplicador								
//				long digito = coluna % 10; // pega o ultimo digito							
//				for (int j=0; j < numero1.length; j++){
//					multiplicacao[j] = digito * numero1[j]; // multiplica o digito pelo numero 1
//				}
//				resultadoParcial = passarCarry(multiplicacao, size); // passa o carry entre os numeros
//				for (int z = 0 ; z < resultadoParcial.length-1; z++){
//					resultadoParcial[z] = completarComZero(resultadoParcial[z], size); // completa com 0
//				}				
//				resultado = concatenarString(resultadoParcial); // concatena encontrando a mult. do digito pelo numero 1				
//				StringBuilder aux = new StringBuilder();
//				aux.append(resultado);
//				for (int k = 0 ; k < contadorGeral ; k++){ // coloca X números de 0 ao final da multiplicacao encontrada
//					aux.append("0");
//				}
//				resultado = aux.toString();				
//				resultadoMult = somar(resultado,resultadoMult,18,"paralelo"); // Soma a multiplicacao com a soma total ja encontrada // Necessario utilizar soma de bignums pois a soma total pode extrapolar o limite máximo que o tipo long em java armazena														
//				coluna = coluna / 10;
//				contadorGeral++;
//			}
//		}		
//		return resultadoMult;		
//	}
	
//	public static String multiplicarSequencial(long[] numero1, long[] numero2,int size){
//		String[] resultadoParcial;
//		long multiplicacao;
//		String resultado;
//		String resultadoMult = "0";
//		int contadorGeral = 0;		
//		for (int i=0; i < numero1.length; i++){ 				
//			for (int j=0; j < numero2.length; j++){
//				multiplicacao = numero1[i] * numero2[j];
//				
//				StringBuilder aux = new StringBuilder();
//				aux.append(multiplicacao);
//				for (int k = 0 ; k < i+j ; k++){ // coloca X números de 0 ao final da multiplicacao encontrada
//					aux.append("000000000");
//				}
//				resultado = aux.toString();
//				resultadoMult = somar(resultado,resultadoMult,18,"paralelo");
//			}								
//		}		
//		return resultadoMult;		
//	}
	
	public static String multiplicarSequencial(long[] numero1, long[] numero2,int size){
		long multiplicacao;
		long [] saida = new long [numero1.length + numero2.length];
		String resultado;
		long aux2, aux3;
		for (int i=0; i < numero1.length; i++){ 				
			for (int j=0; j < numero2.length; j++){
				multiplicacao = numero1[i] * numero2[j];
				aux2 = multiplicacao % 1000000000;
				aux3 = multiplicacao / 1000000000;
				saida[i+j] += aux2;
				saida[i+j+1] += aux3;
				if (saida[i+j] > 1000000000){
					aux2 = saida[i+j] % 1000000000;
					aux3 = saida[i+j] / 1000000000;
					saida[i+j] = aux2;
					saida[i+j+1] += aux3;
				}
			}								
		}		
		resultado = concatenarLong(saida);
		return resultado;
	}
	
	public static String multiplicarParalelo(long[] numero1, long[] numero2, int size,String tipo){
		String resultadoFinal;
		if (tipo.equals("0D")){
			long[] resultadoMultParalelo = Paralelo.multiplicaParalelo(numero1, numero2,tipo);
			resultadoFinal = concatenarLong(resultadoMultParalelo);
			
		}else{
			int[] resultadoMultParalelo = Paralelo.multiplicaParaleloInt(numero1, numero2,tipo);
			resultadoFinal = concatenarInt(resultadoMultParalelo);
		}
			
		
//		for (int i = 0; i < resultadoMultParalelo.length; i++){
//			System.out.println(i+" "+resultadoMultParalelo[i]);
//		}

		
		/*
		 *  casos de teste
		 * 
		 * 1 * 2787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627
		 * 
		 * 
		 * 2787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627278786725755176577567257264272143425432728523782378272373787274238728758367834879932642042894364234287362845728492574254278345274529873472634298472346287562782675723046273840236472362784362057623507265374230462378056235726527356020837525205863725632875623753628572630562727878672575517657756725726427214342543272852378237827237378727423872875836783487993264204289436423428736284572849257425427834527452987347263429847234628756278267572304627384023647236278436205762350726537423046237805623572652735602083752520586372563287562375362857263056272787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627278786725755176577567257264272143425432728523782378272373787274238728758367834879932642042894364234287362845728492574254278345274529873472634298472346287562782675723046273840236472362784362057623507265374230462378056235726527356020837525205863725632875623753628572630562727878672575517657756725726427214342543272852378237827237378727423872875836783487993264204289436423428736284572849257425427834527452987347263429847234628756278267572304627384023647236278436205762350726537423046237805623572652735602083752520586372563287562375362857263056272787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627278786725755176577567257264272143425432728523782378272373787274238728758367834879932642042894364234287362845728492574254278345274529873472634298472346287562782675723046273840236472362784362057623507265374230462378056235726527356020837525205863725632875623753628572630562727878672575517657756725726427214342543272852378237827237378727423872875836783487993264204289436423428736284572849257425427834527452987347263429847234628756278267572304627384023647236278436205762350726537423046237805623572652735602083752520586372563287562375362857263056272787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627278786725755176577567257264272143425432728523782378272373787274238728758367834879932642042894364234287362845728492574254278345274529873472634298472346287562782675723046273840236472362784362057623507265374230462378056235726527356020837525205863725632875623753628572630562727878672575517657756725726427214342543272852378237827237378727423872875836783487993264204289436423428736284572849257425427834527452987347263429847234628756278267572304627384023647236278436205762350726537423046237805623572652735602083752520586372563287562375362857263056272787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627278786725755176577567257264272143425432728523782378272373787274238728758367834879932642042894364234287362845728492574254278345274529873472634298472346287562782675723046273840236472362784362057623507265374230462378056235726527356020837525205863725632875623753628572630562727878672575517657756725726427214342543272852378237827237378727423872875836783487993264204289436423428736284572849257425427834527452987347263429847234628756278267572304627384023647236278436205762350726537423046237805623572652735602083752520586372563287562375362857263056272787867257551765775672572642721434254327285237823782723737872742387287583678348799326420428943642342873628457284925742542783452745298734726342984723462875627826757230462738402364723627843620576235072653742304623780562357265273560208375252058637256328756237536285726305627
		 * 
		 * 42187782417843887943789487242187421877824178438879437894872421874218778241784388794378948724218742187782417843887943789487242187
		 * 42187782417843887943789487242187421877824178438879437894872421874218778241784388794378948724218742187782417843887943789487242187
		 *
		 * 1000000000 * 1000000000
		 */

		//String[] resultadoCarry = passarCarry(resultadoMultParalelo, size);	
		//String resultadoFinal = concatenarString(resultadoCarry);
		
		
		
		return resultadoFinal;
	}
	
	public static String SomaBigIntegerJava(String numero1, String numero2){
		BigInteger number1 = new BigInteger(numero1);
		BigInteger number2 = new BigInteger(numero2);
		
		BigInteger soma = number1.add(number2);
		return soma.toString();
	}
	
	public static String MultBigIntegerJava(String numero1, String numero2){
		BigInteger number1 = new BigInteger(numero1);
		BigInteger number2 = new BigInteger(numero2);
		
		BigInteger mult = number1.multiply(number2);
		return mult.toString();
	}
	
}
