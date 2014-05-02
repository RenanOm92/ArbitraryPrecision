/*
 * JOCL - Java bindings for OpenCL
 * 
 * Code by Renan Oliveira
 */



import static org.jocl.CL.*;

import org.jocl.*;

/**
 * Using JOCL to sum in parallel
 */
public class Paralelo
{
    /**
     * The source code of the OpenCL program to execute
     */
    private static String programSourceSoma =
        "__kernel void "+
        "somaParaleloKernel(__global const long *a,"+
        "             __global const long *b,"+
        "             __global long *c)"+
        "{"+
        "    int gid = get_global_id(0);"+
        "    c[gid] = a[gid] + b[gid];"+
        "}";
    
    private static String programSourceMult =
        "__kernel void "+
        "multiplicacaoParaleloKernel(__global const long *a,"+
        "             __global const long *b,"+
        "             __global long *c)"+
        "{"+
        "    int gid0 = get_global_id(0);"+
        "    int gid1 = get_global_id(1);"+
        "    for (int i = 0; i < size1 * size2; i++){"+
        "    	c[i] = a[gid0] * b[gid1];"+
        "}";
    
    private static String teste = 
    		"__kernel void " +
    		"teste(__global const long *a,"+
    		"      __global const long *b,"+
    		"      __global long* c)" +
    		"{" +
    		" int x = get_global_id(0);" +
    		" int y = get_global_id(1);" +
    		//" for (int i = 0; i < 10; i++){" +
    		" int i = 0;" +
    		" 	c[x+(menor entre A e B*y)] = a[x] * b[y];" + //errado
    		//"}" +
    		"}";
    
    public static long[] somaParalelo(long[]numero1, long[] numero2,int maior){
         
        int numeroDeWork;
        long[] srcArrayA;
        long[] srcArrayB;
        
        
        if (maior == 1){
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

        dst = chamandoOpenCL(srcA,srcB,dst,16,"teste");
        
        return dstArray;
    }
    
    public static long[] multiplicaParalelo(long[]numero1, long[] numero2){
        
        int numeroDeWork;
        numeroDeWork = numero1.length*numero2.length;
        
        long[] srcArrayA = numero1;
        long[] srcArrayB = numero2;      
        long[] dstArray = new long[numeroDeWork];
     
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer srcB = Pointer.to(srcArrayB);
        Pointer dst = Pointer.to(dstArray);
        Pointer pointerSize = Pointer.to(new int[]{numeroDeWork});
        //System.out.println(srcArrayA[0]);
        dst = chamandoOpenCL(srcA,srcB,dst,numeroDeWork,"teste",numero1.length,numero2.length);
        
        return dstArray;
    }
    
    public static Pointer chamandoOpenCL(Pointer srcA, Pointer srcB, Pointer dst, int numeroDeWork, String operacao,int a, int b){
    	
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
        cl_mem memObjects[] = new cl_mem[4];
        memObjects[0] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_long * numeroDeWork, srcA, null);
        memObjects[1] = clCreateBuffer(context, 
            CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
            Sizeof.cl_long * numeroDeWork, srcB, null);
        memObjects[2] = clCreateBuffer(context, 
            CL_MEM_READ_WRITE, 
            Sizeof.cl_long * numeroDeWork, null, null);
        
//        memObjects[3] = clCreateBuffer(context, 
//                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
//                Sizeof.cl_long * numeroDeWork, pointerSize, null);
//        
        cl_program program;
        
//        if (operacao == "somaParaleloKernel"){
//        	program = clCreateProgramWithSource(context,
//                    1, new String[]{ programSourceSoma }, null, null);
//        }
//        else{  
//        	program = clCreateProgramWithSource(context,
//                    1, new String[]{ programSourceMult }, null, null);
//        }
        
        program = clCreateProgramWithSource(context,
                1, new String[]{ teste }, null, null);
       
		// Build the program
        clBuildProgram(program, 0, null, null, null, null);
        
        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, operacao, null);
        
        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0, 
            Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1, 
            Sizeof.cl_mem, Pointer.to(memObjects[1]));
        clSetKernelArg(kernel, 2, 
            Sizeof.cl_mem, Pointer.to(memObjects[2]));
//        clSetKernelArg(kernel, 3, 
//            Sizeof.cl_mem, Pointer.to(memObjects[3]));
//        
        // Set the work-item dimensions
        long global_work_size[] = new long[]{a,b};
        long local_work_size[] = new long[]{1,1};
        
        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 2, null,
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