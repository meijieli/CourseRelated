package class1;

/** Insertion Sort --- O(n ^ 2) */
public class InsertSort {
	public void sort(int[] array){
		if (array == null || array.length == 0)
			return;
		for (int i = 1; i < array.length; i++){ // begin from the second element
			int key = array[i]; // store the element that is going to be inserted
			int j = i - 1; // runner
			while(j >= 0 && array[j] > key){ // look for the insertion position
				array[j + 1] = array[j]; // in-place
				j--;
			}
			array[j + 1] = key; // put element into correct place
		}
	}
}
