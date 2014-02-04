arbitraryPrecision
==================

Creating arbitrary precision for openCL by 

Renan Oliveira Moreira
Universidade Federal de Sergipe

First of all, I'm using Visual Studio(VS) 2010 Express on a O.S. Windows XP and the SDK from the ATIStream version 2.6.

In VS, the project C will need some adjustments to execute openCL, they are:


1º  Project > Configuration Properties > C/C++ > General > Additional Include Directories > 
    Paste here the path of the folder include of your SDK, mine is "C:\Arquivos de programas\ATI Stream\include"
    
2º  Project > Configuration Properties > Linker > General > Additional Library Directories >
    Paste here the path of the folder library of your SDK, mine is "C:\Arquivos de programas\ATI Stream\lib\x86"
    
3º  Project > Configuration Properties > Linker > Input > Additional Dependencies >
    Add the openCL.lib
    
    
Now your VS is ready to execute openCL!



