/*
 * JOCL - Java bindings for OpenCL
 * 
 * Code by Renan Oliveira
 */



import static org.jocl.CL.*;

import org.jocl.*;

public class Paralelo
{ 
	
	// Soma
    private static String codigoOpenCLSoma =
        "__kernel void "+
        "somaParaleloKernel(__global const long *numero1,"+
        "             __global const long *numero2,"+
        "             __global long *saida)"+
        "{"+
        "    int gid = get_global_id(0);"+
        "    saida[gid] = numero1[gid] + numero2[gid];"+
        "}";
   
    // Multiplicacao de hexa, usando shift e AND 
    private static String codigoOpenCLMultiplicacaoHexadec =
        "__kernel void "+
        "multiplicacaoParaleloKernelHexadec(__global const long *numero1,"+
    		"      __global const long *numero2,"+		
    		"      __global long* saida)" +
    		"{" +
    		" int x = get_global_id(0);" +
    		" int y = get_global_id(1);" +
    		
    		" long aux,aux2,aux3; " +
    		
    		" aux = numero1[x] * numero2[y];" + 
    		" aux2 = aux & 0xFF;" +
    		" aux3 = aux >> 8;" +
    		
    		" saida[x+y] = saida[x+y] + aux2; " +
    		" saida[x+y+1] = saida[x+y+1] + aux3; " +
    		
    		"}";
    
    // Totalmente em paralelo, porém as saídas ficam se sobescrevendo no vetor de saída
    private static String codigoOpenCLMultiplicacaoDecimal2D =
        "__kernel void "+
        "multiplicacaoParaleloKernelDec2D(__global const long *numero1,"+
    		"      __global const long *numero2,"+		
    		"      __global long* saida)" +
    		"{" +
    		
    		" for (int i = 0; i < get_global_size(0) + get_global_size(1); i++)" +
    		" 		saida[i] = 0;" +
    		" " +
    		
    		" int x = get_global_id(0);" +
    		" int y = get_global_id(1);" +
    		" long aux,aux2,aux3; " +
    		
    		
    		" aux = numero1[x] * numero2[y];" + 
    		" aux2 = aux % 1000000000;" + // mod, remainder
    		" aux3 = aux / 1000000000;" +
    		" saida[x+y] = saida[x+y] + aux2; " +
    		" saida[x+y+1] = saida[x+y+1] + aux3; " +
    		"}";
  
    // Tentativa de botar com 1 loop apenas com 1D
    private static String codigoOpenCLMultiplicacaoDecimal1D =
    	"__kernel void "+
    	"multiplicacaoParaleloKernelDec1D(__global const long *numero1,"+
		"      __global const long *numero2,"+
		"	   __const int tamanhoNumero2,"+		
		"      __global long* saida)" +
		"{" +
		
		" for (int i = 0; i < get_global_size(0) + tamanhoNumero2; i++)" +
		" 		saida[i] = 0;" +
		" " +
		
		" int x = get_global_id(0);" +
		//" int y = get_global_id(1);" +
		" long aux,aux2,aux3; " +
		
		

		" for (int i = 0; i < tamanhoNumero2  ; i++){" +
		" 	aux = numero1[x] * numero2[i];" + 
		" 	aux2 = aux % 1000000000;" + // mod, remainder
		" 	aux3 = aux / 1000000000;" +
		"	saida[x+i] =  saida[x+i] + aux2;" +
		"	saida[x+i+1] = saida[x+i+1] + aux3; " +  
		" }" +
		"}";
    
    // usando 2 loops, pq global id gera concorrencia e erro!!! FUNCIONANDO com global size 1!
    private static String codigoOpenCLMultiplicacaoDecimal0D =
        	"__kernel void "+
        	"multiplicacaoParaleloKernelDec0D(__global const long *numero1,"+
    		"      __global const long *numero2,"+
    		"	   __const int tamanhoNumero1,"+
    		"	   __const int tamanhoNumero2,"+		
    		"      __global long* saida)" +
    		"{" +
    		
    		" for (int i = 0; i < tamanhoNumero1 + tamanhoNumero2; i++)" +
    		" 		saida[i] = 0;" +
    		
    		" long aux,aux2,aux3; " +
    		
    		
    		" for (int a = 0; a < tamanhoNumero1 ; a++){"+
    		" 	for (int b = 0; b < tamanhoNumero2  ; b++){" +
    		" 		aux = numero1[a] * numero2[b];" + 
    		" 		aux2 = (aux % 1000000000);" + // mod, remainder
    		" 		aux3 = aux / 1000000000;" +
    		"		saida[a+b] = saida[a+b] + aux2 ; " +
    		"		saida[a+b+1] = saida[a+b+1] + aux3; " + 
    		"		if (saida[a+b] >= 1000000000){" +
    		"			aux2 = saida[a+b] % 1000000000;"	+
    		" 			aux3 = saida[a+b] / 1000000000;" +
    		"			saida[a+b] = aux2;" +
    		"			saida[a+b+1] = saida[a+b+1] +aux3;" +
    		"		}"	+
    					
    		" 	}"+
    		" }" +
    		"}";
    
//
//    // Gera um vetor de saída numero 1 * numero 2
//    private static String programSourceMultDec =
//        "__kernel void "+
//        "multiplicacaoParaleloKernelDec(__global const long *numero1,"+
//    		"      __global const long *numero2,"+
//    		"	   __const int tamanhoNumero1,"+		
//    		"      __global long* saida)" +
//    		"{" +
//    		
//    		" for (int i = 0; i < get_global_size(0) + get_global_size(1); i++)" +
//    		" 		saida[i] = 0;" +
//    		" " +
//    		
//    		" int x = get_global_id(0);" +
//    		" int y = get_global_id(1);" +
//    		" long aux,aux2,aux3; " +
//    		
//    		
//    		" aux = numero1[x] * numero2[y];" + 
//    		" aux2 = aux % 100;" + // mod, remainder
//    		" aux3 = aux / 100;" +  
//    		//" aux2 = aux % 1000000000;" + // mod, remainder
//    		//" aux3 = aux / 1000000000;" +
//    		" saida[x+(tamanhoNumero1*y)] = aux ; " + 
//    		"}";
    
    public static long[] somaParalelo(long[]numero1, long[] numero2){
         
        int numeroDeWork;
        long[] srcArrayA;
        long[] srcArrayB;
        
        if (numero1.length >= numero2.length){
        	numeroDeWork = numero1.length;
        	srcArrayA = numero1;
        	srcArrayB = new long[numeroDeWork];
        	for (int i = 0; i < numero2.length; i++){
        		srcArrayB[i] = numero2[i];
        	}
        }else{
        	numeroDeWork = numero2.length;
        	srcArrayB = numero2;
        	srcArrayA = new long[numeroDeWork];
        	for (int i = 0; i < numero1.length; i++){
        		srcArrayA[i] = numero1[i];
        	}
        }

        long[] dstArray = new long[numeroDeWork];
     
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer srcB = Pointer.to(srcArrayB);
        Pointer dst = Pointer.to(dstArray);

        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"somaParaleloKernel",numero1.length,numero2.length);
        
        return dstArray;
    }
    
    public static long[] multiplicaParalelo(long[]numero1, long[] numero2){
        
        int numeroDeWork;
        numeroDeWork = numero1.length + numero2.length;

        int tamanhoNumero1 = numero1.length;
        int tamanhoNumero2 = numero2.length;
        
        long[] srcArrayA = numero1;
        long[] srcArrayB = numero2;      
        long[] dstArray = new long[numeroDeWork];
     
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer srcB = Pointer.to(srcArrayB);
        Pointer dst = Pointer.to(dstArray);

        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"multiplicacaoParaleloKernelHexadec",tamanhoNumero1,tamanhoNumero2);
        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"multiplicacaoParaleloKernelDec2D",tamanhoNumero1,tamanhoNumero2);
        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"multiplicacaoParaleloKernelDec1D",tamanhoNumero1,tamanhoNumero2);
        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"multiplicacaoParaleloKernelDec0D",tamanhoNumero1,tamanhoNumero2);
        
        return dstArray;
    }
    
    public static Pointer chamandoOpenCL(Pointer srcA, Pointer srcB, Pointer dst, int numeroDeWork, String operacao,int tamanhoNumero1, int tamanhoNumero2){
    	
    	// The platform, device type and device number
        // that will be used
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        // Obtain a device ID 
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        cl_context context = clCreateContext(
            contextProperties, 1, new cl_device_id[]{device}, 
            null, null, null);
        
        // Create a command-queue for the selected device
        cl_command_queue commandQueue = 
            clCreateCommandQueue(context, device, 0, null);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[3];
        memObjects[0] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_long * numeroDeWork, srcA, null);
        memObjects[1] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_long * numeroDeWork, srcB, null);
        memObjects[2] = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_long * numeroDeWork, null, null);
        
        cl_program program = null;
        
        if (operacao == "somaParaleloKernel"){
        	program = clCreateProgramWithSource(context,
                    1, new String[]{ codigoOpenCLSoma }, null, null);
        }
        else if (operacao == "multiplicacaoParaleloKernelHexadec"){
        	program = clCreateProgramWithSource(context,
                    1, new String[]{ codigoOpenCLMultiplicacaoHexadec }, null, null);
        }else if (operacao == "multiplicacaoParaleloKernelDec2D"){
        	program = clCreateProgramWithSource(context,
                    1, new String[]{ codigoOpenCLMultiplicacaoDecimal2D }, null, null);
        }else if (operacao == "multiplicacaoParaleloKernelDec1D"){
        	program = clCreateProgramWithSource(context,
                    1, new String[]{ codigoOpenCLMultiplicacaoDecimal1D }, null, null);
        }else if (operacao == "multiplicacaoParaleloKernelDec0D"){
        	program = clCreateProgramWithSource(context,
                    1, new String[]{ codigoOpenCLMultiplicacaoDecimal0D }, null, null);
        }
       
		// Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, operacao, null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, 
            Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1, 
            Sizeof.cl_mem, Pointer.to(memObjects[1]));
        if (operacao == "somaParaleloKernel" || operacao == "multiplicacaoParaleloKernelDec2D" || operacao == "multiplicacaoParaleloKernelHexadec" ){
        	clSetKernelArg(kernel, 2, 
                    Sizeof.cl_mem, Pointer.to(memObjects[2]));
        }
        else if (operacao == "multiplicacaoParaleloKernelDec1D"){
        	clSetKernelArg(kernel, 2, 
                	Sizeof.cl_int, Pointer.to(new int[]{tamanhoNumero2}));
            clSetKernelArg(kernel, 3, 
                    Sizeof.cl_mem, Pointer.to(memObjects[2]));
        }else if (operacao == "multiplicacaoParaleloKernelDec0D"){
        	clSetKernelArg(kernel, 2, 
                	Sizeof.cl_int, Pointer.to(new int[]{tamanhoNumero1}));
        	clSetKernelArg(kernel, 3, 
                	Sizeof.cl_int, Pointer.to(new int[]{tamanhoNumero2}));
            clSetKernelArg(kernel, 4, 
                    Sizeof.cl_mem, Pointer.to(memObjects[2]));
        }
      
        // Set the work-item dimensions
//        long global_work_size[] = new long[]{tamanhoNumero1,tamanhoNumero2};
//        long local_work_size[] = new long[]{1,1};
        long global_work_size[] = new long[]{tamanhoNumero1};
        long local_work_size[] = new long[]{1};

        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel,1, null,
            global_work_size, local_work_size, 0, null, null);
        
        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
           numeroDeWork * Sizeof.cl_long, dst, 0, null, null);
        
        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseMemObject(memObjects[1]);
        clReleaseMemObject(memObjects[2]);
        clReleaseKernel(kernel);
        clReleaseProgram(program);
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);

        return dst;
    	
    }
}