package class11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/** Find the maximum number of activities that can be scheduled without conflict */
public class ActivitySelection {
	
	public static void main(String[] args) {
		Interval i1 = new Interval(0, 2);
		Interval i2 = new Interval(1, 2);
		Interval i3 = new Interval(2, 4);
		Interval i4 = new Interval(1, 4);
		Interval i5 = new Interval(3, 4);
		Interval i6 = new Interval(2, 5);
		Interval i7 = new Interval(3, 5);
		Interval i8 = new Interval(4, 5);
		Interval i9 = new Interval(5, 7);
		Interval[] activities = new Interval[]{i1, i2, i3, i4, i5, i6, i7, i8, i9};
		System.out.println();
		System.out.println(select(activities).size());
	}
	
	public static List<Interval> select(Interval[] activities){
		/**
		 * @Param
		 * activities: list of intervals with a begin time and an end time
		 * e.g. (s1, f1) = (1, 5) (s2, f2) = (3, 9)
		 */
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		HashMap<Integer, Interval> map = new HashMap<>();
		for (int i = 0; i < activities.length; i++){
			map.put(activities[i].right, activities[i]);	// create a map from end to the activity
			pq.add(activities[i].right);					// push right end into PQ
		}
		List<Interval> result = new ArrayList<>();			// add the interval with earliest end time
		result.add(map.get(pq.poll()));		
		while(!pq.isEmpty()){
			if (map.get(pq.peek()).left > result.get(result.size() - 1).right)
				result.add(map.get(pq.poll()));				// find next non-overlapping interval
			else
				pq.poll();									// discard overlapping intervals
		}
		return result;
	}

}

class Interval{
	int left;
	int right;
	public Interval(int left, int right){
		this.left = left;
		this.right = right;
	}
}
