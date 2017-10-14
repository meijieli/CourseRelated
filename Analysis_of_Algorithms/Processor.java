package homework3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Processor {
	
	public static void main(String[] args) {
		int[] jobs = new int[]{4,2,3,6,2,3,5,21,3};
		int m = 5;
		System.out.println(Arrays.toString(processorAssignment(jobs, m)));
	}
	
	public static int[] processorAssignment(int[] jobs, int m){
		/**
		 * @Param
		 * jobs：sequence of job processing times p1, p2, ..., pn
		 * m: number of processors
		 */
		int[] assignment = new int[jobs.length];
		// Implement a PriorityQueue with a compound key
		PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>(){
			@Override
			public int compare(int[] o1, int[] o2) {
				if (o1[0] == o2[0])			// if two processors have same load
					return o1[1] - o2[1];	// compare index
				else
					return o1[0] - o2[0];	// compare workload
			}
		});
		for (int i = 0; i < m; i++)
			pq.add(new int[]{0, i});
		for (int i = 0; i < jobs.length; i++){
			int[] minLoad = pq.poll();
			assignment[i] = minLoad[1];		// assign a processor to this job
			minLoad[0] += jobs[i];			// add load to this processor
			pq.add(minLoad);
		}
		return assignment;
	}
}
