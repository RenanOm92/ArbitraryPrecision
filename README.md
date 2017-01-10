Arbitrary-Precision

Trabalho de Conclusão de Curso - UFS

Cálculos com números muito grandes, também conhecidos como aritmética de bignums ou aritmética de precisão arbitrária, são uma necessidade atual em muitas áreas das ciências, tais como criptografia e astronomia. O tamanho dos números não fica mais limitado a certas arquiteturas, passando a depender apenas da quantidade de memória disponível da máquina. Porém, com o crescimento de tais números, surge também o problema do crescimento do tempo de execução das operações com estes números. E então surge uma possibilidade de utilizar a computação em paralelo para acelerar esses cálculos. Baseado nisso, esse trabalho visa demonstrar a utilização do framework OpenCL para realizar operações de adição e multiplicação de bignums em paralelo. O projeto foi realizado com a linguagem de programação Java e para vincular Java e OpenCL será usado a API JOCL.

Passo a passo de como executar o projeto pode ser identificado na monografia capítulo: 6	EXPERIMENTOS E RESULTADOS

Antigo repositorio é:  https://github.com/renanUFS/arbitraryPrecision

Para executar o projeto é somente necessário o download da pasta “bin” que contém o projeto já compilado e a biblioteca JOCL-0.1.9.jar. É necessário ter a Java Runtime Environment (JRE) instalada na máquina, além dos drivers de OpenCL.
