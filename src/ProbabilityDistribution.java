import java.util.*;
import java.util.Map.Entry;

/*
 * This class represents a probability distribution over a type T as a map from
 * T values to integers. 
 */
class ProbabilityDistribution<T extends Comparable<T>> {

	private final Map<T, Integer> records;
	private Integer total = 0;

	public ProbabilityDistribution() {
		this.records = new HashMap<T, Integer>();
	}

	public int getTotal() {
		return total;
	}

	public Set<Entry<T, Integer>> getEntrySet() {
		return new HashSet<Entry<T, Integer>>(records.entrySet());
	}

	public Map<T, Integer> getRecords() {
		return new HashMap<T, Integer>(records);
	}

	public T pick(NumberGenerator generator) {
		return this.pick(generator.next(total));
	}
	
	// return data from the class
	public T pick(int index) {
		if (index >= total || index < 0)
			throw new IllegalArgumentException(
					"Index has to be less than or equal to the total " + "number of records in the PD");
		int currentIndex = 0;
		List<T> rs = new ArrayList<T>(total);
		rs.addAll(records.keySet());
		Collections.sort(rs, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1 == null && o2 == null ? 0 : o1 == null ? -1 : o2 == null ? 1 : o1.compareTo(o2);
			}
		});
		for (T key : rs) {
			int currentCount = records.get(key);
			if (currentIndex + currentCount > index) {
				return key;
			}
			currentIndex += currentCount;
		}
		throw new RuntimeException(
				"Error in ProbabilityDistribution. Make sure to only add new records through record().");
	}
	
	// add data to the class
	public void record(T t) {
		records.putIfAbsent(t, 0);
		records.put(t, records.get(t) + 1);
		total++;
	}

	public int count(T t) {
		Integer count = records.get(t);
		return count != null ? count.intValue() : 0;
	}

	public Set<T> keySet() {
		return records.keySet();
	}
	
	public String toString(){
		return records.toString();
	}
}
