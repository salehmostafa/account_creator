package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Filter<T> {
	
	/**
	 * 
	 * @param t Any object
	 * @return Whether the object is a match to a given comparison
	 */
	public boolean isMatch(T t);
	
	/**
	 * Gives a list of matching objects from the original list
	 * 
	 * @param original The original list to be filtered
	 * @param filter The filter used to take out undesired objects
	 * @return A new, filtered list
	 */
	public static <T> List<T> getMatches(List<T> original, Filter<T> filter) {
		List<T> returnable = new ArrayList<>();
		for (T t : original) {
			if (filter.isMatch(t))
				returnable.add(t);
		}
		return returnable;
	}
	/**
	 * Gives a list of matching objects from the original array
	 * 
	 * @param original The original, unmodified array
	 * @param filter The filter used to take out undesired objects
	 * @return A list with only the filtered items from the array
	 */
	public static <T> List<T> getMatches(T[] original, Filter<T> filter) {
		return getMatches(Arrays.asList(original), filter);
	}
	
	/**
	 * Checks if a given list contains an object matching the conditions of the filter
	 * 
	 * @param list The list to be checked
	 * @param filter The filter that an object should match
	 * @return Whether an object that matches the filter's conditions is present in the list
	 */
	public static <T> boolean contains(List<T> list, Filter<T> filter) {
		return indexOf(list, filter) != -1;
	}
	
	/**
	 * Returns the first index of an object matching a given filter
	 * 
	 * @param list The list to be scanned through
	 * @param filter The conditions for a matching object
	 * @return The index of the first matching object
	 */
	public static <T> int indexOf(List<T> list, Filter<T> filter) {
		for(int i = 0; i < list.size(); i++)
			if(filter.isMatch(list.get(i)))
				return i;
		return -1;
	}
	
	/**
	 * Returns the last index of an object matching a given filter
	 * 
	 * @param list The list to be scanned through
	 * @param filter The conditions for a matching object
	 * @return The index of the last matching object
	 */
	public static <T> int lastIndexOf(List<T> list, Filter<T> filter) {
		for(int i = list.size() - 1; i >= 0; i--)
			if(filter.isMatch(list.get(i)))
				return i;
		return -1;
	}
	
	/**
	 * Returns a random object matching the given filter
	 * 
	 * @param list The list to be scanned through
	 * @param filter The conditions for a matching object
	 * @return The first instance of a matching object
	 */
	
	public static <T> T getRandom(List<T> list, Filter<T> filter) {
		List<T> matches = getMatches(list, filter);
		return list.size() == 0 ? null : matches.get(Random.random(matches.size()));
	}
	
	/**
	 * Removes the first object matching a given filter 
	 * 
	 * @param list The list to be scanned through
	 * @param filter The conditions for a matching object
	 * @return Whether a matching object was successfully removed
	 */
	public static <T> boolean remove(List<T> list, Filter<T> filter) {
		int index = indexOf(list, filter);
		if(index > -1)
			list.remove(index);
		return index > -1;
	}
	
	/**
	 * 
	 * Removes all objects from a list matching a given filter
	 * 
	 * @param list The list to be scanned through
	 * @param filter The conditions for a matching object
	 */
	public static <T> void removeAll(List<T> list, Filter<T> filter) {
		while(contains(list, filter))
			remove(list, filter);
	}
}
