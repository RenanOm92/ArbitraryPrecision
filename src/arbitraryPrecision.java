// Codigo desenvolvido por Renan Oliveira.

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
		if (arquitetura > 64){
			System.out.println("Arquitetura maior que 64 bits, long do Java usa 64 bits, calculando como 64 bits ");
			arquitetura = 64;
		}
		long numeroCasasDecimais = (long)Math.pow(2, arquitetura-1);
	
		int size = (Long.toString(numeroCasasDecimais).length())-1;

		// TESTE
		size = 2;
		
//		Arquitetura que vou testar vai ser x10, 10 bits, numero maior e 1024, 
//		entao o numero maximo vai usar 9 bits: 512, somando eles chegam a 1024;
//		e eu tenho que tirar 1 digito usando os decimais.
//		Exemplo: Arquitetura suporta maior numero 1024 = 2^10, vou usar	cada numero
//		então até 512 = 2^9, 3 digitos, só que se eu separar em intervalos de 3 digitos,
//		corre o risco de eu pegar números 999, que quando somados extrapolem o 1024
//		que é o maior suportado pela máquina. Então vou pegar o número de digitos de
//		(2^n-1) e tirar 1 digito. Assim sendo, o exemplo de 1024, separaria em intervalos
//		de 2 dígitos.		
		
//		Arquitetura máxima suportada é 64x.	

	//	somar(st1,st2,size);
		
		multiplicar(st1,st2,size);	

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

			numero[i] = Long.parseLong(aux2);	// E o vetor de string para long
			//System.out.println(i+": "+numero[i]); // É eficiente? provavelmente não!
		}		
		
		aux = new char[restoNum];
		
		if (restoNum != 0){ // Pega primeira parte do número (mensagem) 
				 mensagem.getChars(0, restoNum, aux, 0);
				 aux2 = new String(aux);
				 numero[numero.length-1] = Long.parseLong(aux2);
				 //System.out.println(numero[numero.length-1]);
		}
		
		return numero;
	}
	
	public static String somar(String st1, String st2,int size){
		
		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);

//		SOMA SEQUENCIAL
		long tempoInicio = System.currentTimeMillis();
		long[] numeroParcial = fazerSomaEmColunasSequencial(numero1,numero2);
		System.out.println("Tempo da soma sequencial: "+(System.currentTimeMillis()-tempoInicio));
		String[] numero = passarCarry(numeroParcial,size);
		
		for (int i = 0; i < numero.length-1; i++){
			numero[i] = completarComZero(numero[i], size);
		}
		
		String soma = concatenar(numero);	
		System.out.println("Resultado sequenc. : "+soma);	
		return soma;
		
//		SOMA PARALELO
//		tempoInicio = System.currentTimeMillis();
//		long[] numeroParcial2 = fazerSomaEmColunasParalelo(numero1, numero2);
//		System.out.println("Tempo da soma paralelo em openCL: "+(System.currentTimeMillis()-tempoInicio));				
//		String[] numerox = passarCarry(numeroParcial2,size);
//		numerox = completarComZero(numerox, size);
//		String soma2 = concatenar(numerox);		
//		System.out.println("Resultado paralelo : "+soma2);
		
//		SOMA DO PROPIO JAVA -- fazer
//		tempoInicio = System.currentTimeMillis();
//		BigInteger a = new BigInteger()
		
	}
	
public static String somar(long st1, String st2,int size){
		
		final long[] numero1 = {st1};
		final long[] numero2 = separarNumeros(st2, size);

//		SOMA SEQUENCIAL
		long tempoInicio = System.currentTimeMillis();
		long[] numeroParcial = fazerSomaEmColunasSequencial(numero1,numero2);
		System.out.println("Tempo da soma sequencial: "+(System.currentTimeMillis()-tempoInicio));
		String[] numero = passarCarry(numeroParcial,size);
		
		for (int i = 0; i < numero.length-1; i++){
			numero[i] = completarComZero(numero[i], size);
		}	
		
		String soma = concatenar(numero);	
		System.out.println("Resultado sequenc. : "+soma);	
		return soma;
		
		
//		SOMA PARALELO
//		tempoInicio = System.currentTimeMillis();
//		long[] numeroParcial2 = fazerSomaEmColunasParalelo(numero1, numero2);
//		System.out.println("Tempo da soma paralelo em openCL: "+(System.currentTimeMillis()-tempoInicio));				
//		String[] numerox = passarCarry(numeroParcial2,size);
//		numerox = completarComZero(numerox, size);
//		String soma2 = concatenar(numerox);		
//		System.out.println("Resultado paralelo : "+soma2);
		
//		SOMA DO PROPIO JAVA -- fazer
//		tempoInicio = System.currentTimeMillis();
//		BigInteger a = new BigInteger()
		
	}
	
		
	public static long[] fazerSomaEmColunasParalelo(long[] numero1,long[] numero2){

		long[] numeroFinal;
		long[] numeroParcial;
		int maior;
		if (numero1.length < numero2.length){
			maior = 2;
			numeroFinal = new long[numero2.length];
		}else{
			maior = 1;
			numeroFinal = new long[numero1.length];
		}
		numeroParcial = Paralelo.somaParalelo(numero1,numero2,maior);
		for (int i = 0; i < numeroParcial.length; i++){
			numeroFinal[i] = numeroParcial[i];
		}

		return numeroFinal;
	}
	
	public static long[] fazerSomaEmColunasSequencial(long[] numero1, long[] numero2){
		//System.out.println(numero1.length);
		//System.out.println(numero2.length);
		
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
		for (int i = 0; i < numero.length; i++){
			numeroString[i] = Long.toString(numero[i]);
			
			//System.out.println("number "+numeroString[i]);
			if ((numeroString[i].length() > size) && (i < numero.length-1)){ // Se for lento transformar para string, dá pra calcular o (Math.log10 (n) + 1) e ver quantos digitos o numero tem.
				
				// estou pegando 1º posição da string, retorna char, transformo para string, de string para int.
				numero[i+1] = numero[i+1] + (Integer.parseInt(String.valueOf(numeroString[i].charAt(0))));
				numeroString[i] = numeroString[i].substring(1,size+1); // Remove 1º posição
				//numero[i+1] = numero[i+1] + 1; // Carry passado errado.
				//System.out.println(numeroString[i]);
			}	
		}
		return numeroString;
	}

//		Devido a transformada de string para long, o que era 100012 pode virar 100 | 12, 
//		podendo gerar inconsistencia	depois para realizar a concatenação.	
//		Então é necessário preencher com 0 os números que não tem o tamanho da coluna.
	
	public static String completarComZero(String numero, int size){
		StringBuilder concatenador;	
		if (numero.length() < size){
			concatenador = new StringBuilder();
			for (int j = 0; j < size - numero.length(); j++){
				concatenador.append("0");					
			}
			concatenador.append(numero);
			numero = concatenador.toString();
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
	
	// problema de overflow quando multiplciar valor proximo do limite do long?
	
	public static void multiplicar (String st1, String st2, int size){
		
		final long[] numero1 = separarNumeros(st1, size);
		final long[] numero2 = separarNumeros(st2, size);
		
		multiplicarSequencial(numero1, numero2, size);
	}
	
	public static void multiplicarSequencial(long[] numero1, long[] numero2,int size){
		String[] resultadoParcial;
		long[] multiplicacao = new long[numero1.length];
		String resultado;
		String resultadoMult = "0";
		int contadorGeral = 0;
		
		for (int i=0; i < numero2.length; i++){ // pega digito por digito do 2º numero
			System.out.println(numero2[i]);
			long coluna = numero2[i];
			
			while (coluna >= 1){
				long digito = coluna % 10;
				
				for (int j=0; j < numero1.length; j++){
					multiplicacao[j] = digito * numero1[j];
//					passarCarry(multiplicacao, size);
//					resultado = somar(multiplicacao,resultado,size);
//					System.out.println(resultado);
//					resultado = completarComZero(resultado,size);
				}
				resultadoParcial = passarCarry(multiplicacao, size);

				for (int z = 0 ; z < resultadoParcial.length-1; z++){
					resultadoParcial[z] = completarComZero(resultadoParcial[z], size);
				}
				
				resultado = concatenar(resultadoParcial);
				
				StringBuilder aux = new StringBuilder();
				aux.append(resultado);
				for (int k = 0 ; k < contadorGeral ; k++){
					aux.append("0");
				}
				resultado = aux.toString();
				
				System.out.println(resultado +" + "+ resultadoMult );
				// Esse "resultado" necessita adicionar 0 ao final de acordo com posição do digito.
				resultadoMult = somar(resultado,resultadoMult,size);
				
				coluna = coluna / 10;
				contadorGeral++;
			}
			// Pensar e resolver nas colunas no dígito 2.
		}
		System.out.println("Resultado multiplicação sequenc. : "+resultadoMult);
	}
	
}
