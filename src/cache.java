
public class cache {
	int[] cache = new int[100];
	int[] Valid = { 0, 0, 0, 0, 0, 0, 0, 0 };
	int[] tags = new int[8];

	int tag;
	int index;

	int miss;
	int hits;

	public cache() {
		for (int i = 0; i < cache.length; i++) {
			cache[i] = 0;
		}
	}

	public void search(int address) {
		miss = 0;
		hits = 0;
		index = address % 100;
		tag = address / 100;
		if (Valid[index] == 0) {

			cache[index] = CPU.memory.loadMemory(address);
			tags[index] = tag;
			Valid[index] = 1;
			miss++;

		}

		else if (Valid[index] == 1) {

			if (tags[index] == tag) {
				hits++;
			}

			else if (tags[index] != tag) {
				cache[index] = CPU.memory.loadMemory(address);
				tags[index] = tag;
				miss++;
			}

		}

	}

	public int getValueIn(int address) {
		return cache[address];
	}

	public void setValueIn(int address, int value) {
		cache[address] = value;
		CPU.memory.storeMemory(address, value);
	}

}
