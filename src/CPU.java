import java.util.Scanner;
import java.util.Vector;

public class CPU {

	
	static int pc = 0;
	static Memory memory = new Memory();
	static Registers registers = new Registers();
	static cache cache = new cache();
	
	
	
	// to convert any integer number to a binary one in 16 bit
	public static String convertToBinary(int num) {
		String s = Integer.toBinaryString(num);
		while (s.length() < 16) {
			s = "0" + s;
		}
		return s;
	}

	///////////////////////// remove the comments to use the clock_cycles without
	///////////////////////// pipelining

//	public static void clock_cycles() throws Exceptions {
//
//		System.out.println();
//		System.out.println();
//		System.out.println("fetching in the first cycle");
//		System.out.println();
//		System.out.println();
//		System.out.println("decoding in the second cycle");
////		Decode_Stage.decode(instruction);
//		System.out.println();
//		System.out.println();
//		System.out.println("executing in the third cycle");
////		Execute_Stage.execute();
//		System.out.println();
//		System.out.println();
//		System.out.println("memory in the fourth cycle");
////		Memory_Stage.memory();
//		System.out.println();
//		System.out.println();
//		System.out.println("write back in the fifth cycle");
////		WriteBack_Stage.writeBack();
//	}

	
	
	
	public static void main(String[] args) throws Exceptions {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter number of instructions in the program: ");
		int numberOfInstructions = sc.nextInt();

		String[] program = new String[numberOfInstructions * 4];
		System.out.println("Enter the program:");
		for (int i = 0; i < numberOfInstructions * 4; i++) {
			program[i] = sc.next();
		}

		Memory memory = new Memory(program);
		CPU.memory = memory;

		
		// clock_cycles
//		for (int i = 0; i < program.length / 4; i++) {
//			clock_cycles();
//			System.out.println("===================================");
//		}

		
		
		
		//////////////////////// pipelining\\\\\\\\\\\\\\\\\\\\\\

		
		// an empty PipeliningRegisters calles instructions 
		// instructions vector has the same length of our program
		Vector<PipeliningRegister> instructions = new Vector<PipeliningRegister>();
		for (int i = 0; i < program.length / 4; i++) {
			PipeliningRegister p = new PipeliningRegister();
			instructions.add(i, p);
		}

		
		
		int cycles = numberOfInstructions + 4;
		
		for (int i = 0; i < cycles; i++) {

			System.out.println();
			System.out.println("After " + (i + 1) + " clock cycles:");
			System.out.println();

			
			
			if (i == 0) {
				instructions.get(i).setCurrentStage("fetch");
			}

			

			int fetch_ins = -1;				// index of the instruction in the fetch stage
			int decode_ins = -1;			// index of the instruction in the decode stage
			int execute_ins = -1;			// index of the instruction in the execute stage
			int memory_ins = -1;			// index of the instruction in the memory stage
			int writeback_ins = -1;			// index of the instruction in the writeBack stage
			for (int j = 0; j < instructions.size(); j++) {
				if (instructions.get(j).getCurrentStage().equals("fetch")) {
					fetch_ins = j;
				} else if (instructions.get(j).getCurrentStage().equals("decode")) {
					decode_ins = j;
				} else if (instructions.get(j).getCurrentStage().equals("execute")) {
					execute_ins = j;
				} else if (instructions.get(j).getCurrentStage().equals("memory")) {
					memory_ins = j;
				} else if (instructions.get(j).getCurrentStage().equals("writeback")) {
					writeback_ins = j;
				}
			}
			
			
			
			
			//check which stage we will enter
			if (fetch_ins >= 0) {
				instructions.set(fetch_ins, Fetch_Stage.fetch(pc));
				pc = pc + 4;
				instructions.get(fetch_ins).setCurrentStage("fetch");
				System.out.println();
				System.out.println();
			}
			if (decode_ins >= 0) {
				instructions.set(decode_ins, Decode_Stage.decode(instructions.get(decode_ins)));
				instructions.get(decode_ins).setCurrentStage("decode");
				System.out.println();
				System.out.println();
			}
			if (execute_ins >= 0) {
				instructions.set(execute_ins, Execute_Stage.execute(instructions.get(execute_ins)));
				instructions.get(execute_ins).setCurrentStage("execute");
				System.out.println();
				System.out.println();
			}
			if (memory_ins >= 0) {
				instructions.set(memory_ins, Memory_Stage.memory(instructions.get(memory_ins)));
				instructions.get(memory_ins).setCurrentStage("memory");
				System.out.println();
				System.out.println();
			}
			if (writeback_ins >= 0) {
				instructions.set(writeback_ins, WriteBack_Stage.writeBack(instructions.get(writeback_ins)));
				instructions.get(writeback_ins).setCurrentStage("writeback");
				System.out.println();
				System.out.println();

			}

			
			
			
			// we use this vector to update the value of the current stage in each instruction
			Vector<String> stages = new Vector<String>();
			stages.add("fetch");
			stages.add("decode");
			stages.add("execute");
			stages.add("memory");
			stages.add("writeback");
			stages.add("done");
			
			// to update the current stage of each instruction
			for (int j2 = 0; j2 < instructions.size(); j2++) {
				if (instructions.get(j2).getCurrentStage().equals("")) {
					instructions.get(j2).setCurrentStage("fetch");
					break;
				}
				if (stages.indexOf(instructions.get(j2).getCurrentStage()) == 5) {
					continue;
				}
				instructions.get(j2)
						.setCurrentStage(stages.get(stages.indexOf(instructions.get(j2).getCurrentStage()) + 1));
			}
			
			

		}

	}

}
